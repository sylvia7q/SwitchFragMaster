package com.kanban.switchfragmaster.ui.activity.board;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.kanban.switchfragmaster.R;
import com.kanban.switchfragmaster.data.MyApplication;
import com.kanban.switchfragmaster.http.service.GetData;
import com.kanban.switchfragmaster.ui.activity.myself.func.LineSettingActivity;
import com.kanban.switchfragmaster.ui.activity.myself.func.StationSettingActivity;
import com.kanban.switchfragmaster.utils.SharedPreferencesUtils;
import com.kanban.switchfragmaster.view.TitleBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GrakonLineBoardActivity extends Activity {

    @BindView(R.id.activity_grakon_line_board_titlebarview)
    TitleBarView titlebarview;
    @BindView(R.id.grakon_tv_date)
    TextView tv_date;
    @BindView(R.id.grakon_tv_shift)
    TextView tv_Shift;
    @BindView(R.id.grakon_tv_custom_title)
    TextView tvCustomTitle;
    @BindView(R.id.grakon_tv_custom)
    TextView tvCustom;
    @BindView(R.id.grakon_tv_product_no_title)
    TextView tvProductNoTitle;
    @BindView(R.id.grakon_tv_product_no)
    TextView tvProductNo;
    @BindView(R.id.grakon_tv_wo_title)
    TextView tvWoTitle;
    @BindView(R.id.grakon_tv_wo)
    TextView tvWo;
    @BindView(R.id.grakon_tv_wo_num_title)
    TextView tvWoNumTitle;
    @BindView(R.id.grakon_tv_wo_num)
    TextView tvWoNum;
    @BindView(R.id.grakon_tv_target_num_title)
    TextView tvTargetNumTitle;
    @BindView(R.id.grakon_tv_target_num)
    TextView tvTargetNum;
    @BindView(R.id.grakon_tv_real_num_title)
    TextView tvRealNumTitle;
    @BindView(R.id.grakon_tv_real_num)
    TextView tvRealNum;
    @BindView(R.id.grakon_tv_finish_rate_title)
    TextView tvFinishRateTitle;
    @BindView(R.id.grakon_tv_finish_rate)
    TextView tvFinishRate;
    @BindView(R.id.grakon_tv_mo_finish_rate_title)
    TextView tvMoFinishRateTitle;
    @BindView(R.id.grakon_tv_mo_finish_rate)//工单完成率
    TextView tvMoFinishRate;

    @BindView(R.id.grakon_tv_overall_yrt)
    TextView tv_overall_yrt;
    @BindView(R.id.grakon_tv_input)
    TextView tv_input;
    @BindView(R.id.grakon_tv_output)
    TextView tv_output;
    @BindView(R.id.grakon_tv_yield)
    TextView tv_yield;
    @BindView(R.id.grakon_tv_station1)
    TextView tvStation1;
    @BindView(R.id.grakon_tv_input1)
    TextView tv_input1;
    @BindView(R.id.grakon_tv_output1)
    TextView tv_output1;
    @BindView(R.id.grakon_tv_yield1)
    TextView tv_yield1;
    @BindView(R.id.grakon_tv_station2)
    TextView tvStation2;
    @BindView(R.id.grakon_tv_input2)
    TextView tv_input2;
    @BindView(R.id.grakon_tv_output2)
    TextView tv_output2;
    @BindView(R.id.grakon_tv_yield2)
    TextView tv_yield2;
    @BindView(R.id.grakon_tv_station3)
    TextView tvStation3;
    @BindView(R.id.grakon_tv_input3)
    TextView tv_input3;
    @BindView(R.id.grakon_tv_output3)
    TextView tv_output3;
    @BindView(R.id.grakon_tv_yield3)
    TextView tv_yield3;
    @BindView(R.id.grakon_tv_station4)
    TextView tvStation4;
    @BindView(R.id.grakon_tv_input4)
    TextView tv_input4;
    @BindView(R.id.grakon_tv_output4)
    TextView tv_output4;
    @BindView(R.id.grakon_tv_yield4)
    TextView tv_yield4;
    @BindView(R.id.grakon_tv_station5)
    TextView tvStation5;
    @BindView(R.id.grakon_tv_input5)
    TextView tv_input5;
    @BindView(R.id.grakon_tv_output5)
    TextView tv_output5;
    @BindView(R.id.grakon_tv_yield5)
    TextView tv_yield5;
    @BindView(R.id.grakon_ll_over)
    LinearLayout llOver;
    @BindView(R.id.grakon_ll_station1)
    LinearLayout llStation1;
    @BindView(R.id.grakon_ll_station2)
    LinearLayout llStation2;
    @BindView(R.id.grakon_ll_station3)
    LinearLayout llStation3;
    @BindView(R.id.grakon_ll_station4)
    LinearLayout llStation4;
    @BindView(R.id.grakon_ll_station5)
    LinearLayout llStation5;

    String sLineNo = "";  // 线别
    String sStationNoIn = "";  // 站位代码
    String sStationNameIn = "";  // 站位中文名称
    String sStationNameEnIn = "";  // 站位英文名称
    String sStationNameTemp[];  //存储选择站位数组
    TextView tv_line; //线别
    ImageView iv_customer_logo;   //客户LOGO
    @BindView(R.id.grakon_tv_line_status_title)
    TextView tvLineStatusTitle;
    @BindView(R.id.grakon_tv_line_status)
    TextView tvLineStatus;

    private Timer timer = new Timer(true);
    String sInterfaceType;
    int nGet = 0; //获取数据后自动隐藏菜单，默认2次
    boolean isStart = false;
    boolean isPause = false;
    boolean isProDialog = true;

    ProgressDialog proDialog = null;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grakon_line_board);
        ButterKnife.bind(this);
        context = GrakonLineBoardActivity.this;
        initTitleBar();
        initData();
        //启动定时器
        timer.schedule(task, 1000, 1 * 60 * 1000); //表示1000毫秒之後，每隔1000毫秒執行一次
    }

    //定时刷新
    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            GetSmtLineBoard();
        }
    };

    private void initTitleBar() {
        titlebarview.setIvBackImage(View.VISIBLE);
        titlebarview.setLeftText(getString(R.string.grakon_line_monitoring));
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
            sStationNoIn = intent.getStringExtra("sStationNoIn");
            sStationNameIn = intent.getStringExtra("sStationNameIn");
            sInterfaceType = intent.getStringExtra("sInterfaceType");
        }
    }

    public void proDialogView() {
        proDialog = ProgressDialog.show(GrakonLineBoardActivity.this, getString(R.string.loading), getString(R.string.loading_data));
    }

    @Override
    protected void onStart() {
        super.onStart();
        isStart = true;
        isPause = false;
        titlebarview.setLeftText(sLineNo + " " + getString(R.string.line_monitoring));
        int int_sStationNameTemp = 0;
        tv_overall_yrt.setText(R.string.gk_overall_yrt);
        sStationNameIn = SharedPreferencesUtils.getValue(getBaseContext(),"StationNameChsIn", "");
        sStationNameTemp = sStationNameIn.split(","); // 在每个逗号字符处进行分解
        int_sStationNameTemp = sStationNameTemp.length;

        //判断设置的工站个数，显示对应的行
        if (int_sStationNameTemp <= 1) {
            llStation1.setVisibility(View.VISIBLE);
            llStation2.setVisibility(View.GONE);
            llStation3.setVisibility(View.GONE);
            llStation4.setVisibility(View.GONE);
            llStation5.setVisibility(View.GONE);
        } else if (int_sStationNameTemp > 1 && int_sStationNameTemp <= 2) {
            llStation1.setVisibility(View.VISIBLE);
            llStation2.setVisibility(View.VISIBLE);
            llStation3.setVisibility(View.GONE);
            llStation4.setVisibility(View.GONE);
            llStation5.setVisibility(View.GONE);
        } else if (int_sStationNameTemp > 2 && int_sStationNameTemp <= 3) {
            llStation1.setVisibility(View.VISIBLE);
            llStation2.setVisibility(View.VISIBLE);
            llStation3.setVisibility(View.VISIBLE);
            llStation4.setVisibility(View.GONE);
            llStation5.setVisibility(View.GONE);
        } else if (int_sStationNameTemp > 3 && int_sStationNameTemp <= 4) {
            llStation1.setVisibility(View.VISIBLE);
            llStation2.setVisibility(View.VISIBLE);
            llStation3.setVisibility(View.VISIBLE);
            llStation4.setVisibility(View.VISIBLE);
            llStation5.setVisibility(View.GONE);
        } else if (int_sStationNameTemp > 4 && int_sStationNameTemp <= 5) {
            llStation1.setVisibility(View.VISIBLE);
            llStation2.setVisibility(View.VISIBLE);
            llStation3.setVisibility(View.VISIBLE);
            llStation4.setVisibility(View.VISIBLE);
            llStation5.setVisibility(View.VISIBLE);
        }

        //显示设置的工站名称
        for (int i = 0; i < int_sStationNameTemp; i++) {
            if (sStationNameTemp[i].equals("")) {
                continue;
            }
            if (i == 0) {
                tvStation1.setText(sStationNameTemp[i].toString());
            }
            if (i == 1) {
                tvStation2.setText(sStationNameTemp[i].toString());
            }
            if (i == 2) {
                tvStation3.setText(sStationNameTemp[i].toString());
            }
            if (i == 3) {
                tvStation4.setText(sStationNameTemp[i].toString());
            }
            if (i == 4) {
                tvStation5.setText(sStationNameTemp[i].toString());
            }
        }
        GetSmtLineBoard();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isStart = false;
        isPause = true;
        if(proDialog!=null){
            proDialog.dismiss();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null || task != null) {
            timer.cancel();
            task.cancel();
        }
        if(proDialog!=null){
            proDialog.dismiss();
        }
    }

    //获取看板数据
    private void GetSmtLineBoard() {
        if (!isStart) {
            return;
        }
        isStart = false;
        //判断线别是否有选择
        if (sLineNo.equals("")) {
            if (MyApplication.isBack) {
                return;
            }
            Intent intent = new Intent();
            intent.setClass(GrakonLineBoardActivity.this, LineSettingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return;
        }
        //判断站位是否有选择
        if (sStationNoIn.equals("")) {
            if (MyApplication.isBack) {
                return;
            }
            Intent intent = new Intent();
            intent.setClass(GrakonLineBoardActivity.this, StationSettingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return;
        }
        if (isProDialog) {
            proDialogView(); //第一次获取数据时，显示进度显示
        }
        GetSmtLineBoardAsyncTask getSmtLineBoardAsyncTask = new GetSmtLineBoardAsyncTask();
        getSmtLineBoardAsyncTask.execute(sInterfaceType, sLineNo, sStationNoIn, sStationNameIn);
    }

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
                JSONArray jsonArray = new JSONArray(json);
                JSONObject item = jsonArray.getJSONObject(0);
                String sStatus = item.getString("status");
                String sMsg = item.getString("msg");
                String sDateNow = item.getString("sDateNow");
                tv_date.setText(sDateNow); //显示服务器时间

                if (sStatus.equalsIgnoreCase("OFFLINE")) {
                    String sWorkStatusName = item.getString("sWorkStatusName");
                    tvLineStatus.setText("(" + sWorkStatusName + ")");  //显示产线状态
                } else if (sStatus.equalsIgnoreCase("OK")) {
                    String sLineNo = item.getString("sLineNo");//线别
                    String sWorkStatusName = item.getString("sWorkStatusName");//产线状态
                    String sCustomerName = item.getString("sCustomerName");//客户
                    String sWo = item.getString("sWo");//制令单
                    String sProductNo = item.getString("sProductNo");//产品
                    String sWoQtyPlan = item.getString("sWoQtyPlan");//计划数量

                    String sObjectiveOutput = item.getString("sObjectiveOutput");// 目标产量
                    String sQtyOutput = item.getString("sQtyOutput");//实际产量
                    String sAchievingRate = item.getString("sAchievingRate");//达成率
                    String sFinishingRate = item.getString("sFinishingRate");//工单完成率

                    //String sYrt =item.getString("sYrt");
//                    String sMountingEfficiencyRate =item.getString("sMountingEfficiencyRate");//贴装率
//                    String sMountingEfficiencyRate =item.getString("sMissRate");//抛料率

                    String sShiftNo = item.getString("sShiftNo");//班别-编码
                    String sShiftName = item.getString("sShiftName");//班别-名称
                    String sShiftTimeBegin = item.getString("sShiftTimeBegin");//时段-开始
                    String sShiftTimeEnd = item.getString("sShiftTimeEnd");//时段-结束

                    tv_Shift.setText("[" + sShiftName + "  " + getString(R.string.time) + "(" + sShiftTimeBegin + "-" + sShiftTimeEnd + ")" + "]"); // 显示班次//显示时段
                    tvLineStatus.setText(sWorkStatusName);  //显示产线状态
                    tvCustom.setText(sCustomerName); // 显示客户
                    tvProductNo.setText(sProductNo); // 显示产品
                    tvWo.setText(sWo); // 显示制令单
                    tvWoNum.setText(sWoQtyPlan); // 显示制令单数量

                    tvTargetNum.setText(sObjectiveOutput); // 显示目标产量
                    tvRealNum.setText(sQtyOutput); // 显示实际产量
                    tvFinishRate.setText(sAchievingRate + "%"); // 显示达成率
                    tvMoFinishRate.setText(sFinishingRate + "%"); // 显示工单完成率

                    tvTargetNum.getPaint().setFakeBoldText(true);
                    tvRealNum.getPaint().setFakeBoldText(true);
                    tvFinishRate.getPaint().setFakeBoldText(true);
                    tvMoFinishRate.getPaint().setFakeBoldText(true);

                    Double dlAchievingRate = Double.valueOf(sAchievingRate);
                    Double dlFinishingRate = Double.valueOf(sFinishingRate);
                    //达成率
                    if (dlAchievingRate < 80) {
                        tvFinishRate.setTextColor(getResources().getColor(R.color.red));
                    } else {
                        tvFinishRate.setTextColor(getResources().getColor(R.color.blue));
                    }
                    //工单完成率
                    if (dlFinishingRate < 80) {
                        tvMoFinishRate.setTextColor(getResources().getColor(R.color.red));
                    } else {
                        tvMoFinishRate.setTextColor(getResources().getColor(R.color.blue));
                    }

                    String sdlQtyInput = item.getString("sdlQtyInput"); //当班-投入数
                    String sdlQtyOutput = item.getString("sdlQtyOutput"); //当班-产出数
//                    String sdlQtyInput = item.getString("StationOutputTotal"); //当班-投入数
//                    String sdlQtyOutput = item.getString("StationOutputOk"); //当班-产出数
                    String first_pass_yield = item.getString("StationFirstPassRate"); //直通率
                    tv_input.setText(sdlQtyInput); //显示-当班-投入数
                    tv_output.setText(sdlQtyOutput); //显示-当班-产出数
                    tv_yield.setText(first_pass_yield + "%"); //显示-直通率

                    String sStationNoTemp[] = sStationNoIn.split(","); // 在每个逗号字符处进行分解
                    for (int i = 0; i < sStationNoTemp.length; i++) {
                        if (i == 0) {
                            String sStation_input1 = item.getString("StationOutputTotal1"); //工站1-投入数
                            String sStation_output1 = item.getString("StationOutputOk1"); //工站1-产出数
                            tv_input1.setText(sStation_input1); //显示-工站1-投入数
                            tv_output1.setText(sStation_output1); //显示-工站1-产出数

                            String sStationPassRate1 = item.getString("StationPassRate1"); //工站1-良率
                            Double dlStationPassRate1 = Double.valueOf(sStationPassRate1);
                            tv_yield1.setText(sStationPassRate1 + "%");//显示-工站1-良率
                            if (dlStationPassRate1 < 90) {
                                tv_yield1.setTextColor(getResources().getColor(R.color.red));
                            } else {
                                tv_yield1.setTextColor(getResources().getColor(R.color.blue));
                            }
                        }
                        if (i == 1) {
                            String sStation_input2 = item.getString("StationOutputTotal2"); //工站2-投入数
                            String sStation_output2 = item.getString("StationOutputOk2"); //工站2-产出数
                            tv_input2.setText(sStation_input2); //显示-工站2-投入数
                            tv_output2.setText(sStation_output2); //显示-工站2-产出数

                            String sStationPassRate2 = item.getString("StationPassRate2"); //工站2-良率
                            Double dlStationPassRate2 = Double.valueOf(sStationPassRate2);
                            tv_yield2.setText(sStationPassRate2 + "%");//显示-工站2-良率
                            if (dlStationPassRate2 < 90) {
                                tv_yield2.setTextColor(getResources().getColor(R.color.red));
                            } else {
                                tv_yield2.setTextColor(getResources().getColor(R.color.blue));
                            }
                        }
                        if (i == 2) {
                            String sStation_input3 = item.getString("StationOutputTotal3"); //工站3-投入数
                            String sStation_output3 = item.getString("StationOutputOk3"); //工站3-产出数
                            tv_input3.setText(sStation_input3); //显示-工站3-投入数
                            tv_output3.setText(sStation_output3); //显示-工站3-产出数
                            String sStationPassRate3 = item.getString("StationPassRate3"); //工站3-良率
                            Double dlStationPassRate3 = Double.valueOf(sStationPassRate3);
                            tv_yield3.setText(sStationPassRate3 + "%");//显示-工站3-良率
                            if (dlStationPassRate3 < 90) {
                                tv_yield3.setTextColor(getResources().getColor(R.color.red));
                            } else {
                                tv_yield3.setTextColor(getResources().getColor(R.color.blue));
                            }
                        }
                        if (i == 3) {
                            String sStation_input4 = item.getString("StationOutputTotal4"); //工站4-投入数
                            String sStation_output4 = item.getString("StationOutputOk4"); //工站4-产出数
                            tv_input4.setText(sStation_input4); //显示-工站4-投入数
                            tv_output4.setText(sStation_output4); //显示-工站4-产出数
                            String sStationPassRate4 = item.getString("StationPassRate4"); //工站4-良率
                            Double dlStationPassRate4 = Double.valueOf(sStationPassRate4);
                            tv_yield4.setText(sStationPassRate4 + "%");//显示-工站4-良率
                            if (dlStationPassRate4 < 90) {
                                tv_yield4.setTextColor(getResources().getColor(R.color.red));
                            } else {
                                tv_yield4.setTextColor(getResources().getColor(R.color.blue));
                            }
                        }
                        if (i == 4) {
                            String sStation_input5 = item.getString("StationOutputTotal5"); //工站5-投入数
                            String sStation_output5 = item.getString("StationOutputOk5"); //工站5-产出数
                            tv_input5.setText(sStation_input5); //显示-工站5-投入数
                            tv_output5.setText(sStation_output5); //显示-工站5-产出数
                            String sStationPassRate5 = item.getString("StationPassRate5"); //工站5-良率
                            Double dlStationPassRate5 = Double.valueOf(sStationPassRate5);
                            tv_yield5.setText(sStationPassRate5 + "%");//显示-工站5-良率
                            if (dlStationPassRate5 < 90) {
                                tv_yield5.setTextColor(getResources().getColor(R.color.red));
                            } else {
                                tv_yield5.setTextColor(getResources().getColor(R.color.blue));
                            }
                        }
                    }
                } else {
                    Toast.makeText(GrakonLineBoardActivity.this, sMsg, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(GrakonLineBoardActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                if(proDialog!=null){
                    proDialog.dismiss();
                }
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
