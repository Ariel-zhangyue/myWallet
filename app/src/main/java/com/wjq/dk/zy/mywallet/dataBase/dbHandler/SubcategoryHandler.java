package com.wjq.dk.zy.mywallet.dataBase.dbHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wjq.dk.zy.mywallet.dataBase.dbEntry.SubcategoryContract;
import com.wjq.dk.zy.mywallet.dataBase.dbHandler.handlerInterface.SubcategoryHandlerInterface;
import com.wjq.dk.zy.mywallet.dataBase.dbHelper.SubcategoryDBHelper;
import com.wjq.dk.zy.mywallet.model.Subcategory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wangjiaqi on 16/10/28.
 */
/**
 * # CSIT 6000B    #  DaiKun        20373568          kdai@connect.ust.hk
 * # CSIT 6000B    #  Wang JiaQi    20369969          jwangcu@connect.ust.hk
 * # CSIT 6000B    #  Zhang Yue     20366010          yzhangfk@connect.ust.hk*/
public class SubcategoryHandler implements SubcategoryHandlerInterface {
    SubcategoryDBHelper subcategoryDBHelper;


    public SubcategoryHandler(Context context) {
        this.subcategoryDBHelper = new SubcategoryDBHelper(context);
    }

    @Override
    public Subcategory queryBySubcategoryId(String id) {
        Subcategory subcategory = new Subcategory();
        try {
            SQLiteDatabase db = subcategoryDBHelper.getReadableDatabase();

            String[] projection = {
                    SubcategoryContract.SubcategoryEntry.COLUMN_NAME_SUBCATEGORY_ID,
                    SubcategoryContract.SubcategoryEntry.COLUMN_NAME_NAME,
                    SubcategoryContract.SubcategoryEntry.COLUMN_NAME_CATEGORY_ID,
                    SubcategoryContract.SubcategoryEntry.COLUMN_NAME_DATE_CREATED,
                    SubcategoryContract.SubcategoryEntry.COLUMN_NAME_DATE_UPDATED
            };

            Cursor cursor = db.query(
                    SubcategoryContract.SubcategoryEntry.TABLE_NAME,
                    projection,
                    SubcategoryContract.SubcategoryEntry.COLUMN_NAME_SUBCATEGORY_ID + "=?",
                    new String[]{id},
                    null,
                    null,
                    null
            );

            cursor.moveToFirst();

            String subcategoryId = cursor.getString(cursor.getColumnIndexOrThrow(SubcategoryContract.SubcategoryEntry.COLUMN_NAME_SUBCATEGORY_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(SubcategoryContract.SubcategoryEntry.COLUMN_NAME_NAME));
            String CategoryId = cursor.getString(cursor.getColumnIndexOrThrow(SubcategoryContract.SubcategoryEntry.COLUMN_NAME_CATEGORY_ID));
            String dateCreated = cursor.getString(cursor.getColumnIndexOrThrow(SubcategoryContract.SubcategoryEntry.COLUMN_NAME_DATE_CREATED));
            String dateUpdated = cursor.getString(cursor.getColumnIndexOrThrow(SubcategoryContract.SubcategoryEntry.COLUMN_NAME_DATE_UPDATED));

            subcategory.setSubcategoryId(subcategoryId);
            subcategory.setName(name);
            subcategory.setCategoryId(CategoryId);
            subcategory.setDateCreated(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateCreated));
            subcategory.setDateUpdated(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateUpdated));
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return subcategory;
    }

