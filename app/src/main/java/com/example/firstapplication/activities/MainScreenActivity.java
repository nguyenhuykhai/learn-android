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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        // Ánh xạ các view
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
        executorService.execute(() -> {
            List<Food> foodList = foodDAO.getAll();
            runOnUiThread(() -> {
                foodAdapter = new FoodAdapter(foodList);
                recyclerView.setAdapter(foodAdapter);
            });
        });

        // Xử lý sự kiện thêm món ăn
        btnAddFood.setOnClickListener(v -> {
            Food food = new Food();
            food.setName(edtName.getText().toString());
            food.setDescription(edtDescription.getText().toString());
            food.setImageUrl(edtImage.getText().toString());
            food.setPrice(Double.parseDouble(edtPrice.getText().toString()));

            try {
                executorService.execute(() -> {
                    foodDAO.insert(food);
                    runOnUiThread(() -> {
                        foodAdapter.notifyDataSetChanged();
                        Toast.makeText(MainScreenActivity.this, "Food added", Toast.LENGTH_SHORT).show();
                    });
                });
            } catch (Exception e) {
                Toast.makeText(this, "Food added failed", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý sự kiện cập nhật món ăn
        btnEditFood.setOnClickListener(v -> {
            Food food = new Food();
            food.setName(edtName.getText().toString());
            food.setDescription(edtDescription.getText().toString());
            food.setImageUrl(edtImage.getText().toString());
            food.setPrice(Double.parseDouble(edtPrice.getText().toString()));

            try {
                executorService.execute(() -> {
                    foodDAO.update(food);
                    runOnUiThread(() -> {
                        foodAdapter.notifyDataSetChanged();
                        Toast.makeText(MainScreenActivity.this, "Food updated", Toast.LENGTH_SHORT).show();
                    });
                });
            } catch (Exception e) {
                Toast.makeText(this, "Food updated failed", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý sự kiện xóa món ăn
        btnDeleteFood.setOnClickListener(v -> {
            Food food = new Food();
            food.setName(edtName.getText().toString());
            food.setDescription(edtDescription.getText().toString());
            food.setImageUrl(edtImage.getText().toString());
            food.setPrice(Double.parseDouble(edtPrice.getText().toString()));

            try {
                executorService.execute(() -> {
                    foodDAO.delete(food);
                    runOnUiThread(() -> {
                        foodAdapter.notifyDataSetChanged();
                        Toast.makeText(MainScreenActivity.this, "Food deleted", Toast.LENGTH_SHORT).show();
                    });
                });
            } catch (Exception e) {
                Toast.makeText(this, "Food deleted failed", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý sự kiện lấy danh sách món ăn
        btnQueryFood.setOnClickListener(v -> {
            try {
                executorService.execute(() -> {
                    List<Food> foodList = foodDAO.getAll();
                    runOnUiThread(() -> {
                        foodAdapter = new FoodAdapter(foodList);
                        recyclerView.setAdapter(foodAdapter);
                        foodAdapter.notifyDataSetChanged();
                    });
                });
            } catch (Exception e) {
                Toast.makeText(this, "Query failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}