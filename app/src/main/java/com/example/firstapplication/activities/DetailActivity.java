package com.example.firstapplication.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.firstapplication.R;
import com.example.firstapplication.database.FoodDatabaseHelper;
import com.example.firstapplication.entities.Food;

public class DetailActivity extends AppCompatActivity {

    private ImageView foodImageView;
    private TextView nameTextView, descriptionTextView, priceTextView;
    private Button editButton, deleteButton;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Food food;
    private FoodDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        foodImageView = findViewById(R.id.foodImageView);
        nameTextView = findViewById(R.id.nameTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        priceTextView = findViewById(R.id.priceTextView);
        editButton = findViewById(R.id.editButton);
        deleteButton = findViewById(R.id.deleteButton);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        dbHelper = new FoodDatabaseHelper(this);

        int foodId = getIntent().getIntExtra("food_id", -1);
        loadFoodDetails(foodId);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement edit logic
                if (food != null) {
                    // Example: Navigate to edit screen or show edit UI
                    // Intent intent = new Intent(DetailActivity.this, EditFoodActivity.class);
                    // intent.putExtra("food_id", food.getId());
                    // startActivity(intent);
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Implement delete logic
                if (food != null) {
                    deleteFood(food.getId());
                    Toast.makeText(DetailActivity.this, "Food item deleted", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadFoodDetails(foodId);
            }
        });
    }

    private void loadFoodDetails(int foodId) {
        swipeRefreshLayout.setRefreshing(true);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(FoodDatabaseHelper.TABLE_FOOD,
                null,
                FoodDatabaseHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(foodId)},
                null,
                null,
                null);

        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(FoodDatabaseHelper.COLUMN_NAME));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(FoodDatabaseHelper.COLUMN_DESCRIPTION));
            String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(FoodDatabaseHelper.COLUMN_IMAGE_URL));
            double price = cursor.getDouble(cursor.getColumnIndexOrThrow(FoodDatabaseHelper.COLUMN_PRICE));

            food = new Food(foodId, name, description, imageUrl, price);

            Glide.with(this).load(food.getImageUrl()).into(foodImageView);
            nameTextView.setText(food.getName());
            descriptionTextView.setText(food.getDescription());
            priceTextView.setText(String.valueOf(food.getPrice()));

            cursor.close();
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    private void deleteFood(int foodId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(FoodDatabaseHelper.TABLE_FOOD, FoodDatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(foodId)});
    }
}