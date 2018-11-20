package com.kanban.switchfragmaster.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ListView;


import com.kanban.switchfragmaster.R;
import com.kanban.switchfragmaster.adapter.FeedersMaterielAdapter;
import com.kanban.switchfragmaster.data.FeedersMaterielInfo;

import java.util.ArrayList;

/**
 * Created by LongQ on 2017/12/12.
 */

public class FeedersTableView extends LinearLayout {

    private Context context;
    private ListView mListView;
    private FeedersMaterielAdapter feedersAdapter;
    private ArrayList<FeedersMaterielInfo> feederList;
    public FeedersTableView(Context context, ArrayList<FeedersMaterielInfo> feederList) {
        super(context);
        this.context = context;
        this.feederList = feederList;
        initView();
        initData();
    }
    private void initView(){
        LayoutInflater.from(context).inflate(R.layout.fragment_feeders_location_table,this);
        mListView = findViewById(R.id.fling_materiel_listview);
    }

    protected void initData() {
        if(feederList!=null){
            feedersAdapter = new FeedersMaterielAdapter(context, feederList);
            mListView.setAdapter(feedersAdapter);
        }
    }
}
