package com.kanban.switchfragmaster.ui.activity.report;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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
import com.kanban.switchfragmaster.data.EquipmentInfo;
import com.kanban.switchfragmaster.data.MyApplication;
import com.kanban.switchfragmaster.http.service.GetData;
import com.kanban.switchfragmaster.http.service.MyTask;
import com.kanban.switchfragmaster.ui.activity.report.equipment.EquipmentChartView;
import com.kanban.switchfragmaster.ui.activity.report.equipment.EquipmentTableView;
import com.kanban.switchfragmaster.view.BarChart12View;
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

public class EquipmentActivity extends Activity {

    @BindView(R.id.activity_equipment_titlebarview)
    TitleBarView titlebarview;
    private Context context;
    @BindView(R.id.rb_equipment_chart_pager)
    RadioButton rbChartPager;
    @BindView(R.id.rb_equipment_table_pager)
    RadioButton rbTablePager;
    @BindView(R.id.rg_equipment_viewpager_contorl)
    RadioGroup rgEquipmentViewpagerContorl;
    @BindView(R.id.equipment_vp_content)
    ViewPager viewPager;
    @BindView(R.id.equipment_fl_show_content)
    FrameLayout equipmentFlShowContent;
    private List<View> list = new ArrayList<View>();
    private ArrayList<EquipmentInfo> equipmentList = new ArrayList<>();
    private String sLineNo = "";
    private String sWo = "";
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
                    getQuipmentInfo(sLineNo,sWo);
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
        setContentView(R.layout.activity_equipment);
        ButterKnife.bind(this);
        context = EquipmentActivity.this;
        initView();
        changePage();
        initTitleBar();
        initData();
        getQuipmentInfo(sLineNo,sWo);
    }

    private void initTitleBar() {
        titlebarview.setIvBackImage(View.VISIBLE);
        titlebarview.setBackOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titlebarview.setLeftText("设备报表");
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
        getQuipmentInfo(sLineNo,sWo);
    }
    private void getQuipmentInfo(String sLineNo,String sWo){
        //获取-设备运转率信息-数据处理
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
                return GetData.getDataByJson(jsonObj.toString(), "GetOperationRate"); //接口返回数据
//            String ss = "[{\"status\":\"OK\",\"msg\":\"产能数据获取成功!\",\"sDateNow\":\"2017.10.20 18:10\",\"data\":[{\"sPeriodName\":\"08:30~09:30\",\"sOperation\":\"56\",\"sStop\":\"60\"},{\"sPeriodName\":\"09:30~10:30\",\"sOperation\":\"89\",\"sStop\":\"70\"},{\"sPeriodName\":\"10:30~11:30\",\"sOperation\":\"80\",\"sStop\":\"80\"},{\"sPeriodName\":\"11:30~12:30\",\"sOperation\":\"45\",\"sStop\":\"50\"},{\"sPeriodName\":\"12:30~13:30\",\"sOperation\":\"88\",\"sStop\":\"90\"},{\"sPeriodName\":\"13:30~14:30\",\"sOperation\":\"70\",\"sStop\":\"78\"},{\"sPeriodName\":\"14:30~15:30\",\"sOperation\":\"70\",\"sStop\":\"89\"},{\"sPeriodName\":\"15:30~16:30\",\"sOperation\":\"41\",\"sStop\":\"50\"},{\"sPeriodName\":\"16:30~17:30\",\"sOperation\":\"12\",\"sStop\":\"13\"},{\"sPeriodName\":\"17:30~18:30\",\"sOperation\":\"13\",\"sStop\":\"35\"},{\"sPeriodName\":\"18:30~19:30\",\"sOperation\":\"55\",\"sStop\":\"56\"},{\"sPeriodName\":\"19:30~20:30\",\"sOperation\":\"67\",\"sStop\":\"78\"}]}]";
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
                    equipmentList = new ArrayList<>();
                    if (sStatus.equalsIgnoreCase("OK")) {
                        String sData = item.getString("data");
                        JSONArray jsonArrayData = new JSONArray(sData);
                        for (int i = 0; i < jsonArrayData.length(); i++) {
                            JSONObject itemData = jsonArrayData.getJSONObject(i);
                            EquipmentInfo equipmentInfo = new EquipmentInfo();
                            equipmentInfo.setTime(itemData.getString("sPeriodName")); //时段名
                            equipmentInfo.setRunPercent(itemData.getString("sOperation")); //运行百分率
                            equipmentInfo.setStopPercent(itemData.getString("sStop")); //停止百分率
                            equipmentList.add(equipmentInfo);
                        }
                        isTimerStatus = true;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).execute(sLineNo,sWo);
    }

    private void initView() {

        for (int i = 0; i < 10; i++) {
            EquipmentInfo info = new EquipmentInfo();
            info.setRunPercent("70" + i);
            info.setStopPercent("10" + i);
            info.setTime((8 + i) + ":00~" + (9 + i) + ":00");
            equipmentList.add(info);
        }
        View view1 = initActivity();
        list.add(view1);
        EquipmentTableView view2 = new EquipmentTableView(context, equipmentList);
        list.add(view2);
    }

    private View initActivity() {
        //图表的使用方法:
        //使用方式一:
        // 1.新增一个Activity
        // 2.新增一个View,继承Demo中的GraphicalView或DemoView都可，依Demo中View目录下例子绘制图表.
        // 3.将自定义的图表View放置入Activity对应的XML中，将指明其layout_width与layout_height大小.
        // 运行即可看到效果. 可参考非ChartsActivity的那几个图的例子，都是这种方式。

        //使用方式二:
        //代码调用 方式有下面二种方法:
        //方法一:
        //在xml中的FrameLayout下增加图表和ZoomControls,这是利用了现有的xml文件.
        // 1. 新增一个View，绘制图表.
        // 2. 通过下面的代码得到控件，addview即可
        //LayoutInflater factory = LayoutInflater.from(this);
        //View content = (View) factory.inflate(R.layout.activity_multi_touch, null);


        //方法二:
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
        final RelativeLayout chartLayout = new RelativeLayout(context);
        EquipmentChartView equipmentChartView = null;
        if (equipmentList.size() >= 1) {
//            equipmentFlShowContent.removeAllViews();
//            equipmentFlShowContent.invalidate();
            equipmentChartView = new EquipmentChartView(context, equipmentList);
            equipmentChartView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.FILL_PARENT));
