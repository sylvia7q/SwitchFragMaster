package com.kanban.switchfragmaster.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.kanban.switchfragmaster.R;
import com.kanban.switchfragmaster.data.WoInfoWhEntity;
import com.kanban.switchfragmaster.ui.activity.board.WarehouseKanbanLineActivity;
import com.kanban.switchfragmaster.view.CustomCircleProgressBar;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Justin Fang on 2016/6/7.
 */
public class WarehouseKanbanListAdapter extends BaseAdapter {
    /// <summary>
    /// 所有GPeriodOutput 的資料
    /// </summary>
    List<WoInfoWhEntity> items;

    Context context;

    public WarehouseKanbanListAdapter(Context context, List<WoInfoWhEntity> items){
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
      //  return items.size();//返回数组的长度
        return Integer.MAX_VALUE;
    }

    @Override
    public Object getItem(int postition) {
       // return null;
        return this.items.get(postition % this.items.size());
    }

    @Override
    public long getItemId(int postition) {
       // return 0;
        return postition % this.items.size();
    }

    @Override
    public View getView(int postition, View convertView, ViewGroup parent)
    {
        final int index = postition;
        View view = convertView;
        ViewHolder holder = null;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.activity_warehose_list, null);
            holder = new ViewHolder();
            holder.tv_line = view.findViewById(R.id.tv_line);
            holder.tv_line_status = view.findViewById(R.id.tv_line_status);
            holder.tv_wo = view.findViewById(R.id.tv_wo);
            holder.tv_qty = view.findViewById(R.id.tv_qty);
            holder.tv_material_status = view.findViewById(R.id.tv_material_status);
            holder.tv_wo_achieving_rate = view.findViewById(R.id.tv_wo_achieving_rate);
            holder.progressBar = view.findViewById(R.id.warehouse_pb_progressbar);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        WoInfoWhEntity item =this.items.get( postition % this.items.size());
        if(item!=null){
            holder.tv_line.setText(item.getLineNo());
            holder.tv_line_status.setText(item.getLineStatusName());
            holder.tv_wo.setText(item.getWo());
            holder.tv_qty.setText(item.getWoPlanQty());
            holder.tv_material_status.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            if(item.getIsPrepareSufficient().equalsIgnoreCase("Y"))
            {
                holder.tv_material_status.setText(context.getString(R.string.is_to_prepare_fully));
                holder.tv_material_status.setTextColor(context.getResources().getColor(R.color.blue));
            }
            else {
                holder.tv_material_status.setText(context.getString(R.string.not_ready_to_fully));
                holder.tv_material_status.setTextColor(context.getResources().getColor(R.color.red));
            }

            if(item.getWoPlanQty().equalsIgnoreCase("0")||item.getWoPlanQty().equalsIgnoreCase(""))
            {
                holder.tv_wo_achieving_rate.setText("0%");
                holder.progressBar.setProgress(0);
            }
            else
            {
                Double dlAchievingRate = (Double.parseDouble(item.getQtyTotal())/ Double.parseDouble(item.getWoPlanQty()))*100;
                DecimalFormat decimalFormat =new DecimalFormat("0");//构造方法的字符格式这里如果小数不足2位,会以0补足.
                String sAchievingRate = decimalFormat.format(dlAchievingRate) + "%";//format 返回的是字符串
                holder.tv_wo_achieving_rate.setText(sAchievingRate);
                holder.progressBar.setProgress(Integer.parseInt(decimalFormat.format(dlAchievingRate)));
            }

            final String sLineNo = holder.tv_line.getText().toString();
            holder.tv_material_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(context,"["+ ssss+"] "+context.getString(R.string.alert_del_ok), Toast.LENGTH_SHORT).show();
                    Bundle bundle=new Bundle();
                    Intent intent = new Intent();
                    bundle.putString("sLineNo",sLineNo);
                    intent.putExtras(bundle);
                    intent.setClass(context, WarehouseKanbanLineActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                }
            });
        }
        return view;
    }
    final static class ViewHolder{
        private TextView tv_line;
        private TextView tv_line_status;
        private TextView tv_wo;
        private TextView tv_qty;
        private TextView tv_material_status;
        private TextView tv_wo_achieving_rate;
        private CustomCircleProgressBar progressBar;
    }
}
