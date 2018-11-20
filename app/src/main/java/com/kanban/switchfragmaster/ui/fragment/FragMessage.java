package com.kanban.switchfragmaster.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.view.menu.MenuPopupHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;


import com.kanban.switchfragmaster.R;
import com.kanban.switchfragmaster.adapter.MessageAdapter;
import com.kanban.switchfragmaster.data.AppConstants;
import com.kanban.switchfragmaster.data.MessageInfo;
import com.kanban.switchfragmaster.presenter.MyPresenter;
import com.kanban.switchfragmaster.ui.activity.message.ChatActivity;
import com.kanban.switchfragmaster.utils.DialogUtil;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Subscription;

public class FragMessage extends BaseFragment {

    private final static String TAG = FragMessage.class.getName();
    @BindView(R.id.frag_message_lv_data)
    ListView fragMessageLvData;
    private MessageAdapter adapter;
    private List<MessageInfo> messageInfos = new ArrayList<>();
    public MyHandler myHandler = new MyHandler(FragMessage.this);

    private class MyHandler extends Handler {
        private final WeakReference<FragMessage> mFragment;

        public MyHandler(FragMessage fragment) {
            mFragment = new WeakReference<FragMessage>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 2:
                    if (messageInfos != null) {
                        adapter.notifyDataSetChanged();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_frag_message;
    }

    @Override
    protected void initView() {
//        setHasOptionsMenu(true);
//        registerForContextMenu(fragMessageLvData);
    }

    @Override
    protected void initInject() {

    }

    @Override
    protected void loadData() {
        setState(AppConstants.STATE_SUCCESS);
    }

    @Override
    public void onResume() {
        super.onResume();
        init();

    }

    public void init() {
        messageInfos = MyPresenter.getInstance(context).messageArrived();
        if (messageInfos != null) {
            adapter = new MessageAdapter(context, messageInfos);
            fragMessageLvData.setAdapter(adapter);
            if(messageInfos.size()>=1){
                fragMessageLvData.setSelection(0);
                fragMessageLvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (messageInfos != null && messageInfos.size() >= 1) {
                            MessageInfo messageInfo = new MessageInfo();
                            messageInfo.setUserId(messageInfos.get(position).getUserId());
                            messageInfo.setUserName(messageInfos.get(position).getUserName());
                            messageInfo.setSendTime(messageInfos.get(position).getSendTime());
                            messageInfo.setContent(messageInfos.get(position).getContent());
                            messageInfo.setMessageType(messageInfos.get(position).getMessageType());
                            messageInfo.setUnreadCount(0);
                            MyPresenter.getInstance(context).updateMessage(messageInfo);
                            listener.update();
                            Intent aboutIntent = new Intent(context, ChatActivity.class);
                            aboutIntent.putExtra("messageType", messageInfos.get(position).getMessageType());
                            aboutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(aboutIntent);
                        }

                    }
                });
                fragMessageLvData.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        showPopupMenu(view, position);
                        view.setSelected(true);
                        return true;
                    }
                });
            }
        }
        MyPresenter.getInstance(context).setMessageHandler(myHandler);


    }
    //    int checkedItemId = R.id.delete;
    private void showPopupMenu(View ancher, final int position) {
        PopupMenu popupMenu = new PopupMenu(context, ancher);
        //引入菜单资源
        popupMenu.inflate(R.menu.message_delete_menu);
//        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
//            popupMenu.setGravity(Gravity.CENTER);
//        }
        //设置选中
//        popupMenu.getMenu().findItem(R.id.delete).setChecked(true);
        //菜单项的监听
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.delete:
//                        checkedItemId = R.id.delete;
                        deleteDialog(position);
                        break;
                }
                return true;
            }
        });

        //使用反射。强制显示菜单图标
//        try {
//            Field field = popupMenu.getClass().getDeclaredField("mPopup");
//            field.setAccessible(true);
//            MenuPopupHelper mHelper = (MenuPopupHelper) field.get(popupMenu);
//            mHelper.setForceShowIcon(true);
//        } catch (IllegalAccessException | NoSuchFieldException e) {
//            e.printStackTrace();
//        }

        //显示PopupMenu
        popupMenu.show();
    }

    private void deleteDialog(final int position) {
        final MessageInfo info = messageInfos.get(position);
        final Dialog dialog = new Dialog(context, R.style.lb_myDialog);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_item_long_click, null);
        TextView tvCancel = view.findViewById(R.id.dialog_frag_item_delete_cancel);
        TextView tvConfirm = view.findViewById(R.id.dialog_frag_item_delete_confirm);
        dialog.setContentView(view);
        dialog.show();
        DialogUtil.setDialogWidthHeight(context, dialog, 0.8f);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageInfos.remove(position);
                MyPresenter.getInstance(context).deleteMessage(info.getMessageType());
                adapter.notifyDataSetChanged();
                listener.update();
                dialog.dismiss();
            }
        });
    }

    // 2.1 定义用来与外部activity交互，获取到宿主activity
    private FragmentListener listener;


    // 1 定义了所有activity必须实现的接口方法
    public interface FragmentListener {
        void update();
    }

    //把传递进来的activity对象释放掉
    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    // 当FRagmen被加载到activity的时候会被回调
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof FragmentListener) {
            listener = (FragmentListener) activity; // 2.2 获取到宿主activity并赋值
        } else {
            throw new IllegalArgumentException("activity must implements FragmentInteraction");
        }
    }
}
