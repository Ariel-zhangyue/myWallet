package com.wjq.dk.zy.mywallet.customView;

/**
 * Created by Elvn on 2016/11/27.
 */
/**
 * # CSIT 6000B    #  DaiKun        20373568          kdai@connect.ust.hk
 * # CSIT 6000B    #  Wang JiaQi    20369969          jwangcu@connect.ust.hk
 * # CSIT 6000B    #  Zhang Yue     20366010          yzhangfk@connect.ust.hk*/
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wjq.dk.zy.mywallet.R;
import com.wjq.dk.zy.mywallet.model.Subcategory;

import java.util.List;


/**
 * Created by Elvn on 2016/11/24.
 */

public class MyGridView2 extends BaseAdapter {
    private List<Subcategory> list;
    Context c;
    public MyGridView2(Context context,List<Subcategory> l){
        super();
        this.list = l;
        this.c = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(c);
            convertView = inflater.inflate(R.layout.grid_item2, null);
            holder.textView = (TextView) convertView.findViewById(R.id.grid_detail_name);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView.setText(list.get(position).getName());
        return convertView;
    }
    public final class ViewHolder{
        public TextView textView;
    }
}

