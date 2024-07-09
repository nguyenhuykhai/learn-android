package com.example.firstapplication.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.appcompat.app.AppCompatActivity;

import com.example.firstapplication.R;
import com.example.firstapplication.database.FoodDatabaseHelper;

public class AddRestaurantActivity extends AppCompatActivity {

    private EditText nameEditText, addressEditText, phoneEditText;
    private Button saveButton;
    private FoodDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restaurant);

        nameEditText = findViewById(R.id.edit_text_name);
        addressEditText = findViewById(R.id.edit_text_address);
        phoneEditText = findViewById(R.id.edit_text_phone);
        saveButton = findViewById(R.id.button_save);

        dbHelper = new FoodDatabaseHelper(this);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRestaurant();
            }
        });
    }

    private void saveRestaurant() {
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

        try {
            long newRowId = db.insert(FoodDatabaseHelper.TABLE_RESTAURANT, null, values);
            if (newRowId != -1) {
                Toast.makeText(this, "Restaurant added", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error adding restaurant", Toast.LENGTH_SHORT).show();
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
            startActivity(new Intent(AddRestaurantActivity.this, SignInActivity.class));
            finish();
            return true;
        }
        if (item.getItemId() == R.id.food) {
            startActivity(new Intent(AddRestaurantActivity.this, MainActivity.class));
            finish();
            return true;
        }
        if (item.getItemId() == R.id.restaurant) {
            startActivity(new Intent(AddRestaurantActivity.this, RestaurantListActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
