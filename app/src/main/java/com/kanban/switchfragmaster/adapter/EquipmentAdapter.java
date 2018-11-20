package com.kanban.switchfragmaster.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.kanban.switchfragmaster.R;
import com.kanban.switchfragmaster.data.EquipmentInfo;

import java.util.ArrayList;

/**
 * Created by LongQ on 2017/11/27.
 */

public class EquipmentAdapter extends BaseAdapter {
    private ArrayList<EquipmentInfo> equipmentEntities;
    private Context context;

    public EquipmentAdapter(Context context, ArrayList<EquipmentInfo> equipmentEntities) {
        this.context = context;
        this.equipmentEntities = equipmentEntities;
    }

    @Override
    public int getCount() {
        return equipmentEntities.size();
    }

    @Override
    public Object getItem(int position) {
        return equipmentEntities.get(position);
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
            view = LayoutInflater.from(context).inflate(R.layout.equipment_list_item,null);
            holder.time = (TextView) view.findViewById(R.id.equipment_time);
            holder.run = (TextView) view.findViewById(R.id.equipment_run);
            holder.stop = (TextView) view.findViewById(R.id.equipment_stop);
            holder.equipment_layout = (RelativeLayout) view.findViewById(R.id.equipment_layout);
            view.setTag(holder);
        }else {
            holder  = (ViewHolder) view.getTag();
        }
        EquipmentInfo info = equipmentEntities.get(position);
        if(!info.equals("")){
            holder.time.setText(info.getTime());
            holder.run.setText(info.getRunPercent());
            holder.stop.setText(info.getStopPercent());

        }
        return view;
    }

    public static final class ViewHolder{

        private TextView time;
        private TextView run;
        private TextView stop;
        private RelativeLayout equipment_layout;

    }
}
