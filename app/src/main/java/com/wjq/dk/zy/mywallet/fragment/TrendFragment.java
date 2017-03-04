package com.wjq.dk.zy.mywallet.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.wjq.dk.zy.mywallet.R;
import com.wjq.dk.zy.mywallet.dataBase.dbHandler.ExpenseHandler;
import com.wjq.dk.zy.mywallet.dataBase.dbHandler.SubcategoryHandler;
import com.wjq.dk.zy.mywallet.model.Expense;
import com.wjq.dk.zy.mywallet.model.Subcategory;
import com.wjq.dk.zy.mywallet.util.ChartGenerator;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.ComboLineColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PieChartView;
import lecho.lib.hellocharts.view.PreviewColumnChartView;
import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by Elvn on 2016/10/30.
 */
/**
 * # CSIT 6000B    #  DaiKun        20373568          kdai@connect.ust.hk
 * # CSIT 6000B    #  Wang JiaQi    20369969          jwangcu@connect.ust.hk
 * # CSIT 6000B    #  Zhang Yue     20366010          yzhangfk@connect.ust.hk*/
public class TrendFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;

    private FancyButton weekQueryButton;
    private FancyButton monthQueryButton;
    private FancyButton yearQueryButton;

    private Date startDate;
    private Date endDate;