    @Override
    public List<Subcategory> queryByCategoryId(String id) {
        List<Subcategory> subcategoryList = new ArrayList<Subcategory>();
        try {
            SQLiteDatabase db = subcategoryDBHelper.getReadableDatabase();

            String[] projection = {
                    SubcategoryContract.SubcategoryEntry.COLUMN_NAME_SUBCATEGORY_ID,
                    SubcategoryContract.SubcategoryEntry.COLUMN_NAME_NAME,
                    SubcategoryContract.SubcategoryEntry.COLUMN_NAME_CATEGORY_ID,
                    SubcategoryContract.SubcategoryEntry.COLUMN_NAME_DATE_CREATED,
                    SubcategoryContract.SubcategoryEntry.COLUMN_NAME_DATE_UPDATED
            };

            Cursor cursor = db.query(
                    SubcategoryContract.SubcategoryEntry.TABLE_NAME,
                    projection,
                    SubcategoryContract.SubcategoryEntry.COLUMN_NAME_CATEGORY_ID + "=?",
                    new String[]{id},
                    null,
                    null,
                    null
            );

            cursor.moveToFirst();

            for(int i=0;i<cursor.getCount();i++) {
                String subcategoryId = cursor.getString(cursor.getColumnIndexOrThrow(SubcategoryContract.SubcategoryEntry.COLUMN_NAME_SUBCATEGORY_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(SubcategoryContract.SubcategoryEntry.COLUMN_NAME_NAME));
                String CategoryId = cursor.getString(cursor.getColumnIndexOrThrow(SubcategoryContract.SubcategoryEntry.COLUMN_NAME_CATEGORY_ID));
                String dateCreated = cursor.getString(cursor.getColumnIndexOrThrow(SubcategoryContract.SubcategoryEntry.COLUMN_NAME_DATE_CREATED));
                String dateUpdated = cursor.getString(cursor.getColumnIndexOrThrow(SubcategoryContract.SubcategoryEntry.COLUMN_NAME_DATE_UPDATED));

                Subcategory subcategory = new Subcategory();
                subcategory.setSubcategoryId(subcategoryId);
                subcategory.setName(name);
                subcategory.setCategoryId(CategoryId);
                subcategory.setDateCreated(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateCreated));
                subcategory.setDateUpdated(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateUpdated));

                subcategoryList.add(subcategory);

                cursor.moveToNext();
            }
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return subcategoryList;
    }

    @Override
    public long insert(Subcategory subcategory) {
        SQLiteDatabase db = subcategoryDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SubcategoryContract.SubcategoryEntry.COLUMN_NAME_SUBCATEGORY_ID, subcategory.getSubcategoryId());
        values.put(SubcategoryContract.SubcategoryEntry.COLUMN_NAME_NAME, subcategory.getName());
        values.put(SubcategoryContract.SubcategoryEntry.COLUMN_NAME_CATEGORY_ID, subcategory.getCategoryId());
        values.put(SubcategoryContract.SubcategoryEntry.COLUMN_NAME_DATE_CREATED, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        values.put(SubcategoryContract.SubcategoryEntry.COLUMN_NAME_DATE_UPDATED, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        long rowId = db.insert(SubcategoryContract.SubcategoryEntry.TABLE_NAME,null,values);
        db.close();
        return rowId;
    }

    @Override
    public void deleteBySubcategoryId(String id) {
        SQLiteDatabase db = subcategoryDBHelper.getWritableDatabase();
        String sql = "delete from " + SubcategoryContract.SubcategoryEntry.TABLE_NAME + " where " + SubcategoryContract.SubcategoryEntry.COLUMN_NAME_SUBCATEGORY_ID + " = ?";
        db.execSQL(sql, new Object[]{id});
        db.close();
    }

    @Override
    public void deleteAll() {
        SQLiteDatabase db = subcategoryDBHelper.getWritableDatabase();
        String sql = "delete from " + SubcategoryContract.SubcategoryEntry.TABLE_NAME;
        db.execSQL(sql);
        db.close();
    }

    public long update(Subcategory subcategory){
        SQLiteDatabase db = subcategoryDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SubcategoryContract.SubcategoryEntry.COLUMN_NAME_SUBCATEGORY_ID, subcategory.getSubcategoryId());
        values.put(SubcategoryContract.SubcategoryEntry.COLUMN_NAME_NAME, subcategory.getName());
        values.put(SubcategoryContract.SubcategoryEntry.COLUMN_NAME_CATEGORY_ID, subcategory.getCategoryId());
        values.put(SubcategoryContract.SubcategoryEntry.COLUMN_NAME_DATE_CREATED, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        values.put(SubcategoryContract.SubcategoryEntry.COLUMN_NAME_DATE_UPDATED, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        long rowId = db.insert(SubcategoryContract.SubcategoryEntry.TABLE_NAME,null,values);
        db.close();
        return rowId;
    }

}
