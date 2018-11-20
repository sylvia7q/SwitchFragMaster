package com.kanban.switchfragmaster.ui.activity.myself.func;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.kanban.switchfragmaster.R;
import com.kanban.switchfragmaster.http.service.GetData;
import com.kanban.switchfragmaster.utils.SharedPreferencesUtils;
import com.kanban.switchfragmaster.utils.TextViewUtils;
import com.kanban.switchfragmaster.view.TitleBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StationSettingActivity extends Activity {

    @BindView(R.id.activity_station_title_bar)
    TitleBarView titlebarview;
    @BindView(R.id.activity_station_lv_station)
    ListView lv_station;
    @BindView(R.id.activity_station_btn_save)
    Button btnSave;
    private Context context;
    List<String> listStationNo = null;
    List<String> listStationName=null;
    List<String> listStationNameEn=null;
    List<String> listStationNameChs=null;
    ProgressDialog proDialog;
    String sLineNo="";  // 线别

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_setting);
        ButterKnife.bind(this);
        context = StationSettingActivity.this;
        initTitleBar();

    }

    private void initTitleBar() {
        titlebarview.setIvBackImage(View.VISIBLE);
        titlebarview.setBackOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titlebarview.setLeftText("工站设置");
    }

    @Override
    protected void onStart() {
        super.onStart();

        GetStationInfo();
    }
    private void setListener(){
        lv_station.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    /*获取工站*/
    private void GetStationInfo() {
        sLineNo = SharedPreferencesUtils.getValue(getBaseContext(), "LineNo", "");
        //判断线别是否有选择
        if (TextUtils.isEmpty(sLineNo.trim())) {
            Intent intent = new Intent();
            intent.setClass(context, LineSettingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return;
        }
        proDialog = ProgressDialog.show(context, getString(R.string.get_station), getString(R.string.loading_data));
        GetLineStationInfoAsyncTask getStationInfoAsyncTask = new GetLineStationInfoAsyncTask();
        getStationInfoAsyncTask.execute(sLineNo);
    }

    @OnClick(R.id.activity_station_btn_save)
    public void onViewClicked() {
        long[] checkedItemIds = lv_station.getCheckItemIds();
        String sStationNoIn = "";
        String sStationNameIn = "";
        String sStationNameEnIn = "";
        String sStationNameChsIn = "";
        for (int i = 0; i < checkedItemIds.length; i++) {
            if (i == 0) {
                sStationNoIn = listStationNo.get((int) checkedItemIds[i]).toString();
                sStationNameIn = listStationName.get((int) checkedItemIds[i]).toString();
                sStationNameEnIn = listStationNameEn.get((int) checkedItemIds[i]).toString();
                sStationNameChsIn = listStationNameChs.get((int) checkedItemIds[i]).toString();
            } else {
                sStationNoIn = sStationNoIn + "," + listStationNo.get((int) checkedItemIds[i]).toString();
                sStationNameIn = sStationNameIn + "," + listStationName.get((int) checkedItemIds[i]).toString();
                sStationNameEnIn = sStationNameEnIn + "," + listStationNameEn.get((int) checkedItemIds[i]).toString();
                sStationNameChsIn = sStationNameChsIn + "," + listStationNameChs.get((int) checkedItemIds[i]).toString();
            }
        }

        Toast.makeText(context, "[" + sStationNameIn + "]" + getString(R.string.alert_save_ok), Toast.LENGTH_SHORT).show();
        SharedPreferencesUtils.saveValue(getBaseContext(),"StationNoIn", sStationNoIn);
        SharedPreferencesUtils.saveValue(getBaseContext(),"StationNameIn", sStationNameIn);
        SharedPreferencesUtils.saveValue(getBaseContext(),"StationNameEnIn", sStationNameEnIn);
        SharedPreferencesUtils.saveValue(getBaseContext(),"StationNameChsIn", sStationNameChsIn);
        finish();
    }

    /*获取工站数据*/
    public class GetLineStationInfoAsyncTask extends AsyncTask<String, Void, String> {
        String sLineNo = "";

        @Override
        protected void onPreExecute() {
            TextViewUtils.setClickableShow(btnSave,false);
        }

        @Override
        protected String doInBackground(String... params) {
            sLineNo = params[0];
            JSONObject jsonObj = new JSONObject();
            try {
                jsonObj.put("sLineNo", sLineNo);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            return GetData.getDataByJson(jsonObj.toString(), "GetBasStationInfoByLine");
        }

        @Override
        protected void onPostExecute(String json) {
            proDialog.dismiss();
            if (null == json || json.equals("")) {
                Toast.makeText(context, "获取工站信息时，后台接口返回数据为空或null!", Toast.LENGTH_LONG).show();
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
                    listStationNo = new ArrayList<String>();
                    listStationName = new ArrayList<String>();
                    listStationNameEn = new ArrayList<String>();
                    listStationNameChs = new ArrayList<String>();
                    for (int i = 0; i < jsonArrayData.length(); i++) {
                        JSONObject itemData = jsonArrayData.getJSONObject(i);
                        String sStationNo = itemData.getString("STATION_NO");
                        String sStationNameChs = itemData.getString("NAME_CHS");
                        String sSattionNameEn = itemData.getString("NAME_EN");
                        String sStationName;
                        listStationNo.add(sStationNo);
                        listStationNameChs.add(sStationNameChs);
                        listStationNameEn.add(sSattionNameEn);
                        sStationName = sSattionNameEn + "[" + sStationNo + "]";
//                        switch (MyApplication.sLanguage) {
//                            case "E":
//                                sStationName = sSattionNameEn + "[" + sStationNo + "]";
//                                break;
//                            case "S":
//                                sStationName = sStationNameChs + "[" + sStationNo + "]";
//                                break;
//                            default:
//                                sStationName = sStationNameChs + "[" + sStationNo + "]";
//                                break;
//                        }
                        listStationName.add(sStationName);
                    }
                    TextViewUtils.setClickableShow(btnSave,true);
                } else {
                    Toast.makeText(context, sMsg, Toast.LENGTH_LONG).show();
                    return;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            ListAdapter adapter = null;
            adapter = new ArrayAdapter<String>(context, R.layout.simple_list_item_multiple_choice, listStationName);
            lv_station.setAdapter(adapter);
            lv_station.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

            String sStationNoIn = SharedPreferencesUtils.getValue(getBaseContext(),"StationNoIn", "");
            String sStationNoInTemp[] = sStationNoIn.split(",");
            for (int i = 0; i < lv_station.getCount(); i++) {
                String sStationNoTemp = listStationNo.get(i).toString();
                final int nCheck = i;
                for (int j = 0; j < sStationNoInTemp.length; j++) {
                    //获取的站位代码，与保存的数组比对。若OK，则站位显示勾先把状态 //equalsIgnoreCase() StringString不区分大小写
                    if (sStationNoTemp.equalsIgnoreCase(sStationNoInTemp[j].toString())) {
                        lv_station.setItemChecked(nCheck, true);
                    }
                }
            }

        }
    }

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
