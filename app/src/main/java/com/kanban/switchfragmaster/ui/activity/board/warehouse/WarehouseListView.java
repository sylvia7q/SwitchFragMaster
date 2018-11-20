package com.kanban.switchfragmaster.ui.activity.board.warehouse;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;


import com.kanban.switchfragmaster.R;
import com.kanban.switchfragmaster.data.MyApplication;
import com.kanban.switchfragmaster.data.WoInfoWhEntity;
import com.kanban.switchfragmaster.utils.WarehouseKanbanTimeTaskScroll;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class WarehouseListView extends LinearLayout{
    private Context context;
    private ListView mListView;
    private List<WoInfoWhEntity> listWoInfo;
    private CheckBox cbx_pause_auto_scroll;
    public WarehouseListView(Context context,List<WoInfoWhEntity> listWoInfo) {
        super(context);
        this.context = context;
        this.listWoInfo = listWoInfo;
        initView();
        initData();
    }
    private void initView(){
        LayoutInflater.from(context).inflate(R.layout.activity_warehouse_list_view,this);
        mListView = findViewById(R.id.activity_warehouse_kanban_lv_data);
        cbx_pause_auto_scroll = findViewById(R.id.activity_warehouse_kanbancbx_pause_auto_scroll);
        cbx_pause_auto_scroll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    MyApplication.blPauseScroll = true;
                } else {
                    MyApplication.blPauseScroll = false;
                }
            }
        });
    }

    protected void initData() {
        if (listWoInfo != null) {
            new Timer().schedule(new WarehouseKanbanTimeTaskScroll(context, mListView, listWoInfo), 20, 30);
        }
    }

}
