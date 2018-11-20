package com.kanban.switchfragmaster.ui.activity.board;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.kanban.switchfragmaster.R;
import com.kanban.switchfragmaster.data.MyApplication;
import com.kanban.switchfragmaster.http.service.GetData;
import com.kanban.switchfragmaster.ui.activity.myself.FuncSettingActivity;
import com.kanban.switchfragmaster.utils.SharedPreferencesUtils;
import com.kanban.switchfragmaster.utils.ToastUtil;
import com.kanban.switchfragmaster.view.PieChart01View;
import com.kanban.switchfragmaster.view.TitleBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SmtLineBoardActivity extends Activity {

    @BindView(R.id.activity_smt_line_board_titlebarview)
    TitleBarView titlebarview;
    @BindView(R.id.product_tv_custom_title)
    TextView tvCustomTitle;
    @BindView(R.id.product_tv_custom)
    TextView tvCustom;
    @BindView(R.id.product_tv_product_no_title)
    TextView tvProductNoTitle;
    @BindView(R.id.product_tv_product_no)
    TextView tvProductNo;
    @BindView(R.id.product_tv_wo_title)
    TextView tvWoTitle;
    @BindView(R.id.product_tv_wo)
    TextView tvWo;
    @BindView(R.id.product_tv_wo_num_title)
    TextView tvWoNumTitle;
    @BindView(R.id.product_tv_wo_num)
    TextView tvWoNum;
    @BindView(R.id.product_tv_target_num_title)
    TextView tvTargetNumTitle;
    @BindView(R.id.product_tv_target_num)
    TextView tvTargetNum;
    @BindView(R.id.product_tv_real_num_title)
    TextView tvRealNumTitle;
    @BindView(R.id.product_tv_real_num)
    TextView tvRealNum;
    @BindView(R.id.product_tv_finish_rate_title)
    TextView tvFinishRateTitle;
    @BindView(R.id.product_tv_finish_rate)
    TextView tvFinishRate;
    @BindView(R.id.product_tv_mo_finish_rate_title)
    TextView tvMoFinishRateTitle;
    @BindView(R.id.product_tv_mo_finish_rate)
    TextView tvMoFinishRate;
    @BindView(R.id.product_tv_add_good_rate_title)
    TextView tvAddGoodRateTitle;
    @BindView(R.id.product_tv_add_good_rate)
    TextView tvAddGoodRate;
    @BindView(R.id.product_tv_pass_rate_title_1)
    TextView tvPassRate1Title;
    @BindView(R.id.product_tv_pass_rate_1)
    TextView tvPassRate1;
    @BindView(R.id.product_tv_pass_rate_title_2)
    TextView tvPassRate2Title;
    @BindView(R.id.product_tv_pass_rate_2)
    TextView tvPassRate2;
    @BindView(R.id.product_tv_reject_rate_ppm_title)
    TextView tvRejectRatePpmTitle;
    @BindView(R.id.product_tv_reject_rate_ppm)
    TextView tvRejectRatePpm;
    @BindView(R.id.product_tv_line_status)
    TextView tvLineStatus;
    @BindView(R.id.product_tv_date)
    TextView tvDate;
    @BindView(R.id.product_tv_shift)
    TextView tvShift;
    @BindView(R.id.product_tv_pass_rate_layout)
    LinearLayout llPassRate;
    @BindView(R.id.ll_pie_chart)
    LinearLayout llPieChart;
    @BindView(R.id.product_tv_line_status_title)
    TextView productTvLineStatusTitle;

    private Context context;
    String sLineNo = "";
    String sStationNoIn = "";
    String sStationNameIn = "";

    boolean isStart = false;
    boolean isPause = false;

    String sInterfaceType = "SMT";

    private Timer timer = new Timer(true);
    ProgressDialog proDialog = null;
    boolean isProDialog = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smt_line_board);
        ButterKnife.bind(this);
        context = SmtLineBoardActivity.this;
        initTitleBar();
        initData();
        initView();
        MyApplication.isBack = false;
        //启动定时器
        timer.schedule(task, 1000, 1 * 60 * 1000);
    }

    private void initTitleBar() {
        titlebarview.setIvBackImage(View.VISIBLE);
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
            sLineNo = intent.getStringExtra("sLineNo");
//            sInterfaceType = intent.getStringExtra("sInterfaceType");
        }
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
        sStationNameIn = SharedPreferencesUtils.getValue(getBaseContext(), "StationNameEnIn", "");//获取工站代码
        GetSmtLineBoard();
    }

    //任务
    private TimerTask task = new TimerTask() {
        public void run() {
            GetSmtLineBoard();
        }
    };

    public void initView() {

        /*switch (sInterfaceType) {
            case "SMT":
                titlebarview.setLeftText(getString(R.string.smt_line_monitoring));
                break;
            case "HI":
                titlebarview.setLeftText(R.string.hi_line_monitoring);
                tvRejectRatePpm.setVisibility(View.GONE);
                break;
            default:
                titlebarview.setLeftText(R.string.smt_line_monitoring);
                break;
        }*/

        tvCustomTitle.setTypeface(MyApplication.typeFaceSTZHONGS);
        tvCustom.setTypeface(MyApplication.typeFaceSTZHONGS);
        tvProductNoTitle.setTypeface(MyApplication.typeFaceSTZHONGS);
        tvProductNo.setTypeface(MyApplication.typeFaceSTZHONGS);
        tvWoTitle.setTypeface(MyApplication.typeFaceSTZHONGS);
        tvWo.setTypeface(MyApplication.typeFaceSTZHONGS);
        tvWoNumTitle.setTypeface(MyApplication.typeFaceSTZHONGS);
        tvWoNum.setTypeface(MyApplication.typeFaceSTZHONGS);

        tvTargetNumTitle.setTypeface(MyApplication.typeFaceSTZHONGS);
        tvTargetNum.setTypeface(MyApplication.typeFaceSTZHONGS);
        tvRealNumTitle.setTypeface(MyApplication.typeFaceSTZHONGS);
        tvRealNum.setTypeface(MyApplication.typeFaceSTZHONGS);
        tvFinishRateTitle.setTypeface(MyApplication.typeFaceSTZHONGS);
        tvFinishRate.setTypeface(MyApplication.typeFaceSTZHONGS);
        tvMoFinishRateTitle.setTypeface(MyApplication.typeFaceSTZHONGS);
        tvMoFinishRate.setTypeface(MyApplication.typeFaceSTZHONGS);

        tvAddGoodRateTitle.setTypeface(MyApplication.typeFaceSTZHONGS);
        tvAddGoodRate.setTypeface(MyApplication.typeFaceSTZHONGS);
        tvPassRate1Title.setTypeface(MyApplication.typeFaceSTZHONGS);
        tvPassRate1.setTypeface(MyApplication.typeFaceSTZHONGS);
        tvPassRate2Title.setTypeface(MyApplication.typeFaceSTZHONGS);
        tvPassRate2.setTypeface(MyApplication.typeFaceSTZHONGS);
        tvRejectRatePpmTitle.setTypeface(MyApplication.typeFaceSTZHONGS);
        tvRejectRatePpm.setTypeface(MyApplication.typeFaceSTZHONGS);

    }


    public void proDialogView() {
        proDialog = ProgressDialog.show(context, getString(R.string.loading), getString(R.string.loading_data));
    }

    //清空数据
    public void clearText() {
        setContentText(getString(R.string.nothing));
    }

    //线头数据
    public void sLineData() {
        if (sInterfaceType.equalsIgnoreCase("SMT")) {
            titlebarview.setLeftText(getString(R.string.production_line_status) + "-[" + sLineNo + "]");
        }
        String sStationFix = " " + getString(R.string.good_rate_content);
        sStationNameIn = SharedPreferencesUtils.getValue(getBaseContext(), "StationNameChsIn", "");
        String sStationNameTemp[] = sStationNameIn.split(",");
        if (sStationNameTemp.length <= 1) {
            llPassRate.setVisibility(View.GONE);
        } else {
            llPassRate.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < sStationNameTemp.length; i++) {
            if (sStationNameTemp[i].equals("")) {
                continue;
            }
            if (i == 0) {
                tvPassRate1Title.setText(sStationNameTemp[i].toString() + sStationFix);
            }
            if (i == 1) {
                tvPassRate1Title.setText(sStationNameTemp[i].toString() + sStationFix);
            }
        }

    }

    private boolean checkLine() {
        sLineNo = SharedPreferencesUtils.getValue(context, "LineNo", MyApplication.sLineNo);//获取线别代码
        if (TextUtils.isEmpty(sLineNo)) {
            ToastUtil.showShortToast(context, getString(R.string.set_line_please));
            return false;
        }
        return true;
    }

    private boolean checkStation() {
        sStationNoIn = SharedPreferencesUtils.getValue(getBaseContext(), "StationNoIn", "");//获取工站代码
        if (TextUtils.isEmpty(sStationNoIn)) {
            ToastUtil.showShortToast(context, getString(R.string.set_station_please));
            return false;
        }
        return true;
    }

    /*获取线看板数据*/
    private void GetSmtLineBoard() {
        if (!isStart) {
            return;
        }
        isStart = false;
        if (!TextUtils.isEmpty(sLineNo) && !TextUtils.isEmpty(sLineNo)) {

            if (isProDialog) {
                proDialogView(); //第一次获取数据时，显示进度显示
            }
            GetSmtLineBoardAsyncTask getSmtLineBoardAsyncTask = new GetSmtLineBoardAsyncTask();
            getSmtLineBoardAsyncTask.execute(sInterfaceType, sLineNo, sStationNoIn, sStationNameIn);
        }

    }

    /*获取线看板数据*/
    public class GetSmtLineBoardAsyncTask extends AsyncTask<String, Void, String> {
        String sLineNo = "";
        String sStationNoIn = "";
        String sStationNameIn = "";
        String sInterfaceType = "";

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            sInterfaceType = params[0];
            sLineNo = params[1];
            sStationNoIn = params[2];
            sStationNameIn = params[3];
            // sLanguage=params[4];
            // sRespon=params[3];
            JSONObject jsonObj = new JSONObject();
            try {
                jsonObj.put("sInterfaceType", sInterfaceType);
                jsonObj.put("sLineNo", sLineNo);
                jsonObj.put("sStationNoIn", sStationNoIn);
                jsonObj.put("sMesUser", MyApplication.sMesUser);
                jsonObj.putOpt("sFactoryNo", MyApplication.sFactoryNo);
                jsonObj.put("sLanguage", MyApplication.sLanguage);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            return GetData.getDataByJson(jsonObj.toString(), "GetSmtLineBoard");
        }

        @Override
        protected void onPostExecute(String json) {
            isProDialog = false;
            proDialog.dismiss();
            if (!isPause) {
                isStart = true;
            }

            if (null == json || json.equals("")) {
                return;
            }
            try {
//                clearText(); //清空数据
                JSONArray jsonArray = new JSONArray(json);
                JSONObject item = jsonArray.getJSONObject(0);
                String sStatus = item.getString("status");
                String sMsg = item.getString("msg");
                String sDateNow = item.getString("sDateNow");
                String sWorkStatus = item.getString("sWorkStatus");
                String sWorkStatusName = item.getString("sWorkStatusName");
                if (sStatus.equalsIgnoreCase("OFFLINE")) {
                    setContentText(getString(R.string.nothing));
//                    no_working_status.setVisibility(View.VISIBLE);
//                    working_status.setVisibility(View.GONE);
//                    tv_status.setText(sWorkStatusName);
//                    tv_status_date.setText(sDateNow); //显示时间
                    //设置字体加粗
//                    TextPaint tp = tv_status.getPaint();
//                    tp.setFakeBoldText(true);
                } else if (sStatus.equalsIgnoreCase("OK")) {
                    sLineData();
                    //判断接口返回的当前线别的状态
                    if (!sWorkStatus.equalsIgnoreCase("WORKING")) {
//                        setContentText(getString(R.string.nothing));
//                        no_working_status.setVisibility(View.VISIBLE);
//                        working_status.setVisibility(View.GONE);
//                        tv_status.setText(sWorkStatusName);
//                        tv_status_date.setText(sDateNow); //显示时间
//                        //设置字体加粗
//                        TextPaint tp = tv_status.getPaint();
//                        tp.setFakeBoldText(true);
                    } else {
//                        no_working_status.setVisibility(View.GONE);
//                        working_status.setVisibility(View.VISIBLE);
                        tvLineStatus.setText(sWorkStatusName); //线体状态
                        tvLineStatus.getPaint().setFakeBoldText(true);
                        tvDate.setText(sDateNow); //显示时间

                        String sLineNo = item.getString("sLineNo");
                        String sCustomerName = item.getString("sCustomerName");
                        String sWo = item.getString("sWo");
                        String sProductNo = item.getString("sProductNo");
                        String sWoQtyPlan = item.getString("sWoQtyPlan");
                        String sObjectiveOutput = item.getString("sObjectiveOutput");
                        String sQtyOutput = item.getString("sQtyOutput");
                        String sAchievingRate = item.getString("sAchievingRate");
                        String sFinishingRate = item.getString("sFinishingRate");//完成率
                        String sYrt = item.getString("sYrt");
                        String sMountingEfficiencyRate = item.getString("sMissRate");//抛料率
                        String sShiftName = item.getString("sShiftName");
                        String sShiftTimeBegin = item.getString("sShiftTimeBegin");
                        String sShiftTimeEnd = item.getString("sShiftTimeEnd");
                        productTvLineStatusTitle.setText(sLineNo);
                        tvShift.setText("["+ sShiftName + "   " + getString(R.string.time) + "(" + sShiftTimeBegin + "-" + sShiftTimeEnd + ")" + "]"); // 显示班次
                        tvCustom.setText(sCustomerName); //显示客户
                        tvProductNo.setText(sProductNo); //显示产品号
                        tvWo.setText(sWo);  //显示制令单
                        tvWoNum.setText(sWoQtyPlan); //显示计划数量

                        tvTargetNum.setText(sObjectiveOutput); //目标产量
                        tvRealNum.setText(sQtyOutput); //实际产量
                        tvFinishRate.setText(sAchievingRate + "%"); //达成率
                        tvMoFinishRate.setText(sFinishingRate + "%"); //工单完成率

                        tvTargetNum.getPaint().setFakeBoldText(true);
                        tvRealNum.getPaint().setFakeBoldText(true);
                        tvFinishRate.getPaint().setFakeBoldText(true);
                        tvMoFinishRate.getPaint().setFakeBoldText(true);

                        tvAddGoodRate.setText(sYrt + "%"); //累计良率
                        tvRejectRatePpm.setText(sMountingEfficiencyRate);//抛料率
                        tvAddGoodRate.getPaint().setFakeBoldText(true);
                        tvPassRate1.getPaint().setFakeBoldText(true);
                        tvPassRate2.getPaint().setFakeBoldText(true);
                        tvRejectRatePpm.getPaint().setFakeBoldText(true);

                        Double dlAchievingRate = Double.valueOf(sAchievingRate);
                        Double dlFinishingRate = Double.valueOf(sFinishingRate);
                        Double dlsYrt = Double.valueOf(sYrt);
                        Double dlMountingEfficiencyRate = Double.valueOf(sMountingEfficiencyRate);
                        if (dlAchievingRate < 80) {
                            tvFinishRate.setTextColor(getResources().getColor(R.color.red));
                        } else {
                            tvFinishRate.setTextColor(getResources().getColor(R.color.lb_blue));
                        }

                        if (dlFinishingRate < 80) {
                            tvMoFinishRate.setTextColor(getResources().getColor(R.color.red));
                        } else {
                            tvMoFinishRate.setTextColor(getResources().getColor(R.color.lb_blue));
                        }

                        if (dlsYrt < 90) {
                            tvAddGoodRate.setTextColor(getResources().getColor(R.color.red));
                        } else {
                            tvAddGoodRate.setTextColor(getResources().getColor(R.color.lb_blue));
                        }

                        if (dlMountingEfficiencyRate < 80) {
                            tvRejectRatePpm.setTextColor(getResources().getColor(R.color.red));
                        } else {
                            tvRejectRatePpm.setTextColor(getResources().getColor(R.color.lb_blue));
                        }
                        initPieChart(sYrt,sMountingEfficiencyRate);
                        String sStationNoTemp[] = sStationNoIn.split(",");
                        for (int i = 0; i < sStationNoTemp.length; i++) {
                            if (i == 0) {
                                if (item.has("StationPassRate1")) {
                                    String sStationPassRate1 = item.getString("StationPassRate1");
                                    Double dlStationPassRate1 = Double.valueOf(sStationPassRate1);
                                    tvPassRate1.setText(sStationPassRate1 + "%");
                                    if (dlStationPassRate1 < 90) {
                                        tvPassRate1.setTextColor(getResources().getColor(R.color.red));
                                    } else {
                                        tvPassRate1.setTextColor(getResources().getColor(R.color.lb_blue));
                                    }
                                }

                            }
                            if (i == 1) {
                                if (item.has("StationPassRate1")) {
                                    String sStationPassRate2 = item.getString("StationPassRate2");
                                    Double dlStationPassRate2 = Double.valueOf(sStationPassRate2);
                                    tvPassRate2.setText(sStationPassRate2 + "%");
                                    if (dlStationPassRate2 < 90) {
                                        tvPassRate2.setTextColor(getResources().getColor(R.color.red));
                                    } else {
                                        tvPassRate2.setTextColor(getResources().getColor(R.color.lb_blue));
                                    }
                                }

                            }

                        }
                    }
                } else {
                    Toast.makeText(context, sMsg, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }
    }

    private void initPieChart(String x1,String x2) {
        llPieChart.removeAllViews();
        PieChart01View view = new PieChart01View(context,x1,x2);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//        view.setLayoutParams(new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.FILL_PARENT,
//                LinearLayout.LayoutParams.FILL_PARENT));
        view.setLayoutParams(layoutParams);
        llPieChart.addView(view);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPause = true;
        isStart = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null || task != null) {
            timer.cancel();
            task.cancel();
        }
    }

    //长按mTitleBarView 页面上 设置 点击事件跳转设置页面
    private View.OnClickListener settingOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            Intent intent = new Intent();
            intent.setClass(context, FuncSettingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    };

    private void setContentText(String text) {

        tvCustom.setText(text);
        tvWo.setText(text);
        tvWoNum.setText(text);
        tvProductNo.setText(text);
        tvTargetNum.setText(text);
        tvRealNum.setText(text);
        tvFinishRate.setText(text);
        tvMoFinishRate.setText(text);
        tvAddGoodRate.setText(text);
        tvPassRate1.setText(text);
        tvPassRate2.setText(text);
        tvRejectRatePpm.setText(text);
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
