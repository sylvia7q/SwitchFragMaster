package com.kanban.switchfragmaster.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.kanban.switchfragmaster.R;
import com.kanban.switchfragmaster.data.ExceptionShowInfo;

import java.util.List;

/**
 * Created by LongQ on 2017/8/15.
 * 看板异常信息显示列表加载
 */

public class ExceptionShowAdapter extends BaseAdapter {

    private Context context;
    private List<ExceptionShowInfo> exceptionShowInfos;

    public ExceptionShowAdapter(Context context, List<ExceptionShowInfo> exceptionShowInfos) {
        this.context = context;
        this.exceptionShowInfos = exceptionShowInfos;
    }
    @Override
    public int getCount() {
        return exceptionShowInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return exceptionShowInfos.get(position);
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
            view = LayoutInflater.from(context).inflate(R.layout.exception_show_list_item,null);
            holder.exception_type = view.findViewById(R.id.item_exception_show_type);
            holder.exception_info = view.findViewById(R.id.item_exception_show_info);
            holder.item_exception_bg = view.findViewById(R.id.item_exception_bg);
            view.setTag(holder);
        }else {
            holder  = (ViewHolder) view.getTag();
        }
        ExceptionShowInfo info = exceptionShowInfos.get(position);
        if(info!= null){
            holder.exception_type.setText(info.getException_type());
            holder.exception_info.setText(info.getException_info());
        }
        return view;
    }

    public static final class ViewHolder{
        private TextView exception_type;
        private TextView exception_info;
        private LinearLayout item_exception_bg;

    }
}
