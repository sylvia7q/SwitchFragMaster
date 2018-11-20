package com.kanban.switchfragmaster.ui.activity.myself.func;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SearchView;
import android.content.Context;
import android.widget.Button;


import com.kanban.switchfragmaster.R;
import com.kanban.switchfragmaster.data.MyApplication;
import com.kanban.switchfragmaster.http.service.GetData;
import com.kanban.switchfragmaster.utils.DialogUtil;
import com.kanban.switchfragmaster.utils.SharedPreferencesUtils;
import com.kanban.switchfragmaster.utils.ToastUtil;
import com.kanban.switchfragmaster.view.TitleBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LineSettingActivity extends Activity {

    @BindView(R.id.activity_line_title_bar)
    TitleBarView titlebarview;
    @BindView(R.id.activity_line_lv_line)
    ListView lv_line;
    @BindView(R.id.activity_line_current_line_show) TextView currentLineShow;
    @BindView(R.id.activity_line_select_line_show) TextView selectLineShow;
    @BindView(R.id.activity_line_search) SearchView search;
    List<String> listLine = null;
    ProgressDialog proDialog;
    
    private Context context;
    private List<String> curlistItems;
    private String sLineNo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_setting);
        ButterKnife.bind(this);
        context = LineSettingActivity.this;
        initTitleBar();
        initView();
    }

    private void initTitleBar(){
        titlebarview.setIvBackImage(View.VISIBLE);
        titlebarview.setBackOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titlebarview.setLeftText("线别设置");
        titlebarview.setBtnRightOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lineNo = selectLineShow.getText().toString().trim();
                if(!TextUtils.isEmpty(lineNo)){
                    MyApplication.sLineNo = lineNo;
                    currentLineShow.setText(lineNo); //显示-已选中线别
                    SharedPreferencesUtils.saveValue(getBaseContext(),"LineNo",MyApplication.sLineNo);
                    finish();
                }else {
                    ToastUtil.showShortToast(context,getString(R.string.select_line_confirm));
                }
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        GetLineInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String sLineNo = SharedPreferencesUtils.getValue(getBaseContext(), "LineNo", "");
        //显示当前线别
        currentLineShow.setText(sLineNo);
    }

    public void initView() {
        lv_line.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sLineNo = curlistItems.get(position);
                selectLineShow.setText(sLineNo); //显示-已选中线别
            }
        });
    }

    /*获取线别*/
    private  void  GetLineInfo() {
        GetLineInfoAsyncTask getLineInfoAsyncTask = new GetLineInfoAsyncTask();
        getLineInfoAsyncTask.execute();
        search();

    }

    /*获取线别数据*/
    public class GetLineInfoAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            titlebarview.setBtnRightClicable(false);
            proDialog = ProgressDialog.show(context, getString(R.string.get_line), getString(R.string.loading_data));
        }

        @Override
        protected String doInBackground(String... params) {
            return  GetData.getDataByJson(null,"GetBasLineInfo");
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
                listLine = new ArrayList<>();
                if (sStatus.equalsIgnoreCase("OK")) {
                    String sData = item.getString("data");
                    JSONArray jsonArrayData = new JSONArray(sData);
                    for (int i = 0; i < jsonArrayData.length(); i++) {
                        JSONObject itemData = jsonArrayData.getJSONObject(i);
                        String Line = itemData.getString("LINE_NO");
                        listLine.add(Line);
                        ListAdapter adapter = new ArrayAdapter<String>(context, R.layout.simple_list_item_single_choice, listLine);
                        lv_line.setAdapter(adapter);
                        lv_line.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

                        String sLineNo = SharedPreferencesUtils.getValue(getBaseContext(),"LineNo", "");
                        MyApplication.sLineNo=sLineNo;
                        for (int j = 0; j < lv_line.getCount(); j++) {

                            String sLineNoTemp = listLine.get(j).toString();
                            final int nCheck = j;

                            if (sLineNoTemp.equalsIgnoreCase(sLineNo)) {
                                lv_line.setItemChecked(nCheck, true);
                            }
                        }
                    }
                    titlebarview.setBtnRightClicable(true);
                } else {
                    DialogUtil.showStatusDialog(context,sStatus, sMsg);
                }
                curlistItems = listLine;

            } catch (JSONException e) {
                e.printStackTrace();
                DialogUtil.showStatusDialog(context, getString(R.string.error), e.getMessage());
            }
        }
    }

/**
     * 线别筛选
     */
    private void search() {
        search.setIconifiedByDefault(false);
        search.setSubmitButtonEnabled(false);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() > 0) {
                    search.setIconified(true);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if(curlistItems!=null){
                    List<String> listSearch = new ArrayList<>();
                    for(int i=0;i<curlistItems.size();i++){
                        String content = curlistItems.get(i).toString();
                        if(content.toUpperCase().contains(newText.toUpperCase())) {
                            listSearch.add(content);
                        }
                    }
                    if(TextUtils.isEmpty(newText)){
                        curlistItems = listLine;

                    }else{
                        curlistItems = listSearch;
                    }
                     if (curlistItems != null) {
                            ListAdapter  adapter= new ArrayAdapter<>(context, android.R.layout.simple_list_item_single_choice, curlistItems);
                            lv_line.setAdapter(adapter);
                        }

                }

                return true;
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(proDialog!=null){
                proDialog.dismiss();
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
