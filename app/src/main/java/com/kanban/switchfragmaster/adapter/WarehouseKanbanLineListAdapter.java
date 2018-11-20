package com.kanban.switchfragmaster.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.kanban.switchfragmaster.R;
import com.kanban.switchfragmaster.data.WoInfoWhLineEntity;

import java.util.List;

/**
 * Created by YANGT on 2016-08-02.
 */
public class WarehouseKanbanLineListAdapter extends BaseAdapter {
    List<WoInfoWhLineEntity> items;
    Context context;

    public WarehouseKanbanLineListAdapter(Context context, List<WoInfoWhLineEntity> items) {
        this.context = context;
        this.items = items;
    }


    @Override
    public int getCount() {
        //return Integer.MAX_VALUE;//返回数组的长度
        return items.size();//返回数组的长度
    }

    @Override
    public Object getItem(int position) {
        //return this.items.get(position % this.items.size());
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        //return position % this.items.size();
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //WoInfoWhLineEntity item =this.items.get( position % this.items.size());
        //final int index = position;
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_warehose_line_list, null);
            holder = new ViewHolder();
            holder.sEquipmentId = convertView.findViewById(R.id.sEquipmentId);
            holder.sStration = convertView.findViewById(R.id.sStration);
            holder.sPartNo = convertView.findViewById(R.id.sPartNo);
            holder.sTotalNO = convertView.findViewById(R.id.sTotalNO);
            holder.sPartStatus = convertView.findViewById(R.id.sPartStatus);
            holder.sQty = convertView.findViewById(R.id.sQty);
            holder.sQuantity = convertView.findViewById(R.id.sQuantity);
            holder.sMinutes = convertView.findViewById(R.id.sMinutes);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        WoInfoWhLineEntity item = (WoInfoWhLineEntity) getItem(position);
        if (item != null) {
            //赋值
            holder.sEquipmentId.setText(item.getsEquipmentId());
            holder.sStration.setText(item.getsStration());
            holder.sPartNo.setText(item.getsPartNo());
            holder.sTotalNO.setText(item.getsTotalNO());
            holder.sPartStatus.setText(item.getsPartStatus());
            holder.sQty.setText(item.getsQty());
            holder.sQuantity.setText(item.getsQuantity());
            holder.sMinutes.setText(item.getsMinutes());
        }

        return convertView;
    }

    final static class ViewHolder {
        private TextView sEquipmentId;
        private TextView sStration;
        private TextView sPartNo;
        private TextView sTotalNO;
        private TextView sPartStatus;
        private TextView sQty;
        private TextView sQuantity;
        private TextView sMinutes;
    }
}
