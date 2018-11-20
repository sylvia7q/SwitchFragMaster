package com.kanban.switchfragmaster.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.kanban.switchfragmaster.R;
import com.kanban.switchfragmaster.data.ProductTableInfo;

import java.util.ArrayList;

/**
 * Created by LongQ on 2017/8/7.
 * 生产图表的表格信息加载
 */

public class ProductTableInfoAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ProductTableInfo> mList;

    public ProductTableInfoAdapter(Context context, ArrayList<ProductTableInfo> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null){
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.list_item_fragment_product_table_info,null);
            holder.mTimeTextView = (TextView) view.findViewById(R.id.FRAGMENT_PRODUCT_TABLE_INFO_TIME);
            holder.mReallyTextView = (TextView) view.findViewById(R.id.FRAGMENT_PRODUCT_TABLE_INFO_REALLY);
            holder.mStandandTextView = (TextView) view.findViewById(R.id.FRAGMENT_PRODUCT_TABLE_INFO_STANDAND);
            holder.item_bg = (RelativeLayout) view.findViewById(R.id.table_info_list_item_layout);
            view.setTag(holder);
        }else {
            holder  = (ViewHolder) view.getTag();
        }
        ProductTableInfo info = mList.get(position);
        if(!info.equals("")){
            holder.mTimeTextView.setText(info.getTime());
            holder.mReallyTextView.setText(info.getReally());
            holder.mStandandTextView.setText(info.getStandand());
        }
        return view;
    }


    /**
     * 在这里面加载ListView中的每个item的布局
     */
    public static class ViewHolder {
        private TextView mTimeTextView;
        private TextView mReallyTextView;
        private TextView mStandandTextView;
        private RelativeLayout item_bg;
    }
}