//            equipmentChartView.invalidate();
//            equipmentFlShowContent.addView(equipmentChartView);
            chartLayout.addView(equipmentChartView, layoutParams);
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

    private View initActivity2() {
        //图表的使用方法:
        //使用方式一:
        // 1.新增一个Activity
        // 2.新增一个View,继承Demo中的GraphicalView或DemoView都可，依Demo中View目录下例子绘制图表.
        // 3.将自定义的图表View放置入Activity对应的XML中，将指明其layout_width与layout_height大小.
        // 运行即可看到效果. 可参考非ChartsActivity的那几个图的例子，都是这种方式。

        //使用方式二:
        //代码调用 方式有下面二种方法:
        //方法一:
        //在xml中的FrameLayout下增加图表和ZoomControls,这是利用了现有的xml文件.
        // 1. 新增一个View，绘制图表.
        // 2. 通过下面的代码得到控件，addview即可
        //LayoutInflater factory = LayoutInflater.from(this);
        //View content = (View) factory.inflate(R.layout.activity_multi_touch, null);


        //方法二:
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
        int scrHeight = (int) (dm.heightPixels * 1.0);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                scrWidth, scrHeight);

        //居中显示
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        //图表view放入布局中，也可直接将图表view放入Activity对应的xml文件中
        final RelativeLayout chartLayout = new RelativeLayout(this);

        chartLayout.addView(new BarChart12View(this), layoutParams);

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
        rgEquipmentViewpagerContorl.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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
                        rbChartPager.setChecked(true);
                        break;
                    case 1:
                        rbTablePager.setChecked(true);
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
