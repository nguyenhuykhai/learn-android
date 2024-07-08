package com.example.firstapplication.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.firstapplication.R;
import com.example.firstapplication.database.FoodDatabaseHelper;

public class RestaurantDetailActivity extends AppCompatActivity {

    private TextView nameTextView;
    private TextView addressTextView;
    private TextView phoneTextView;
    private FoodDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        nameTextView = findViewById(R.id.restaurant_name);
        addressTextView = findViewById(R.id.restaurant_address);
        phoneTextView = findViewById(R.id.restaurant_phone);

        dbHelper = new FoodDatabaseHelper(this);

        int restaurantId = getIntent().getIntExtra("restaurant_id", -1);
        loadRestaurantDetails(restaurantId);
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

            nameTextView.setText(name);
            addressTextView.setText(address);
            phoneTextView.setText(phone);

            cursor.close();
        }
    }
}