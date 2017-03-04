package com.wjq.dk.zy.mywallet.dataBase.dbHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wjq.dk.zy.mywallet.dataBase.dbEntry.BudgetContract;
import com.wjq.dk.zy.mywallet.dataBase.dbHandler.handlerInterface.BudgetHandlerInterface;
import com.wjq.dk.zy.mywallet.dataBase.dbHelper.BudgetDBHelper;
import com.wjq.dk.zy.mywallet.model.Budget;
import com.wjq.dk.zy.mywallet.util.IDGenerator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wangjiaqi on 16/10/27.
 */
/**
 * # CSIT 6000B    #  DaiKun        20373568          kdai@connect.ust.hk
 * # CSIT 6000B    #  Wang JiaQi    20369969          jwangcu@connect.ust.hk
 * # CSIT 6000B    #  Zhang Yue     20366010          yzhangfk@connect.ust.hk*/
public class BudgetHandler implements BudgetHandlerInterface {
    BudgetDBHelper budgetDBHelper;


    public BudgetHandler(Context context) {
        this.budgetDBHelper = new BudgetDBHelper(context);
    }

    public Budget queryByBudgetId(String id) {
        Budget budget = new Budget();
        try {
            SQLiteDatabase db = budgetDBHelper.getReadableDatabase();
            String[] projection = {
                    BudgetContract.BudgetEntry.COLUMN_NAME_BUDGET_ID,
                    BudgetContract.BudgetEntry.COLUMN_NAME_AMOUNT,
                    BudgetContract.BudgetEntry.COLUMN_NAME_EXPENSE_SUM,
                    BudgetContract.BudgetEntry.COLUMN_NAME_MONTH,
                    BudgetContract.BudgetEntry.COLUMN_NAME_YEAR,
                    BudgetContract.BudgetEntry.COLUMN_NAME_DATE_CREATED,
                    BudgetContract.BudgetEntry.COLUMN_NAME_DATE_UPDATED
            };

            // How you want the results sorted in the resulting Cursor
//        String sortOrder =
//                BudgetContract.BudgetEntry.COLUMN_NAME_DATE_UPDATED + " DESC";

            Cursor cursor = db.query(
                    BudgetContract.BudgetEntry.TABLE_NAME,  // The table to query
                    projection,                               // The columns to return
                    BudgetContract.BudgetEntry.COLUMN_NAME_BUDGET_ID + " = ?",                                // The columns for the WHERE clause
                    new String[]{id},                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );

            cursor.moveToFirst();
//            for (int i = 0; i < cursor.getCount(); i++) {
            String ID = cursor.getString(cursor.getColumnIndexOrThrow(BudgetContract.BudgetEntry.COLUMN_NAME_BUDGET_ID));
            String amount = cursor.getString(cursor.getColumnIndexOrThrow(BudgetContract.BudgetEntry.COLUMN_NAME_AMOUNT));
            String expenseSum = cursor.getString(cursor.getColumnIndexOrThrow(BudgetContract.BudgetEntry.COLUMN_NAME_EXPENSE_SUM));
            String month = cursor.getString(cursor.getColumnIndexOrThrow(BudgetContract.BudgetEntry.COLUMN_NAME_MONTH));
            String year = cursor.getString(cursor.getColumnIndexOrThrow(BudgetContract.BudgetEntry.COLUMN_NAME_YEAR));
            String dateCreated = cursor.getString(cursor.getColumnIndexOrThrow(BudgetContract.BudgetEntry.COLUMN_NAME_DATE_CREATED));
            String dateUpdated = cursor.getString(cursor.getColumnIndexOrThrow(BudgetContract.BudgetEntry.COLUMN_NAME_DATE_UPDATED));

            budget.setBudgetId(ID);
            budget.setAmount(amount);
            budget.setExpenseSum(expenseSum);
            budget.setMonth(month);
            budget.setYear(year);
            budget.setDateCreated(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateCreated));
            budget.setDateUpdated(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateUpdated));

