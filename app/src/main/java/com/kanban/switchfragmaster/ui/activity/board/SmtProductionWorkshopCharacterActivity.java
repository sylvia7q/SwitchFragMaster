package com.kanban.switchfragmaster.ui.activity.board;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.kanban.switchfragmaster.R;
import com.kanban.switchfragmaster.adapter.SmtProductionWorkshopCharacterAdapter;
import com.kanban.switchfragmaster.data.MyApplication;
import com.kanban.switchfragmaster.data.SevenusSmtWorkshopCharacterEntity;
import com.kanban.switchfragmaster.http.service.GetData;
import com.kanban.switchfragmaster.ui.activity.board.workshop.SmtProductionWorkshopKanbanActivity;
import com.kanban.switchfragmaster.ui.activity.board.workshop_character.SpinnerBarChart02View;
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

public class SmtProductionWorkshopCharacterActivity extends Activity {
    @BindView(R.id.workshop_character_title_bar)
    TitleBarView titlebarview;
    @BindView(R.id.workshop_character_lv_list)
    ListView lv_list;
    @BindView(R.id.workshop_character_tv_date)
    TextView tv_date;//服务器时间
    String sMoreLineNoIn = "";  // 多线别
    @BindView(R.id.workshop_character_chart)
    LinearLayout llChart;
    private Timer timer_list = new Timer(); //定时刷新ListView上下滚动
    private Timer timer_switching = new Timer(); //两个Activity切换
    int nGet = 0; //获取数据后自动隐藏菜单，默认2次
    boolean isStart = false;
    boolean isPause = false;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smt_production_workshop_character);
        ButterKnife.bind(this);
        context = SmtProductionWorkshopCharacterActivity.this;
        initTitleBar();
    }

    private void initTitleBar() {
        titlebarview.setIvBackImage(View.VISIBLE);
        titlebarview.setLeftText(getString(R.string.sevenus_quality_title_tv1));
        titlebarview.setBackOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        isStart = true;
        isPause = false;
        initData();
    }

    private void initData() {
        sMoreLineNoIn = SharedPreferencesUtils.getValue(context, "MoreLineNoIn", "");
        //获取数据的方法
        GetWorkshopCharacterLineDate();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isStart = false;
        isPause = true;
        if (timer_list != null) {
            timer_list.cancel();
            timer_list = null;
        }
        if (timer_switching != null) {
            timer_switching.cancel();
            timer_switching = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer_list != null) {
            timer_list.cancel();
            timer_list = null;
        }
        if (timer_switching != null) {
            timer_switching.cancel();
            timer_switching = null;
        }
    }

    private void GetWorkshopCharacterLineDate() {
        if (!isStart) {
            return;
        }
        isStart = false;
        SmtProductionWorkshopCharacterAsyncTask getWorkshopCharacterLineDateAsyncTask = new SmtProductionWorkshopCharacterAsyncTask();
        getWorkshopCharacterLineDateAsyncTask.execute();
    }

    private class SmtProductionWorkshopCharacterAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            JSONObject jsonObj = new JSONObject();
            try {
                jsonObj.put("sLineNoIn", sMoreLineNoIn);
                jsonObj.put("sMesUser", MyApplication.sMesUser);
                jsonObj.putOpt("sFactoryNo", MyApplication.sFactoryNo);
                jsonObj.put("sLanguage", MyApplication.sLanguage);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            return GetData.getDataByJson(jsonObj.toString(), "GetWorkShopExtend");
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
                String sDateNow = item.getString("sDateNow");
                String sNumber = item.getString("sSpoilageRateColCount"); //列
                int sCount = Integer.parseInt(sNumber); //string转int
                tv_date.setText(sDateNow); //显示时间
                List<SevenusSmtWorkshopCharacterEntity> listWolienInfo = new ArrayList<SevenusSmtWorkshopCharacterEntity>();
                if (sStatus.equalsIgnoreCase("OK")) {
                    String sData = item.getString("data");
                    JSONArray jsonArrayData = new JSONArray(sData);
                    int jsonArrda = jsonArrayData.length();
                    for (int i = 0; i < jsonArrda; i++) {
                        JSONObject itemData = jsonArrayData.getJSONObject(i);
                        SevenusSmtWorkshopCharacterEntity mBasWoInfoLine = new SevenusSmtWorkshopCharacterEntity();
                        mBasWoInfoLine.setsLineNo(itemData.getString("LINE_NO")); //线别
                        mBasWoInfoLine.setsRty(itemData.getString("RTY")); //RTY
                        if (sCount == 1) {
                            mBasWoInfoLine.setsHrowingRate1(itemData.getString("SPOILAGE_RATE1")); //1#抛料率
                            mBasWoInfoLine.setsHrowingRate2("NA"); //2#抛料率
                            mBasWoInfoLine.setsHrowingRate3("NA"); //3#抛料率
                        }
                        if (sCount == 2) {
                            mBasWoInfoLine.setsHrowingRate1(itemData.getString("SPOILAGE_RATE1")); //1#抛料率
                            if (itemData.getString("SPOILAGE_RATE2").equals("")) {
                                mBasWoInfoLine.setsHrowingRate2("NA"); //2#抛料率
                            } else {
                                mBasWoInfoLine.setsHrowingRate2(itemData.getString("SPOILAGE_RATE2")); //2#抛料率
                            }
                            mBasWoInfoLine.setsHrowingRate3("NA"); //3#抛料率
                        }
                        if (sCount == 3) {
                            mBasWoInfoLine.setsHrowingRate1(itemData.getString("SPOILAGE_RATE1")); //1#抛料率
                            if (itemData.getString("SPOILAGE_RATE2").equals("")) {
                                mBasWoInfoLine.setsHrowingRate2("NA"); //2#抛料率
                            } else {
                                mBasWoInfoLine.setsHrowingRate2(itemData.getString("SPOILAGE_RATE2")); //2#抛料率
                            }
                            if (itemData.getString("SPOILAGE_RATE3").equals("")) {
                                mBasWoInfoLine.setsHrowingRate3("NA"); //3#抛料率
                            } else {
                                mBasWoInfoLine.setsHrowingRate3(itemData.getString("SPOILAGE_RATE3")); //3#抛料率
                            }
                        }
                        if (sCount >= 4) {
                            mBasWoInfoLine.setsHrowingRate1(itemData.getString("SPOILAGE_RATE1")); //1#抛料率
                            if (itemData.getString("SPOILAGE_RATE2").equals("")) {
                                mBasWoInfoLine.setsHrowingRate2("NA"); //2#抛料率
                            } else {
                                mBasWoInfoLine.setsHrowingRate2(itemData.getString("SPOILAGE_RATE2")); //2#抛料率
                            }
                            if (itemData.getString("SPOILAGE_RATE3").equals("")) {
                                mBasWoInfoLine.setsHrowingRate3("NA"); //3#抛料率
                            } else {
                                mBasWoInfoLine.setsHrowingRate3(itemData.getString("SPOILAGE_RATE3")); //3#抛料率
                            }
                        }
                        mBasWoInfoLine.setsProductionState(itemData.getString("STATUS")); //产线状态代码
                        mBasWoInfoLine.setsProductionStateName(itemData.getString("STATUS_NAME")); //产线状态名称
                        listWolienInfo.add(mBasWoInfoLine);
                    }

                } else {
                    Toast.makeText(SmtProductionWorkshopCharacterActivity.this, sMsg, Toast.LENGTH_SHORT).show();
                }
                if(listWolienInfo!=null){
                    lv_list.setAdapter(new SmtProductionWorkshopCharacterAdapter(SmtProductionWorkshopCharacterActivity.this, listWolienInfo));
//                    timer_switching.schedule(mTimerTask, 60 * 1000); //切换至生产数据
                    renderChart(listWolienInfo);
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(SmtProductionWorkshopCharacterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }
    private void renderChart(List<SevenusSmtWorkshopCharacterEntity> characterEntities) {
        int width = DensityUtil.dip2px(getApplicationContext(), 300);
        int height = DensityUtil.dip2px(getApplicationContext(), 400);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        llChart.removeAllViews();
        SpinnerBarChart02View barChart02= new SpinnerBarChart02View(this,0,characterEntities);
        llChart.addView(barChart02,layoutParams);

    }
    //任务
    private TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            Intent intent = new Intent();
            intent.setClass(SmtProductionWorkshopCharacterActivity.this, SmtProductionWorkshopKanbanActivity.class);
            startActivity(intent);
            SmtProductionWorkshopCharacterActivity.this.finish();
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
