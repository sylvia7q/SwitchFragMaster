package com.kanban.switchfragmaster.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.kanban.switchfragmaster.R;
import com.kanban.switchfragmaster.data.OnlineWarnInfo;

import java.util.List;

/**
 * Created by LongQ on 2017/8/14.
 */

public class OnlineWarnAdapter extends BaseAdapter {

    private List<OnlineWarnInfo> materielWarnInfoList;
    private Context context;

    public OnlineWarnAdapter(Context context, List<OnlineWarnInfo> materielWarnInfoList) {
        this.context = context;
        this.materielWarnInfoList = materielWarnInfoList;
    }

    @Override
    public int getCount() {
        return materielWarnInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return materielWarnInfoList.get(position);
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
            view = LayoutInflater.from(context).inflate(R.layout.fragment_materiel_warn_info_list_item,null);
            holder.online_warn_device = (TextView) view.findViewById(R.id.online_warn_device);
            holder.station_location = (TextView) view.findViewById(R.id.station_location);
            holder.online_warn_reel_no = (TextView) view.findViewById(R.id.online_warn_reel_no);
            holder.online_warn_part_no = (TextView) view.findViewById(R.id.online_warn_part_no);
            holder.dosage_tv = (TextView) view.findViewById(R.id.dosage_tv);
            holder.online_warn_feed_num = (TextView) view.findViewById(R.id.online_warn_feed_num);
            holder.online_num = (TextView) view.findViewById(R.id.online_num);
            holder.need_num = (TextView) view.findViewById(R.id.need_num);
            holder.usable_date = (TextView) view.findViewById(R.id.usable_date);
            holder.usable_percent = (TextView) view.findViewById(R.id.usable_percent);
            holder.part_desc = (TextView) view.findViewById(R.id.part_desc);
            holder.warn_bg_layout = (LinearLayout) view.findViewById(R.id.online_warn_bg_layout);
            view.setTag(holder);
        }else {
            holder  = (ViewHolder) view.getTag();
        }
        OnlineWarnInfo info = materielWarnInfoList.get(position);
        if (!info.equals("")) {
            holder.online_warn_device.setText(info.getsMachine());
            holder.station_location.setText(info.getsFid());
            holder.online_warn_reel_no.setText(info.getsReelNo());
            holder.online_warn_part_no.setText(info.getsPartNo());
            holder.dosage_tv.setText(info.getsConsumeQty());
            holder.online_warn_feed_num.setText(info.getsFeedQty());
            holder.online_num.setText(info.getsCurrentQty());
            holder.need_num.setText(info.getsQtyNeed());
            holder.usable_date.setText(info.getsTime());
            holder.usable_percent.setText(info.getsUsablePercent());
            holder.part_desc.setText(info.getsPartDesc());
        }
        return view;
    }


    public static final class ViewHolder{

        private TextView online_warn_device;
        private TextView station_location;
        private TextView online_warn_reel_no;
        private TextView online_warn_part_no;
        private TextView dosage_tv;
        private TextView online_warn_feed_num;
        private TextView online_num;
        private TextView need_num;
        private TextView usable_date;
        private TextView usable_percent;
        private TextView part_desc;
        private LinearLayout warn_bg_layout;

    }
}
