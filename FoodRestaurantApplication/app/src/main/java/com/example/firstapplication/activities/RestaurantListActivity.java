package com.example.firstapplication.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.firstapplication.R;
import com.example.firstapplication.adapter.RestaurantAdapter;
import com.example.firstapplication.database.FoodDatabaseHelper;
import com.example.firstapplication.entities.Restaurant;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class RestaurantListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RestaurantAdapter adapter;
    private FoodDatabaseHelper dbHelper;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);

        recyclerView = findViewById(R.id.restaurant_list);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        fab = findViewById(R.id.fab);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dbHelper = new FoodDatabaseHelper(this);

        loadRestaurantList();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadRestaurantList();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RestaurantListActivity.this, AddRestaurantActivity.class));
            }
        });
    }

    private void loadRestaurantList() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(FoodDatabaseHelper.TABLE_RESTAURANT,
                null, null, null, null, null, null);

        List<Restaurant> restaurants = new ArrayList<>();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(FoodDatabaseHelper.RESTAURANT_COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(FoodDatabaseHelper.RESTAURANT_COLUMN_NAME));
            String address = cursor.getString(cursor.getColumnIndexOrThrow(FoodDatabaseHelper.RESTAURANT_COLUMN_ADDRESS));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow(FoodDatabaseHelper.RESTAURANT_COLUMN_PHONE));
            restaurants.add(new Restaurant(id, name, address, phone));
        }
        cursor.close();

        adapter = new RestaurantAdapter(restaurants, new RestaurantAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Restaurant restaurant) {
                Intent intent = new Intent(RestaurantListActivity.this, RestaurantDetailActivity.class);
                intent.putExtra("restaurant_id", restaurant.getId());
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(adapter);
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
            startActivity(new Intent(RestaurantListActivity.this, SignInActivity.class));
            finish();
            return true;
        }
        if (item.getItemId() == R.id.food) {
            startActivity(new Intent(RestaurantListActivity.this, MainActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}