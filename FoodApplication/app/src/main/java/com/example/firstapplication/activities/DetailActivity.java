package com.example.firstapplication.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.firstapplication.R;
import com.example.firstapplication.database.FoodDatabaseHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText descriptionEditText;
    private EditText priceEditText;
    private ImageView foodImageView;
    private Button updateButton;
    private Button deleteButton;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FoodDatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        nameEditText = findViewById(R.id.nameEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        priceEditText = findViewById(R.id.priceEditText);
        foodImageView = findViewById(R.id.foodImageView);
        updateButton = findViewById(R.id.updateButton);
        deleteButton = findViewById(R.id.deleteButton);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        dbHelper = new FoodDatabaseHelper(this);

        int foodId = getIntent().getIntExtra("food_id", -1);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadFoodDetails(foodId);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        loadFoodDetails(foodId);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateFoodDetails(foodId);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFoodDetails(foodId);
            }
        });
    }

    private void loadFoodDetails(int foodId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(FoodDatabaseHelper.TABLE_FOOD,
                null, FoodDatabaseHelper.COLUMN_ID + "=?",
                new String[]{String.valueOf(foodId)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(FoodDatabaseHelper.COLUMN_NAME));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(FoodDatabaseHelper.COLUMN_DESCRIPTION));
            String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(FoodDatabaseHelper.COLUMN_IMAGE_URL));
            double price = cursor.getDouble(cursor.getColumnIndexOrThrow(FoodDatabaseHelper.COLUMN_PRICE));

            nameEditText.setText(name);
            descriptionEditText.setText(description);
            priceEditText.setText(String.format("$%.2f", price));
            Picasso.get().load(imageUrl).into(foodImageView);

            cursor.close();
        }
    }

    private void updateFoodDetails(int foodId) {
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(FoodDatabaseHelper.COLUMN_NAME, nameEditText.getText().toString());
            contentValues.put(FoodDatabaseHelper.COLUMN_DESCRIPTION, descriptionEditText.getText().toString());
            contentValues.put(FoodDatabaseHelper.COLUMN_PRICE, Double.parseDouble(priceEditText.getText().toString().replace("$", "")));
            int rowsAffected = db.update(FoodDatabaseHelper.TABLE_FOOD, contentValues, FoodDatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(foodId)});
            if (rowsAffected > 0) {
                Toast.makeText(this, "Food details updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
            }
            loadFoodDetails(foodId);
        } catch (SQLException e) {
            Toast.makeText(this, "Update failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void deleteFoodDetails(int foodId) {
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            int rowsAffected = db.delete(FoodDatabaseHelper.TABLE_FOOD, FoodDatabaseHelper.COLUMN_ID + "=?", new String[]{String.valueOf(foodId)});
            if (rowsAffected > 0) {
                Toast.makeText(this, "Food details deleted successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(DetailActivity.this, MainActivity.class));
            } else {
                Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show();
            }
        } catch (SQLException e) {
            Toast.makeText(this, "Delete failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
            sharedPreferences.edit().putBoolean("authenticated", false).apply();
            startActivity(new Intent(DetailActivity.this, SignInActivity.class));
            finish();
            return true;
        }
        if (item.getItemId() == R.id.food) {
            startActivity(new Intent(DetailActivity.this, MainActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}