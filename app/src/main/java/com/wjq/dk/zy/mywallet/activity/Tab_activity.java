package com.wjq.dk.zy.mywallet.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.wjq.dk.zy.mywallet.R;
import com.wjq.dk.zy.mywallet.dataBase.dbHandler.BudgetHandler;
import com.wjq.dk.zy.mywallet.dataBase.dbHandler.ExpenseHandler;
import com.wjq.dk.zy.mywallet.dataBase.dbHandler.SubcategoryHandler;
import com.wjq.dk.zy.mywallet.fragment.AddFragment;
import com.wjq.dk.zy.mywallet.fragment.BudgetFragment;
import com.wjq.dk.zy.mywallet.fragment.HomeFragment;
import com.wjq.dk.zy.mywallet.fragment.SetFragment;
import com.wjq.dk.zy.mywallet.fragment.TrendFragment;
import com.wjq.dk.zy.mywallet.model.Budget;
import com.wjq.dk.zy.mywallet.model.Expense;
import com.wjq.dk.zy.mywallet.model.Subcategory;
import com.wjq.dk.zy.mywallet.util.IDGenerator;
import com.wjq.dk.zy.mywallet.util.NumberFormatUtil;

import org.apache.commons.lang3.time.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
/**
 * # CSIT 6000B    #  DaiKun        20373568          kdai@connect.ust.hk
 * # CSIT 6000B    #  Wang JiaQi    20369969          jwangcu@connect.ust.hk
 * # CSIT 6000B    #  Zhang Yue     20366010          yzhangfk@connect.ust.hk*/
public class Tab_activity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {
    private ArrayList<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {   //set up tab navigation
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_activity);
        initActionBar();
        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE
                );
        bottomNavigationBar
                .setActiveColor("#666666")
                .setInActiveColor("#FFFFFF")
                .setBarBackgroundColor("#f0f0f0");
        bottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.house, "Home").setActiveColorResource(R.color.bottomBar))
                .addItem(new BottomNavigationItem(R.drawable.graph, "Trend").setActiveColorResource(R.color.bottomBar))
                .addItem(new BottomNavigationItem(R.drawable.edit, "Add").setActiveColorResource(R.color.bottomBar))
                .addItem(new BottomNavigationItem(R.drawable.rich, "Budget").setActiveColorResource(R.color.bottomBar))
                .addItem(new BottomNavigationItem(R.drawable.settings, "Setting").setActiveColorResource(R.color.bottomBar))
                .setFirstSelectedPosition(0)
                .initialise();

        fragments = getFragments();
        setDefaultFragment();
        bottomNavigationBar.setTabSelectedListener(this);

        checkBudget();

        getTestExpenses();
    }

    private ArrayList<Fragment> getFragments() {  //set up 5 main fragments
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(HomeFragment.newInstance("Home"));
        fragments.add(TrendFragment.newInstance("Trend"));
        fragments.add(AddFragment.newInstance("Add"));
        fragments.add(BudgetFragment.newInstance("Budget"));
        //fragments.add(BudgetDetailFragment.newInstance("Detail"));
        fragments.add(SetFragment.newInstance("Set"));
        return fragments;
    }

    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.realtabcontent, HomeFragment.newInstance("Home"));
        transaction.commit();
    }

    @Override
    public void onTabSelected(int position) {  // Switching between different fragments
        if (fragments != null) {
            if (position < fragments.size()) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment fragment = fragments.get(position);
                ft.replace(R.id.realtabcontent, fragment);
                ft.addToBackStack(null);
                ft.commitAllowingStateLoss();
            }
        }
    }

    @Override
    public void onTabUnselected(int position) {
        if (fragments != null) {
            if (position < fragments.size()) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment fragment = fragments.get(position);
                ft.remove(fragment);
                ft.commitAllowingStateLoss();
            }
        }
    }

    @Override
    public void onTabReselected(int position) {

    }

    public void initActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);
        setTitle("MyWallet");
    }

    private void checkBudget() {   //If there is no budget exist, set up one record whose amount is 0 by default
        BudgetHandler budgetHandler = new BudgetHandler(getApplicationContext());
        String year = String.valueOf(DateUtils.toCalendar(new Date()).get(Calendar.YEAR));
        String month = String.valueOf(DateUtils.toCalendar(new Date()).get(Calendar.MONTH)+1);
        Budget budget = budgetHandler.queryByYearAndMonth(year, month);
        if (budget.getBudgetId() == null) {
            String ID = IDGenerator.generateID();
            budget = new Budget();
            budget.setBudgetId(ID);
            budget.setAmount("0.0");
            budget.setExpenseSum("0.0");
            budget.setYear(year);
            budget.setMonth(month);
            budgetHandler.insert(budget);
        }
    }

    private List getTestExpenses() {   //Get initial testing data

        ExpenseHandler expenseHandler = new ExpenseHandler(getApplicationContext());
        SubcategoryHandler subcategoryHandler = new SubcategoryHandler(getApplicationContext());
        BudgetHandler budgetHandler = new BudgetHandler(getApplicationContext());

        expenseHandler.deleteAll();
        subcategoryHandler.deleteAll();
        budgetHandler.deleteAll();

        List subcategoryList = new ArrayList();
        String[] subcategoryName = new String[]{"Lunch", "Pants","Hotel","Subway"};
        for (int i = 1; i < 5; i++) {
            Subcategory subcategory = new Subcategory();
            subcategory.setSubcategoryId(String.valueOf(i));
            subcategory.setName(subcategoryName[i-1]);
            subcategory.setCategoryId(String.valueOf(i));
            subcategoryList.add(subcategory);
            subcategoryHandler.insert(subcategory);

        }


        List list = new ArrayList();
        for (int i = 0; i < 90; i++) {
            Expense expense = new Expense();
            expense.setAmount(NumberFormatUtil.format(String.valueOf(i + Math.random() * 10)));
            Calendar calendar = DateUtils.toCalendar(new Date());
            calendar.add(Calendar.DAY_OF_MONTH, -1 * i);
            expense.setDateCreated(calendar.getTime());
            expense.setDayCreated(calendar.getTime());
            expense.setSubcategory((Subcategory) subcategoryList.get(i % 3));
            list.add(expense);


            expenseHandler.insert(expense);
        }
        return list;
    }
}


