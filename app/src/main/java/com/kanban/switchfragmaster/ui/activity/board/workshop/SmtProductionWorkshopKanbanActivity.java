package com.kanban.switchfragmaster.ui.activity.board.workshop;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.kanban.switchfragmaster.R;
import com.kanban.switchfragmaster.adapter.SmtProductionWorkshopKanbanAdapter;
import com.kanban.switchfragmaster.data.MyApplication;
import com.kanban.switchfragmaster.data.SevenusSmtWorkshopEntity;
import com.kanban.switchfragmaster.http.service.GetData;
import com.kanban.switchfragmaster.ui.activity.board.SmtLineBoardActivity;
import com.kanban.switchfragmaster.ui.activity.board.SmtProductionWorkshopCharacterActivity;
import com.kanban.switchfragmaster.utils.SharedPreferencesUtils;
import com.kanban.switchfragmaster.view.TitleBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xclcharts.common.DensityUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SmtProductionWorkshopKanbanActivity extends Activity {
    @BindView(R.id.workshop_title_bar)
    TitleBarView titlebarview;
    String sInterfaceType = "SMT";
    String sMoreLineNoIn = "";  // 多线别
    @BindView(R.id.workshop_lv_list)
    ListView lv_list;
    @BindView(R.id.workshop_tv_date)
    TextView tv_date;//服务器时间

    private Timer timer_prod = new Timer();
    private Timer timer_cha = new Timer();
    boolean isStart = false;
    boolean isPause = false;
    private Context context;
    private List<SevenusSmtWorkshopEntity> listWolienInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smt_production_workshop_kanban);
        ButterKnife.bind(this);
        context = SmtProductionWorkshopKanbanActivity.this;
        initTitleBar();
    }

    private void initTitleBar() {
        titlebarview.setIvBackImage(View.VISIBLE);
        titlebarview.setLeftText(getString(R.string.smt_production_workshop_kanban1));
        titlebarview.setBackOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            sInterfaceType = intent.getStringExtra("sInterfaceType");
        }
        sMoreLineNoIn = SharedPreferencesUtils.getValue(context, "MoreLineNoIn", "");
        //获取数据的方法
        GetProductionWorkshopLineDate();
        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent newsIntent = new Intent(context, SmtLineBoardActivity.class);
                newsIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                newsIntent.putExtra("sLineNo", listWolienInfo.get(i).getsLineNo());
                context.startActivity(newsIntent);
            }
        });

    }
    private void renderChart(List<SevenusSmtWorkshopEntity> workshopEntities) {
        int width = DensityUtil.dip2px(getApplicationContext(), 300);
        int height = DensityUtil.dip2px(getApplicationContext(), 400);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
//        workshopChart.removeAllViews();
        SpinnerBarChart01View barChart01= new SpinnerBarChart01View(this,0,workshopEntities);
//        workshopChart.addView(barChart01,layoutParams);

    }
    @Override
    protected void onStart() {
        super.onStart();
        isStart = true;
        isPause = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isStart = false;
        isPause = true;
        if (timer_prod != null) {
            timer_prod.cancel();
            timer_prod = null;
        }
        if (timer_cha != null) {
            timer_cha.cancel();
            timer_cha = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer_prod != null) {
            timer_prod.cancel();
            timer_prod = null;
        }
        if (timer_cha != null) {
            timer_cha.cancel();
            timer_cha = null;
        }
    }

    //获取生产状况数据
    private void GetProductionWorkshopLineDate() {
        if (!isStart) {
            return;
        }
        isStart = false;
        if (!TextUtils.isEmpty(sMoreLineNoIn)) {//判断车间是否有选择
            GetSmtProductionWorkshopKanbanAsyncTask getWarehoseKanbanLineDateAsyncTask = new GetSmtProductionWorkshopKanbanAsyncTask();
            getWarehoseKanbanLineDateAsyncTask.execute();
        }

    }

    public class GetSmtProductionWorkshopKanbanAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            JSONObject jsonObj = new JSONObject();
            try {
                jsonObj.put("sInterfaceType", "SMT");
                jsonObj.put("sLineNoIn", sMoreLineNoIn);
                jsonObj.put("sMesUser", MyApplication.sMesUser);
                jsonObj.putOpt("sFactoryNo", MyApplication.sFactoryNo);
                jsonObj.put("sLanguage", MyApplication.sLanguage);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            return GetData.getDataByJson(jsonObj.toString(), "GetWorkShop");
        }

        @Override
        protected void onPostExecute(String json) {
            if (!isPause) {
                isStart = true;
            }
            if (null == json || json.equals("")) {
                return;
            }
            try {
                JSONArray jsonArray = new JSONArray(json);
                JSONObject item = jsonArray.getJSONObject(0);
                String sStatus = item.getString("status");
                String sMsg = item.getString("msg");
                if (item.has("sDateNow")) {
                    String sDateNow = item.getString("sDateNow");
                    tv_date.setText(sDateNow); //显示时间
                }
               listWolienInfo = new ArrayList<>();
                if (sStatus.equalsIgnoreCase("OK")) {
                    String sData = item.getString("data");
                    JSONArray jsonArrayData = new JSONArray(sData);
                    int jsonArrda = jsonArrayData.length();
                    for (int i = 0; i < jsonArrda; i++) {
                        JSONObject itemData = jsonArrayData.getJSONObject(i);
                        SevenusSmtWorkshopEntity mBasWoInfoLine = new SevenusSmtWorkshopEntity();
                        mBasWoInfoLine.setsLineNo(itemData.getString("sLineNo")); //线别
//                        mBasWoInfoLine.setsCustomerNo(itemData.getString("sCustomerName")); //客户
                        mBasWoInfoLine.setsProductNo(itemData.getString("sProductNo")); //产品号
                        mBasWoInfoLine.setSo(itemData.getString("sWo")); //制令单
                        mBasWoInfoLine.setsMoPlanQty(itemData.getString("sWoQtyPlan")); //制令单数据
                        String sFinishingRate = itemData.getString("sFinishingRate");
                        mBasWoInfoLine.setsMoFinishingRate(sFinishingRate); //工单完成率
//                        String sOperationRate = itemData.getString("sOperationRate") + "%";
                        mBasWoInfoLine.setsProductionState(itemData.getString("sWorkStatusName")); //产线状态
                        listWolienInfo.add(mBasWoInfoLine);
                    }
                } else {
                    Toast.makeText(SmtProductionWorkshopKanbanActivity.this, sMsg, Toast.LENGTH_LONG).show();
                }
                if (listWolienInfo != null) {
                    lv_list.setAdapter(new SmtProductionWorkshopKanbanAdapter(context, listWolienInfo));
//                    timer_prod.schedule(mTimerTask_Cha, 60 * 1000); //切换至品质数据
//                    renderChart(listWolienInfo);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(SmtProductionWorkshopKanbanActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    //任务-切换至品质数据
    private TimerTask mTimerTask_Cha = new TimerTask() {
        @Override
        public void run() {
            Intent intent = new Intent();
            intent.setClass(SmtProductionWorkshopKanbanActivity.this, SmtProductionWorkshopCharacterActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            SmtProductionWorkshopKanbanActivity.this.finish();
        }
    };


    private long nExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - nExitTime) > 2000) {
                // Object mHelperUtils;
                Toast.makeText(this, R.string.again_according_to_exit_the_program, Toast.LENGTH_SHORT).show();
                nExitTime = System.currentTimeMillis();

            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
