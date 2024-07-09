package com.example.firstapplication.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.firstapplication.dao.FoodDAO;
import com.example.firstapplication.entities.Food;

@Database(entities = {Food.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FoodDAO foodDAO();

    private static AppDatabase INSTANCE;

    public static synchronized AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "food_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
