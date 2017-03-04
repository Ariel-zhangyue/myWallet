package com.wjq.dk.zy.mywallet.dataBase.dbHandler;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wjq.dk.zy.mywallet.dataBase.dbEntry.CategoryContract;
import com.wjq.dk.zy.mywallet.dataBase.dbHandler.handlerInterface.CategoryHandlerInterface;
import com.wjq.dk.zy.mywallet.dataBase.dbHelper.CategoryDBHelper;
import com.wjq.dk.zy.mywallet.model.Category;
import com.wjq.dk.zy.mywallet.model.Subcategory;

import java.text.SimpleDateFormat;

/**
 * Created by wangjiaqi on 16/10/28.
 */
/**
 * # CSIT 6000B    #  DaiKun        20373568          kdai@connect.ust.hk
 * # CSIT 6000B    #  Wang JiaQi    20369969          jwangcu@connect.ust.hk
 * # CSIT 6000B    #  Zhang Yue     20366010          yzhangfk@connect.ust.hk*/
public class CategoryHandler implements CategoryHandlerInterface {
    CategoryDBHelper categgoryDBHelper;
    SubcategoryHandler subcategoryHandler;

    public CategoryHandler(Context context) {
        categgoryDBHelper = new CategoryDBHelper(context);
        subcategoryHandler = new SubcategoryHandler(context);
    }

    @Override
    public Category queryBySubcategoryId(String id) {
        Category category = new Category();
        try {
            SQLiteDatabase db = categgoryDBHelper.getReadableDatabase();

            Subcategory subcategory = subcategoryHandler.queryBySubcategoryId(id);

            String[] projection = {
                    CategoryContract.CategoryEntry.COLUMN_NAME_CATEGORY_ID,
                    CategoryContract.CategoryEntry.COLUMN_NAME_NAME,
                    CategoryContract.CategoryEntry.COLUMN_NAME_DATE_CREATED,
                    CategoryContract.CategoryEntry.COLUMN_NAME_DATE_UPDATED
            };

            Cursor cursor = db.query(
                    CategoryContract.CategoryEntry.TABLE_NAME,  // The table to query
                    projection,                               // The columns to return
                    CategoryContract.CategoryEntry.COLUMN_NAME_CATEGORY_ID + " = ?",                                // The columns for the WHERE clause
                    new String[]{subcategory.getCategoryId()},                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );

            cursor.moveToFirst();

            String ID = cursor.getString(cursor.getColumnIndexOrThrow(CategoryContract.CategoryEntry.COLUMN_NAME_CATEGORY_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(CategoryContract.CategoryEntry.COLUMN_NAME_NAME));
            String dateCreated = cursor.getString(cursor.getColumnIndexOrThrow(CategoryContract.CategoryEntry.COLUMN_NAME_DATE_CREATED));
            String dateUpdated = cursor.getString(cursor.getColumnIndexOrThrow(CategoryContract.CategoryEntry.COLUMN_NAME_DATE_UPDATED));

            category.setCategoryId(ID);
            category.setName(name);
            category.setDateCreated(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateCreated));
            category.setDateUpdated(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateUpdated));
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return category;


    }
}