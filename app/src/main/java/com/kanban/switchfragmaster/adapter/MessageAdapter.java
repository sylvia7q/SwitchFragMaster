package com.kanban.switchfragmaster.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.kanban.switchfragmaster.R;
import com.kanban.switchfragmaster.data.MessageInfo;

import java.util.List;

/**
 * Created by LongQ on 2017/11/27.
 */

public class MessageAdapter extends BaseAdapter implements View.OnClickListener{
    private List<MessageInfo> messageInfos;
    private Context context;
    // add click callback
    public OnReadClickListener onItemReadClick;
    private int selectedPosition = -1;// 选中的位置

    public MessageAdapter(Context context, List<MessageInfo> messageInfos) {
        this.context = context;
        this.messageInfos = messageInfos;
    }

    @Override
    public int getCount() {
        return messageInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return messageInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
      final ViewHolder holder;
        if(view == null){
            holder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.list_item_frag_message_info,null);
            holder.ll_bg = view.findViewById(R.id.frag_message_ll_bg);
            holder.tvUserName = view.findViewById(R.id.frag_message_user_name);
            holder.ivMessageType = view.findViewById(R.id.frag_message_iv_type);
            holder.tvContent =  view.findViewById(R.id.frag_message_content);
            holder.tvTime = view.findViewById(R.id.frag_message_time);
            holder.tvCount = view.findViewById(R.id.frag_message_count);
            view.setTag(holder);
        }else {
            holder  = (ViewHolder) view.getTag();
        }
        MessageInfo info = messageInfos.get(position);
        if(info != null){
            holder.tvUserName.setText(info.getMessageType() + "");
            holder.tvContent.setText(info.getContent());
            holder.tvTime.setText(info.getSendTime());
            int count = info.getUnreadCount();
            if(count > 0){
                holder.tvCount.setVisibility(View.VISIBLE);
                holder.tvCount.setText(count + "");
            }else {
                holder.tvCount.setVisibility(View.GONE);
            }

            int type = info.getMessageType();
            if(type == 1){
                holder.ivMessageType.setBackgroundResource(R.mipmap.message_type1);
            }else if(type == 2){
                holder.ivMessageType.setBackgroundResource(R.mipmap.message_type2);
            }else if(type == 3){
                holder.ivMessageType.setBackgroundResource(R.mipmap.message_type3);
            }else if(type == 4){
                holder.ivMessageType.setBackgroundResource(R.mipmap.message_type4);
            }else {
                holder.ivMessageType.setBackgroundResource(R.mipmap.broadcast);
            }
        }
        return view;
    }
    @Override
    public void onClick(View v) {
        onItemReadClick.onItemClick(v);

    }

    public static final class ViewHolder{

        private LinearLayout ll_bg;
        private TextView tvUserName;
        private ImageView ivMessageType;
        private TextView tvContent;
        private TextView tvTime;
        private TextView tvCount;
    }
    public  interface OnReadClickListener {
        // true add; false cancel
        void onItemClick(View v);
    }



    public void setOnReadClickListener(OnReadClickListener onItemReadClick) {
        this.onItemReadClick = onItemReadClick;
    }
}

