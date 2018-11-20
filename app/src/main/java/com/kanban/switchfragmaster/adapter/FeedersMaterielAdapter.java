package com.kanban.switchfragmaster.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.kanban.switchfragmaster.R;
import com.kanban.switchfragmaster.data.FeedersMaterielInfo;

import java.util.ArrayList;

/**
 * Created by LongQ on 2017/8/15.
 * 看板供料器位置抛料率表格加载
 */

public class FeedersMaterielAdapter extends BaseAdapter {

    private ArrayList<FeedersMaterielInfo> feedersInfos;
    private Context context;

    public FeedersMaterielAdapter(Context context, ArrayList<FeedersMaterielInfo> feedersInfos) {
        this.context = context;
        this.feedersInfos = feedersInfos;
    }

    @Override
    public int getCount() {
        return  feedersInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return feedersInfos.get(position);
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
            view = LayoutInflater.from(context).inflate(R.layout.feeders_materiel_list_item,null);
            holder.auto_num_tv = (TextView) view.findViewById(R.id.fragment_feeders_auto_num);
            holder.equipment_code_tv = (TextView) view.findViewById(R.id.fragment_feeders_equipmentcode);
            holder.line_number_tv = (TextView) view.findViewById(R.id.fragment_feeders_linenum);
            holder.location_tv = (TextView) view.findViewById(R.id.fragment_feeders_location);
            holder.absorb_tv = (TextView) view.findViewById(R.id.fragment_feeders_absorb);
            holder.reject_num_tv = (TextView) view.findViewById(R.id.fragment_feeders_reject_num);
            holder.reject_rate_tv = (TextView) view.findViewById(R.id.fragment_feeders_reject_rate);
            holder.mbg_layout = (LinearLayout) view.findViewById(R.id.feeders_layout);
            view.setTag(holder);
        }else {
            holder  = (ViewHolder) view.getTag();
        }
        FeedersMaterielInfo info = feedersInfos.get(position);
        if(info!=null){
            holder.auto_num_tv.setText((position + 1) + "");
            holder.equipment_code_tv.setText(info.getFeeders_equipmentCoding());
            holder.line_number_tv.setText(info.getFeeders_lineNum());
            holder.location_tv.setText(info.getFeeders_location());
            holder.absorb_tv.setText(info.getFeeders_absorbNum());
            holder.reject_num_tv.setText(info.getFeeders_rejectNum());
            holder.reject_rate_tv.setText(info.getFeeders_rejectRate() + "%");
        }
        return view;
    }

    public static final class ViewHolder{

        private TextView auto_num_tv;
        private TextView equipment_code_tv;
        private TextView line_number_tv;
        private TextView location_tv;
        private TextView absorb_tv;
        private TextView reject_rate_tv;
        private TextView reject_num_tv;
        private LinearLayout mbg_layout;

    }
}
