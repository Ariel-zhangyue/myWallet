package com.wjq.dk.zy.mywallet.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.wjq.dk.zy.mywallet.R;
import com.wjq.dk.zy.mywallet.customView.MyBudegtExpandableAdapter;
import com.wjq.dk.zy.mywallet.dataBase.dbHandler.BudgetHandler;
import com.wjq.dk.zy.mywallet.model.Budget;

import org.apache.commons.lang3.time.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Calendar;




/**
 * Created by Elvn on 2016/10/30.
 */
/**
 * # CSIT 6000B    #  DaiKun        20373568          kdai@connect.ust.hk
 * # CSIT 6000B    #  Wang JiaQi    20369969          jwangcu@connect.ust.hk
 * # CSIT 6000B    #  Zhang Yue     20366010          yzhangfk@connect.ust.hk*/
public class BudgetFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    // private NumberProgressBar bBnp1;
    private ExpandableListView bExpandableListView;
    List<Budget> bList;
    List<String> bGroup = new ArrayList<>();
    List<List<Budget>> bChild = new ArrayList<>();




    // TODO: Rename and change types of parameters
    private String mParam1;

    public static BudgetFragment newInstance(String param1) {
        BudgetFragment fragment = new BudgetFragment();
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

    private FragmentManager fragmentManager;
    private FragmentTransaction beginTransaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (container == null) {
            return null;
        }
        getActivity().setTitle("Budget");
        final View contentView = inflater.inflate(R.layout.fragment_budget, null);

        initBData();

        //Using ExpandableListView to display monthly budget
        //Group is time slot, child is the ratio of sumExpense/budget and budget progress bar
        bExpandableListView = (ExpandableListView) contentView.findViewById(R.id.budget_listview);
        MyBudegtExpandableAdapter myBudegtExpandableAdapter = new MyBudegtExpandableAdapter(getActivity(), bGroup, bChild);
        //expandableListView.setGroupIndicator(getActivity().getResources().getDrawable(R.drawable.expandableindicator));
        bExpandableListView.setAdapter(myBudegtExpandableAdapter);
        bExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Log.i("budget", bChild.get(groupPosition).get(childPosition).getAmount());
                //click to enter budgetDetail Fragment
                Budget exactBudget = new Budget();
                exactBudget = bChild.get(groupPosition).get(childPosition);
                Bundle bundle = new Bundle();
                bundle.putSerializable("exactBudget",exactBudget);  // send argument which is the exact budget object to detail fragment
                fragmentManager = getFragmentManager();
                BudgetDetailFragment bd_fragment = new BudgetDetailFragment();
                beginTransaction = fragmentManager.beginTransaction();
                bd_fragment.setArguments(bundle);
                beginTransaction.setCustomAnimations(R.anim.slide_left_out, R.anim.slide_right_in);
                beginTransaction.replace(R.id.realtabcontent,bd_fragment);
                beginTransaction.addToBackStack(null);
                beginTransaction.commit();

                return true;
            }
        });

        return contentView;
    }

    public void initBData() {  //adapt data from database to listView

        BudgetHandler budgetHandler = new BudgetHandler(getActivity());

        bList = budgetHandler.queryAllBudget();
        bGroup.clear();
        for (int i =0;i<bList.size();i++){
            bGroup.add(bList.get(i).getMonth()+"/"+bList.get(i).getYear());
            List list = new ArrayList();
            list.add(bList.get(i));
            bChild.add(list);
        }

    }

    public void getTestBudget(){
        BudgetHandler budgetHandler = new BudgetHandler(getContext());
        budgetHandler.deleteAll();
        List list = new ArrayList();

        //SimpleDateFormat formatC = new SimpleDateFormat("yyyy-MM-dd");
        for (int i =1; i<4;i++){
            Calendar calendar = DateUtils.toCalendar(new Date());
            calendar.set(2016,(12-i),1);
            Date date = calendar.getTime();


            Budget testBudget = new Budget();
            testBudget.setBudgetId(String.valueOf(i));
            testBudget.setAmount(String.valueOf(5000-500*i));
            testBudget.setExpenseSum(String.valueOf(5000-800*i));
            testBudget.setMonth(String.valueOf(10-i));
            testBudget.setYear("2016");
            testBudget.setDateCreated(date);
            budgetHandler.insert(testBudget);
            list.add(testBudget);

        }





    }

}
