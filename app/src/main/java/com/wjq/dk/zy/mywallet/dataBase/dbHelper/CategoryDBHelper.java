package com.wjq.dk.zy.mywallet.dataBase.dbHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.wjq.dk.zy.mywallet.dataBase.dbEntry.CategoryContract;

/**
 * Created by wangjiaqi on 16/10/27.
 */

public class CategoryDBHelper extends SQLiteOpenHelper {

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_TABLE = "CREATE TABLE " + CategoryContract.CategoryEntry.TABLE_NAME + " (" +
            CategoryContract.CategoryEntry.COLUMN_NAME_CATEGORY_ID + " INTEGER PRIMARY KEY," +
            CategoryContract.CategoryEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
            CategoryContract.CategoryEntry.COLUMN_NAME_DATE_CREATED + TEXT_TYPE + COMMA_SEP +
            CategoryContract.CategoryEntry.COLUMN_NAME_DATE_UPDATED + TEXT_TYPE +
            " )";

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Category.db";

    public CategoryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
