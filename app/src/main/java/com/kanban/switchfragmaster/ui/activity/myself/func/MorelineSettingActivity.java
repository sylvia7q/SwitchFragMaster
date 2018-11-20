package com.kanban.switchfragmaster.ui.activity.myself.func;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.kanban.switchfragmaster.R;
import com.kanban.switchfragmaster.data.MyApplication;
import com.kanban.switchfragmaster.http.service.GetData;
import com.kanban.switchfragmaster.utils.DialogUtil;
import com.kanban.switchfragmaster.utils.SharedPreferencesUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MorelineSettingActivity extends Activity {

    @BindView(R.id.avctivity_more_line_lv_more_line)
    ListView lv_more_line;

    List<String> listLineNo = null;
    List<String> listLineName = null;
    List<String> listLineNameEn = null;
    List<String> listLineNameChs = null;
    ProgressDialog proDialog;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moreline_setting);
        ButterKnife.bind(this);
        context = MorelineSettingActivity.this;

    }


    @Override
    protected void onStart() {
        super.onStart();
        proDialog = ProgressDialog.show(MorelineSettingActivity.this, getString(R.string.get_line), getString(R.string.loading_data));
        GetMoreLineInfo();
    }

    /*获取线别*/
    private void GetMoreLineInfo() {
        GetMoreLineInfoAsyncTask getMoreLineInfoAsyncTask = new GetMoreLineInfoAsyncTask();
        getMoreLineInfoAsyncTask.execute();
    }

    /*获取线别数据*/
    public class GetMoreLineInfoAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {

            return GetData.getDataByJson(null, "GetBasLineInfo");
        }

        @Override
        protected void onPostExecute(String json) {
            proDialog.dismiss();
            if (null == json || json.equals("")) {
                return;
            }
            try {
                JSONArray jsonArray = new JSONArray(json);
                JSONObject item = jsonArray.getJSONObject(0);
                String sStatus = item.getString("status");
                String sMsg = item.getString("msg");
                listLineNo = new ArrayList<String>();
                listLineName = new ArrayList<String>();
                listLineNameEn = new ArrayList<String>();
                listLineNameChs = new ArrayList<String>();
                if (sStatus.equalsIgnoreCase("OK")) {
                    String sData = item.getString("data");
                    JSONArray jsonArrayData = new JSONArray(sData);
                    for (int i = 0; i < jsonArrayData.length(); i++) {
                        JSONObject itemData = jsonArrayData.getJSONObject(i);
                        String sLineNo = itemData.getString("LINE_NO");
                        String sLineNameChs = itemData.getString("NAME_CHS");
                        String sLineNameEn = itemData.getString("NAME_EN");
                        String sLineName;
                        listLineNo.add(sLineNo);
                        listLineNameChs.add(sLineNameChs);
                        listLineNameEn.add(sLineNameEn);
                        switch (MyApplication.sLanguage) {
                            case "E":
                                sLineName = sLineNameEn + "[" + sLineNo + "]";
                                break;
                            case "S":
                                sLineName = sLineNameChs + "[" + sLineNo + "]";
                                break;
                            default:
                                sLineName = sLineNameChs + "[" + sLineNo + "]";
                                break;
                        }
                        listLineName.add(sLineName);
                    }
                } else {
                    DialogUtil.showStatusDialog(context, sStatus, sMsg);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ListAdapter adapter = null;
            adapter = new ArrayAdapter<String>(MorelineSettingActivity.this, android.R.layout.simple_list_item_multiple_choice, listLineName);
            lv_more_line.setAdapter(adapter);
            lv_more_line.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

            String sMoreLineNo = SharedPreferencesUtils.getValue(getBaseContext(), "MoreLineNoIn", "");
            String sMoreLineNoInTemp[] = sMoreLineNo.split(",");
            for (int i = 0; i < lv_more_line.getCount(); i++) {
                String sStationNoTemp = listLineNo.get(i).toString();
                final int nCheck = i;
                for (int j = 0; j < sMoreLineNoInTemp.length; j++) {
                    //获取的线别代码，与保存的数组比对。若OK，则站位显示勾先把状态 //equalsIgnoreCase() StringString不区分大小写
                    if (sStationNoTemp.equalsIgnoreCase(sMoreLineNoInTemp[j].toString())) {
                        lv_more_line.setItemChecked(nCheck, true);
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
