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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.wjq.dk.zy.mywallet.R;
import com.wjq.dk.zy.mywallet.customView.ManageGridAdapter;
import com.wjq.dk.zy.mywallet.dataBase.dbHandler.SubcategoryHandler;
import com.wjq.dk.zy.mywallet.model.Subcategory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Elvn on 2016/10/30.
 */
/**
 * # CSIT 6000B    #  DaiKun        20373568          kdai@connect.ust.hk
 * # CSIT 6000B    #  Wang JiaQi    20369969          jwangcu@connect.ust.hk
 * # CSIT 6000B    #  Zhang Yue     20366010          yzhangfk@connect.ust.hk*/
public class SetFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;

    public static SetFragment newInstance(String param1) {
        SetFragment fragment = new SetFragment();
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
    private SetFragment fragment_set;
    Button buttonEat;
    Button buttonTrans;
    Button buttonClothes;
    Button buttonLiving;
    List<Subcategory> mdata_list;
    Button confirmManage;
    int index = 1;
    int subIndex;
    TextView textview;
    GridView manage_Grid;
    Subcategory subcategory;
    ManageGridAdapter simpleAdapter;
    private boolean isShowDelete;
    private String m_Text = "";



    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(container == null){
            return null;
        }
        getActivity().setTitle("Setting");
        final View contentView = inflater.inflate(R.layout.fragment_set, container, false);
        initView(contentView);
        manage_Grid = (GridView) contentView.findViewById(R.id.manage_gridView); //usign ManageGridView
        mdata_list = new ArrayList<>();
        subcategory = new Subcategory();
        initData(index);
        simpleAdapter = new ManageGridAdapter(getActivity(),mdata_list);  //mdata_list is the sub-category array list of each categories
        manage_Grid.setAdapter(simpleAdapter);
        manage_Grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if(position==adapterView.getChildCount()-1){

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Add");
                    View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.add_dialog, null);
                    // Set up the input
                    final EditText input = (EditText) viewInflated.findViewById(R.id.dialog_input);
                    // Specify the type of input expected;
                    input.setInputType(InputType.TYPE_CLASS_TEXT);  //text input
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
            }
        });

        manage_Grid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {  //long click to delete item
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {  //long click the sub-category to delete that item
                if(position < mdata_list.size()){  //except the  last "adding" button
                    if (isShowDelete) {
                        isShowDelete = false;
                        simpleAdapter.setIsShowDelete(isShowDelete);
                    } else {
                        isShowDelete = true;
                        simpleAdapter.setIsShowDelete(isShowDelete);

                    }
                }

                return false;
            }
        });




        return contentView;
    }

    public class SetOnClickListener implements DialogInterface.OnClickListener{

        final EditText input;

        public SetOnClickListener(EditText input) {
            this.input = input;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            if(!"".equals(input.getText().toString())&&input.getText().toString()!=null){  // not null
                m_Text = input.getText().toString();    //add value,  m_Text is the string get from input box
            }
            Subcategory subcategory = new Subcategory();
            subcategory.setName(m_Text);
            subcategory.setCategoryId(String.valueOf(index));
            SubcategoryHandler subcategoryHandler = new SubcategoryHandler(getContext());
            subcategoryHandler.insert(subcategory);
            manager = getFragmentManager();
            fragment_set = new SetFragment();
            ft = manager.beginTransaction();
            ft.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_right_out);
            ft.replace(R.id.realtabcontent, fragment_set);
            ft.addToBackStack(null);
            ft.commit();
        }
    }


    private void initView(View contentView) {      //initial view

        buttonEat = (Button) contentView.findViewById(R.id.manage_tab_eating);
        buttonLiving = (Button) contentView.findViewById(R.id.manage_tab_living);
        buttonTrans = (Button) contentView.findViewById(R.id.manage_tab_trans);
        buttonClothes = (Button) contentView.findViewById(R.id.manage_tab_clothes);
        buttonEat.setOnClickListener(this);
        buttonLiving.setOnClickListener(this);
        buttonTrans.setOnClickListener(this);
        buttonClothes.setOnClickListener(this);
        buttonEat.setSelected(true);

    }

    private void initData(int index) {           //  initial data 
        mdata_list.clear();
        SubcategoryHandler subcategoryHandler = new SubcategoryHandler(getActivity());
        List<Subcategory> list = subcategoryHandler.queryByCategoryId(String.valueOf(index));
        mdata_list.addAll(list);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){  ////1-4 click to display subCategory of each category
            case R.id.manage_tab_eating:
                index = 1;
                initData(1);
                simpleAdapter.notifyDataSetChanged();
                buttonEat.setSelected(true);
                buttonTrans.setSelected(false);
                buttonClothes.setSelected(false);
                buttonLiving.setSelected(false);
                break;
            case R.id.manage_tab_trans:
                index = 2;
                initData(2);
                simpleAdapter.notifyDataSetChanged();
                buttonEat.setSelected(false);
                buttonTrans.setSelected(true);
                buttonClothes.setSelected(false);
                buttonLiving.setSelected(false);
                break;
            case R.id.manage_tab_clothes:
                index = 3;
                initData(3);
                simpleAdapter.notifyDataSetChanged();
                buttonEat.setSelected(false);
                buttonTrans.setSelected(false);
                buttonClothes.setSelected(true);
                buttonLiving.setSelected(false);
                break;
            case R.id.manage_tab_living:
                index = 4;
                initData(4);
                simpleAdapter.notifyDataSetChanged();
                buttonEat.setSelected(false);
                buttonTrans.setSelected(false);
                buttonClothes.setSelected(false);
                buttonLiving.setSelected(true);
                break;

            default:
                index = 1;
                buttonEat.setSelected(true);
                buttonTrans.setSelected(false);
                buttonClothes.setSelected(false);
                buttonLiving.setSelected(false);
                break;
        }
        Log.i("index","index= "+index);

    }
}
