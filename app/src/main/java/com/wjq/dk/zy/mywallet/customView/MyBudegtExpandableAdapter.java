package com.wjq.dk.zy.mywallet.customView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wjq.dk.zy.mywallet.R;
import com.wjq.dk.zy.mywallet.model.Budget;
import com.wjq.dk.zy.mywallet.util.NumberFormatUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuezhang on 11/23/16.
 */
/**
 * # CSIT 6000B    #  DaiKun        20373568          kdai@connect.ust.hk
 * # CSIT 6000B    #  Wang JiaQi    20369969          jwangcu@connect.ust.hk
 * # CSIT 6000B    #  Zhang Yue     20366010          yzhangfk@connect.ust.hk*/
public class MyBudegtExpandableAdapter extends BaseExpandableListAdapter {
    public List<String> group;
    public List<List<Budget>> child;
    Context c;


    public MyBudegtExpandableAdapter(Context context, List<String> g, List<List<Budget>> children) {
        super();
        this.c = context;
        this.group = new ArrayList<String>();
        this.child = new ArrayList<List<Budget>>();
        group.addAll(g);
        child.addAll(children);
    }

    @Override
    public int getGroupCount() {
        return group.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return child.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return group.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return child.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {  //set groupView of budget's time and pulling arrow
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(c);
            convertView = inflater.inflate(R.layout.budget_group, null);
        }

        TextView title = (TextView) convertView.findViewById(R.id.budget_group_title);
        ImageView im = (ImageView) convertView.findViewById(R.id.budget_group_indicator);
        title.setText(group.get(groupPosition));
        if (isExpanded) {
            im.setBackgroundResource(R.drawable.down_arrow);
        } else {
            im.setBackgroundResource(R.drawable.right_arrow);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) { //set child view as the ratio of expense/budgetof budget and progress bar
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(c);
            convertView = inflater.inflate(R.layout.budget_item, null);
        }
        TextView bProportion = (TextView) convertView.findViewById(R.id.proportion);
        bProportion.setText(NumberFormatUtil.format(child.get(groupPosition).get(childPosition).getExpenseSum()) + "/" + NumberFormatUtil.format(child.get(groupPosition).get(childPosition).getAmount()));

        NumberProgressBar bNPB = (NumberProgressBar) convertView.findViewById(R.id.budget_numberprogressBar);
        String amount = child.get(groupPosition).get(childPosition).getAmount();
        String expenseSum = child.get(groupPosition).get(childPosition).getExpenseSum();
        bNPB.setMax(Integer.valueOf(amount.substring(0, amount.lastIndexOf("."))));
        bNPB.setProgress(Integer.valueOf(expenseSum.substring(0, expenseSum.lastIndexOf("."))));


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
