package com.example.firstapplication.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.firstapplication.R;
import com.example.firstapplication.database.FoodDatabaseHelper;
import com.example.firstapplication.entities.Food;

import java.util.ArrayList;

public class AddFoodActivity extends AppCompatActivity {

    private EditText nameEditText, descriptionEditText, priceEditText, imageUrlEditText;
    private Spinner restaurantSpinner;
    private Button saveButton;
    private FoodDatabaseHelper dbHelper;
    private ArrayList<String> restaurantNames;
    private ArrayList<Integer> restaurantIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        nameEditText = findViewById(R.id.edit_text_name);
        descriptionEditText = findViewById(R.id.edit_text_description);
        priceEditText = findViewById(R.id.edit_text_price);
        imageUrlEditText = findViewById(R.id.edit_text_image_url);
        restaurantSpinner = findViewById(R.id.spinner_restaurant);
        saveButton = findViewById(R.id.button_save);

        dbHelper = new FoodDatabaseHelper(this);

        loadRestaurantData();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFood();
            }
        });
    }

    private void loadRestaurantData() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(FoodDatabaseHelper.TABLE_RESTAURANT,
                new String[]{FoodDatabaseHelper.RESTAURANT_COLUMN_ID, FoodDatabaseHelper.RESTAURANT_COLUMN_NAME},
                null, null, null, null, null);

        restaurantNames = new ArrayList<>();
        restaurantIds = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(FoodDatabaseHelper.RESTAURANT_COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(FoodDatabaseHelper.RESTAURANT_COLUMN_NAME));

                restaurantIds.add(id);
                restaurantNames.add(name);
            } while (cursor.moveToNext());
            cursor.close();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, restaurantNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        restaurantSpinner.setAdapter(adapter);
    }

    private void saveFood() {
        String name = nameEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String priceString = priceEditText.getText().toString().trim();
        String imageUrl = imageUrlEditText.getText().toString().trim();
        int selectedRestaurantIndex = restaurantSpinner.getSelectedItemPosition();
        int restaurantId = restaurantIds.get(selectedRestaurantIndex);

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(description) || TextUtils.isEmpty(priceString)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceString);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid price", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FoodDatabaseHelper.COLUMN_NAME, name);
        values.put(FoodDatabaseHelper.COLUMN_DESCRIPTION, description);
        values.put(FoodDatabaseHelper.COLUMN_IMAGE_URL, imageUrl);
        values.put(FoodDatabaseHelper.COLUMN_PRICE, price);
        values.put(FoodDatabaseHelper.COLUMN_RESTAURANT_ID, restaurantId);

        try {
            long newRowId = db.insert(FoodDatabaseHelper.TABLE_FOOD, null, values);
            if (newRowId != -1) {
                Toast.makeText(this, "Food item added", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddFoodActivity.this, MainActivity.class));
            } else {
                Toast.makeText(this, "Error adding food item", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Database error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
            startActivity(new Intent(AddFoodActivity.this, SignInActivity.class));
            finish();
            return true;
        }
        if (item.getItemId() == R.id.food) {
            startActivity(new Intent(AddFoodActivity.this, MainActivity.class));
            finish();
            return true;
        }
        if (item.getItemId() == R.id.restaurant) {
            startActivity(new Intent(AddFoodActivity.this, RestaurantListActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}