package com.example.firstapplication.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.firstapplication.entities.Food;

import java.util.List;

@Dao
public interface FoodDAO {

    @Query("SELECT * FROM food")
    List<Food> getAll();

    @Query("SELECT * FROM food WHERE id IN (:foodId)")
    Food loadFoodById(int foodId);

    @Query("SELECT * FROM food WHERE name LIKE :name LIMIT 1")
    Food findByName(String name);

    @Insert
    long insert(Food food);

    @Update
    int update(Food food);

    @Delete
    int delete(Food food);
}
