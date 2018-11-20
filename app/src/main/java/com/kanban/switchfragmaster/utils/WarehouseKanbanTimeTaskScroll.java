package com.kanban.switchfragmaster.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;


import com.kanban.switchfragmaster.adapter.WarehouseKanbanListAdapter;
import com.kanban.switchfragmaster.data.MyApplication;
import com.kanban.switchfragmaster.data.WoInfoWhEntity;

import java.util.List;
import java.util.TimerTask;

/**
 * Created by Justin Fang on 2016/6/12.
 */
public class WarehouseKanbanTimeTaskScroll extends TimerTask {
    private ListView listView;

    public WarehouseKanbanTimeTaskScroll(Context context, ListView listView, List<WoInfoWhEntity> items){
        this.listView = listView;
        listView.setAdapter(new WarehouseKanbanListAdapter(context,items));
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if(!MyApplication.blPauseScroll) {
                listView.smoothScrollBy(1, 0);//ListView上下滚动，参数代表像素(X像素,Y像素)
            }
        };
    };

    @Override
    public void run() {
        Message msg = handler.obtainMessage();
        handler.sendMessage(msg);
    }
}
