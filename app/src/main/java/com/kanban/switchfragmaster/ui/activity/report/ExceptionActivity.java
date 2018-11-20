package com.kanban.switchfragmaster.ui.activity.report;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;


import com.kanban.switchfragmaster.R;
import com.kanban.switchfragmaster.adapter.ExceptionShowAdapter;
import com.kanban.switchfragmaster.data.ExceptionShowInfo;
import com.kanban.switchfragmaster.data.MyApplication;
import com.kanban.switchfragmaster.http.service.GetData;
import com.kanban.switchfragmaster.http.service.MyTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExceptionActivity extends Activity {
    @BindView(R.id.lv_exception)
    ListView lvException;
    private Context context;
    private ExceptionShowAdapter exceptionShowAdapter;//看板主界面异常信息适配器
    private List<ExceptionShowInfo> exceptionList;//看板主界面异常信息列表
    private String sLineNo = "";
    private String sWo = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exception);
        ButterKnife.bind(this);
        context = ExceptionActivity.this;
        initData();
    }
    private void initData(){
        Intent intent = getIntent();
        if(intent!=null){
            sLineNo = intent.getStringExtra("sLineNo");
            sWo = intent.getStringExtra("sWo");
            initException(sLineNo,sWo);
        }
    }

    //获取-异常信息查询
    private void initException(String sLineNo, String sWo) {
        //获取-异常信息查询-数据处理
        new MyTask().setTaskListener(new MyTask.TaskListener() {
            @Override
            public void start() {

            }

            @Override
            public String doInBackground(String[] params) {
                JSONObject jsonObj = new JSONObject();
                String sLineNo = params[0];
                String sWo = params[1];
                try {
                    jsonObj.put("sMesUser", MyApplication.sMesUser);
                    jsonObj.put("sFactoryNo", MyApplication.sFactoryNo);
                    jsonObj.put("sLanguage", MyApplication.sLanguage);
                    jsonObj.put("sUserNo", MyApplication.sLoginUserNo);
                    jsonObj.put("sClientIp", MyApplication.sClientIp);
                    jsonObj.put("sLineNo", sLineNo);
                    jsonObj.put("sWo", sWo);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                return GetData.getDataByJson(jsonObj.toString(), "GetSysCommStopActions"); //接口返回数据
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
                    exceptionList = new ArrayList<>();
                    if (sStatus.equalsIgnoreCase("OK")) {
                        String sData = item.getString("data");
                        JSONArray jsonArrayData = new JSONArray(sData);
                        for (int i = 0; i < jsonArrayData.length(); i++) {
                            JSONObject itemData = jsonArrayData.getJSONObject(i);
                            ExceptionShowInfo exceptionShowInfo = new ExceptionShowInfo();
                            exceptionShowInfo.setException_info(itemData.getString("sNote")); //异常信息
                            exceptionShowInfo.setException_type(itemData.getString("sStopType")); //异常类型
                            exceptionList.add(exceptionShowInfo);
                        }
                    }
                    if (exceptionList.size() >= 1) {
                        exceptionShowAdapter = new ExceptionShowAdapter(context, exceptionList);
                        lvException.setAdapter(exceptionShowAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).execute(sLineNo, sWo);

    }

}
