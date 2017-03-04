package com.wjq.dk.zy.mywallet.customView;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wjq.dk.zy.mywallet.R;
import com.wjq.dk.zy.mywallet.model.Expense;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Elvn on 2016/11/20.
 */
/**
 * # CSIT 6000B    #  DaiKun        20373568          kdai@connect.ust.hk
 * # CSIT 6000B    #  Wang JiaQi    20369969          jwangcu@connect.ust.hk
 * # CSIT 6000B    #  Zhang Yue     20366010          yzhangfk@connect.ust.hk*/
public class MyExpandableAdapter extends BaseExpandableListAdapter {
    public List<String>group;
    public Map<String,List<Expense>>child;
    Context c;

    public MyExpandableAdapter(Context context, List<String> g, Map<String,List<Expense>> children){
        super();
        this.c = context;
        this.group = new ArrayList<String>();
        this.child = new HashMap<String,List<Expense>>();
        group.addAll(g);
        child.putAll(children);
    }
    @Override
    public int getGroupCount() {
        return group.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return child.get(group.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return group.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return child.get(group.get(groupPosition)).get(childPosition);
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
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) { //set groupView as time of each day
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(c);
            convertView = inflater.inflate(R.layout.home_group, null);
        }
        TextView title = (TextView) convertView.findViewById(R.id.group_title);
        convertView.setTag(R.id.group_title,groupPosition);
        convertView.setTag(R.id.item_name,-1);
        ImageView im = (ImageView) convertView.findViewById(R.id.group_indicator);
        title.setText(group.get(groupPosition));
        if(isExpanded){
            im.setBackgroundResource(R.drawable.down_arrow);
        }else{
            im.setBackgroundResource(R.drawable.right_arrow);
        }
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {   // set childView as expense list of particular day
            LayoutInflater inflater = LayoutInflater.from(c);
            convertView = inflater.inflate(R.layout.home_item, null);
        }
        TextView ename = (TextView) convertView.findViewById(R.id.item_name);
        TextView enumber = (TextView) convertView.findViewById(R.id.item_num);
        convertView.setTag(R.id.group_title,groupPosition);
        convertView.setTag(R.id.item_name,childPosition);

        ename.setText(child.get(group.get(groupPosition)).get(childPosition).getSubcategory().getName());
        enumber.setText(child.get(group.get(groupPosition)).get(childPosition).getAmount());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
