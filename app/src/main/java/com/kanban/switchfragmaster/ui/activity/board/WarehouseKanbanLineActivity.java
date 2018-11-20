package com.kanban.switchfragmaster.ui.activity.board;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.kanban.switchfragmaster.R;
import com.kanban.switchfragmaster.adapter.WarehouseKanbanLineListAdapter;
import com.kanban.switchfragmaster.data.MyApplication;
import com.kanban.switchfragmaster.data.WoInfoWhLineEntity;
import com.kanban.switchfragmaster.http.service.GetData;
import com.kanban.switchfragmaster.view.TitleBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WarehouseKanbanLineActivity extends Activity {

    @BindView(R.id.warehouse_line_title_bar)
    TitleBarView titlebarview;
    @BindView(R.id.warehouse_list)
    ListView list1;
    @BindView(R.id.warehouse_line_tv_date)
    TextView tv_date;//当前时间
    @BindView(R.id.warehouse_line_cbx_pause_auto_scroll)
    CheckBox cbx_pause_auto_scroll;//暂停自动滚动

    private String sLineNo;
    private String sDealyTime;
    private String sIsByBom;
    private Timer timer = new Timer(true);
    private Context context;
    private List<WoInfoWhLineEntity> listWolienInfo;

//    备注： GetWarehouseMaterialMonitor  sIsByBom = "N"  返回数据说明
//     MACHINE_SEQ             --设备
//     MACHINE_NO     		--设备名称
//     FID			--站位
//     FID_SORTID		--站们序号
//     PART_NO			--物料号
//     PART_DESC		--产品描述
//     VICE_FLAG		--0 代表主料，大于0铺料
//     USE_QTY			--用量
//     NEED_TOTAL 		---需求总数
//     IS_INCLUDED_UNION 	---是否拼板用量
//     TOTAL_PREP_QTY 		---备料数量
//     TOTAL_PREP_COUNT 	---备料盘数
//     STORE_QTY		---拉存数量
//     STORE_COUNT		--拉存盘数
//     FINISH_FEED_QTY		--用完数量
//     FINISH_FEED_COUNT	--用完盘数
//     FEED_QTY 		--上料数量
//     TOTAL_FEED_COUNT	--上料盘数
//     TOTAL_FEED_QTY		--上料总数
//     TOTAL_TIME 		--当前时间  2016-08-03 00:40:13
//     TIME_CAN_USE		--可用时间
//     NEED_PREP_QTY		--还需备料数量
//     IS_PREP 		--是否备料
//     IS_SUB			--是否铺料


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse_kanban_line);
        ButterKnife.bind(this);
        context = context;
        initTitleBar();
        initView();
        //timer.schedule(task, 0, 120*1000);定时刷新
    }
    private void initTitleBar() {
        titlebarview.setIvBackImage(View.VISIBLE);
        titlebarview.setLeftText(getString(R.string.title_warehouse_board1));
        titlebarview.setBackOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void initView() {
        Bundle bundle = this.getIntent().getExtras();
        sLineNo = bundle.getString("sLineNo");
        sDealyTime = "60";
        sIsByBom = "N";
        cbx_pause_auto_scroll.setOnClickListener(pauseAutoScrollOnClickListener);
    }

    protected void onStart() {
        super.onStart();
        GetWarehoseKanbanLineDate();
    }

    /*
    //任务
    private TimerTask task = new TimerTask() {
        public void run() {
            GetWarehoseKanbanLineDate();//获取数据
        }
    };
    */

    //暂停自动滚动
    private View.OnClickListener pauseAutoScrollOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (cbx_pause_auto_scroll.isChecked()) {
                MyApplication.blPauseScroll = true;
            } else {
                MyApplication.blPauseScroll = false;
            }

        }
    };


    //获取数据
    private void GetWarehoseKanbanLineDate() {
        GetWarehoseKanbanLineDateAsyncTask getWarehoseKanbanLineDateAsyncTask = new GetWarehoseKanbanLineDateAsyncTask();
        getWarehoseKanbanLineDateAsyncTask.execute(sLineNo, sDealyTime, sIsByBom);
    }

    public class GetWarehoseKanbanLineDateAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            sLineNo = params[0];
            sDealyTime = params[1];
            sIsByBom = params[2];
            JSONObject jsonObj = new JSONObject();
            try {
                jsonObj.put("sLineNo", sLineNo);
                jsonObj.put("sDealyTime", sDealyTime);
                jsonObj.put("sIsByBom", sIsByBom);
                jsonObj.put("sMesUser", MyApplication.sMesUser);
                jsonObj.putOpt("sFactoryNo", MyApplication.sFactoryNo);
                jsonObj.put("sLanguage", MyApplication.sLanguage);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            return GetData.getDataByJson(jsonObj.toString(), "GetWarehouseMaterialMonitor");
        }

        @Override
        protected void onPostExecute(String json) {
            try {
                JSONArray jsonArray = new JSONArray(json);
                JSONObject item = jsonArray.getJSONObject(0);
                String sStatus = item.getString("status");
                String sMsg = item.getString("msg");
                String sDateNow = item.getString("sDateNow");
                tv_date.setText(sDateNow);
                listWolienInfo = new ArrayList<WoInfoWhLineEntity>();
                if (sStatus.equalsIgnoreCase("OK")) {
                    String sData = item.getString("data");
                    JSONArray jsonArrayData = new JSONArray(sData);
                    for (int i = 0; i < jsonArrayData.length(); i++) {
                        JSONObject itemData = jsonArrayData.getJSONObject(i);
                        WoInfoWhLineEntity mBasWoInfoLine = new WoInfoWhLineEntity();
                        mBasWoInfoLine.setsEquipmentId(itemData.getString("MACHINE_SEQ"));
                        mBasWoInfoLine.setsStration(itemData.getString("FID"));
                        mBasWoInfoLine.setsPartNo(itemData.getString("PART_NO"));
                        mBasWoInfoLine.setsTotalNO(itemData.getString("NEED_TOTAL"));
                        mBasWoInfoLine.setsPartStatus(itemData.getString("TOTAL_PREP_QTY"));
                        mBasWoInfoLine.setsQty(itemData.getString("FINISH_FEED_QTY"));
                        mBasWoInfoLine.setsQuantity(itemData.getString("STORE_QTY"));
                        mBasWoInfoLine.setsMinutes(itemData.getString("TIME_CAN_USE"));
                        listWolienInfo.add(mBasWoInfoLine);
                    }
                    //new Timer().schedule(new WarehouseKanbanListTimeTaskScroll(context, list1,listWolienInfo), 20, 30);

                } else {
                    Toast.makeText(context, sMsg, Toast.LENGTH_SHORT).show();
                }
                if(listWolienInfo!=null){
                    list1.setAdapter(new WarehouseKanbanLineListAdapter(context, listWolienInfo));
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }

    }
}
