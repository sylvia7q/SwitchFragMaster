package com.kanban.switchfragmaster.ui.activity.board;

import android.app.Activity;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;


import com.kanban.switchfragmaster.R;
import com.kanban.switchfragmaster.data.MyApplication;
import com.kanban.switchfragmaster.data.WoInfoWhEntity;
import com.kanban.switchfragmaster.http.service.GetData;
import com.kanban.switchfragmaster.ui.activity.board.warehouse.SpinnerBarChart03View;
import com.kanban.switchfragmaster.ui.activity.board.warehouse.WarehouseListView;
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

public class WarehouseActivity extends Activity {

    @BindView(R.id.activity_warehouse_titlebarview)
    TitleBarView titlebarview;
    @BindView(R.id.rb_warehouse_list_pager)
    RadioButton rbListPager;
    @BindView(R.id.rb_warehouse_chart_pager)
    RadioButton rbChartPager;
    @BindView(R.id.rg_warehouse_viewpager_contorl)
    RadioGroup rgWarehouseViewpagerContorl;
    @BindView(R.id.warehouse_vp_content)
    ViewPager viewPager;
    @BindView(R.id.warehouse_fl_show_content)
    FrameLayout warehouseFlShowContent;
    @BindView(R.id.activity_warehouse_kanban_tv_on_production)
    TextView tv_on_production;
    @BindView(R.id.activity_warehouse_kanban_tv_plan_stop_production)
    TextView tv_plan_stop_production;
    private Context context;
    private List<WoInfoWhEntity> listWoInfo;
    private List<View> list = new ArrayList<View>();
    private String sLineNo = "";
    String sInterfaceType = "SMT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse);
        ButterKnife.bind(this);
        context = WarehouseActivity.this;
        initTitleBar();

    }

    private void initTitleBar() {
        titlebarview.setIvBackImage(View.VISIBLE);
        titlebarview.setLeftText(getString(R.string.warehouse_monitoring));
        titlebarview.setBackOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        WarehouseListView view1 = new WarehouseListView(context, listWoInfo);
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
        if (listWoInfo.size() >= 1) {
            SpinnerBarChart03View barChart03 = new SpinnerBarChart03View(this, 0, listWoInfo);
            barChart03.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.FILL_PARENT));
            chartLayout.addView(barChart03, layoutParams);
        }

        //增加控件
        ((ViewGroup) content).addView(chartLayout);
        return content;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }
    private void initData(){
        getWarehouseKanbanBoard();
    }

    private boolean checkLine() {
        sLineNo = SharedPreferencesUtils.getValue(context, "LineNo", MyApplication.sLineNo);//获取线别代码
        if (TextUtils.isEmpty(sLineNo)) {
            ToastUtil.showShortToast(context, getString(R.string.set_line_please));
            return false;
        }
        return true;
    }

    /*获取仓库看板数据*/
    private void getWarehouseKanbanBoard() {
        if (checkLine()) {
            GetSmtLineBoardAsyncTask getSmtLineBoardAsyncTask = new GetSmtLineBoardAsyncTask();
            getSmtLineBoardAsyncTask.execute(sInterfaceType, sLineNo);
        }
    }

    /*获取线看板数据*/
    public class GetSmtLineBoardAsyncTask extends AsyncTask<String, Void, String> {
        String sInterfaceType = "";
        String sLineNo = "";

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            sInterfaceType = params[0];
            sLineNo = params[1];
            JSONObject jsonObj = new JSONObject();
            try {
                jsonObj.put("sMesUser", MyApplication.sMesUser);
                jsonObj.putOpt("sFactoryNo", MyApplication.sFactoryNo);
                jsonObj.put("sLanguage", MyApplication.sLanguage);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
            return GetData.getDataByJson(jsonObj.toString(), "GetWarehouseStockSetBoard");
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
//                tv_date.setText(sDateNow);

                listWoInfo = new ArrayList<WoInfoWhEntity>();
                if (sStatus.equalsIgnoreCase("OK")) {

                    String sData = item.getString("data");
                    JSONArray jsonArrayData = new JSONArray(sData);
                    int nOnLine = 0;
                    int nOffLine = 0;
                    for (int i = 0; i < jsonArrayData.length(); i++) {
                        JSONObject itemData = jsonArrayData.getJSONObject(i);
                        WoInfoWhEntity mBasWoInfo = new WoInfoWhEntity();
                        mBasWoInfo.setLineNo(itemData.getString("LINE_NO"));
                        String sLineStatus = itemData.getString("WORK_STATUS");
                        if (sLineStatus.equalsIgnoreCase("WORKING")) {
                            nOnLine++;
                        } else {
                            nOffLine++;
                        }
                        mBasWoInfo.setLineStatusName(itemData.getString("WORK_STATUS_NAME"));
                        mBasWoInfo.setWo(itemData.getString("WO"));
                        mBasWoInfo.setWoPlanQty(itemData.getString("QTY_PLAN"));
                        mBasWoInfo.setIsPrepareSufficient(itemData.getString("IS_PREPARE_SUFFICIENT"));
                        mBasWoInfo.setQtyTotal(itemData.getString("QTY_TOTAL"));
                        listWoInfo.add(mBasWoInfo);
                    }
                    tv_on_production.setText(getString(R.string.total) + " " + String.valueOf(nOnLine) + " " + getString(R.string.line_total));
                    tv_plan_stop_production.setText(getString(R.string.total) + " " + String.valueOf(nOffLine) + " " + getString(R.string.line_total));


                    //lv_list.setAdapter(new WarehouseKanbanListAdapter(context,listWoInfo));

                } else {
                    Toast.makeText(context, sMsg, Toast.LENGTH_SHORT).show();
                }
                if (listWoInfo != null) {
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
        rgWarehouseViewpagerContorl.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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
