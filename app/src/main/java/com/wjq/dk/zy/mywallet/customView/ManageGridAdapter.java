package com.wjq.dk.zy.mywallet.customView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wjq.dk.zy.mywallet.R;
import com.wjq.dk.zy.mywallet.dataBase.dbHandler.SubcategoryHandler;
import com.wjq.dk.zy.mywallet.model.Subcategory;

import java.util.List;

/**
 * Created by yuezhang on 11/27/16.
 */
/**
 * # CSIT 6000B    #  DaiKun        20373568          kdai@connect.ust.hk
 * # CSIT 6000B    #  Wang JiaQi    20369969          jwangcu@connect.ust.hk
 * # CSIT 6000B    #  Zhang Yue     20366010          yzhangfk@connect.ust.hk*/
public class ManageGridAdapter extends BaseAdapter {
    class ViewHolder{
        ImageView deleteSubcategory,add;
        TextView subcategoryName;

    }
    private List<Subcategory> list;
    Context c;
    final int position = 0;
    private boolean isShowDelete;

    public ManageGridAdapter(Context context,List<Subcategory> l){
        super();
        this.list = l;
        this.c = context;
    }
    @Override
    public int getCount() {
        return (list.size()+1);
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view;
        final ViewHolder viewHolder;


        if (convertView == null) {
            view = LayoutInflater.from(c).inflate(R.layout.grid_item_manage, null);
            viewHolder=new ViewHolder();

            viewHolder.subcategoryName = (TextView) view.findViewById(R.id.manage_sub);
            viewHolder.add = (ImageView) view.findViewById(R.id.addView);
            viewHolder.subcategoryName.setText("");

            viewHolder.deleteSubcategory = (ImageView) view.findViewById(R.id.delete_markView);
            view.setTag(viewHolder);
            //convertView = inflater.inflate(R.layout.grid_item_manage, null);
        }else{
            view=convertView;
            viewHolder= (ViewHolder) view.getTag();
        }




        if(position < list.size()){  //except the last grid for "add" button, adapt Subcategory data
            Subcategory subcategory = (Subcategory) getItem(position);
            viewHolder.subcategoryName.setText(subcategory.getName());
            viewHolder.add.setVisibility(View.GONE);
            viewHolder.subcategoryName.setVisibility(View.VISIBLE);
            viewHolder.deleteSubcategory.setVisibility(View.VISIBLE);
            viewHolder.subcategoryName.setFocusable(false);
            viewHolder.deleteSubcategory.setVisibility(isShowDelete ? View.VISIBLE : View.GONE);
            if(isShowDelete) {
                viewHolder.deleteSubcategory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {     //click "delete" icon to delete category
                        deleteSubcategory((Subcategory) getItem(position));
                        list.remove(position);
                        setIsShowDelete(false);
                    }
                });
            }

        }else {      //"add" button at the end of gird view

            viewHolder.add.setImageResource(R.drawable.add);
            viewHolder.add.setVisibility(View.VISIBLE);
            viewHolder.subcategoryName.setVisibility(View.GONE);
            viewHolder.deleteSubcategory.setVisibility(View.GONE);
        }



        viewHolder.subcategoryName.setOnClickListener(new View.OnClickListener() {  //longClick to modigy category
            @Override
            public void onClick(View view) {  //select different super categories
                viewHolder.subcategoryName.setFocusable(true);
                notifyDataSetChanged();
            }
        });


        return view;
    }



    public void setIsShowDelete(boolean isShowDelete) {
        this.isShowDelete = isShowDelete;
        notifyDataSetChanged();
    }

    private void deleteSubcategory(Subcategory subcategory){    // execute delete operation from database
        SubcategoryHandler subcategoryHandler = new SubcategoryHandler(c);
        subcategoryHandler.deleteBySubcategoryId(subcategory.getSubcategoryId());
    }

}
