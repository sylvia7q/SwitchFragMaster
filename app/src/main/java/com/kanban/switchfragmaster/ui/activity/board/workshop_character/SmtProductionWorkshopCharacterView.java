package com.kanban.switchfragmaster.ui.activity.board.workshop_character;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ListView;


import com.kanban.switchfragmaster.R;
import com.kanban.switchfragmaster.adapter.SmtProductionWorkshopCharacterAdapter;
import com.kanban.switchfragmaster.data.SevenusSmtWorkshopCharacterEntity;

import java.util.List;

public class SmtProductionWorkshopCharacterView extends LinearLayout {
    private Context context;
    private ListView mListView;
    private List<SevenusSmtWorkshopCharacterEntity> listWolienInfo ;
    public SmtProductionWorkshopCharacterView(Context context,List<SevenusSmtWorkshopCharacterEntity> listWolienInfo) {
        super(context);
        this.context = context;
        this.listWolienInfo = listWolienInfo;
        initView();
        initData();
    }
    private void initView(){
        LayoutInflater.from(context).inflate(R.layout.frag_smt_product_workshop_kanban_character,this);
        mListView = findViewById(R.id.workshop_character_lv_list);
    }

    protected void initData() {
        if(listWolienInfo!=null){
            mListView.setAdapter(new SmtProductionWorkshopCharacterAdapter(context, listWolienInfo));
        }
    }
}
