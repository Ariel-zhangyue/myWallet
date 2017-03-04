package com.wjq.dk.zy.mywallet.dataBase.dbHandler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wjq.dk.zy.mywallet.dataBase.dbEntry.ExpenseContract;
import com.wjq.dk.zy.mywallet.dataBase.dbHandler.handlerInterface.ExpenseHandlerInterface;
import com.wjq.dk.zy.mywallet.dataBase.dbHelper.ExpenseDBHelper;
import com.wjq.dk.zy.mywallet.model.Budget;
import com.wjq.dk.zy.mywallet.model.Expense;
import com.wjq.dk.zy.mywallet.model.Subcategory;
import com.wjq.dk.zy.mywallet.util.IDGenerator;

import org.apache.commons.lang3.time.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by wangjiaqi on 16/10/28.
 */
/**
 * # CSIT 6000B    #  DaiKun        20373568          kdai@connect.ust.hk
 * # CSIT 6000B    #  Wang JiaQi    20369969          jwangcu@connect.ust.hk
 * # CSIT 6000B    #  Zhang Yue     20366010          yzhangfk@connect.ust.hk*/
public class ExpenseHandler implements ExpenseHandlerInterface {
    ExpenseDBHelper expenseDBHelper;
    SubcategoryHandler subcategoryHandler;
    BudgetHandler budgetHandler;

    public ExpenseHandler(Context context) {
        expenseDBHelper = new ExpenseDBHelper(context);
        subcategoryHandler = new SubcategoryHandler(context);
        budgetHandler = new BudgetHandler(context);
    }

    public Expense queryByExpenseId(String id) {
        Expense expense = new Expense();
        try {
            SQLiteDatabase db = expenseDBHelper.getReadableDatabase();
            String[] projection = {
                    ExpenseContract.ExpenseEntry.COLUMN_NAME_EXPENSE_ID,
                    ExpenseContract.ExpenseEntry.COLUMN_NAME_BUDGET_ID,
                    ExpenseContract.ExpenseEntry.COLUMN_NAME_AMOUNT,
                    ExpenseContract.ExpenseEntry.COLUMN_NAME_SUBCATEGORY_ID,
                    ExpenseContract.ExpenseEntry.COLUMN_NAME_DAY_CREATED,
                    ExpenseContract.ExpenseEntry.COLUMN_NAME_DATE_CREATED,
                    ExpenseContract.ExpenseEntry.COLUMN_NAME_DATE_UPDATED
            };

            Cursor cursor = db.query(
                    ExpenseContract.ExpenseEntry.TABLE_NAME,  // The table to query
                    projection,                               // The columns to return
                    ExpenseContract.ExpenseEntry.COLUMN_NAME_EXPENSE_ID + " = ?",                                // The columns for the WHERE clause
                    new String[]{id},                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );

            cursor.moveToFirst();
            String ID = cursor.getString(cursor.getColumnIndexOrThrow(ExpenseContract.ExpenseEntry.COLUMN_NAME_EXPENSE_ID));
            String amount = cursor.getString(cursor.getColumnIndexOrThrow(ExpenseContract.ExpenseEntry.COLUMN_NAME_AMOUNT));
            String subCategoryId = cursor.getString(cursor.getColumnIndexOrThrow(ExpenseContract.ExpenseEntry.COLUMN_NAME_SUBCATEGORY_ID));
            String budgetId = cursor.getString(cursor.getColumnIndexOrThrow(ExpenseContract.ExpenseEntry.COLUMN_NAME_BUDGET_ID));
            String dayCreated = cursor.getString(cursor.getColumnIndexOrThrow(ExpenseContract.ExpenseEntry.COLUMN_NAME_DAY_CREATED));
            String dateCreated = cursor.getString(cursor.getColumnIndexOrThrow(ExpenseContract.ExpenseEntry.COLUMN_NAME_DATE_CREATED));
            String dateUpdated = cursor.getString(cursor.getColumnIndexOrThrow(ExpenseContract.ExpenseEntry.COLUMN_NAME_DATE_UPDATED));

            Subcategory subcategory = subcategoryHandler.queryBySubcategoryId(subCategoryId);
            expense.setExpenseId(ID);
            expense.setAmount(amount);
            expense.setSubcategory(subcategory);
            expense.setBudgetId(budgetId);
            expense.setDayCreated(DateUtils.parseDate(dayCreated, "yyyy-MM-dd"));
            expense.setDateCreated(DateUtils.parseDate(dateCreated, "yyyy-MM-dd HH:mm:ss"));
            expense.setDateUpdated(DateUtils.parseDate(dateUpdated, "yyyy-MM-dd HH:mm:ss"));
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return expense;
    }

    public List<String> queryAllDate() {
        SQLiteDatabase db = expenseDBHelper.getReadableDatabase();
        List<String> group = new ArrayList<>();
        String[] columns = new String[]{"distinct day_created"};
        Cursor cursor = db.query(ExpenseContract.ExpenseEntry.TABLE_NAME, columns, null, null, null, null, new String("day_created desc"));
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            group.add(cursor.getString(cursor.getColumnIndexOrThrow(ExpenseContract.ExpenseEntry.COLUMN_NAME_DAY_CREATED)));
        }
        db.close();
        return group;
    }

