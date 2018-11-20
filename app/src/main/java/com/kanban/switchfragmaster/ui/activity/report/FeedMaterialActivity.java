package com.kanban.switchfragmaster.ui.activity.report;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;


import com.kanban.switchfragmaster.data.FeedersMaterielInfo;
import com.kanban.switchfragmaster.data.MyApplication;
import com.kanban.switchfragmaster.http.service.GetData;
import com.kanban.switchfragmaster.http.service.MyTask;
import com.kanban.switchfragmaster.view.PieChart3D01View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FeedMaterialActivity extends Activity {

    private List<FeedersMaterielInfo> feederList;

    private boolean IsGetData = true; //调用后台接口获取数据(当显示最后一页数据时，IsGetData = true)
    private int nFeederTotalCount = 0;//供料器分页显示时总页数
    private int nPageRow = 13;//供料器分页显示时每页显示行数
    private int nCurrentPage = 0;//供料器分页显示时当前显示第几页
    private ArrayList<FeedersMaterielInfo> pageFeederList;
    private ArrayList<FeedersMaterielInfo> curFeederList;
    private int nStart = 0; //当前页第一行
    private int nEnd = 0; //当前页最后一行
    private String sLineNo = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(android.view.Window.FEATURE_NO_TITLE); //设置没标题
        initActivity();
        this.setTitle("抛料率报表");
    }
    private void initActivity() {
        //完全动态创建,无须XML文件.
        FrameLayout content = new FrameLayout(this);

        //缩放控件放置在FrameLayout的上层，用于放大缩小图表
        FrameLayout.LayoutParams frameParm = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        frameParm.gravity = Gravity.BOTTOM|Gravity.RIGHT;

		   /*
		  //缩放控件放置在FrameLayout的上层，用于放大缩小图表
	       mZoomControls = new ZoomControls(this);
	       mZoomControls.setIsZoomInEnabled(true);
	       mZoomControls.setIsZoomOutEnabled(true);
		   mZoomControls.setLayoutParams(frameParm);
		   */

        //图表显示范围在占屏幕大小的90%的区域内
        android.util.DisplayMetrics dm = getResources().getDisplayMetrics();
        int scrWidth = (int) (dm.widthPixels * 0.9);
        int scrHeight = (int) (dm.heightPixels * 0.8);
        android.widget.RelativeLayout.LayoutParams layoutParams = new android.widget.RelativeLayout.LayoutParams(
                scrWidth, scrHeight);

        //居中显示
        layoutParams.addRule(android.widget.RelativeLayout.CENTER_IN_PARENT);
        //图表view放入布局中，也可直接将图表view放入Activity对应的xml文件中
        final android.widget.RelativeLayout chartLayout = new android.widget.RelativeLayout(this);

        chartLayout.addView(new PieChart3D01View(this), layoutParams);

        //增加控件
        ((ViewGroup) content).addView(chartLayout);
        //((ViewGroup) content).addView(mZoomControls);
        setContentView(content);
        //放大监听
        //  mZoomControls.setOnZoomInClickListener(new OnZoomInClickListenerImpl());
        //缩小监听
        //  mZoomControls.setOnZoomOutClickListener(new OnZoomOutClickListenerImpl());
    }
    private void initData(){
        Intent intent = getIntent();
        if(intent!=null){
            sLineNo = intent.getStringExtra("sLineNo");
            getSmtConnectionLotFeederSerial(sLineNo);
        }
    }

    //获取-供料器位置抛料率信息
    private void getSmtConnectionLotFeederSerial(String sLineNo) {

        //获取-供料器位置抛料率信息-数据处理
        new MyTask().setTaskListener(new MyTask.TaskListener() {
            @Override
            public void start() {

            }

            @Override
            public String doInBackground(String[] params) {
                JSONObject jsonObj = new JSONObject();
                String sLineNo = params[0];
                try {
                    jsonObj.put("sMesUser", MyApplication.sMesUser);
                    jsonObj.put("sFactoryNo", MyApplication.sFactoryNo);
                    jsonObj.put("sLanguage", MyApplication.sLanguage);
                    jsonObj.put("sUserNo", MyApplication.sLoginUserNo);
                    jsonObj.put("sClientIp", MyApplication.sClientIp);
                    jsonObj.put("sLineNo", sLineNo);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
//          sData = "[{\"status\":\"OK\",\"msg\":\"获取SMT生产线看板数据成功!\",\"sDateNow\":\"2017/09/05 17:46\",\"data\":[{\"sWorkStatus\":\"WORKING\",\"sWorkStatusName\":\"正常生产\",\"sLineNo\":\"SMT2-1\",\"sCustomerName\":\"\",\"sWo\":\"//2017061501-1\",\"sWoQtyOK\":\"0\",\"sWoQtyPlan\":\"1000\",\"sFinishingRate\":\"0\",\"sWo_updtime\":\"2017/09/05 17:40:55\",\"sAOIRate\":\"0\",\"sWoQtyOutput\":\"0\"},{\"sWorkStatus\":\"WORKING\",\"sWorkStatusName\":\"正常生产\",\"sLineNo\":\"SMT2-2\",\"sCustomerName\":\"\",\"sWo\":\"\",\"sWoQtyOK\":\"0\",\"sWoQtyPlan\":\"0\",\"sFinishingRate\":\"0\",\"sWo_updtime\":\"2017/7/1 16:45:53\",\"sAOIRate\":\"0\",\"sWoQtyOutput\":\"0\"},{\"sWorkStatus\":\"WORKING\",\"sWorkStatusName\":\"正常生产\",\"sLineNo\":\"SMT2-3-1\",\"sCustomerName\":\"\",\"sWo\":\"\",\"sWoQtyOK\":\"0\",\"sWoQtyPlan\":\"0\",\"sFinishingRate\":\"0\",\"sWo_updtime\":\"2016/11/14 10:54:47\",\"sAOIRate\":\"0\",\"sWoQtyOutput\":\"0\"},{\"sWorkStatus\":\"WORKING\",\"sWorkStatusName\":\"正常生产\",\"sLineNo\":\"SMT2-3-2\",\"sCustomerName\":\"\",\"sWo\":\"\",\"sWoQtyOK\":\"0\",\"sWoQtyPlan\":\"0\",\"sFinishingRate\":\"0\",\"sWo_updtime\":\"2016/11/14 10:54:47\",\"sAOIRate\":\"0\",\"sWoQtyOutput\":\"0\"},{\"sWorkStatus\":\"WORKING\",\"sWorkStatusName\":\"正常生产\",\"sLineNo\":\"SMT2-4\",\"sCustomerName\":\"华星光电\",\"sWo\":\"HX1874-1\",\"sWoQtyOK\":\"9997\",\"sWoQtyPlan\":\"10000\",\"sFinishingRate\":\"0\",\"sWo_updtime\":\"2017/9/2 15:00:37\",\"sAOIRate\":\"100.00\",\"sWoQtyOutput\":\"9997\"},{\"sWorkStatus\":\"WORKING\",\"sWorkStatusName\":\"正常生产\",\"sLineNo\":\"SMT2-6\",\"sCustomerName\":\"\",\"sWo\":\"HX160826-1\",\"sWoQtyOK\":\"20000\",\"sWoQtyPlan\":\"10000\",\"sFinishingRate\":\"200.00\",\"sWo_updtime\":\"2017/8/4 9:54:14\",\"sAOIRate\":\"100.00\",\"sWoQtyOutput\":\"20000\"},{\"sWorkStatus\":\"WORKING\",\"sWorkStatusName\":\"正常生产\",\"sLineNo\":\"SMT2-7\",\"sCustomerName\":\"华星光电\",\"sWo\":\"HX1148-1\",\"sWoQtyOK\":\"0\",\"sWoQtyPlan\":\"25000\",\"sFinishingRate\":\"0\",\"sWo_updtime\":\"2016/12/9 10:37:27\",\"sAOIRate\":\"0\",\"sWoQtyOutput\":\"0\"},{\"sWorkStatus\":\"WORKING\",\"sWorkStatusName\":\"正常生产\",\"sLineNo\":\"SMT2-8\",\"sCustomerName\":\"华星光电\",\"sWo\":\"HX1902B-2\",\"sWoQtyOK\":\"0\",\"sWoQtyPlan\":\"5400\",\"sFinishingRate\":\"0\",\"sWo_updtime\":\"2017/4/18 17:14:02\",\"sAOIRate\":\"0\",\"sWoQtyOutput\":\"0\"},{\"sWorkStatus\":\"WORKING\",\"sWorkStatusName\":\"正常生产\",\"sLineNo\":\"SMT2-9\",\"sCustomerName\":\"\",\"sWo\":\"20170511-1\",\"sWoQtyOK\":\"0\",\"sWoQtyPlan\":\"2000\",\"sFinishingRate\":\"0\",\"sWo_updtime\":\"2017/5/23 19:53:13\",\"sAOIRate\":\"0\",\"sWoQtyOutput\":\"0\"},{\"sWorkStatus\":\"WORKING\",\"sWorkStatusName\":\"正常生产\",\"sLineNo\":\"SMT2-10\",\"sCustomerName\":\"\",\"sWo\":\"20170315-1\",\"sWoQtyOK\":\"14\",\"sWoQtyPlan\":\"2000\",\"sFinishingRate\":\"0.80\",\"sWo_updtime\":\"2017/3/16 10:53:42\",\"sAOIRate\":\"87.50\",\"sWoQtyOutput\":\"16\"}]}]";
                return GetData.getDataByJson(jsonObj.toString(), "GetSmtConnectionLotFeederSerial"); //接口返回数据
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
                    feederList = new ArrayList<>();
                    if (sStatus.equalsIgnoreCase("OK")) {
                        String sData = item.getString("data");
                        JSONArray jsonArrayData = new JSONArray(sData);
                        for (int i = 0; i < jsonArrayData.length(); i++) {
                            JSONObject itemData = jsonArrayData.getJSONObject(i);
                            FeedersMaterielInfo info = new FeedersMaterielInfo();
                            info.setFeeders_equipmentCoding(itemData.getString("sMachineNo")); //设备编号
                            info.setFeeders_lineNum(itemData.getString("sROWID")); //行号
                            info.setFeeders_location(itemData.getString("sFid")); //位置
                            info.setFeeders_absorbNum(itemData.getString("sPickupQty")); //吸取数
                            info.setFeeders_rejectNum(itemData.getString("sMissQty")); //抛料数量
                            info.setFeeders_rejectRate(itemData.getString("sMissRate")); //抛料率
                            feederList.add(info);
                        }
                      /*  if (IsGetData) {//第一次初始化数据，获取完数据之后开始分页显示，显示完之后再获取数据
                            curFeederList = feederList;
                            IsGetData = false; //初始化变量
                            nFeederTotalCount = 0;  //每页序号-初始化变量
                            nCurrentPage = 0; //相当于当前页号-初始化变量
                            splitePageShow();//供料器分页显示
                        } else {
                            splitePageShow();//供料器分页显示
                        }*/
                    } else {
//                        IsGetData = true; //调用后台接口获取数据(获取数据异常时)
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
//                    IsGetData = true; //调用后台接口获取数据(获取数据异常时)
                }
            }
        }).execute(sLineNo);
    }
    //供料器分页显示
    private void splitePageShow() {
        if (curFeederList.size() >= 1) {
            nFeederTotalCount = curFeederList.size() % nPageRow == 0 ? curFeederList.size() / nPageRow : (curFeederList.size() / nPageRow) + 1;//取总页数
            if (nCurrentPage + 1 >= nFeederTotalCount) //显示最后一页数据
            {
                nStart = nCurrentPage * nPageRow; //当前页第一行 = 当前页号 * 页显示数
                nEnd = curFeederList.size() - 1; //当前页最后一行
                IsGetData = true;
            } else { //显示不为最后一页数据
                nStart = nCurrentPage * nPageRow; //当前页第一行 = 当前页号 * 页显示数
                nEnd = (nCurrentPage + 1) * nPageRow - 1; //当前页最后一行
            }
            pageFeederList = new ArrayList<>();
            for (int i = 0; i < curFeederList.size(); i++) {
                if (i >= nStart && i <= nEnd) {
                    FeedersMaterielInfo info = new FeedersMaterielInfo();
                    info.setFeeders_equipmentCoding(curFeederList.get(i).getFeeders_equipmentCoding()); //设备编号
                    info.setFeeders_lineNum(curFeederList.get(i).getFeeders_lineNum()); //行号
                    info.setFeeders_location(curFeederList.get(i).getFeeders_location()); //位置
                    info.setFeeders_absorbNum(curFeederList.get(i).getFeeders_absorbNum()); //吸取数
                    info.setFeeders_rejectNum(curFeederList.get(i).getFeeders_rejectNum()); //抛料数量
                    info.setFeeders_rejectRate(curFeederList.get(i).getFeeders_rejectRate()); //抛料率
                    pageFeederList.add(info);
                }
            }
        }
    }
}
