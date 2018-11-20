package com.kanban.switchfragmaster.ui.activity.board.workshop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;


import com.kanban.switchfragmaster.R;
import com.kanban.switchfragmaster.adapter.SmtProductionWorkshopKanbanAdapter;
import com.kanban.switchfragmaster.data.SevenusSmtWorkshopEntity;
import com.kanban.switchfragmaster.ui.activity.board.SmtLineBoardActivity;

import java.util.ArrayList;
import java.util.List;

public class SmtProductWorkshopKanbanView extends LinearLayout {
    private Context context;
    private ListView mListView;
    private List<SevenusSmtWorkshopEntity> listWolienInfo ;
    public SmtProductWorkshopKanbanView(Context context,List<SevenusSmtWorkshopEntity> listWolienInfo) {
        super(context);
        this.context = context;
        this.listWolienInfo = listWolienInfo;
        initView();
        initData();
    }
    private void initView(){
        LayoutInflater.from(context).inflate(R.layout.frag_smt_product_workshop_kanban,this);
        mListView = findViewById(R.id.workshop_lv_list);
    }

    protected void initData() {
        if (listWolienInfo != null) {
            mListView.setAdapter(new SmtProductionWorkshopKanbanAdapter(context, listWolienInfo));
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent newsIntent = new Intent(context, SmtLineBoardActivity.class);
                    newsIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(newsIntent);
                }
            });
        }
    }
}
