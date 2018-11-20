package com.kanban.switchfragmaster.ui.activity.board;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.Toast;


import com.kanban.switchfragmaster.R;
import com.kanban.switchfragmaster.data.MyApplication;
import com.kanban.switchfragmaster.data.SevenusSmtWorkshopCharacterEntity;
import com.kanban.switchfragmaster.http.service.GetData;
import com.kanban.switchfragmaster.ui.activity.board.workshop_character.SmtProductionWorkshopCharacterView;
import com.kanban.switchfragmaster.ui.activity.board.workshop_character.SpinnerBarChart02View;
import com.kanban.switchfragmaster.utils.SharedPreferencesUtils;
import com.kanban.switchfragmaster.view.TitleBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xclcharts.common.DensityUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WorkshopCharacterActivity extends Activity {

    @BindView(R.id.activity_workshop_character_titlebarview)
    TitleBarView titlebarview;
    @BindView(R.id.rb_workshop_character_list_pager)
    RadioButton rbListPager;
    @BindView(R.id.rb_workshop_character_chart_pager)
    RadioButton rbChartPager;
    @BindView(R.id.rg_workshop_character_viewpager_contorl)
    RadioGroup rgWorkshopCharacterViewpagerContorl;
    @BindView(R.id.workshop_character_vp_content)
    ViewPager viewPager;
    @BindView(R.id.workshop_character_fl_show_content)
    FrameLayout workshopCharacterFlShowContent;
    private Context context;
    private List<View> list = new ArrayList<View>();
    private String sMoreLineNoIn = "";  // 多线别
    private   List<SevenusSmtWorkshopCharacterEntity> listWolienInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workshop_character);
        ButterKnife.bind(this);
        context = WorkshopCharacterActivity.this;
        initTitleBar();
        initTitleBar();
        initData();
    }

    private void initTitleBar() {
        titlebarview.setIvBackImage(View.VISIBLE);
        titlebarview.setLeftText(getString(R.string.sevenus_quality_title_tv1));
        titlebarview.setBackOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void initData() {
        sMoreLineNoIn = SharedPreferencesUtils.getValue(context, "MoreLineNoIn", "");
        //获取数据的方法
        GetWorkshopCharacterLineDate();
    }
    private void initView() {
        SmtProductionWorkshopCharacterView view1 = new SmtProductionWorkshopCharacterView(context, listWolienInfo);
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
            SpinnerBarChart02View barChart02= new SpinnerBarChart02View(this,0,listWolienInfo);
            barChart02
                    .setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.FILL_PARENT));
            chartLayout.addView(barChart02,layoutParams);
        }

        //增加控件
        ((ViewGroup) content).addView(chartLayout);
        return content;
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void GetWorkshopCharacterLineDate() {

        SmtProductionWorkshopCharacterAsyncTask getWorkshopCharacterLineDateAsyncTask = new SmtProductionWorkshopCharacterAsyncTask();
        getWorkshopCharacterLineDateAsyncTask.execute();
    }

    private class SmtProductionWorkshopCharacterAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            JSONObject jsonObj = new JSONObject();
            try {
                jsonObj.put("sLineNoIn", sMoreLineNoIn);
                jsonObj.put("sMesUser", MyApplication.sMesUser);
                jsonObj.putOpt("sFactoryNo", MyApplication.sFactoryNo);
                jsonObj.put("sLanguage", MyApplication.sLanguage);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            return GetData.getDataByJson(jsonObj.toString(), "GetWorkShopExtend");
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
                String sDateNow = item.getString("sDateNow");
                String sNumber = item.getString("sSpoilageRateColCount"); //列
                int sCount = Integer.parseInt(sNumber); //string转int
//                tv_date.setText(sDateNow); //显示时间
               listWolienInfo = new ArrayList<SevenusSmtWorkshopCharacterEntity>();
                if (sStatus.equalsIgnoreCase("OK")) {
                    String sData = item.getString("data");
                    JSONArray jsonArrayData = new JSONArray(sData);
                    int jsonArrda = jsonArrayData.length();
                    for (int i = 0; i < jsonArrda; i++) {
                        JSONObject itemData = jsonArrayData.getJSONObject(i);
                        SevenusSmtWorkshopCharacterEntity mBasWoInfoLine = new SevenusSmtWorkshopCharacterEntity();
                        mBasWoInfoLine.setsLineNo(itemData.getString("LINE_NO")); //线别
                        mBasWoInfoLine.setsRty(itemData.getString("RTY")); //RTY
                        if (sCount == 1) {
                            mBasWoInfoLine.setsHrowingRate1(itemData.getString("SPOILAGE_RATE1")); //1#抛料率
                            mBasWoInfoLine.setsHrowingRate2("NA"); //2#抛料率
                            mBasWoInfoLine.setsHrowingRate3("NA"); //3#抛料率
                        }
                        if (sCount == 2) {
                            mBasWoInfoLine.setsHrowingRate1(itemData.getString("SPOILAGE_RATE1")); //1#抛料率
                            if (itemData.getString("SPOILAGE_RATE2").equals("")) {
                                mBasWoInfoLine.setsHrowingRate2("NA"); //2#抛料率
                            } else {
                                mBasWoInfoLine.setsHrowingRate2(itemData.getString("SPOILAGE_RATE2")); //2#抛料率
                            }
                            mBasWoInfoLine.setsHrowingRate3("NA"); //3#抛料率
                        }
                        if (sCount == 3) {
                            mBasWoInfoLine.setsHrowingRate1(itemData.getString("SPOILAGE_RATE1")); //1#抛料率
                            if (itemData.getString("SPOILAGE_RATE2").equals("")) {
                                mBasWoInfoLine.setsHrowingRate2("NA"); //2#抛料率
                            } else {
                                mBasWoInfoLine.setsHrowingRate2(itemData.getString("SPOILAGE_RATE2")); //2#抛料率
                            }
                            if (itemData.getString("SPOILAGE_RATE3").equals("")) {
                                mBasWoInfoLine.setsHrowingRate3("NA"); //3#抛料率
                            } else {
                                mBasWoInfoLine.setsHrowingRate3(itemData.getString("SPOILAGE_RATE3")); //3#抛料率
                            }
                        }
                        if (sCount >= 4) {
                            mBasWoInfoLine.setsHrowingRate1(itemData.getString("SPOILAGE_RATE1")); //1#抛料率
                            if (itemData.getString("SPOILAGE_RATE2").equals("")) {
                                mBasWoInfoLine.setsHrowingRate2("NA"); //2#抛料率
                            } else {
                                mBasWoInfoLine.setsHrowingRate2(itemData.getString("SPOILAGE_RATE2")); //2#抛料率
                            }
                            if (itemData.getString("SPOILAGE_RATE3").equals("")) {
                                mBasWoInfoLine.setsHrowingRate3("NA"); //3#抛料率
                            } else {
                                mBasWoInfoLine.setsHrowingRate3(itemData.getString("SPOILAGE_RATE3")); //3#抛料率
                            }
                        }
                        mBasWoInfoLine.setsProductionState(itemData.getString("STATUS")); //产线状态代码
                        mBasWoInfoLine.setsProductionStateName(itemData.getString("STATUS_NAME")); //产线状态名称
                        listWolienInfo.add(mBasWoInfoLine);
                    }

                } else {
                    Toast.makeText(context, sMsg, Toast.LENGTH_SHORT).show();
                }
                if(listWolienInfo!=null){
                    initView();
                    changePage();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }

    //页面滑动图片切换功能
    private void changePage() {
        rgWorkshopCharacterViewpagerContorl.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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
