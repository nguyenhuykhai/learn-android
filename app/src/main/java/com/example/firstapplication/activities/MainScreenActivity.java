package com.example.firstapplication.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstapplication.R;
import com.example.firstapplication.adapter.FoodAdapter;
import com.example.firstapplication.dao.FoodDAO;
import com.example.firstapplication.database.AppDatabase;
import com.example.firstapplication.entities.Food;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainScreenActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FoodAdapter foodAdapter;
    private FoodDAO foodDAO;

    private EditText edtName, edtDescription, edtImage, edtPrice;
    private Button btnAddFood, btnEditFood, btnDeleteFood, btnQueryFood;

    private ExecutorService executorService;
    private List<Food> foodList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        // Initialize views
        edtName = findViewById(R.id.edtName);
        edtDescription = findViewById(R.id.edtDescription);
        edtImage = findViewById(R.id.edtImage);
        edtPrice = findViewById(R.id.edtPrice);
        btnAddFood = findViewById(R.id.btnAddFood);
        btnEditFood = findViewById(R.id.btnEditFood);
        btnDeleteFood = findViewById(R.id.btnDeleteFood);
        btnQueryFood = findViewById(R.id.btnQueryFood);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
        foodDAO = db.foodDAO();
        executorService = Executors.newSingleThreadExecutor();

        // Load data on a background thread
        loadFoodData();

        // Handle add food event
        btnAddFood.setOnClickListener(v -> addFood());

        // Handle edit food event
        btnEditFood.setOnClickListener(v -> editFood());

        // Handle delete food event
        btnDeleteFood.setOnClickListener(v -> deleteFood());

        // Handle query food event
        btnQueryFood.setOnClickListener(v -> loadFoodData());
    }

    private void loadFoodData() {
        executorService.execute(() -> {
            try {
                foodList = foodDAO.getAll();
                runOnUiThread(() -> {
                    foodAdapter = new FoodAdapter(foodList, food -> {
                        // Handle item click (optional)
                    });
                    recyclerView.setAdapter(foodAdapter);
                    foodAdapter.notifyDataSetChanged();
                });
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(MainScreenActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void addFood() {
        Food food = new Food();
        food.setName(edtName.getText().toString());
        food.setDescription(edtDescription.getText().toString());
        food.setImageUrl(edtImage.getText().toString());
        food.setPrice(Double.parseDouble(edtPrice.getText().toString()));

        executorService.execute(() -> {
            try {
                foodDAO.insert(food);
                runOnUiThread(() -> {
                    loadFoodData();
                    Toast.makeText(MainScreenActivity.this, "Food added", Toast.LENGTH_SHORT).show();
                });
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(MainScreenActivity.this, "Food addition failed", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void editFood() {
        Food food = new Food();
        food.setName(edtName.getText().toString());
        food.setDescription(edtDescription.getText().toString());
        food.setImageUrl(edtImage.getText().toString());
        food.setPrice(Double.parseDouble(edtPrice.getText().toString()));

        executorService.execute(() -> {
            try {
                foodDAO.update(food);
                runOnUiThread(() -> {
                    loadFoodData();
                    Toast.makeText(MainScreenActivity.this, "Food updated", Toast.LENGTH_SHORT).show();
                });
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(MainScreenActivity.this, "Food update failed", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void deleteFood() {
        Food food = new Food();
        food.setName(edtName.getText().toString());
        food.setDescription(edtDescription.getText().toString());
        food.setImageUrl(edtImage.getText().toString());
        food.setPrice(Double.parseDouble(edtPrice.getText().toString()));

        executorService.execute(() -> {
            try {
                foodDAO.delete(food);
                runOnUiThread(() -> {
                    loadFoodData();
                    Toast.makeText(MainScreenActivity.this, "Food deleted", Toast.LENGTH_SHORT).show();
                });
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(MainScreenActivity.this, "Food deletion failed", Toast.LENGTH_SHORT).show());
            }
        });
    }
}