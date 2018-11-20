package com.kanban.switchfragmaster.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.kanban.switchfragmaster.R;
import com.kanban.switchfragmaster.data.SevenusSmtWorkshopCharacterEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/3/16.
 */

public class SmtProductionWorkshopCharacterAdapter extends BaseAdapter {
    List<SevenusSmtWorkshopCharacterEntity> items;
    Context context;

    public SmtProductionWorkshopCharacterAdapter(Context context, List<SevenusSmtWorkshopCharacterEntity> items){
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();//返回数组的长度
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolder holder = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.activity_smt_production_workshop_character_list,null);
            holder = new ViewHolder();
            holder.sLineNo=  view.findViewById(R.id.line_no);
            holder.sRty = view.findViewById(R.id.rty);
            holder.sHrowingRate1 =view.findViewById(R.id.hrowing_rate1);
            holder.sHrowingRate2 =view.findViewById(R.id.hrowing_rate2);
            holder.sHrowingRate3 =view.findViewById(R.id.hrowing_rate3);
            holder.sProductionState =view.findViewById(R.id.production_state);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        SevenusSmtWorkshopCharacterEntity item = (SevenusSmtWorkshopCharacterEntity)getItem(position);
        if(item!=null){
            //赋值
            holder.sLineNo.setText(item.getsLineNo());
            holder.sRty.setText(item.getsRty());
            holder.sHrowingRate1.setText(item.getsHrowingRate1());
            holder.sHrowingRate2.setText(item.getsHrowingRate2());
            holder.sHrowingRate3.setText(item.getsHrowingRate3());
            holder.sProductionState.setText(item.getsProductionStateName());
        }

        return view;
    }
    final static class ViewHolder{
        private TextView sLineNo;
        private TextView sRty;
        private TextView sHrowingRate1;
        private TextView sHrowingRate2;
        private TextView sHrowingRate3;
        private TextView sProductionState;
    }
}
