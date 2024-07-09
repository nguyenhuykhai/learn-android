package com.example.firstapplication.activities;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.firstapplication.R;
import com.example.firstapplication.database.FoodDatabaseHelper;

public class RestaurantDetailActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText addressEditText;
    private EditText phoneEditText;
    private Button updateButton;
    private Button deleteButton;
    private FoodDatabaseHelper dbHelper;
    private int restaurantId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        nameEditText = findViewById(R.id.restaurant_name);
        addressEditText = findViewById(R.id.restaurant_address);
        phoneEditText = findViewById(R.id.restaurant_phone);
        updateButton = findViewById(R.id.button_update);
        deleteButton = findViewById(R.id.button_delete);

        dbHelper = new FoodDatabaseHelper(this);

        restaurantId = getIntent().getIntExtra("restaurant_id", -1);
        loadRestaurantDetails(restaurantId);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateRestaurant();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmAndDeleteRestaurant();
            }
        });
    }

    private void loadRestaurantDetails(int restaurantId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(FoodDatabaseHelper.TABLE_RESTAURANT,
                null, FoodDatabaseHelper.RESTAURANT_COLUMN_ID + "=?",
                new String[]{String.valueOf(restaurantId)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(FoodDatabaseHelper.RESTAURANT_COLUMN_NAME));
            String address = cursor.getString(cursor.getColumnIndexOrThrow(FoodDatabaseHelper.RESTAURANT_COLUMN_ADDRESS));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow(FoodDatabaseHelper.RESTAURANT_COLUMN_PHONE));

            nameEditText.setText(name);
            addressEditText.setText(address);
            phoneEditText.setText(phone);

            cursor.close();
        }
    }

    private void updateRestaurant() {
        String name = nameEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(address) || TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FoodDatabaseHelper.RESTAURANT_COLUMN_NAME, name);
        values.put(FoodDatabaseHelper.RESTAURANT_COLUMN_ADDRESS, address);
        values.put(FoodDatabaseHelper.RESTAURANT_COLUMN_PHONE, phone);

        int rowsUpdated = db.update(FoodDatabaseHelper.TABLE_RESTAURANT, values,
                FoodDatabaseHelper.RESTAURANT_COLUMN_ID + "=?", new String[]{String.valueOf(restaurantId)});

        if (rowsUpdated > 0) {
            Toast.makeText(this, "Restaurant updated", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(RestaurantDetailActivity.this, RestaurantListActivity.class));
        } else {
            Toast.makeText(this, "Error updating restaurant", Toast.LENGTH_SHORT).show();
        }
    }

    private void confirmAndDeleteRestaurant() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to delete this restaurant?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteRestaurant();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteRestaurant() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted = db.delete(FoodDatabaseHelper.TABLE_RESTAURANT,
                FoodDatabaseHelper.RESTAURANT_COLUMN_ID + "=?", new String[]{String.valueOf(restaurantId)});

        if (rowsDeleted > 0) {
            Toast.makeText(this, "Restaurant deleted", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(RestaurantDetailActivity.this, RestaurantListActivity.class));
        } else {
            Toast.makeText(this, "Error deleting restaurant", Toast.LENGTH_SHORT).show();
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
            startActivity(new Intent(RestaurantDetailActivity.this, SignInActivity.class));
            finish();
            return true;
        }
        if (item.getItemId() == R.id.restaurant) {
            startActivity(new Intent(RestaurantDetailActivity.this, RestaurantListActivity.class));
            finish();
            return true;
        }
        if (item.getItemId() == R.id.food) {
            startActivity(new Intent(RestaurantDetailActivity.this, MainActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}