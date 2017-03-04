package com.wjq.dk.zy.mywallet.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wjq.dk.zy.mywallet.R;
import com.wjq.dk.zy.mywallet.customView.NumberProgressBar;
import com.wjq.dk.zy.mywallet.dataBase.dbHandler.BudgetHandler;
import com.wjq.dk.zy.mywallet.dataBase.dbHandler.ExpenseHandler;
import com.wjq.dk.zy.mywallet.dataBase.dbHandler.SubcategoryHandler;
import com.wjq.dk.zy.mywallet.model.Budget;
import com.wjq.dk.zy.mywallet.model.Expense;
import com.wjq.dk.zy.mywallet.model.Subcategory;
import com.wjq.dk.zy.mywallet.util.ChartGenerator;
import com.wjq.dk.zy.mywallet.util.NumberFormatUtil;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lecho.lib.hellocharts.view.PieChartView;


/**
 * Created by Elvn on 2016/10/30.
 */
/**
 * # CSIT 6000B    #  DaiKun        20373568          kdai@connect.ust.hk
 * # CSIT 6000B    #  Wang JiaQi    20369969          jwangcu@connect.ust.hk
 * # CSIT 6000B    #  Zhang Yue     20366010          yzhangfk@connect.ust.hk*/
public class BudgetDetailFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private Date startDate;
    private Date endDate;
    private PieChartView pieChart;
    private NumberProgressBar bdBNP;
    private String budgetId;
    BudgetHandler budgetHandler;
    Budget budget;
    Button modifyBudget;
    private String m_Text = "";
    private FragmentManager manager;
    private FragmentTransaction ft;
    private BudgetDetailFragment budgetDetailFragment;

    public static BudgetDetailFragment newInstance(String param1) {
        BudgetDetailFragment fragment = new BudgetDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (container == null) {
            return null;
        }


        Bundle bundle = getArguments();        //get exact budget id from budgetFragment
        budget = (Budget) bundle.getSerializable("exactBudget");
        getActivity().setTitle("Budget Detail");
        final View contentView = inflater.inflate(R.layout.fragment_budget_detail, null);
        pieChart = (PieChartView) contentView.findViewById(R.id.detailPieChartForExpense);
        bdBNP = (NumberProgressBar) contentView.findViewById(R.id.budgetDetailNumberProgressBar);

//        if(budget.getAmount().lastIndexOf(".")!=-1){
        bdBNP.setMax(Integer.valueOf(budget.getAmount().substring(0, budget.getAmount().lastIndexOf("."))));   //set budgetAmount as the max value of progress bar
//        }else{
//            bdBNP.setMax(Integer.valueOf(budget.getAmount()));   //set budgetAmount as the max value of progress bar
//        }
//
//        if(budget.getAmount().lastIndexOf(".")!=-1){
        bdBNP.setProgress(Integer.valueOf(budget.getExpenseSum().substring(0, budget.getExpenseSum().lastIndexOf("."))));
//        }else{
//            bdBNP.setProgress(Integer.valueOf(budget.getExpenseSum()));
//        }

        modifyBudget = (Button) contentView.findViewById(R.id.modify_budget);
        modifyBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //click modify button to re-set budget amount through self-designed dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Modify");
                View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.budget_dialog, null);
                // Set up the input
                final EditText input = (EditText) viewInflated.findViewById(R.id.budget_dialog_input);
                // Specify the type of input expected;
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setText(budget.getAmount());
                builder.setView(viewInflated);

                // Set up the buttons
                builder.setPositiveButton(android.R.string.ok, new SetOnClickListener(input));
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
        getBudgetDetailTime(); // calculate time period for that month


        List expenses = getExpenseListGroupBySubcategory(DateFormatUtils.format(startDate, "yyyy-MM-dd"), DateFormatUtils.format(endDate, "yyyy-MM-dd"));
        if (expenses.size() != 0) {
            //generate the piechart
            ChartGenerator.generatePieChart(pieChart, expenses);
        } else { //if there is no expense record for that month
            Toast.makeText(getActivity(), "No expenses in this month", Toast.LENGTH_SHORT).show();
        }


        return contentView;
    }

    public class SetOnClickListener implements DialogInterface.OnClickListener {

        final EditText input;

        public SetOnClickListener(EditText input) {
            this.input = input;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            if (!"".equals(input.getText().toString()) && input.getText().toString() != null) {  // not null
                m_Text = input.getText().toString();    //add value    m_Text 是从input获取的category的字符串
            }
            m_Text = NumberFormatUtil.format(m_Text);

            Budget modified_budget = budget;
            modified_budget.setAmount(m_Text);
            BudgetHandler budgetHandler = new BudgetHandler(getContext());
            budgetHandler.update(modified_budget);

            Bundle bundle = new Bundle();
            bundle.putSerializable("exactBudget", modified_budget);

            manager = getFragmentManager();
            budgetDetailFragment = new BudgetDetailFragment();
            ft = manager.beginTransaction();
            budgetDetailFragment.setArguments(bundle);
            ft.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_right_out);
            ft.replace(R.id.realtabcontent, budgetDetailFragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    }


    private List getExpenseListGroupBySubcategory(String startDate, String endDate) {

        ExpenseHandler expenseHandler = new ExpenseHandler(getContext());
        List expenseList = expenseHandler.getExpenseListGroupBySubcategory(startDate, endDate);

        return expenseList;

    }

    public void getBudgetDetailTime() { //set beginning time as 1st day and end date as the last day of this month
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String sDate = budget.getYear() + "-" + budget.getMonth() + "-01";
        try {
            startDate = format.parse(sDate);
            Log.i("starTime", sDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        endDate = DateUtils.addDays(DateUtils.addMonths(startDate, 1), -1);
        Log.i("endTime", endDate.toString());


    }

    private List getTestExpenses() {  //get test data

        ExpenseHandler expenseHandler = new ExpenseHandler(getContext());
        SubcategoryHandler subcategoryHandler = new SubcategoryHandler(getContext());

        expenseHandler.deleteAll();
        subcategoryHandler.deleteAll();

        List subcategoryList = new ArrayList();
        for (int i = 0; i < 3; i++) {
            Subcategory subcategory = new Subcategory();
            subcategory.setSubcategoryId(String.valueOf(i));
            subcategory.setName(String.valueOf(i));
            subcategory.setCategoryId("1");
            subcategoryList.add(subcategory);
            subcategoryHandler.insert(subcategory);
        }


        List list = new ArrayList();
        for (int i = 0; i < 30; i++) {
            Expense expense = new Expense();
            expense.setAmount(String.valueOf(i + Math.random() * 10));
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
