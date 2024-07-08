package com.example.firstapplication.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.firstapplication.R;
import com.example.firstapplication.database.FoodDatabaseHelper;

public class RestaurantListActivity extends AppCompatActivity {

    private ListView listView;
    private FoodDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);

        listView = findViewById(R.id.restaurant_list);
        dbHelper = new FoodDatabaseHelper(this);

        loadRestaurantList();
    }

    private void loadRestaurantList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(FoodDatabaseHelper.TABLE_RESTAURANT,
                null, null, null, null, null, null);

        String[] from = {
                FoodDatabaseHelper.RESTAURANT_COLUMN_NAME,
                FoodDatabaseHelper.RESTAURANT_COLUMN_ADDRESS,
                FoodDatabaseHelper.RESTAURANT_COLUMN_PHONE
        };

        int[] to = {
                R.id.restaurant_name,
                R.id.restaurant_address,
                R.id.restaurant_phone
        };

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.restaurant_item, cursor, from, to, 0);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(RestaurantListActivity.this, RestaurantDetailActivity.class);
                intent.putExtra("restaurant_id", id);
                startActivity(intent);
            }
        });
    }
}