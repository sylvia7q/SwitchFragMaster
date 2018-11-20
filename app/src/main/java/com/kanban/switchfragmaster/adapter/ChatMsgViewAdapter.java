package com.kanban.switchfragmaster.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.kanban.switchfragmaster.R;
import com.kanban.switchfragmaster.data.ChatMsgInfo;

import java.util.List;

public class ChatMsgViewAdapter extends BaseAdapter {

    private ChatMsgInfo entity;
    public OnDeleteListener listener;
    //ListView视图的内容由IMsgViewType决定
    public static interface IMsgViewType
    {
        //对方发来的信息
        int IMVT_COM_MSG = 0;
        //自己发出的信息
        int IMVT_TO_MSG = 1;
    }

    private static final String TAG = ChatMsgViewAdapter.class.getSimpleName();
    private List<ChatMsgInfo> data;
    private Context context;
    private LayoutInflater mInflater;

    public ChatMsgViewAdapter(Context context, List<ChatMsgInfo> data) {
        this.context = context;
        this.data = data;
        mInflater = LayoutInflater.from(context);
    }

    //获取ListView的项个数
    public int getCount() {
        return data.size();
    }

    //获取项
    public Object getItem(int position) {
        return data.get(position);
    }

    //获取项的ID
    public long getItemId(int position) {
        return position;
    }

    //获取项的类型
    public int getItemViewType(int position) {
        // TODO Auto-generated method stub
        ChatMsgInfo entity = data.get(position);
        if(entity.getMessageType()==0){
            return IMsgViewType.IMVT_COM_MSG;
        }else {
            return IMsgViewType.IMVT_TO_MSG;
        }
    }

    //获取项的类型数
    public int getViewTypeCount() {
        // TODO Auto-generated method stub
        return 2;
    }

    //获取View
    public View getView(final int position, View convertView, ViewGroup parent) {
//        int MsgType = entity.getMessageType();
        int MsgType = 0;
        ViewHolder viewHolder = null;
        if (convertView == null)
        {
            if (MsgType == 0) {
                //如果是对方发来的消息，则显示的是左气泡
                convertView = mInflater.inflate(R.layout.chatting_item_msg_text_left, null);
            }else{
                //如果是自己发出的消息，则显示的是右气泡
                convertView = mInflater.inflate(R.layout.chatting_item_msg_text_right, null);
            }

            viewHolder = new ViewHolder();
            viewHolder.ivUserIcon = convertView.findViewById(R.id.iv_user_icon);
            viewHolder.tvSendTime = convertView.findViewById(R.id.tv_sendtime);
            viewHolder.tvContent = convertView.findViewById(R.id.tv_chat_content);
            viewHolder.cbDelete = convertView.findViewById(R.id.cb_message_delete);
            viewHolder.type = MsgType;

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        entity = data.get(position);
        if(entity != null){
            String sendTime = entity.getSendTime();
            viewHolder.tvSendTime.setText(sendTime);
            viewHolder.tvContent.setText(entity.getContent());
            int type = entity.getMessageType();
            if(type == 1){
                viewHolder.ivUserIcon.setBackgroundResource(R.mipmap.message_type1);
            }else if(type == 2){
                viewHolder.ivUserIcon.setBackgroundResource(R.mipmap.message_type2);
            }else if(type == 3){
                viewHolder.ivUserIcon.setBackgroundResource(R.mipmap.message_type3);
            }else if(type == 4){
                viewHolder.ivUserIcon.setBackgroundResource(R.mipmap.message_type4);
            }else {
                viewHolder.ivUserIcon.setBackgroundResource(R.mipmap.broadcast);
            }
            viewHolder.tvContent.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(v != null && listener != null){
                        listener.delete(position,v);
                        return true;
                    }
                    return false;
                }
            });
            viewHolder.cbDelete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    listener.delete(position,isChecked);
                }
            });
        }

        return convertView;
    }

    //通过ViewHolder显示项的内容
    static class ViewHolder {
        public ImageView ivUserIcon;
        public TextView tvSendTime;
        public TextView tvContent;
        public CheckBox cbDelete;
        public int type = 0;
    }
    public interface OnDeleteListener{
        void delete(int position, View view);
    }
    public void setOnDeleteListener(OnDeleteListener listener){
        this.listener = listener;
    }

}

