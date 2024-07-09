package com.example.firstapplication.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.firstapplication.R;
import com.example.firstapplication.database.AppDatabase;
import com.example.firstapplication.adapter.FoodAdapter;
import com.example.firstapplication.database.FoodDatabaseHelper;
import com.example.firstapplication.entities.Food;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FoodAdapter foodAdapter;
    private FloatingActionButton fab;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FoodDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        if (!sharedPreferences.getBoolean("authenticated", false)) {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddFoodActivity.class));
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadFoodItems();
            }
        });

        dbHelper = new FoodDatabaseHelper(this);
        loadFoodItems();
    }

    private void loadFoodItems() {
        swipeRefreshLayout.setRefreshing(true);
        List<Food> foodList = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT f." + FoodDatabaseHelper.COLUMN_ID + ", " +
                "f." + FoodDatabaseHelper.COLUMN_NAME + ", " +
                "f." + FoodDatabaseHelper.COLUMN_DESCRIPTION + ", " +
                "f." + FoodDatabaseHelper.COLUMN_PRICE + ", " +
                "f." + FoodDatabaseHelper.COLUMN_IMAGE_URL + ", " +
                "f." + FoodDatabaseHelper.COLUMN_RESTAURANT_ID + ", " +
                "r." + FoodDatabaseHelper.RESTAURANT_COLUMN_NAME + " " +
                "FROM " + FoodDatabaseHelper.TABLE_FOOD + " f " +
                "JOIN " + FoodDatabaseHelper.TABLE_RESTAURANT + " r " +
                "ON f." + FoodDatabaseHelper.COLUMN_RESTAURANT_ID + " = r." + FoodDatabaseHelper.RESTAURANT_COLUMN_ID;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(FoodDatabaseHelper.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(FoodDatabaseHelper.COLUMN_NAME));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(FoodDatabaseHelper.COLUMN_DESCRIPTION));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(FoodDatabaseHelper.COLUMN_PRICE));
                String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(FoodDatabaseHelper.COLUMN_IMAGE_URL));
                int restaurantId = cursor.getInt(cursor.getColumnIndexOrThrow(FoodDatabaseHelper.COLUMN_RESTAURANT_ID));
                String restaurantName = cursor.getString(cursor.getColumnIndexOrThrow(FoodDatabaseHelper.RESTAURANT_COLUMN_NAME));

                Food food = new Food(id, name, description, imageUrl, price, restaurantId, restaurantName);
                foodList.add(food);
            } while (cursor.moveToNext());

            cursor.close();
        }

        foodAdapter = new FoodAdapter(foodList, new FoodAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Food food) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("food_id", food.getId());
                intent.putExtra("restaurant_id", food.getRestaurantId());
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(foodAdapter);
        swipeRefreshLayout.setRefreshing(false);
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
            startActivity(new Intent(MainActivity.this, SignInActivity.class));
            finish();
            return true;
        }
        if (item.getItemId() == R.id.restaurant) {
            startActivity(new Intent(MainActivity.this, RestaurantListActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}