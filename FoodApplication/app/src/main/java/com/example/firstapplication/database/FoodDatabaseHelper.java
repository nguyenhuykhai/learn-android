package com.example.firstapplication.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class FoodDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "food";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_FOOD = "food";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_IMAGE_URL = "image_url";
    public static final String COLUMN_PRICE = "price";

    private static final String TABLE_FOOD_CREATE =
            "CREATE TABLE " + TABLE_FOOD + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_IMAGE_URL + " TEXT, " +
                    COLUMN_PRICE + " REAL " + ")";

    public FoodDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_FOOD_CREATE);
        insertInitialData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOOD);
        onCreate(db);
    }

    private void insertInitialData(SQLiteDatabase db) {

        db.execSQL("INSERT INTO " + TABLE_FOOD + " (" +
                COLUMN_NAME + ", " +
                COLUMN_DESCRIPTION + ", " +
                COLUMN_IMAGE_URL + ", " +
                COLUMN_PRICE + ") VALUES ('Pizza', 'Delicious cheese pizza', 'https://th.bing.com/th/id/OIP._qKNgK-roTJV4QnQS2JKGwHaFj?rs=1&pid=ImgDetMain', 9.99);");

        db.execSQL("INSERT INTO " + TABLE_FOOD + " (" +
                COLUMN_NAME + ", " +
                COLUMN_DESCRIPTION + ", " +
                COLUMN_IMAGE_URL + ", " +
                COLUMN_PRICE + ") VALUES ('Burger', 'Juicy beef burger', 'https://th.bing.com/th/id/R.364257dda9548d77cc5917abd56cd7a3?rik=mIKjx7t9F9fPOg&riu=http%3a%2f%2fdealer-communications.com%2fwp-content%2fuploads%2f2014%2f05%2fhamburger.jpg&ehk=8CFqnWF%2fkSblm5TsfWSKLvXsB1jlg9y9sUg0o4fMCnU%3d&risl=&pid=ImgRaw&r=0', 7.99);");

        db.execSQL("INSERT INTO " + TABLE_FOOD + " (" +
                COLUMN_NAME + ", " +
                COLUMN_DESCRIPTION + ", " +
                COLUMN_IMAGE_URL + ", " +
                COLUMN_PRICE  + ") VALUES ('Pasta', 'Creamy alfredo pasta', 'https://th.bing.com/th/id/R.eb69d23965fc6dd2147d31fb8d021fc2?rik=V14Z0nBK7vfmDQ&pid=ImgRaw&r=0', 8.99);");
    }
}