            db.close();
            //cursor.moveToNext();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return budget;
    }

    @Override
    public Budget queryByYearAndMonth(String yearPara, String monthPara) {
        Budget budget = new Budget();
        try {
            SQLiteDatabase db = budgetDBHelper.getReadableDatabase();
            String[] projection = {
                    BudgetContract.BudgetEntry.COLUMN_NAME_BUDGET_ID,
                    BudgetContract.BudgetEntry.COLUMN_NAME_AMOUNT,
                    BudgetContract.BudgetEntry.COLUMN_NAME_EXPENSE_SUM,
                    BudgetContract.BudgetEntry.COLUMN_NAME_MONTH,
                    BudgetContract.BudgetEntry.COLUMN_NAME_YEAR,
                    BudgetContract.BudgetEntry.COLUMN_NAME_DATE_CREATED,
                    BudgetContract.BudgetEntry.COLUMN_NAME_DATE_UPDATED
            };

            // How you want the results sorted in the resulting Cursor
//        String sortOrder =
//                BudgetContract.BudgetEntry.COLUMN_NAME_DATE_UPDATED + " DESC";

            Cursor cursor = db.query(
                    BudgetContract.BudgetEntry.TABLE_NAME,  // The table to query
                    projection,                               // The columns to return
                    BudgetContract.BudgetEntry.COLUMN_NAME_YEAR + " = ? and " + BudgetContract.BudgetEntry.COLUMN_NAME_MONTH + " = ?",                                // The columns for the WHERE clause
                    new String[]{yearPara, monthPara},                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );


            if (cursor.getCount()!=0) {
                cursor.moveToFirst();
//            for (int i = 0; i < cursor.getCount(); i++) {
                String ID = cursor.getString(cursor.getColumnIndexOrThrow(BudgetContract.BudgetEntry.COLUMN_NAME_BUDGET_ID));
                String amount = cursor.getString(cursor.getColumnIndexOrThrow(BudgetContract.BudgetEntry.COLUMN_NAME_AMOUNT));
                String expenseSum = cursor.getString(cursor.getColumnIndexOrThrow(BudgetContract.BudgetEntry.COLUMN_NAME_EXPENSE_SUM));
                String month = cursor.getString(cursor.getColumnIndexOrThrow(BudgetContract.BudgetEntry.COLUMN_NAME_MONTH));
                String year = cursor.getString(cursor.getColumnIndexOrThrow(BudgetContract.BudgetEntry.COLUMN_NAME_YEAR));
                String dateCreated = cursor.getString(cursor.getColumnIndexOrThrow(BudgetContract.BudgetEntry.COLUMN_NAME_DATE_CREATED));
                String dateUpdated = cursor.getString(cursor.getColumnIndexOrThrow(BudgetContract.BudgetEntry.COLUMN_NAME_DATE_UPDATED));

                budget.setBudgetId(ID);
                budget.setAmount(amount);
                budget.setExpenseSum(expenseSum);
                budget.setMonth(month);
                budget.setYear(year);
                budget.setDateCreated(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateCreated));
                budget.setDateUpdated(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateUpdated));
            }
            db.close();
            //cursor.moveToNext();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return budget;
    }

    public List<Budget> queryAllBudget() {
        List<Budget> bList = new ArrayList<>();
        try {
            SQLiteDatabase db = budgetDBHelper.getReadableDatabase();
            String[] projection = {
                    BudgetContract.BudgetEntry.COLUMN_NAME_BUDGET_ID,
                    BudgetContract.BudgetEntry.COLUMN_NAME_AMOUNT,
                    BudgetContract.BudgetEntry.COLUMN_NAME_EXPENSE_SUM,
                    BudgetContract.BudgetEntry.COLUMN_NAME_MONTH,
                    BudgetContract.BudgetEntry.COLUMN_NAME_YEAR,
                    BudgetContract.BudgetEntry.COLUMN_NAME_DATE_CREATED,
                    BudgetContract.BudgetEntry.COLUMN_NAME_DATE_UPDATED
            };

            // How you want the results sorted in the resulting Cursor
//        String sortOrder =
//                BudgetContract.BudgetEntry.COLUMN_NAME_DATE_UPDATED + " DESC";

            Cursor cursor = db.query(
                    BudgetContract.BudgetEntry.TABLE_NAME,  // The table to query
                    projection,                               // The columns to return
                    null,                                // The columns for the WHERE clause
                    null,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    BudgetContract.BudgetEntry.COLUMN_NAME_DATE_CREATED + " desc"                     // The sort order
            );
            Budget budget;

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                budget = new Budget();
                String ID = cursor.getString(cursor.getColumnIndexOrThrow(BudgetContract.BudgetEntry.COLUMN_NAME_BUDGET_ID));
                String amount = cursor.getString(cursor.getColumnIndexOrThrow(BudgetContract.BudgetEntry.COLUMN_NAME_AMOUNT));
                String expenseSum = cursor.getString(cursor.getColumnIndexOrThrow(BudgetContract.BudgetEntry.COLUMN_NAME_EXPENSE_SUM));
                String month = cursor.getString(cursor.getColumnIndexOrThrow(BudgetContract.BudgetEntry.COLUMN_NAME_MONTH));
                String year = cursor.getString(cursor.getColumnIndexOrThrow(BudgetContract.BudgetEntry.COLUMN_NAME_YEAR));
                String dateCreated = cursor.getString(cursor.getColumnIndexOrThrow(BudgetContract.BudgetEntry.COLUMN_NAME_DATE_CREATED));
                String dateUpdated = cursor.getString(cursor.getColumnIndexOrThrow(BudgetContract.BudgetEntry.COLUMN_NAME_DATE_UPDATED));

                budget.setBudgetId(ID);
                budget.setAmount(amount);
                budget.setExpenseSum(expenseSum);
                budget.setMonth(month);

                budget.setYear(year);
                budget.setDateCreated(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateCreated));
                budget.setDateUpdated(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateUpdated));
                bList.add(budget);
                if (bList.size() > 5) {
                    break;
                }
                //cursor.moveToNext();
            }
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bList;
    }

    public long insert(Budget budget) {
        SQLiteDatabase db = budgetDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BudgetContract.BudgetEntry.COLUMN_NAME_BUDGET_ID, budget.getBudgetId()!=null? budget.getBudgetId():IDGenerator.generateID());
        values.put(BudgetContract.BudgetEntry.COLUMN_NAME_AMOUNT, budget.getAmount());
        values.put(BudgetContract.BudgetEntry.COLUMN_NAME_EXPENSE_SUM, budget.getExpenseSum());
        values.put(BudgetContract.BudgetEntry.COLUMN_NAME_MONTH, budget.getMonth());
        values.put(BudgetContract.BudgetEntry.COLUMN_NAME_YEAR, budget.getYear());
        values.put(BudgetContract.BudgetEntry.COLUMN_NAME_DATE_CREATED, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        values.put(BudgetContract.BudgetEntry.COLUMN_NAME_DATE_UPDATED, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        long rowId = db.insert(BudgetContract.BudgetEntry.TABLE_NAME, null, values);
        db.close();
        return rowId;
    }

    public void deleteByBudgetId(String id) {
        SQLiteDatabase db = budgetDBHelper.getWritableDatabase();
        String sql = "delete from " + BudgetContract.BudgetEntry.TABLE_NAME + " where " + BudgetContract.BudgetEntry.COLUMN_NAME_BUDGET_ID + " = ?";
        db.execSQL(sql, new Object[]{id});
        db.close();
    }


    public void deleteAll() {
        SQLiteDatabase db = budgetDBHelper.getWritableDatabase();
        String sql = "delete from " + BudgetContract.BudgetEntry.TABLE_NAME;
        db.execSQL(sql);
        db.close();
    }


    public long update(Budget budget) {
        SQLiteDatabase db = budgetDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BudgetContract.BudgetEntry.COLUMN_NAME_BUDGET_ID, budget.getBudgetId());
        values.put(BudgetContract.BudgetEntry.COLUMN_NAME_AMOUNT, budget.getAmount());
        values.put(BudgetContract.BudgetEntry.COLUMN_NAME_EXPENSE_SUM, budget.getExpenseSum());
        values.put(BudgetContract.BudgetEntry.COLUMN_NAME_MONTH, budget.getMonth());
        values.put(BudgetContract.BudgetEntry.COLUMN_NAME_YEAR, budget.getYear());
        values.put(BudgetContract.BudgetEntry.COLUMN_NAME_DATE_CREATED, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(budget.getDateCreated()));
        values.put(BudgetContract.BudgetEntry.COLUMN_NAME_DATE_UPDATED, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        long rowId = db.update(BudgetContract.BudgetEntry.TABLE_NAME, values, BudgetContract.BudgetEntry.COLUMN_NAME_BUDGET_ID + "=?", new String[]{budget.getBudgetId()});
        db.close();
        return rowId;
    }
}