    public long querySum(String starTime, String endTime) {
        SQLiteDatabase db = expenseDBHelper.getWritableDatabase();

        long sum = 0;

        return sum;
    }

    public long insert(Expense expense) {
        SQLiteDatabase db = expenseDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        String year = String.valueOf(DateUtils.toCalendar(expense.getDayCreated() == null ? new Date() : expense.getDayCreated()).get(Calendar.YEAR));
        String month = String.valueOf(DateUtils.toCalendar(expense.getDayCreated() == null ? new Date() : expense.getDayCreated()).get(Calendar.MONTH)+1);

        Budget budget = budgetHandler.queryByYearAndMonth(year, month);
        if(budget.getBudgetId()==null){
            String ID = IDGenerator.generateID();
            budget = new Budget();
            budget.setBudgetId(ID);
            budget.setAmount("5000.0");
            budget.setExpenseSum("0.0");
            budget.setYear(year);
            budget.setMonth(month);
            budget.setDateCreated(expense.getDayCreated());
            budgetHandler.insert(budget);

        }

        expense.setBudgetId(budget.getBudgetId());

        values.put(ExpenseContract.ExpenseEntry.COLUMN_NAME_EXPENSE_ID, IDGenerator.generateID());
        values.put(ExpenseContract.ExpenseEntry.COLUMN_NAME_AMOUNT, expense.getAmount());
        values.put(ExpenseContract.ExpenseEntry.COLUMN_NAME_SUBCATEGORY_ID, expense.getSubcategory().getSubcategoryId());
        values.put(ExpenseContract.ExpenseEntry.COLUMN_NAME_BUDGET_ID, expense.getBudgetId());
        values.put(ExpenseContract.ExpenseEntry.COLUMN_NAME_DAY_CREATED, new SimpleDateFormat("yyyy-MM-dd").format(expense.getDayCreated() == null ? new Date() : expense.getDayCreated()));
        values.put(ExpenseContract.ExpenseEntry.COLUMN_NAME_DATE_CREATED, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(expense.getDateCreated() == null ? new Date() : expense.getDateCreated()));
        values.put(ExpenseContract.ExpenseEntry.COLUMN_NAME_DATE_UPDATED, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        long rowId = db.insert(ExpenseContract.ExpenseEntry.TABLE_NAME, null, values);


//        budget = budgetHandler.queryByBudgetId(expense.getBudgetId());
        budget.setExpenseSum(String.valueOf(Double.valueOf(budget.getExpenseSum()) + Double.valueOf(expense.getAmount())));
        budgetHandler.update(budget);

        db.close();
        return rowId;
    }

    public void deleteByExpenseId(String id) {
        SQLiteDatabase db = expenseDBHelper.getWritableDatabase();
        String sql = "delete from " + ExpenseContract.ExpenseEntry.TABLE_NAME + " where " + ExpenseContract.ExpenseEntry.COLUMN_NAME_EXPENSE_ID + " = ?";
        db.execSQL(sql, new Object[]{id});
        db.close();
    }

    public long update(Expense expense) {
        SQLiteDatabase db = expenseDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ExpenseContract.ExpenseEntry.COLUMN_NAME_BUDGET_ID, expense.getBudgetId());
        values.put(ExpenseContract.ExpenseEntry.COLUMN_NAME_AMOUNT, expense.getAmount());
        values.put(ExpenseContract.ExpenseEntry.COLUMN_NAME_SUBCATEGORY_ID, expense.getSubcategory().getSubcategoryId());
        values.put(ExpenseContract.ExpenseEntry.COLUMN_NAME_BUDGET_ID, expense.getBudgetId());
        values.put(ExpenseContract.ExpenseEntry.COLUMN_NAME_DAY_CREATED, new SimpleDateFormat("yyyy-MM-dd").format(expense.getDayCreated()));
        values.put(ExpenseContract.ExpenseEntry.COLUMN_NAME_DATE_CREATED, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(expense.getDateCreated()));
        values.put(ExpenseContract.ExpenseEntry.COLUMN_NAME_DATE_UPDATED, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        long rowId = db.update(ExpenseContract.ExpenseEntry.TABLE_NAME, values, ExpenseContract.ExpenseEntry.COLUMN_NAME_EXPENSE_ID + "=?", new String[]{expense.getExpenseId()});
        db.close();
        return rowId;
    }

    @Override
    public List getExpenseListGroupBySubcategory(String startDate, String endDate) {
        List expenseList = new ArrayList();
        try {
            SQLiteDatabase db = expenseDBHelper.getReadableDatabase();
            String[] projection = {
                    "sum(" + ExpenseContract.ExpenseEntry.COLUMN_NAME_AMOUNT + ") as amount",
                    ExpenseContract.ExpenseEntry.COLUMN_NAME_SUBCATEGORY_ID
            };

            Cursor cursor = db.query(
                    ExpenseContract.ExpenseEntry.TABLE_NAME,  // The table to query
                    projection,                               // The columns to return
                    ExpenseContract.ExpenseEntry.COLUMN_NAME_DATE_CREATED + " between  dateTime(?) and dateTime(?)",                                // The columns for the WHERE clause
                    new String[]{startDate, endDate},                            // The values for the WHERE clause
                    ExpenseContract.ExpenseEntry.COLUMN_NAME_SUBCATEGORY_ID,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );

            cursor.moveToFirst();

            for (int i = 0; i < cursor.getCount(); i++) {
                String amount = cursor.getString(cursor.getColumnIndexOrThrow(ExpenseContract.ExpenseEntry.COLUMN_NAME_AMOUNT));
                String subCategoryId = cursor.getString(cursor.getColumnIndexOrThrow(ExpenseContract.ExpenseEntry.COLUMN_NAME_SUBCATEGORY_ID));

                Subcategory subcategory = subcategoryHandler.queryBySubcategoryId(subCategoryId);
                Expense expense = new Expense();
                expense.setAmount(amount);
                expense.setSubcategory(subcategory);

                expenseList.add(expense);
                cursor.moveToNext();
            }
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return expenseList;
    }

    @Override
    public List getExpenseListGroupByDay(String startDate, String endDate) {
        List expenseList = new ArrayList();
        try {
            SQLiteDatabase db = expenseDBHelper.getReadableDatabase();
            String[] projection = {
                    "sum(" + ExpenseContract.ExpenseEntry.COLUMN_NAME_AMOUNT + ") as amount",
                    ExpenseContract.ExpenseEntry.COLUMN_NAME_DAY_CREATED
            };

            Cursor cursor = db.query(
                    ExpenseContract.ExpenseEntry.TABLE_NAME,  // The table to query
                    projection,                               // The columns to return
                    ExpenseContract.ExpenseEntry.COLUMN_NAME_DATE_CREATED + " between  dateTime(?) and dateTime(?)",                                // The columns for the WHERE clause
                    new String[]{startDate, endDate},                            // The values for the WHERE clause
                    ExpenseContract.ExpenseEntry.COLUMN_NAME_DAY_CREATED,                      // don't group the rows
                    null,                                     // don't filter by row groups
                    null                                 // The sort order
            );

            cursor.moveToFirst();

            for (int i = 0; i < cursor.getCount(); i++) {
                String amount = cursor.getString(cursor.getColumnIndexOrThrow(ExpenseContract.ExpenseEntry.COLUMN_NAME_AMOUNT));
                String dayCreated = cursor.getString(cursor.getColumnIndexOrThrow(ExpenseContract.ExpenseEntry.COLUMN_NAME_DAY_CREATED));

                Expense expense = new Expense();
                expense.setAmount(amount);
                expense.setDayCreated(DateUtils.parseDate(dayCreated, "yyyy-MM-dd"));

                expenseList.add(expense);
                cursor.moveToNext();
            }
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return expenseList;
    }

    @Override
    public void deleteAll() {
        SQLiteDatabase db = expenseDBHelper.getWritableDatabase();
        String sql = "delete from " + ExpenseContract.ExpenseEntry.TABLE_NAME;
        db.execSQL(sql);
        db.close();
    }

    public List<Expense> queryByDay(String day) {
        List<Expense> list = new ArrayList<>();
        try {
            SQLiteDatabase db = expenseDBHelper.getReadableDatabase();
            String[] projection = {
                    ExpenseContract.ExpenseEntry.COLUMN_NAME_EXPENSE_ID,
                    ExpenseContract.ExpenseEntry.COLUMN_NAME_BUDGET_ID,
                    ExpenseContract.ExpenseEntry.COLUMN_NAME_AMOUNT,
                    ExpenseContract.ExpenseEntry.COLUMN_NAME_SUBCATEGORY_ID,
                    ExpenseContract.ExpenseEntry.COLUMN_NAME_DAY_CREATED,
                    ExpenseContract.ExpenseEntry.COLUMN_NAME_DATE_CREATED,
                    ExpenseContract.ExpenseEntry.COLUMN_NAME_DATE_UPDATED
            };

            Cursor cursor = db.query(
                    ExpenseContract.ExpenseEntry.TABLE_NAME,
                    projection,
                    ExpenseContract.ExpenseEntry.COLUMN_NAME_DAY_CREATED + " = ?",
                    new String[]{day},
                    null,
                    null,
                    ExpenseContract.ExpenseEntry.COLUMN_NAME_DATE_CREATED + " desc"
            );
            Expense expense;
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                expense = new Expense();
                String ID = cursor.getString(cursor.getColumnIndexOrThrow(ExpenseContract.ExpenseEntry.COLUMN_NAME_EXPENSE_ID));
                String amount = cursor.getString(cursor.getColumnIndexOrThrow(ExpenseContract.ExpenseEntry.COLUMN_NAME_AMOUNT));
                String subCategoryId = cursor.getString(cursor.getColumnIndexOrThrow(ExpenseContract.ExpenseEntry.COLUMN_NAME_SUBCATEGORY_ID));
                String budgetId = cursor.getString(cursor.getColumnIndexOrThrow(ExpenseContract.ExpenseEntry.COLUMN_NAME_BUDGET_ID));
                String dayCreated = cursor.getString(cursor.getColumnIndexOrThrow(ExpenseContract.ExpenseEntry.COLUMN_NAME_DAY_CREATED));
                String dateCreated = cursor.getString(cursor.getColumnIndexOrThrow(ExpenseContract.ExpenseEntry.COLUMN_NAME_DATE_CREATED));
                String dateUpdated = cursor.getString(cursor.getColumnIndexOrThrow(ExpenseContract.ExpenseEntry.COLUMN_NAME_DATE_UPDATED));

                Subcategory subcategory = subcategoryHandler.queryBySubcategoryId(subCategoryId);
                expense.setExpenseId(ID);
                expense.setAmount(amount);
                expense.setSubcategory(subcategory);
                expense.setBudgetId(budgetId);
                expense.setDayCreated(DateUtils.parseDate(dayCreated, "yyyy-MM-dd"));
                expense.setDateCreated(DateUtils.parseDate(dateCreated, "yyyy-MM-dd HH:mm:ss"));
                expense.setDateUpdated(DateUtils.parseDate(dateUpdated, "yyyy-MM-dd HH:mm:ss"));
                list.add(expense);
            }
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
