package com.wjq.dk.zy.mywallet.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.wjq.dk.zy.mywallet.R;
import com.wjq.dk.zy.mywallet.customView.MyGridAdapter;
import com.wjq.dk.zy.mywallet.dataBase.dbHandler.ExpenseHandler;
import com.wjq.dk.zy.mywallet.dataBase.dbHandler.SubcategoryHandler;
import com.wjq.dk.zy.mywallet.model.Expense;
import com.wjq.dk.zy.mywallet.model.Subcategory;

import java.util.ArrayList;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by Elvn on 2016/10/30.
 */
/**
 * # CSIT 6000B    #  DaiKun        20373568          kdai@connect.ust.hk
 * # CSIT 6000B    #  Wang JiaQi    20369969          jwangcu@connect.ust.hk
 * # CSIT 6000B    #  Zhang Yue     20366010          yzhangfk@connect.ust.hk*/

public class AddFragment extends Fragment implements View.OnClickListener{
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;

    public static AddFragment newInstance(String param1) {
        AddFragment fragment = new AddFragment();
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
    Button buttonEat;
    Button buttonTrans;
    Button buttonClothes;
    Button buttonLiving;
    FancyButton cancel;
    FancyButton submit;
    EditText editText;
    TextView textView;
    GridView gridView;
    MyGridAdapter simpleAdapter;
    Subcategory subcategory;
    Expense expense;
    int index = 1;
    List<Subcategory> mdata_list;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(container == null){
            return null;
        }
        //this is used to keep the bottom bar not be pushed up when keyboard appears.
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getActivity().setTitle("Add");
        final View contentView = inflater.inflate(R.layout.fragment_add, container, false);
        initView(contentView);  // initial view content
        mdata_list = new ArrayList<>();   // mdata_list is the subCategory 's list of each category
        expense = new Expense();
        initData(index);
        //set adapter for Listview
        simpleAdapter = new MyGridAdapter(getActivity(),mdata_list);
        gridView.setAdapter(simpleAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {  //click item to choose subCategory of that expense
                view.setSelected(true);
                subcategory = mdata_list.get(position);
                textView.setText(subcategory.getName());
            }
        });
        return contentView;
    }

    private void initView(View contentView) {
        gridView = (GridView) contentView.findViewById(R.id.gridView);
        editText = (EditText) contentView.findViewById(R.id.editText_amount);
        editText.setText("");
        textView = (TextView) contentView.findViewById(R.id.showed_subcategory);
        textView.setText(" ");
        buttonEat = (Button) contentView.findViewById(R.id.tab_eating);
        buttonLiving = (Button) contentView.findViewById(R.id.tab_living);
        buttonTrans = (Button) contentView.findViewById(R.id.tab_trans);
        buttonClothes = (Button) contentView.findViewById(R.id.tab_clothes);
        cancel = (FancyButton) contentView.findViewById(R.id.add_cancel);
        submit = (FancyButton) contentView.findViewById(R.id.add_sure);
        buttonEat.setOnClickListener(this);
        buttonLiving.setOnClickListener(this);
        buttonTrans.setOnClickListener(this);
        buttonClothes.setOnClickListener(this);
        cancel.setOnClickListener(this);
        submit.setOnClickListener(this);
        buttonEat.setSelected(true);

    }

    private void initData(int index) {   //query Subcategory list for category
        mdata_list.clear();
        //load correspond subcategories
        SubcategoryHandler subcategoryHandler = new SubcategoryHandler(getActivity());
        List<Subcategory> list = subcategoryHandler.queryByCategoryId(String.valueOf(index));
        mdata_list.addAll(list);
    }

    @Override
    public void onClick(View v) {
        //using this to collect all click listeners together
        //several buttons' click listener
        switch (v.getId()){
            case R.id.tab_eating:  //Click different categories to display corresponding sub-categories
                index = 1;
                //reset the data and notify adapter
                initData(1);
                simpleAdapter.notifyDataSetChanged();
                buttonEat.setSelected(true);
                buttonTrans.setSelected(false);
                buttonClothes.setSelected(false);
                buttonLiving.setSelected(false);
                break;
            case R.id.tab_trans:
                index = 2;
                initData(2);
                simpleAdapter.notifyDataSetChanged();
                buttonEat.setSelected(false);
                buttonTrans.setSelected(true);
                buttonClothes.setSelected(false);
                buttonLiving.setSelected(false);
                break;
            case R.id.tab_clothes:
                index = 3;
                initData(3);
                simpleAdapter.notifyDataSetChanged();
                buttonEat.setSelected(false);
                buttonTrans.setSelected(false);
                buttonClothes.setSelected(true);
                buttonLiving.setSelected(false);
                break;
            case R.id.tab_living:
                index = 4;
                initData(4);
                simpleAdapter.notifyDataSetChanged();
                buttonEat.setSelected(false);
                buttonTrans.setSelected(false);
                buttonClothes.setSelected(false);
                buttonLiving.setSelected(true);
                break;
            case R.id.add_cancel:  // cancel adding
                manager = getFragmentManager();
                fragment_home = new HomeFragment();
                ft = manager.beginTransaction();
                ft.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_right_out);
                ft.replace(R.id.realtabcontent, fragment_home);
                ft.addToBackStack(null);
                ft.commit();

                break;
            case R.id.add_sure:   // sure adding
                if(editText.getText().toString().equals("")||subcategory.equals(null)){
                    Toast.makeText(getActivity(),"You haven`t input an amount or you haven`t chosen a subcategory",Toast.LENGTH_SHORT).show();
                }else {
                    expense.setSubcategory(subcategory);
                    expense.setBudgetId("1");
                    expense.setAmount(editText.getText().toString());
                    ExpenseHandler expenseHandler = new ExpenseHandler(getActivity());
                    expenseHandler.insert(expense);   // get data to insert to database
                    manager = getFragmentManager();
                    fragment_home = new HomeFragment();
                    ft = manager.beginTransaction();
                    ft.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_right_out);
                    ft.replace(R.id.realtabcontent, fragment_home);
                    ft.addToBackStack(null);
                    ft.commit();
                }
                break;
            default:
                index = 1;
                buttonEat.setSelected(true);  // default category is eating
                buttonTrans.setSelected(false);
                buttonClothes.setSelected(false);
                buttonLiving.setSelected(false);
                break;
        }
        Log.i("index","index= "+index);

    }
}
