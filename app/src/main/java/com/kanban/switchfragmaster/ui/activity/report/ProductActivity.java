package com.kanban.switchfragmaster.ui.activity.report;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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


import com.kanban.switchfragmaster.R;
import com.kanban.switchfragmaster.data.MyApplication;
import com.kanban.switchfragmaster.data.ProductTableInfo;
import com.kanban.switchfragmaster.http.service.GetData;
import com.kanban.switchfragmaster.http.service.MyTask;
import com.kanban.switchfragmaster.ui.activity.report.product.ProductChartView;
import com.kanban.switchfragmaster.ui.activity.report.product.ProductTableView;
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

public class ProductActivity extends Activity {
    @BindView(R.id.activity_product_titlebarview)
    TitleBarView titlebarview;
    private Context context;
    @BindView(R.id.rb_product_chart_pager)
    RadioButton rbProductChartPager;
    @BindView(R.id.rb_product_table_pager)
    RadioButton rbProductTablePager;
    @BindView(R.id.rg_product_viewpager_contorl)
    RadioGroup rgProductViewpagerContorl;
    @BindView(R.id.product_vp_content)
    ViewPager viewPager;
    @BindView(R.id.product_fl_show_content)
    FrameLayout productFlShowContent;
    private List<View> list = new ArrayList<View>();
    private ArrayList<ProductTableInfo> productTableInfos = new ArrayList<>();
    private String sLineNo = "";

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
                    getGwoOutputPeriod(sLineNo);
                }
            }

        }
    };


    //工作线程
    private class WorkThread extends Thread {
        @Override
        public void run() {
            //......处理比较耗时的操作

            //处理完成后给handler发送消息
            Message msg = new Message();
            msg.what = 1;
            handler.sendMessage(msg);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                timer.schedule(task, 20000, iRefreshTimeSet);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_chart);
        ButterKnife.bind(this);
        context = ProductActivity.this;
        initView();
        changePage();
        initTitleBar();
        initData();
        new WorkThread().start();
    }
    private void initTitleBar(){
        titlebarview.setIvBackImage(View.VISIBLE);
        titlebarview.setBackOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titlebarview.setLeftText("生产报表");
    }
    private void initData(){
        Intent intent = getIntent();
        if(intent!=null){
            sLineNo = intent.getStringExtra("sLineNo");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isTimerBack = true; //是否屏蔽定时任务
        getGwoOutputPeriod(sLineNo);//获取-生产图表信息
    }

    //获取-生产图表信息
    private void getGwoOutputPeriod(String sLineNo) {
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
                return GetData.getDataByJson(jsonObj.toString(), "GetGwoOutputPeriod"); //接口返回数据
//            String ss = "[{\"status\":\"OK\",\"msg\":\"产能数据获取成功!\",\"sDateNow\":\"2017.10.20 18:10\",\"data\":[{\"sPeriodName\":\"08:30~09:30\",\"sOutputTotal\":\"56\",\"sOutputTarget\":\"60\"},{\"sPeriodName\":\"09:30~10:30\",\"sOutputTotal\":\"89\",\"sOutputTarget\":\"70\"},{\"sPeriodName\":\"10:30~11:30\",\"sOutputTotal\":\"80\",\"sOutputTarget\":\"80\"},{\"sPeriodName\":\"11:30~12:30\",\"sOutputTotal\":\"45\",\"sOutputTarget\":\"50\"},{\"sPeriodName\":\"12:30~13:30\",\"sOutputTotal\":\"88\",\"sOutputTarget\":\"90\"},{\"sPeriodName\":\"13:30~14:30\",\"sOutputTotal\":\"70\",\"sOutputTarget\":\"78\"},{\"sPeriodName\":\"14:30~15:30\",\"sOutputTotal\":\"70\",\"sOutputTarget\":\"89\"},{\"sPeriodName\":\"15:30~16:30\",\"sOutputTotal\":\"41\",\"sOutputTarget\":\"50\"},{\"sPeriodName\":\"16:30~17:30\",\"sOutputTotal\":\"12\",\"sOutputTarget\":\"13\"},{\"sPeriodName\":\"17:30~18:30\",\"sOutputTotal\":\"13\",\"sOutputTarget\":\"35\"},{\"sPeriodName\":\"18:30~19:30\",\"sOutputTotal\":\"55\",\"sOutputTarget\":\"56\"},{\"sPeriodName\":\"19:30~20:30\",\"sOutputTotal\":\"67\",\"sOutputTarget\":\"78\"}]}]";
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
                    productTableInfos = new ArrayList<>();
                    if (sStatus.equalsIgnoreCase("OK")) {
                        String sData = item.getString("data");
                        JSONArray jsonArrayData = new JSONArray(sData);
                        for (int i = 0; i < jsonArrayData.length(); i++) {
                            JSONObject itemData = jsonArrayData.getJSONObject(i);
                            ProductTableInfo productTableInfo = new ProductTableInfo();
                            String sPeriodName = itemData.getString("sPeriodName");
                            String sOutputTotal = itemData.getString("sOutputTotal");
                            String sOutputTarget = itemData.getString("sOutputTarget");
                            if (TextUtils.isEmpty(sOutputTotal)) {
                                sOutputTotal = "0";
                            }
                            if (TextUtils.isEmpty(sOutputTarget)) {
                                sOutputTarget = "0";
                            }
                            productTableInfo.setTime(sPeriodName); //时段
                            productTableInfo.setReally(sOutputTotal); //实际产能
                            productTableInfo.setStandand(sOutputTarget); //标准产能
                            productTableInfos.add(productTableInfo);
                        }
                        isTimerStatus = true; //是否启用定时刷新-调用接口获取数据
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).execute(sLineNo);
    }
    private void initView() {

        View view1 = initActivity();
        list.add(view1);
        ProductTableView view2 = new ProductTableView(context, productTableInfos);
        list.add(view2);
    }

    private View initActivity() {
        //完全动态创建,无须XML文件.
        FrameLayout content = new FrameLayout(this);

        //缩放控件放置在FrameLayout的上层，用于放大缩小图表
        FrameLayout.LayoutParams frameParm = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        frameParm.gravity = Gravity.BOTTOM | Gravity.RIGHT;

		   /*
		  //缩放控件放置在FrameLayout的上层，用于放大缩小图表
	       mZoomControls = new ZoomControls(this);
	       mZoomControls.setIsZoomInEnabled(true);
	       mZoomControls.setIsZoomOutEnabled(true);
		   mZoomControls.setLayoutParams(frameParm);
		   */

        //图表显示范围在占屏幕大小的90%的区域内
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int scrWidth = (int) (dm.widthPixels * 1.0);
        int scrHeight = (int) (dm.heightPixels * 0.8);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                scrWidth, scrHeight);

        //居中显示
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        //图表view放入布局中，也可直接将图表view放入Activity对应的xml文件中
        final RelativeLayout chartLayout = new RelativeLayout(this);

//        chartLayout.addView(new BarChart12View(this), layoutParams);
        ProductChartView productChartView = null;
        if (productTableInfos.size() >= 1) {
            productChartView = new ProductChartView(context, productTableInfos);
            productChartView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.FILL_PARENT));
            chartLayout.addView(productChartView, layoutParams);
        }
        //增加控件
        ((ViewGroup) content).addView(chartLayout);
        //((ViewGroup) content).addView(mZoomControls);
//        setContentView(content);
        return content;
        //放大监听
        //  mZoomControls.setOnZoomInClickListener(new OnZoomInClickListenerImpl());
        //缩小监听
        //  mZoomControls.setOnZoomOutClickListener(new OnZoomOutClickListenerImpl());
    }

    //页面滑动图片切换功能
    private void changePage() {
        rgProductViewpagerContorl.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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
                        rbProductChartPager.setChecked(true);
                        break;
                    case 1:
                        rbProductTablePager.setChecked(true);
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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        isTimerBack = false; //是否屏蔽定时任务
        if (timer != null || task != null) {
            timer.cancel();
            task.cancel();
        }
    }
}
