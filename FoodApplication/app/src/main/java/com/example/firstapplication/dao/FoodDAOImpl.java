package com.example.firstapplication.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.room.Query;

import com.example.firstapplication.database.FoodDatabaseHelper;
import com.example.firstapplication.entities.Food;

import java.util.ArrayList;
import java.util.List;

public class FoodDAOImpl implements FoodDAO {

    private FoodDatabaseHelper dbHelper;

    public FoodDAOImpl(Context context) {
        dbHelper = new FoodDatabaseHelper(context);
    }

    @Override
    public long insert(Food food) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FoodDatabaseHelper.COLUMN_NAME, food.getName());
        values.put(FoodDatabaseHelper.COLUMN_DESCRIPTION, food.getDescription());
        values.put(FoodDatabaseHelper.COLUMN_IMAGE_URL, food.getImageUrl());
        values.put(FoodDatabaseHelper.COLUMN_PRICE, food.getPrice());

        return db.insert(FoodDatabaseHelper.TABLE_FOOD, null, values);
    }

    @Override
    public int update(Food food) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FoodDatabaseHelper.COLUMN_NAME, food.getName());
        values.put(FoodDatabaseHelper.COLUMN_DESCRIPTION, food.getDescription());
        values.put(FoodDatabaseHelper.COLUMN_IMAGE_URL, food.getImageUrl());
        values.put(FoodDatabaseHelper.COLUMN_PRICE, food.getPrice());

        return db.update(FoodDatabaseHelper.TABLE_FOOD, values, FoodDatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(food.getId())});
    }

    @Override
    public int delete(Food food) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete(FoodDatabaseHelper.TABLE_FOOD, FoodDatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(food.getId())});
    }

    @Override
    public Food loadFoodById(int foodId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(FoodDatabaseHelper.TABLE_FOOD, null, FoodDatabaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(foodId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Food food = new Food();
            int idIndex = cursor.getColumnIndex(FoodDatabaseHelper.COLUMN_ID);
            int nameIndex = cursor.getColumnIndex(FoodDatabaseHelper.COLUMN_NAME);
            int descriptionIndex = cursor.getColumnIndex(FoodDatabaseHelper.COLUMN_DESCRIPTION);
            int imageUrlIndex = cursor.getColumnIndex(FoodDatabaseHelper.COLUMN_IMAGE_URL);
            int priceIndex = cursor.getColumnIndex(FoodDatabaseHelper.COLUMN_PRICE);

            if (idIndex != -1) food.setId(cursor.getInt(idIndex));
            if (nameIndex != -1) food.setName(cursor.getString(nameIndex));
            if (descriptionIndex != -1) food.setDescription(cursor.getString(descriptionIndex));
            if (imageUrlIndex != -1) food.setImageUrl(cursor.getString(imageUrlIndex));
            if (priceIndex != -1) food.setPrice(cursor.getDouble(priceIndex));

            cursor.close();
            return food;
        }
        return null;
    }

    @Query("SELECT * FROM food")
    @Override
    public List<Food> getAll() {
        List<Food> foodList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(FoodDatabaseHelper.TABLE_FOOD, null, null, null, null, null, null);
        if (cursor != null) {
            int idIndex = cursor.getColumnIndex(FoodDatabaseHelper.COLUMN_ID);
            int nameIndex = cursor.getColumnIndex(FoodDatabaseHelper.COLUMN_NAME);
            int descriptionIndex = cursor.getColumnIndex(FoodDatabaseHelper.COLUMN_DESCRIPTION);
            int imageUrlIndex = cursor.getColumnIndex(FoodDatabaseHelper.COLUMN_IMAGE_URL);
            int priceIndex = cursor.getColumnIndex(FoodDatabaseHelper.COLUMN_PRICE);

            while (cursor.moveToNext()) {
                Food food = new Food();
                if (idIndex != -1) food.setId(cursor.getInt(idIndex));
                if (nameIndex != -1) food.setName(cursor.getString(nameIndex));
                if (descriptionIndex != -1) food.setDescription(cursor.getString(descriptionIndex));
                if (imageUrlIndex != -1) food.setImageUrl(cursor.getString(imageUrlIndex));
                if (priceIndex != -1) food.setPrice(cursor.getDouble(priceIndex));
                foodList.add(food);
            }
            cursor.close();
        }
        return foodList;
    }

    @Override
    public Food findByName(String name) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(FoodDatabaseHelper.TABLE_FOOD, null, FoodDatabaseHelper.COLUMN_NAME + " LIKE ?", new String[]{name}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Food food = new Food();
            int idIndex = cursor.getColumnIndex(FoodDatabaseHelper.COLUMN_ID);
            int nameIndex = cursor.getColumnIndex(FoodDatabaseHelper.COLUMN_NAME);
            int descriptionIndex = cursor.getColumnIndex(FoodDatabaseHelper.COLUMN_DESCRIPTION);
            int imageUrlIndex = cursor.getColumnIndex(FoodDatabaseHelper.COLUMN_IMAGE_URL);
            int priceIndex = cursor.getColumnIndex(FoodDatabaseHelper.COLUMN_PRICE);

            if (idIndex != -1) food.setId(cursor.getInt(idIndex));
            if (nameIndex != -1) food.setName(cursor.getString(nameIndex));
            if (descriptionIndex != -1) food.setDescription(cursor.getString(descriptionIndex));
            if (imageUrlIndex != -1) food.setImageUrl(cursor.getString(imageUrlIndex));
            if (priceIndex != -1) food.setPrice(cursor.getDouble(priceIndex));

            cursor.close();
            return food;
        }
        return null;
    }
}
