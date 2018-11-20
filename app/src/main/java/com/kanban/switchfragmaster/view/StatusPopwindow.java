package com.kanban.switchfragmaster.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.kanban.switchfragmaster.R;

import java.util.List;

/**
 * @Packagename com.example.topcee.wms.view
 * @Author LongQ
 * @Date 2017/9/12
 * @Descible 生产物料退库筛选状态下拉列表显示
 */

public class StatusPopwindow<T> extends PopupWindow {

    private LayoutInflater inflater;
    private ListView mListView;
    private List<String> stateList;
    private MyAdapter mAdapter;
    private Context context;
    private String key;

    public StatusPopwindow(Context context, List<String> stateList, String key, OnItemClickListener clickListener) {
        super(context);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.stateList = stateList;
        this.key = key;
        init(clickListener);
    }
    public StatusPopwindow(Context context, List<String> stateList, OnItemClickListener clickListener) {
        super(context);
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.stateList = stateList;
        init(clickListener);
    }
    private void init(OnItemClickListener clickListener) {
        View view = inflater.inflate(R.layout.status_spiner_window_layout, null);
        setContentView(view);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00);
        setBackgroundDrawable(dw);
        mListView = (ListView) view.findViewById(R.id.status_listview);
        mListView.setAdapter(mAdapter = new MyAdapter());
        mListView.setOnItemClickListener(clickListener);
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return stateList.size();
        }

        @Override
        public Object getItem(int position) {
            return stateList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.status_spiner_item_layout, null);
                holder.tvName = (TextView) convertView.findViewById(R.id.tvStatusName);
                holder.tvName.setTextColor(context.getResources().getColor(R.color.lb_red));
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvName.setText(getItem(position).toString());
//            holder.tvName.setText(stateList.get(position).get(key));
            return convertView;
        }
    }

    final static class ViewHolder {
        private TextView tvName;
    }
}
