package com.wjq.dk.zy.mywallet.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.wjq.dk.zy.mywallet.R;
import com.wjq.dk.zy.mywallet.dataBase.dbHandler.BudgetHandler;
import com.wjq.dk.zy.mywallet.dataBase.dbHandler.ExpenseHandler;
import com.wjq.dk.zy.mywallet.dataBase.dbHandler.SubcategoryHandler;
import com.wjq.dk.zy.mywallet.model.Budget;
import com.wjq.dk.zy.mywallet.model.Expense;
import com.wjq.dk.zy.mywallet.model.Subcategory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * # CSIT 6000B    #  DaiKun        20373568          kdai@connect.ust.hk
 * # CSIT 6000B    #  Wang JiaQi    20369969          jwangcu@connect.ust.hk
 * # CSIT 6000B    #  Zhang Yue     20366010          yzhangfk@connect.ust.hk*/
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //DBTest
        TextView dbResult = (TextView) findViewById(R.id.DBResult);
        //getApplicationContext().deleteDatabase("Budget.db");
        String result = DBTest();
        dbResult.setText(result);
        DBExpenseTest();
        DBSubcategoryTest();

    }

    private List getTestExpenseList() throws ParseException {
        List expenses = new ArrayList();
        for(int i=0;i<7;i++){
            Expense expense = new Expense();
            expense.setAmount(String.valueOf(10*i));
            expense.setDateCreated(new SimpleDateFormat("dd-MM-yyyy").parse(new Date().toString()));
            expenses.add(expense);
        }
        return expenses;
    }

    private String DBTest() {
        String result;
        BudgetHandler budgetHandler = new BudgetHandler(getApplicationContext());
        Budget budget = new Budget();
        budget.setBudgetId("1");
        budget.setAmount("1000");
        budget.setMonth("10");
        budget.setYear("2016");
        budget.setDateCreated(new Date());
        budget.setDateUpdated(new Date());

        result = "Original Budget is :" + budget.getBudgetId() + " , " + budget.getBudgetId() + " , " + budget.getAmount() + " , " + budget.getYear() + " , " + budget.getMonth() + " , " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(budget.getDateCreated()) +"\n";

        budgetHandler.insert(budget);
        budget.setAmount("2500");
        budget.setYear("2018");
        budget.setMonth("9");
        budgetHandler.update(budget);
        budget = budgetHandler.queryByBudgetId(budget.getBudgetId());

        result = result + "Updated Budget is :" + budget.getBudgetId() + " , " + budget.getBudgetId() + " , " + budget.getAmount() + " , " + budget.getYear() + " , " + budget.getMonth() + " , " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(budget.getDateCreated()) +"\n";

        return result;

    }
    private void DBExpenseTest(){
        ExpenseHandler expenseHandler = new ExpenseHandler(getApplicationContext());
        Expense expense = new Expense();
        expense.setAmount("100");
        expense.setBudgetId("1");
        expense.setDateCreated(new Date());
        expense.setDateUpdated(new Date());
        expense.setExpenseId("001");
        Log.i("Test","Original Expense is :"+expense.getExpenseId()+ " , " + expense.getAmount()+ " , " + expense.getBudgetId()+ " , " +new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(expense.getDateCreated()) );
        expenseHandler.insert(expense);

        Expense mExpense = new Expense();
        mExpense = expenseHandler.queryByExpenseId("001");
        Log.i("Test","Queried Expense is :"+mExpense.getExpenseId()+ " , " + mExpense.getAmount()+ " , " + mExpense.getBudgetId()+ " , " +new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(mExpense.getDateCreated()) );

        List<String>list = expenseHandler.queryAllDate();
        Log.i("Test","There are :"+list.size()+"days have record");


    }
    private void DBSubcategoryTest(){
        SubcategoryHandler subcategoryHandler = new SubcategoryHandler(getApplicationContext());
        Subcategory subcategory = new Subcategory();
        subcategory.setCategoryId("1");
        subcategory.setDateCreated(new Date());
        subcategory.setName("Test");
        String id = subcategory.getSubcategoryId();
        subcategoryHandler.insert(subcategory);
        Log.i("Test","Original Subcategory is :"+ subcategory.getName() + " , " + subcategory.getCategoryId()+new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(subcategory.getDateCreated()) );
        Subcategory subcategory1 = subcategoryHandler.queryBySubcategoryId(id);
        Log.i("Test","Queried Subcategory is :"+ subcategory1.getName() + " , " + subcategory1.getCategoryId()+new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(subcategory1.getDateCreated()) );

    }
}

