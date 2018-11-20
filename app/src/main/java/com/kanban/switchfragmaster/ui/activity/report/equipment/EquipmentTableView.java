package com.kanban.switchfragmaster.ui.activity.report.equipment;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.kanban.switchfragmaster.R;
import com.kanban.switchfragmaster.adapter.EquipmentAdapter;
import com.kanban.switchfragmaster.data.EquipmentInfo;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Created by LongQ on 2017/8/15.
 * 运转率图表的表格界面
 */
public class EquipmentTableView extends LinearLayout {

    private Context context;
    private ListView mListView;
    private EquipmentAdapter adapter;
    private ArrayList<EquipmentInfo> equipmentList;

    public EquipmentTableView(Context context, ArrayList<EquipmentInfo> equipmentList) {
        super(context);

        this.equipmentList = equipmentList;
        this.context = context;
        initView();
        initData();
    }

    private void initView(){
        LayoutInflater.from(context).inflate(R.layout.fragment_frag_equipment_table,this);
        mListView = findViewById(R.id.equipment_listview);
    }

    protected void initData() {
        if(equipmentList!=null){
            adapter = new EquipmentAdapter(context, equipmentList);
            mListView.setAdapter(adapter);
        }
    }
}
