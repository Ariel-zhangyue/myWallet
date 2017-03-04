package com.wjq.dk.zy.mywallet.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;

import com.wjq.dk.zy.mywallet.R;
import com.wjq.dk.zy.mywallet.customView.MyExpandableAdapter;
import com.wjq.dk.zy.mywallet.customView.NumberProgressBar;
import com.wjq.dk.zy.mywallet.dataBase.dbHandler.BudgetHandler;
import com.wjq.dk.zy.mywallet.dataBase.dbHandler.ExpenseHandler;
import com.wjq.dk.zy.mywallet.dataBase.dbHandler.SubcategoryHandler;
import com.wjq.dk.zy.mywallet.model.Budget;
import com.wjq.dk.zy.mywallet.model.Expense;
import com.wjq.dk.zy.mywallet.model.Subcategory;

import org.apache.commons.lang3.time.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * # CSIT 6000B    #  DaiKun        20373568          kdai@connect.ust.hk
 * # CSIT 6000B    #  Wang JiaQi    20369969          jwangcu@connect.ust.hk
 * # CSIT 6000B    #  Zhang Yue     20366010          yzhangfk@connect.ust.hk*/
public class HomeFragment extends Fragment {
    private NumberProgressBar bnp;
    private ExpandableListView expandableListView;
    List<String>group;
    Map<String,List<Expense>>child;
    Budget budget;
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;

    public static HomeFragment newInstance(String param1) {
        HomeFragment fragment = new HomeFragment();
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
    private FragmentManager manager;
    private FragmentTransaction ft;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(container == null){
            return null;
        }
        getActivity().setTitle("Home");
        final View contentView = inflater.inflate(R.layout.fragment_home,null);
        initData();
        bnp = (NumberProgressBar) contentView.findViewById(R.id.numberprogressBar);
        bnp.setMax(Integer.valueOf(budget.getAmount().substring(0, budget.getAmount().lastIndexOf("."))));   //set budgetAmount as the max value of progress bar
        bnp.setProgress(Integer.valueOf(budget.getExpenseSum().substring(0, budget.getExpenseSum().lastIndexOf("."))));

        //Adapt expense list by expandableListView ordered by time
        expandableListView = (ExpandableListView) contentView.findViewById(R.id.expand_listview);
        MyExpandableAdapter myExpandableAdapter = new MyExpandableAdapter(getActivity(),group,child);
        expandableListView.setGroupIndicator(null);
        expandableListView.setAdapter(myExpandableAdapter);
        for (int i = 0; i < group.size(); i++) {
            expandableListView.expandGroup(i);
        }
        //on item click event
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Expense mexpense;

                //click to enter ExpenseDetail fragment

                mexpense = child.get(group.get(groupPosition)).get(childPosition);
                //transmit it to detail fragment
                Bundle bundle = new Bundle();
                bundle.putSerializable("expense", mexpense);  // send argument(expense object) to detail fragment
                manager = getFragmentManager();
                ExpenseDetailFragment fragment_detail = new ExpenseDetailFragment();
                ft = manager.beginTransaction();
                fragment_detail.setArguments(bundle);
                ft.setCustomAnimations(R.anim.slide_left_out, R.anim.slide_right_in);
                ft.replace(R.id.realtabcontent, fragment_detail);
                ft.addToBackStack(null);
                ft.commit();
                return true;
            }
        });
        //on item long click event
        expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //long click one single expense to delete that record
                final int groupPos = (Integer) view.getTag(R.id.group_title);
                final int childPos = (Integer) view.getTag(R.id.item_name);
                //check if the click is on child
                if(childPos!=-1){  //by using dialog to confirm
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Delete this expense!");
                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Expense mexpense;
                            mexpense = child.get(group.get(groupPos)).get(childPos);
                            ExpenseHandler expenseHandler = new ExpenseHandler(getActivity());
                            expenseHandler.deleteByExpenseId(mexpense.getExpenseId());
                            manager = getFragmentManager();
                            HomeFragment fragment_home = new HomeFragment();
                            ft = manager.beginTransaction();
                            ft.setCustomAnimations(R.anim.slide_left_out, R.anim.slide_right_in);
                            ft.replace(R.id.realtabcontent, fragment_home);
                            ft.addToBackStack(null);
                            ft.commit();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                return false;
            }
        });

        return contentView;
    }
    public void initData(){
        ExpenseHandler expenseHandler = new ExpenseHandler(getActivity());

        BudgetHandler budgetHandler = new BudgetHandler(getContext());
        child = new HashMap<>();
        //query all dates first
        group = expenseHandler.queryAllDate();
        for(int i = 0;i<group.size();i++){//for each date, query its expenses.
            child.put(group.get(i),expenseHandler.queryByDay(group.get(i)));
        }

        String year = String.valueOf(DateUtils.toCalendar(new Date()).get(Calendar.YEAR));
        String month = String.valueOf(DateUtils.toCalendar(new Date()).get(Calendar.MONTH)+1);
        budget = budgetHandler.queryByYearAndMonth(year, month);
    }


}