//    private int daysInterval;

    private LinearLayout comboLineColumnChartField;
    private LinearLayout previewColumnChartField;
    private LinearLayout columnLineDependencyField;


    private PieChartView pieChart;
    private ComboLineColumnChartView comboLineColumnChart;
    private ColumnChartView columnChartForPreview;
    private PreviewColumnChartView previewColumnChart;
    private LineChartView lineChart;
    private ColumnChartView columnChartForDependency;

    private static final int COMBOCHART_DAYS_NUMBER = 11;
    private static final int PREVIEWCHART_DAYS_NUMBER = 60;

    private static final int COMBOCHART_TYPE = 1;
    private static final int PREVIEWCHART_TYPE = 2;
    private static final int COLUMN_LINE_DEPENDENCY_TYPE = 3;

    public static TrendFragment newInstance(String param1) {
        TrendFragment fragment = new TrendFragment();
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
        getActivity().setTitle("Trend");
        final View contentView = inflater.inflate(R.layout.fragment_trend, container, false);


        //get button object and set onClickListener
        // display the chart in three different time period for better understanding of expense trend
        weekQueryButton = (FancyButton) contentView.findViewById(R.id.weekChartQuery);
        monthQueryButton = (FancyButton) contentView.findViewById(R.id.monthChartQuery);
        yearQueryButton = (FancyButton) contentView.findViewById(R.id.yearChartQuery);

        weekQueryButton.setOnClickListener(this);
        monthQueryButton.setOnClickListener(this);
        yearQueryButton.setOnClickListener(this);

        //get layout
        comboLineColumnChartField = (LinearLayout) contentView.findViewById(R.id.comboLineColumnChartField);
        previewColumnChartField = (LinearLayout) contentView.findViewById(R.id.previewColumnChartField);
        columnLineDependencyField = (LinearLayout) contentView.findViewById(R.id.columnLineDependencyField);

//        comboLineColumnChartField.setVisibility(View.GONE);
//        previewColumnChartField.setVisibility(View.GONE);
//        columnLineDependencyField.setVisibility(View.GONE);

        //get chart object of different types
        pieChart = (PieChartView) contentView.findViewById(R.id.pieChartForExpense);

        comboLineColumnChart = (ComboLineColumnChartView) contentView.findViewById(R.id.comboLineColumnChart);

        columnChartForPreview = (ColumnChartView) contentView.findViewById(R.id.columnChartForPreview);
        previewColumnChart = (PreviewColumnChartView) contentView.findViewById(R.id.previewColumnChart);

        lineChart = (LineChartView) contentView.findViewById(R.id.lineChart);
        columnChartForDependency = (ColumnChartView) contentView.findViewById(R.id.columnChartForDependency);


//        getTestExpenses();

        return contentView;
    }

    /**
     * generate charts when button is clicked
     */
    private void generateChart() {
        //get the expenses to be displayed in the chart
        List expenses = getExpenseListGroupBySubcategory(DateFormatUtils.format(startDate, "yyyy-MM-dd"), DateFormatUtils.format(endDate, "yyyy-MM-dd"));

        //generate the piechart
        ChartGenerator.generatePieChart(pieChart, expenses);

        //draw the column&line chart
        generateColumnOrLineChart(startDate, endDate);
    }

    /**
     * draw the column or line chart in the fragment
     */
    private void generateColumnOrLineChart(Date startDate, Date endDate) {

        List dayExpenseList = getExpenseListGroupByDay(DateFormatUtils.format(startDate, "yyyy-MM-dd"), DateFormatUtils.format(endDate, "yyyy-MM-dd"));
        //get the chart type in term of the days interval
        int chartType = getChartType(getdaysInterval(startDate,endDate));

        if (chartType == COMBOCHART_TYPE) {
            //hide the chart layout  we do not wanna display
            comboLineColumnChartField.setVisibility(View.VISIBLE);
            previewColumnChartField.setVisibility(View.GONE);
            columnLineDependencyField.setVisibility(View.GONE);

            //generate the chart

            ChartGenerator.generateComboChartForDay(comboLineColumnChart, dayExpenseList);

        } else if (chartType == PREVIEWCHART_TYPE) {
            //hide the chart layout  we do not wanna display
            comboLineColumnChartField.setVisibility(View.GONE);
            previewColumnChartField.setVisibility(View.VISIBLE);
            columnLineDependencyField.setVisibility(View.GONE);

            //generate the chart

            ChartGenerator.generatePreviewColumnChartForMonth(columnChartForPreview, previewColumnChart, dayExpenseList);

        } else if (chartType == COLUMN_LINE_DEPENDENCY_TYPE) {
            //hide the chart layout  we do not wanna display
            comboLineColumnChartField.setVisibility(View.GONE);
            previewColumnChartField.setVisibility(View.GONE);
            columnLineDependencyField.setVisibility(View.VISIBLE);

            //generate the chart
            ChartGenerator.generateLineColumnDependencyForYear(lineChart, columnChartForDependency, dayExpenseList, getMonthExpenseList(startDate, endDate));
        }
    }

    /**
     * get interval between two dates
     * @param startDate
     * @param endDate
     * @return
     */
    private int getdaysInterval(Date startDate,Date endDate){
        long milli = Math.abs(endDate.getTime()-startDate.getTime());
        return (int) (milli/(60*60*24*1000));
    }

    /**
     * get what chart should be displayed in term of the days interval
     *
     * @param days
     * @return
     */

    private int getChartType(int days) {
        if (days < COMBOCHART_DAYS_NUMBER) {
            return COMBOCHART_TYPE;
        } else if (days < PREVIEWCHART_DAYS_NUMBER) {
            return PREVIEWCHART_TYPE;
        }
        return COLUMN_LINE_DEPENDENCY_TYPE;

    }


    /**
     * @return
     */
    private Date addDays(Date date, int days) {
        Calendar calendar = DateUtils.toCalendar(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }

    private List getExpenseListGroupBySubcategory(String startDate, String endDate) {

        ExpenseHandler expenseHandler = new ExpenseHandler(getContext());
        List expenseList = expenseHandler.getExpenseListGroupBySubcategory(startDate, endDate);

        return expenseList;

    }

    private List getExpenseListGroupByDay(String startDate, String endDate) {

        ExpenseHandler expenseHandler = new ExpenseHandler(getContext());
        List expenseList = expenseHandler.getExpenseListGroupByDay(startDate, endDate);

        return expenseList;

    }

    private List getMonthExpenseList(Date startDate, Date endDate) {
        ExpenseHandler expenseHandler = new ExpenseHandler(getContext());

        List expensesInDay;
        List expensesInMonth = new ArrayList();

        while (DateUtils.addMonths(startDate, 1).before(endDate)) {
            expensesInDay = expenseHandler.getExpenseListGroupByDay(DateFormatUtils.format(startDate, "yyyy-MM-dd"), DateFormatUtils.format(DateUtils.addMonths(startDate, 1), "yyyy-MM-dd"));
            Expense expense = new Expense();
            expense.setAmount("0");
            for (int i = 0; i < expensesInDay.size(); i++) {
                Expense expenseInDay = (Expense) expensesInDay.get(i);
                expense.setAmount(String.valueOf(Double.valueOf(expense.getAmount()) + Double.valueOf(expenseInDay.getAmount())));
            }
            expense.setDayCreated(startDate);
            expensesInMonth.add(expense);
            startDate = DateUtils.addMonths(startDate, 1);
        }
        expensesInDay = expenseHandler.getExpenseListGroupByDay(DateFormatUtils.format(startDate, "yyyy-MM-dd"), DateFormatUtils.format(endDate, "yyyy-MM-dd"));
        Expense expense = new Expense();
        expense.setAmount("0");
        for (int i = 0; i < expensesInDay.size(); i++) {
            Expense expenseInDay = (Expense) expensesInDay.get(i);
            expense.setAmount(String.valueOf(Double.valueOf(expense.getAmount()) + Double.valueOf(expenseInDay.getAmount())));
        }
        expense.setDayCreated(startDate);
        expensesInMonth.add(expense);

        return expensesInMonth;
    }

    private List getTestExpenses() {

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
        for (int i = 0; i < 90; i++) {
            Expense expense = new Expense();
            expense.setAmount(String.valueOf(i + Math.random() * 10));
            Calendar calendar = DateUtils.toCalendar(new Date());
            calendar.add(Calendar.DAY_OF_MONTH, -1*i);
            expense.setDateCreated(calendar.getTime());
            expense.setDayCreated(calendar.getTime());
            expense.setSubcategory((Subcategory) subcategoryList.get(i % 3));
            list.add(expense);


            expenseHandler.insert(expense);
        }
        return list;
    }


    @Override
    public void onClick(View v) {
        Calendar calendar;

        switch (v.getId()) {
            case R.id.weekChartQuery:
                calendar = DateUtils.toCalendar(new Date());
                endDate = calendar.getTime();
                calendar.add(Calendar.DAY_OF_MONTH, -7);
                startDate = calendar.getTime();

                break;
            case R.id.monthChartQuery:
                calendar = DateUtils.toCalendar(new Date());
                endDate = calendar.getTime();
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                startDate = calendar.getTime();

                break;
            case R.id.yearChartQuery:
                calendar = DateUtils.toCalendar(new Date());
                endDate = calendar.getTime();
                calendar.set(Calendar.DAY_OF_YEAR, 1);
                startDate = calendar.getTime();

                break;
            default:

                break;
        }
        generateChart();

    }
}
