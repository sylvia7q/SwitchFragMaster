package com.kanban.switchfragmaster.ui.activity.message;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;


import com.kanban.switchfragmaster.R;
import com.kanban.switchfragmaster.adapter.ChatMsgViewAdapter;
import com.kanban.switchfragmaster.data.ChatMsgInfo;
import com.kanban.switchfragmaster.presenter.MyPresenter;
import com.kanban.switchfragmaster.view.TitleBarView;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatActivity extends Activity{
    @BindView(R.id.activity_chat_titlebarview)
    TitleBarView titlebarview;
    @BindView(R.id.listview)
    ListView mListView;
    //聊天的内容
    private List<ChatMsgInfo> chatMsgInfos = new ArrayList<ChatMsgInfo>();
    private String sendId = "";
    private String sendName = "";
    private String content = "";
    private String date = "";
    private int messageType;
    private Context context;
    private ChatMsgViewAdapter mAdapter;
    public MyHandler myHandler = new MyHandler(this);
    private int pos = 0;

    private class MyHandler extends Handler {
        private final WeakReference<ChatActivity> mActivity;

        public MyHandler(ChatActivity activity) {
            mActivity = new WeakReference<ChatActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if(chatMsgInfos!=null) {
                        mAdapter.notifyDataSetChanged();
                    }
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        context = ChatActivity.this;
        initData();
        initTitleBar();
    }
    private void initTitleBar() {
        titlebarview.setIvBackImage(View.VISIBLE);
        titlebarview.setBackOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        titlebarview.setLeftText("");
    }

    private void initData(){
        Intent intent = getIntent();

        if(intent!=null){
            messageType = intent.getIntExtra("messageType",0);
        }
        chatMsgInfos = MyPresenter.getInstance(context).chatMsgArrived(messageType);
        titlebarview.setCenterText(messageType + "");
        if(chatMsgInfos != null){
            mAdapter = new ChatMsgViewAdapter(context,chatMsgInfos);
            mListView.setAdapter(mAdapter);
            if(chatMsgInfos.size() >=1 ){
                mListView.setSelection(chatMsgInfos.size());
                mAdapter.setOnDeleteListener(new ChatMsgViewAdapter.OnDeleteListener() {
                    @Override
                    public void delete(int position, View view) {
                        showPopupMenu(view,position);
                    }
                });
            }
        }
        MyPresenter.getInstance(context).setChatHandler(myHandler);

    }
    private void showPopupMenu(View view, int position) {
        pos = position;
        PopupMenu popupMenu = new PopupMenu(context, view);
        //引入菜单资源
        popupMenu.inflate(R.menu.chatmsg_delete_menu);
        //菜单项的监听
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.delete:
                        ChatMsgInfo info = chatMsgInfos.get(pos);
                        chatMsgInfos.remove(pos);
                        if(info!=null){
                            List<ChatMsgInfo> infoList = MyPresenter.getInstance(context).deleteChatMsg(info);
                            if(infoList.size() == 0){
                                finish();
                            }
                        }
                        break;
                }
                return true;
            }
        });
        //显示PopupMenu
        popupMenu.show();
    }
//    private void send() {
//        String contString = mEditTextContent.getText().toString();
//        if (contString.length() > 0) {
//            ChatMsgInfo entity = new ChatMsgInfo();
//            entity.setSendTime(getDate());
////            entity.setSendName(sendName);
//            entity.setMessageType(0);
//            entity.setContent(contString);
//            chatMsgInfos.add(entity);
//
//            mAdapter.notifyDataSetChanged();
//            mEditTextContent.setText("");
//            mListView.setSelection(mListView.getCount() - 1);
//        }
//    }



    private void back() {
        finish();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
