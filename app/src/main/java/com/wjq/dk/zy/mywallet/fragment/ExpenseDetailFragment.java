package com.wjq.dk.zy.mywallet.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.wjq.dk.zy.mywallet.R;
import com.wjq.dk.zy.mywallet.customView.MyGridAdapter;
import com.wjq.dk.zy.mywallet.customView.MyGridView2;
import com.wjq.dk.zy.mywallet.dataBase.dbHandler.ExpenseHandler;
import com.wjq.dk.zy.mywallet.dataBase.dbHandler.SubcategoryHandler;
import com.wjq.dk.zy.mywallet.model.Expense;
import com.wjq.dk.zy.mywallet.model.Subcategory;

import java.util.ArrayList;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

import static com.wjq.dk.zy.mywallet.R.id.textView;

/**
 * Created by Elvn on 2016/11/25.
 */

/**
 * # CSIT 6000B    #  DaiKun        20373568          kdai@connect.ust.hk
 * # CSIT 6000B    #  Wang JiaQi    20369969          jwangcu@connect.ust.hk
 * # CSIT 6000B    #  Zhang Yue     20366010          yzhangfk@connect.ust.hk*/
public class ExpenseDetailFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;

    public static ExpenseDetailFragment newInstance(String param1) {
        ExpenseDetailFragment fragment = new ExpenseDetailFragment();
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
    private HomeFragment fragment_home;
    Button tab_eat;
    Button tab_trans;
    Button tab_clothes;
    Button tab_living;
    EditText editText;
    TextView textView;
    FancyButton back;
    FancyButton modify;
    GridView gridView;
    MyGridView2 simpleAdapter;
    List<Subcategory> mdata_list;
    int index;
    int childPosition;
    Expense expense;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(container == null) {
            return null;
        }
        Bundle bundle = getArguments();
        expense = new Expense();
        expense = (Expense) bundle.getSerializable("expense");
        index = Integer.parseInt(expense.getSubcategory().getCategoryId());
        mdata_list = new ArrayList<>();
        getActivity().setTitle("Expense Detail");
        final View contentView = inflater.inflate(R.layout.fragment_expense_detail, container, false);
        initView(contentView);
        initData(index);

        editText.setText(expense.getAmount());
        textView.setText(expense.getSubcategory().getName());
        simpleAdapter = new MyGridView2(getActivity(),mdata_list);  // adapt subcategory array list to grid view
        gridView.setAdapter(simpleAdapter);

        //set click state by the expense transmitted in
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // TODO Auto-generated method stub
                gridView.getChildAt(childPosition).setSelected(true);
            }
        }, 500);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
                expense.setSubcategory(mdata_list.get(position));
                textView.setText(mdata_list.get(position).getName());
            }
        });
        return contentView;
    }

    private void initView(View contentView) {
        gridView = (GridView) contentView.findViewById(R.id.gridView_detail);
        tab_eat = (Button) contentView.findViewById(R.id.tab_eating_detail);
        tab_trans = (Button) contentView.findViewById(R.id.tab_trans_detail);
        tab_clothes = (Button) contentView.findViewById(R.id.tab_clothes_detail);
        tab_living = (Button) contentView.findViewById(R.id.tab_living_detail);
        back = (FancyButton) contentView.findViewById(R.id.detail_back);
        modify = (FancyButton) contentView.findViewById(R.id.detail_modify);
        editText = (EditText) contentView.findViewById(R.id.editText_amount_detail);
        textView = (TextView) contentView.findViewById(R.id.showed_subcategory_detail);
        tab_eat.setOnClickListener(this);
        tab_trans.setOnClickListener(this);
        tab_clothes.setOnClickListener(this);
        tab_living.setOnClickListener(this);
        modify.setOnClickListener(this);
        back.setOnClickListener(this);
        switch(index){
            case 1:
                tab_eat.setSelected(true);
                break;
            case 2:
                tab_trans.setSelected(true);
                break;
            case 3:
                tab_clothes.setSelected(true);
                break;
            case 4:
                tab_living.setSelected(true);
                break;
        }


    }
    private void initData(int index) {
        mdata_list.clear();
        SubcategoryHandler subcategoryHandler = new SubcategoryHandler(getActivity());
        List<Subcategory> list = subcategoryHandler.queryByCategoryId(String.valueOf(index));
        mdata_list.addAll(list);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tab_eating_detail:  //1-4 click to re-choose subCategory of expense record
                index = 1;
                initData(1);
                simpleAdapter.notifyDataSetChanged();
                tab_eat.setSelected(true);
                tab_trans.setSelected(false);
                tab_clothes.setSelected(false);
                tab_living.setSelected(false);
                break;
            case R.id.tab_trans_detail:
                index = 2;
                initData(2);
                simpleAdapter.notifyDataSetChanged();
                tab_eat.setSelected(false);
                tab_trans.setSelected(true);
                tab_clothes.setSelected(false);
                tab_living.setSelected(false);
                break;
            case R.id.tab_clothes_detail:
                index = 3;
                initData(3);
                simpleAdapter.notifyDataSetChanged();
                tab_eat.setSelected(false);
                tab_trans.setSelected(false);
                tab_clothes.setSelected(true);
                tab_living.setSelected(false);
                break;
            case R.id.tab_living_detail:
                index = 4;
                initData(4);
                simpleAdapter.notifyDataSetChanged();
                tab_eat.setSelected(false);
                tab_trans.setSelected(false);
                tab_clothes.setSelected(false);
                tab_living.setSelected(true);
                break;
            case R.id.detail_back:  // return to home fragment
                //
                manager = getFragmentManager();
                fragment_home = new HomeFragment();
                ft = manager.beginTransaction();
                ft.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_right_out);
                ft.replace(R.id.realtabcontent, fragment_home);
                ft.addToBackStack(null);
                ft.commit();
                break;
            case R.id.detail_modify:  // sure to modify that expense record and return to home fragment
                expense.setAmount(editText.getText().toString());
                //update data
                ExpenseHandler expenseHandler = new ExpenseHandler(getActivity());
                expenseHandler.update(expense);
                manager = getFragmentManager();
                fragment_home = new HomeFragment();
                ft = manager.beginTransaction();
                ft.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_right_out);
                ft.replace(R.id.realtabcontent, fragment_home);
                ft.addToBackStack(null);
                ft.commit();
                break;
        }
        Log.i("index","index= "+index);

    }
}
