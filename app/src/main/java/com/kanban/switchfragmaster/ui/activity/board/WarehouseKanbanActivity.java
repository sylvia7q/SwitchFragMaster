package com.kanban.switchfragmaster.ui.activity.board;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.kanban.switchfragmaster.R;
import com.kanban.switchfragmaster.data.MyApplication;
import com.kanban.switchfragmaster.data.WoInfoWhEntity;
import com.kanban.switchfragmaster.http.service.GetData;
import com.kanban.switchfragmaster.utils.SharedPreferencesUtils;
import com.kanban.switchfragmaster.utils.ToastUtil;
import com.kanban.switchfragmaster.utils.WarehouseKanbanTimeTaskScroll;
import com.kanban.switchfragmaster.view.TitleBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WarehouseKanbanActivity extends Activity {

    @BindView(R.id.activity_warehouse_kanban_title_bar)
    TitleBarView titlebarview;
    @BindView(R.id.activity_warehouse_kanban_tv_on_production_title)
    TextView tvOnProductionTitle;
    @BindView(R.id.activity_warehouse_kanban_tv_on_production)
    TextView tv_on_production;
    @BindView(R.id.activity_warehouse_kanban_tv_plan_stop_production_title)
    TextView tvPlanStopProductionTitle;
    @BindView(R.id.activity_warehouse_kanban_tv_plan_stop_production)
    TextView tv_plan_stop_production;
    @BindView(R.id.activity_warehouse_kanban_lv_data)
    ListView lvData;
    @BindView(R.id.activity_warehouse_kanban_tv_date)
    TextView tv_date; //当前时间
    @BindView(R.id.activity_warehouse_kanbancbx_pause_auto_scroll)
    CheckBox cbx_pause_auto_scroll;

    String sLineNo = "";
    String sInterfaceType = "";

    private Timer timer = new Timer(true);
    boolean isStart = false;
    boolean isPause = false;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse_kanban);
        ButterKnife.bind(this);
        context = WarehouseKanbanActivity.this;
        initTitleBar();
        initView();
        //启动定时器
        /**
         * 第一个参数是是 TimerTask 类对象
         * 第二个参数"0"的意思是:(0就表示无延迟)--用户调用 schedule() 方法后，要等待这么长的时间才可以第一次执行 run() 方法。
         * 第三个参数"60*60*1000"的意思就是:(单位是毫秒60*60*1000为一小时)(单位是毫秒3*60*1000为三分钟)第一次调用之后，从第二次开始每隔多长的时间调用一次 run() 方法。
         */
        timer.schedule(task, 0, 120 * 1000);
    }

    private void initTitleBar() {
        titlebarview.setIvBackImage(View.VISIBLE);
        titlebarview.setLeftText(getString(R.string.warehouse_monitoring));
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
        getWarehouseKanbanBoard();/*获取仓库看板数据*/
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            sLineNo = intent.getStringExtra("sLineNo");
            sInterfaceType = intent.getStringExtra("sInterfaceType");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null || task != null) {
            timer.cancel();
            task.cancel();
        }
    }

    //任务
    private TimerTask task = new TimerTask() {
        public void run() {
            getWarehouseKanbanBoard();
        }
    };

    public void initView() {
        cbx_pause_auto_scroll.setOnClickListener(pauseAutoScrollOnClickListener);
    }
    private boolean checkLine() {
        sLineNo = SharedPreferencesUtils.getValue(context, "LineNo", MyApplication.sLineNo);//获取线别代码
        if (TextUtils.isEmpty(sLineNo)) {
            ToastUtil.showShortToast(context, getString(R.string.set_line_please));
            return false;
        }
        return true;
    }
    /*获取仓库看板数据*/
    private void getWarehouseKanbanBoard() {
        if (!isStart) {
            return;
        }
        isStart = false;
        if (checkLine()) {
            GetSmtLineBoardAsyncTask getSmtLineBoardAsyncTask = new GetSmtLineBoardAsyncTask();
            getSmtLineBoardAsyncTask.execute(sInterfaceType, sLineNo);
        }
    }

    /*获取线看板数据*/
    public class GetSmtLineBoardAsyncTask extends AsyncTask<String, Void, String> {
        String sInterfaceType = "";
        String sLineNo = "";

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            sInterfaceType = params[0];
            sLineNo = params[1];
            JSONObject jsonObj = new JSONObject();
            try {
                jsonObj.put("sMesUser", MyApplication.sMesUser);
                jsonObj.putOpt("sFactoryNo", MyApplication.sFactoryNo);
                jsonObj.put("sLanguage", MyApplication.sLanguage);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            return GetData.getDataByJson(jsonObj.toString(), "GetWarehouseStockSetBoard");
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
                tv_date.setText(sDateNow);

                List<WoInfoWhEntity> listWoInfo = new ArrayList<WoInfoWhEntity>();
                if (sStatus.equalsIgnoreCase("OK")) {

                    String sData = item.getString("data");
                    JSONArray jsonArrayData = new JSONArray(sData);
                    int nOnLine = 0;
                    int nOffLine = 0;
                    for (int i = 0; i < jsonArrayData.length(); i++) {
                        JSONObject itemData = jsonArrayData.getJSONObject(i);
                        WoInfoWhEntity mBasWoInfo = new WoInfoWhEntity();
                        mBasWoInfo.setLineNo(itemData.getString("LINE_NO"));
                        String sLineStatus = itemData.getString("WORK_STATUS");
                        if (sLineStatus.equalsIgnoreCase("WORKING")) {
                            nOnLine++;
                        } else {
                            nOffLine++;
                        }
                        mBasWoInfo.setLineStatusName(itemData.getString("WORK_STATUS_NAME"));
                        mBasWoInfo.setWo(itemData.getString("WO"));
                        mBasWoInfo.setWoPlanQty(itemData.getString("QTY_PLAN"));
                        mBasWoInfo.setIsPrepareSufficient(itemData.getString("IS_PREPARE_SUFFICIENT"));
                        mBasWoInfo.setQtyTotal(itemData.getString("QTY_TOTAL"));
                        listWoInfo.add(mBasWoInfo);
                    }
                    tv_on_production.setText(getString(R.string.total) + " " + String.valueOf(nOnLine) + " " + getString(R.string.line_total));
                    tv_plan_stop_production.setText(getString(R.string.total) + " " + String.valueOf(nOffLine) + " " + getString(R.string.line_total));

                    new Timer().schedule(new WarehouseKanbanTimeTaskScroll(WarehouseKanbanActivity.this, lvData, listWoInfo), 20, 30);

                    //lv_list.setAdapter(new WarehouseKanbanListAdapter(WarehouseKanbanActivity.this,listWoInfo));

                } else {
                    Toast.makeText(WarehouseKanbanActivity.this, sMsg, Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(WarehouseKanbanActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }


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
