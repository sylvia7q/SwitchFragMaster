package com.kanban.switchfragmaster.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.kanban.switchfragmaster.R;
import com.kanban.switchfragmaster.data.SevenusSmtWorkshopEntity;
import com.kanban.switchfragmaster.view.CustomCircleProgressBar;

import java.util.List;

/**
 * Created by YANGT on 2017/2/16.
 */

public class SmtProductionWorkshopKanbanAdapter extends BaseAdapter {

    List<SevenusSmtWorkshopEntity> items;
    Context context;

    public SmtProductionWorkshopKanbanAdapter(Context context, List<SevenusSmtWorkshopEntity> items){
        this.context = context;
        this.items = items;
    }

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

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_smt_production_workshop_kanban_list,null);
            holder = new ViewHolder();
            holder.tvLineNo =  convertView.findViewById(R.id.line_no);
            holder.tvProductNo =convertView.findViewById(R.id.product_no);
            holder.tvWo =convertView.findViewById(R.id.wo);
            holder.tvWoPlanQty =convertView.findViewById(R.id.wo_plan_qty);
            holder.tvMoFinishingRate =convertView.findViewById(R.id.mo_finishing_rate);
            holder.tvProductionState =convertView.findViewById(R.id.production_state);
            holder.pbProgressbar =convertView.findViewById(R.id.pb_progressbar);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        SevenusSmtWorkshopEntity item = (SevenusSmtWorkshopEntity)getItem(position);
        if(item!=null){
            //赋值
            holder.tvLineNo.setText(item.getsLineNo());
            holder.tvProductNo.setText(item.getsProductNo());
            holder.tvWo.setText(item.getSo());
            holder.tvWoPlanQty.setText(item.getsMoPlanQty());
            holder.tvMoFinishingRate.setText(item.getsMoFinishingRate() + "%");
            holder.tvProductionState.setText(item.getsProductionState());
            holder.pbProgressbar.setProgress(Integer.parseInt(item.getsMoFinishingRate()));
        }

        return convertView;
    }
    final static class ViewHolder{
        private TextView tvLineNo;
        private TextView tvProductNo;
        private TextView tvWo;
        private TextView tvWoPlanQty;
        private TextView tvMoFinishingRate;
        private TextView tvActivation;
        private TextView tvProductionState;
        private CustomCircleProgressBar pbProgressbar;
    }
}
