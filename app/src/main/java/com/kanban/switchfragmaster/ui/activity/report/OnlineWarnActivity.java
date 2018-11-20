package com.kanban.switchfragmaster.ui.activity.report;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;


import com.kanban.switchfragmaster.R;
import com.kanban.switchfragmaster.adapter.OnlineWarnAdapter;
import com.kanban.switchfragmaster.data.MyApplication;
import com.kanban.switchfragmaster.data.OnlineWarnInfo;
import com.kanban.switchfragmaster.http.service.GetData;
import com.kanban.switchfragmaster.http.service.MyTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OnlineWarnActivity extends Activity {

    @BindView(R.id.online_warn_list_view)
    ListView onlineWarnListView;
    private Context context;
    private List<OnlineWarnInfo> onlineWarnInfoLsit;
    private OnlineWarnAdapter mOnlineWarnAdapter;
    private String sWo = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_warn);
        ButterKnife.bind(this);
        context = OnlineWarnActivity.this;
        initData();
    }
    private void initData(){
        Intent intent = getIntent();
        if(intent!=null){
            sWo = intent.getStringExtra("sWo");
            getSmtFeedlistFidItem(sWo);
        }
    }

    //获取-在线预警信息查询
    private void getSmtFeedlistFidItem(String sWo) {

        //获取-预警信息查询-数据处理
        new MyTask().setTaskListener(new MyTask.TaskListener() {
            @Override
            public void start() {

            }

            @Override
            public String doInBackground(String[] params) {
                JSONObject jsonObj = new JSONObject();
                String sWo = params[0];
                try {
                    jsonObj.put("sMesUser", MyApplication.sMesUser);
                    jsonObj.put("sFactoryNo", MyApplication.sFactoryNo);
                    jsonObj.put("sLanguage", MyApplication.sLanguage);
                    jsonObj.put("sUserNo", MyApplication.sLoginUserNo);
                    jsonObj.put("sClientIp", MyApplication.sClientIp);
                    jsonObj.put("sWo", sWo);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                return GetData.getDataByJson(jsonObj.toString(), "GetSmtFeedlistFidItem"); //接口返回数据
//            String ss = "[{\"status\":\"OK\",\"msg\":\"在线预警数据获取成功!\",\"sDateNow\":\"2017.10.20 15:23\",\"data\":[{\"sMachineSeq\":\"1\",\"sFid\":\"212L\",\"sReelNo\":\"R17101400034\",\"sFeedPartNo\":\"G.62.01.BOX104KAXX\",\"sConsumeQty\":\"29\",\"sFeedQty\":\"250\",\"sCurrentQty\":\"250\",\"sQtyNeed\":\"145\",\"sTime\":\"103\",\"sFeedPartDesc\":\"0402/16V/100nF/±10％/X5R\"}]}]";
//            return ss;
            }

            @Override
            public void result(String json) {
                if (null == json || json.equals("")) {
                    return;
                }
                try {
                    JSONArray jsonArray = new JSONArray(json);
                    JSONObject item = jsonArray.getJSONObject(0);
                    String sStatus = item.getString("status");
                    String sMsg = item.getString("msg");
                    if (sStatus.equalsIgnoreCase("OK")) {
                        String sData = item.getString("data");
                        JSONArray jsonArrayData = new JSONArray(sData);
                        int jsonArrda = jsonArrayData.length();
                        onlineWarnInfoLsit = new ArrayList<>();
                        for (int i = 0; i < jsonArrda; i++) {
                            JSONObject itemData = jsonArrayData.getJSONObject(i);
                            OnlineWarnInfo onlineWarnInfo = new OnlineWarnInfo();
                            onlineWarnInfo.setsMachine(itemData.getString("sMachineSeq")); //设备
                            onlineWarnInfo.setsFid(itemData.getString("sFid")); //站位
                            onlineWarnInfo.setsReelNo(itemData.getString("sReelNo")); //料卷号
                            onlineWarnInfo.setsPartNo(itemData.getString("sFeedPartNo")); //物料号
                            onlineWarnInfo.setsPartDesc(itemData.getString("sFeedPartDesc")); //物料描述
                            onlineWarnInfo.setsConsumeQty(itemData.getString("sConsumeQty")); //用量
                            onlineWarnInfo.setsFeedQty(itemData.getString("sFeedQty")); //上料数量
                            onlineWarnInfo.setsCurrentQty(itemData.getString("sCurrentQty")); //在线数量
                            onlineWarnInfo.setsQtyNeed(itemData.getString("sQtyNeed")); //需求数量
                            onlineWarnInfo.setsTime(itemData.getString("sTime")); // 可用时间（剩余时间）
                            onlineWarnInfo.setsPartDesc(itemData.getString("sFeedPartDesc")); // 物料描述
                            onlineWarnInfoLsit.add(onlineWarnInfo);
                        }
                    }
                    if (onlineWarnInfoLsit.size() >= 1) {
                        mOnlineWarnAdapter = new OnlineWarnAdapter(context, onlineWarnInfoLsit);
                        onlineWarnListView.setAdapter(mOnlineWarnAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).execute(sWo);
    }


}
