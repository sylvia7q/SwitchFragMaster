package com.kanban.switchfragmaster.ui.activity.board;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.kanban.switchfragmaster.R;
import com.kanban.switchfragmaster.data.MyApplication;
import com.kanban.switchfragmaster.data.SevenusSmtWorkshopEntity;
import com.kanban.switchfragmaster.http.service.GetData;
import com.kanban.switchfragmaster.ui.activity.board.workshop.SmtProductWorkshopKanbanView;
import com.kanban.switchfragmaster.ui.activity.board.workshop.SpinnerBarChart01View;
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

public class WorkshopActivity extends Activity {

    @BindView(R.id.activity_workshop_titlebarview)
    TitleBarView titlebarview;
    @BindView(R.id.rb_workshop_list_pager)
    RadioButton rbListPager;
    @BindView(R.id.rb_workshop_chart_pager)
    RadioButton rbChartPager;
    @BindView(R.id.rg_workshop_viewpager_contorl)
    RadioGroup rgWorkshopViewpagerContorl;
    @BindView(R.id.workshop_vp_content)
    ViewPager viewPager;
    @BindView(R.id.workshop_fl_show_content)
    FrameLayout workshopFlShowContent;
    private Context context;
    private List<View> list = new ArrayList<View>();
    private List<SevenusSmtWorkshopEntity> listWolienInfo;
    private String sLineNo = "";
    String sInterfaceType = "SMT";
    String sMoreLineNoIn = "";  // 多线别
    private Timer timer = new Timer(true);
    boolean isTimerStatus = false; //是否启用定时刷新-调用接口获取数据
    boolean isTimerBack = true; //是否屏蔽定时任务
    int iRefreshTimeSet = 60000; //60秒-看板刷新时间(秒)
    //获取后台数据-定时任务
    private TimerTask task = new TimerTask() {
        public void run() {
            if (isTimerBack) { //是否屏蔽定时任务
                if (isTimerStatus) {  ////是否启用定时刷新-调用接口获取数据
                    //获取数据的方法
                    GetProductionWorkshopLineDate();
                }
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workshop);
        ButterKnife.bind(this);
        context = WorkshopActivity.this;
        initTitleBar();
        initData();

    }
    private void initTitleBar() {
        titlebarview.setIvBackImage(View.VISIBLE);
        titlebarview.setBackOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titlebarview.setLeftText("SMT车间看板");
    }
    private void initData(){
        Intent intent = getIntent();
        if (intent != null) {
            sInterfaceType = intent.getStringExtra("sInterfaceType");
        }
        sMoreLineNoIn = SharedPreferencesUtils.getValue(context, "MoreLineNoIn", "");
        GetProductionWorkshopLineDate();
    }
    private void initView() {
        SmtProductWorkshopKanbanView view1 = new SmtProductWorkshopKanbanView(context, listWolienInfo);
        list.add(view1);
        View view2 = initActivity();
        list.add(view2);
    }
    private View initActivity() {
        //完全动态创建,无须XML文件.
        FrameLayout content = new FrameLayout(this);

        //缩放控件放置在FrameLayout的上层，用于放大缩小图表
        FrameLayout.LayoutParams frameParm = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        frameParm.gravity = Gravity.BOTTOM | Gravity.RIGHT;

        //图表显示范围在占屏幕大小的90%的区域内
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int scrWidth = (int) (dm.widthPixels * 1.0);
        int scrHeight = (int) (dm.heightPixels * 0.8);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                scrWidth, scrHeight);

        //居中显示
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        //图表view放入布局中，也可直接将图表view放入Activity对应的xml文件中
        final RelativeLayout chartLayout = new RelativeLayout(context);
        if (listWolienInfo.size() >= 1) {
            SpinnerBarChart01View barChart01= new SpinnerBarChart01View(this,0,listWolienInfo);
            barChart01.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.FILL_PARENT));
            chartLayout.addView(barChart01,layoutParams);
        }

        //增加控件
        ((ViewGroup) content).addView(chartLayout);
        return content;
    }
    @Override
    protected void onResume() {
        super.onResume();
        isTimerBack = true; //是否屏蔽定时任务

    }
    //获取生产状况数据
    private void GetProductionWorkshopLineDate() {
        if (!TextUtils.isEmpty(sMoreLineNoIn)) {//判断车间是否有选择
            GetSmtProductionWorkshopKanbanAsyncTask getWarehoseKanbanLineDateAsyncTask = new GetSmtProductionWorkshopKanbanAsyncTask();
            getWarehoseKanbanLineDateAsyncTask.execute();
        }

    }

    public class GetSmtProductionWorkshopKanbanAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            JSONObject jsonObj = new JSONObject();
            try {
                jsonObj.put("sInterfaceType", sInterfaceType);
                jsonObj.put("sLineNoIn", sMoreLineNoIn);
                jsonObj.put("sMesUser", MyApplication.sMesUser);
                jsonObj.putOpt("sFactoryNo", MyApplication.sFactoryNo);
                jsonObj.put("sLanguage", MyApplication.sLanguage);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            return GetData.getDataByJson(jsonObj.toString(), "GetWorkShop");
        }

        @Override
        protected void onPostExecute(String json) {
           
            if (null == json || json.equals("")) {
                return;
            }
            try {
                JSONArray jsonArray = new JSONArray(json);
                JSONObject item = jsonArray.getJSONObject(0);
                String sStatus = item.getString("status");
                String sMsg = item.getString("msg");
                if (item.has("sDateNow")) {
                    String sDateNow = item.getString("sDateNow");
//                    tv_date.setText(sDateNow); //显示时间
                }
                listWolienInfo = new ArrayList<SevenusSmtWorkshopEntity>();
                if (sStatus.equalsIgnoreCase("OK")) {
                    String sData = item.getString("data");
                    JSONArray jsonArrayData = new JSONArray(sData);
                    int jsonArrda = jsonArrayData.length();
                    for (int i = 0; i < jsonArrda; i++) {
                        JSONObject itemData = jsonArrayData.getJSONObject(i);
                        SevenusSmtWorkshopEntity mBasWoInfoLine = new SevenusSmtWorkshopEntity();
                        mBasWoInfoLine.setsLineNo(itemData.getString("sLineNo")); //线别
//                        mBasWoInfoLine.setsCustomerNo(itemData.getString("sCustomerName")); //客户
                        mBasWoInfoLine.setsProductNo(itemData.getString("sProductNo")); //产品号
                        mBasWoInfoLine.setSo(itemData.getString("sWo")); //制令单
                        mBasWoInfoLine.setsMoPlanQty(itemData.getString("sWoQtyPlan")); //制令单数据
                        String sFinishingRate = itemData.getString("sFinishingRate");
                        mBasWoInfoLine.setsMoFinishingRate(sFinishingRate); //工单完成率
//                        String sOperationRate = itemData.getString("sOperationRate") + "%";
                        mBasWoInfoLine.setsProductionState(itemData.getString("sWorkStatusName")); //产线状态
                        listWolienInfo.add(mBasWoInfoLine);
                    }
                } else {
                    Toast.makeText(context, sMsg, Toast.LENGTH_LONG).show();
                }
                if(listWolienInfo!=null){
                    initView();
                    changePage();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
    //页面滑动图片切换功能
    private void changePage() {
        rgWorkshopViewpagerContorl.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.rb_equipment_chart_pager:
                        viewPager.setCurrentItem(0);// 设置当前页面
//                        vpContent.setCurrentItem(0,false);// false去掉viewpager切换页面的动画
                        break;
                    case R.id.rb_equipment_table_pager:
                        viewPager.setCurrentItem(1);
                        break;
                }

            }
        });
        FunPagerAdapter funPagerAdapter = new FunPagerAdapter(list);
        viewPager.setAdapter(funPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        rbListPager.setChecked(true);
                        break;
                    case 1:
                        rbChartPager.setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private class FunPagerAdapter extends PagerAdapter {
        private List<View> list;

        public FunPagerAdapter(List list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView(list.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ((ViewPager) container).addView(list.get(position));
            return list.get(position);
        }
    }
}
