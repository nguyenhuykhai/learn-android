package com.example.firstapplication.activities;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.firstapplication.R;
import com.example.firstapplication.database.FoodDatabaseHelper;
import com.example.firstapplication.entities.Food;

public class AddFoodActivity extends AppCompatActivity {

    private EditText nameEditText, descriptionEditText, priceEditText, imageUrlEditText;
    private Button saveButton;
    private FoodDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        nameEditText = findViewById(R.id.edit_text_name);
        descriptionEditText = findViewById(R.id.edit_text_description);
        priceEditText = findViewById(R.id.edit_text_price);
        imageUrlEditText = findViewById(R.id.edit_text_image_url);
        saveButton = findViewById(R.id.button_save);

        dbHelper = new FoodDatabaseHelper(this);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFood();
            }
        });
    }

    private void saveFood() {
        String name = nameEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String priceString = priceEditText.getText().toString().trim();
        String imageUrl = imageUrlEditText.getText().toString().trim();

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

        long newRowId = db.insert(FoodDatabaseHelper.TABLE_FOOD, null, values);

        if (newRowId != -1) {
            Toast.makeText(this, "Food item added", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error adding food item", Toast.LENGTH_SHORT).show();
        }
    }
}