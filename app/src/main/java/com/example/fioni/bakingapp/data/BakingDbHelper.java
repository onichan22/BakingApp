package com.example.fioni.bakingapp.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by fioni on 10/14/2017.
 */

public class BakingDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "baking.db";
    private static final int DATABASE_VERSION = '1';

    final String SQL_CREATE_RECIPE_TABLE = "CREATE TABLE " +
            BakingContract.Recipes.TABLE_NAME_RECIPES + "(" +
            BakingContract.Recipes._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            BakingContract.Recipes.COL_R_ID + " TEXT NOT NULL," +
            BakingContract.Recipes.COL_R_NAME + " TEXT NOT NULL," +
            BakingContract.Recipes.COL_R_SERVINGS + " TEXT NOT NULL," +
            BakingContract.Recipes.COL_R_IMAGE + "" +
            ");";

    final String SQL_CREATE_INGREDIENTS_TABLE = "CREATE TABLE " +
            BakingContract.Ingredients.TABLE_NAME_INGREDIENTS + "(" +
            BakingContract.Ingredients._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            BakingContract.Ingredients.COL_I_RID + " TEXT NOT NULL," +
            BakingContract.Ingredients.COL_I_QUANTITY + " TEXT NOT NULL," +
            BakingContract.Ingredients.COL_I_MEASURE + " TEXT NOT NULL," +
            BakingContract.Ingredients.COL_I_NAME + " TEXT NOT NULL " +
            ");";

    final String SQL_CREATE_STEPS_TABLE = "CREATE TABLE " +
            BakingContract.Steps.TABLE_NAME_STEPS + "(" +
            BakingContract.Steps._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            BakingContract.Steps.COL_S_RID + " TEXT NOT NULL," +
            BakingContract.Steps.COL_S_ID + " TEXT NOT NULL," +
            BakingContract.Steps.COL_S_SHORT_DESC + " TEXT NOT NULL," +
            BakingContract.Steps.COL_S_DESC + " TEXT NOT NULL," +
            BakingContract.Steps.COL_S_VIDEO_URL + " ," +
            BakingContract.Steps.COL_S_THUMB_URL + " " +
            ");";

    public BakingDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_RECIPE_TABLE);
        db.execSQL(SQL_CREATE_INGREDIENTS_TABLE);
        db.execSQL(SQL_CREATE_STEPS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST " + BakingContract.Recipes.TABLE_NAME_RECIPES);
        db.execSQL("DROP TABLE IF EXIST " + BakingContract.Ingredients.TABLE_NAME_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXIST " + BakingContract.Steps.TABLE_NAME_STEPS);
        onCreate(db);
    }
}
