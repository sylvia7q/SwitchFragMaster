package com.kanban.switchfragmaster.ui;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


import com.kanban.switchfragmaster.R;
import com.kanban.switchfragmaster.autoupdate.UpdateManager;
import com.kanban.switchfragmaster.data.MessageInfo;
import com.kanban.switchfragmaster.data.MyApplication;
import com.kanban.switchfragmaster.http.service.GetData;
import com.kanban.switchfragmaster.http.service.UDPClient;
import com.kanban.switchfragmaster.presenter.BasePresenter;
import com.kanban.switchfragmaster.presenter.MyPresenter;
import com.kanban.switchfragmaster.ui.activity.myself.func.MorelineSettingActivity;
import com.kanban.switchfragmaster.ui.fragment.FragBoard;
import com.kanban.switchfragmaster.ui.fragment.FragMessage;
import com.kanban.switchfragmaster.ui.fragment.FragMyself;
import com.kanban.switchfragmaster.utils.AppUtil;
import com.kanban.switchfragmaster.utils.SharedPreferencesUtils;
import com.kanban.switchfragmaster.view.TitleBarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity<T extends BasePresenter> extends FragmentActivity implements FragMessage.FragmentListener {

    private static final String TAG = HomeActivity.class.getName();
    @BindView(R.id.titlebarview)
    TitleBarView titlebarview;
    @BindView(R.id.vp_content)
    ViewPager viewPager;
    @BindView(R.id.rb_message_pager)
    RadioButton rbMessagePager;
//    @BindView(R.id.rb_report_pager)
//    RadioButton rbReportPager;
    @BindView(R.id.rb_status_pager)
    RadioButton rbStatusPager;
    @BindView(R.id.rb_myself_pager)
    RadioButton rbAboutPager;
    @BindView(R.id.rg_home_viewpager_contorl)
    RadioGroup rgHomeViewpagerContorl;
    @BindView(R.id.activity_home_bottom_tv_message_notice_count)
    TextView tvMessageCount;
    @BindView(R.id.fl_show_content)
    FrameLayout flShowContent;


    public static Context context;
    private List<MessageInfo> messageList = new ArrayList<>();
    private List<String> reportList;
    private List<String> productList;
    private TextView tvMessage;
    private TextView tvReport;
    private TextView tvProduct;
    private TextView tvAbout;
    private String reportName = "";
    private String productName = "";
    private String companyName = "";
    private FrameLayout flShow;
    private Fragment fragMessage;//推送消息
    private Fragment fragBoard;//看板
//    private Fragment fragReport;//报表
//    private Fragment fragStatus;//生产情况
    private Fragment fragMyself;//关于
    private HomeFragmentPageAdapter adapter;
    private UDPClient client = null;
    public String json = "";

    private String sFactoryNo = "0"; //工厂代码
    private String sLanguage = "S"; //语言
    private String sClientIp = ""; //语言
    private String sMac = ""; //语言


    public MyHandler myHandler = new MyHandler(this);

    private class MyHandler extends Handler {
        private final WeakReference<HomeActivity> mActivity;

        public MyHandler(HomeActivity activity) {
            mActivity = new WeakReference<HomeActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    json = msg.obj.toString();
                    if (!TextUtils.isEmpty(json.trim())) {
                        MyPresenter.getInstance(context).connSuccess(json);
                        showNoticeCount();
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case 2:
                    Log.e(TAG, msg.obj.toString());
                    break;
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        context = HomeActivity.this;
        titlebarview.setLeftText("消息");
        new Thread(new Runnable() {
            @Override
            public void run() {
                conn();

            }
        }).start();
        send();
        initFrag();

    }


    private void initFrag() {
        rgHomeViewpagerContorl.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                switch (i) {
                    case R.id.rb_message_pager:
                        viewPager.setCurrentItem(0);// 设置当前页面
                        titlebarview.setLeftText("消息");
                        titlebarview.setBtnRightClickable(View.GONE);
//                        vpContent.setCurrentItem(0,false);// false去掉viewpager切换页面的动画
                        break;
//                    case R.id.rb_report_pager:
//                        viewPager.setCurrentItem(1);
//                        titlebarview.setLeftText("报表");
//                        break;
                    case R.id.rb_status_pager:
                        viewPager.setCurrentItem(1);
                        titlebarview.setLeftText("生产状况");
                        titlebarview.setBtnRightClickable(View.VISIBLE);
                        titlebarview.setBtnRight(R.drawable.line_set_selector);
                        titlebarview.setBtnRightOnclickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent aboutIntent = new Intent(context, MorelineSettingActivity.class);
                                aboutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(aboutIntent);
                            }
                        });
                        break;
                    case R.id.rb_myself_pager:
                        viewPager.setCurrentItem(2);
                        titlebarview.setLeftText("我");
                        titlebarview.setBtnRightClickable(View.GONE);
                        break;
                }

            }
        });
        fragMessage = new FragMessage();
        fragBoard = new FragBoard();
//        fragReport = new FragReport();
//        fragStatus = new FragProductStatus();
        fragMyself = new FragMyself();
        List<Fragment> mFragmentList = new ArrayList<>();
        mFragmentList.add(fragMessage);
        mFragmentList.add(fragBoard);
//        mFragmentList.add(fragReport);
//        mFragmentList.add(fragStatus);
        mFragmentList.add(fragMyself);
        adapter = new HomeFragmentPageAdapter(getSupportFragmentManager(), mFragmentList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        rgHomeViewpagerContorl.check(R.id.rb_message_pager);
                        titlebarview.setLeftText("消息");
                        titlebarview.setBtnRightClickable(View.GONE);
                        break;
//                    case 1:
//                        rgHomeViewpagerContorl.check(R.id.rb_report_pager);
//                        titlebarview.setLeftText("报表");
//                        break;
//                    case 1:
//                        rgHomeViewpagerContorl.check(R.id.rb_status_pager);
//                        titlebarview.setLeftText("看板");
//                        break;
                    case 1:
                        rgHomeViewpagerContorl.check(R.id.rb_status_pager);
                        titlebarview.setLeftText("生产状况");
                        titlebarview.setBtnRight(R.drawable.line_set_selector);
                        titlebarview.setBtnRightClickable(View.VISIBLE);
                        titlebarview.setBtnRightOnclickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent aboutIntent = new Intent(context, MorelineSettingActivity.class);
                                aboutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(aboutIntent);
                            }
                        });
                        break;
                    case 2:
                        rgHomeViewpagerContorl.check(R.id.rb_myself_pager);
                        titlebarview.setLeftText("我");
                        titlebarview.setBtnRightClickable(View.GONE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        showNoticeCount();
    }

    @Override
    public void update() {
        showNoticeCount();
    }

    class HomeFragmentPageAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragmentList;
        private List<String> tagList = new ArrayList<String>();

        public HomeFragmentPageAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }

        public HomeFragmentPageAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> tagList) {
            super(fm);
            this.fragmentList = fragmentList;
            this.tagList = tagList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            if (fragmentList == null) {
                return 0;
            } else {
                return fragmentList.size();
            }
        }

        /**
         * 首页显示title，每日推荐等..
         * 若有问题，移到对应单独页面
         */
        @Override
        public CharSequence getPageTitle(int position) {
            if (tagList != null) {
                return tagList.get(position);
            } else {
                return "";
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub
            /*if (position == 0){
                removeFragment(container,position);
            }*/
            return super.instantiateItem(container, position);
        }

        private String getFragmentTag(int viewId, int index) {
            try {
                Class<FragmentPagerAdapter> cls = FragmentPagerAdapter.class;
                Class<?>[] parameterTypes = {int.class, long.class};
                Method method = cls.getDeclaredMethod("makeFragmentName",
                        parameterTypes);
                method.setAccessible(true);
                String tag = (String) method.invoke(this, viewId, index);
                return tag;
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }

        private void removeFragment(ViewGroup container, int index) {
            String tag = getFragmentTag(container.getId(), index);
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
            if (fragment == null)
                return;
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.remove(fragment);
            ft.commit();
            /*if(messageList!=null){
                Bundle bundle = new Bundle();
                bundle.putSerializable("messageList", messageList);
                Log.e(TAG, "fragMessage----messageList = " + messageList.toString());
                fragment.setArguments(bundle);
            }*/
            getSupportFragmentManager().executePendingTransactions();
        }

        @Override
        public int getItemPosition(Object object) {
            // TODO Auto-generated method stub
            return POSITION_NONE;
        }

    }

    private void showNoticeCount() {
        int count = MyPresenter.getInstance(context).getUnReadcount();
        if (count > 0) {
            tvMessageCount.setText(count + "");
            tvMessageCount.setVisibility(View.VISIBLE);
        } else {
            tvMessageCount.setVisibility(View.GONE);
        }
    }

    private void conn() {
        //建立线程池
        ExecutorService exec = Executors.newCachedThreadPool();
        client = new UDPClient();
        exec.execute(client);
        client.setHomeHandler(myHandler);
    }

    private void send() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 2;
                String str = "message";
                client.send(str);
                message.obj = str;
                myHandler.sendMessage(message);

            }
        });
        thread.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetMesUser();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        client.setUdpLife(false);
    }

    //检查app是否需要更新
    private void AutoUpdateManager() {
        UpdateManager mUpdateManager = new UpdateManager(this);
        mUpdateManager.checkUpdateInfo(false);//获取APP版本(本地与服务器)比对，是否需要更新
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            // 为Intent设置Action、Category属性
            intent.setAction(Intent.ACTION_MAIN);// "android.intent.action.MAIN"
            intent.addCategory(Intent.CATEGORY_HOME); //"android.intent.category.HOME"
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initData() {
        //获取本地文件数据
        String sServerIp = SharedPreferencesUtils.getValue(getBaseContext(), "sServerIp", "127.0.0.1");
        Integer nServerPort = SharedPreferencesUtils.getValue(getBaseContext(), "sServerPort", 0);
        //变量赋值
        sClientIp = AppUtil.getIp(); //IP地址
        sMac = AppUtil.getMacAddress(); //MAC地址
        //调用WebService接口赋值(后续新增APK只需要修改此处应对数据)
        String sApkName = "TIMS_RPT.apk"; //APK名称
        String sApkVersionXml = "TIMS_RPT"; //APK版本XML节点(验证APK版本时传参)
        String sCheckVersion = "CheckVersion"; //APK版本Check方法(验证APK版本时传入方法名)
        String sServiceBese = "http://" + sServerIp + ":" + nServerPort; //IP+端口
        String sWeService = "/ADS.EPS.WEBSERVICE/TimsRptService.asmx";
        String sUrl = sServiceBese + sWeService; //WebService地址
        String sDownloadUrl = sServiceBese + "/ADS.EPS.WEBSERVICE/update/" + sApkName; //APK下载地址
        String sVersionXml = sServiceBese + "/ADS.EPS.WEBSERVICE/TimsRptVersion.xml"; //APK版本地址
        //保存数据至本地文件中
        SharedPreferencesUtils.saveValue(getBaseContext(), "sFactoryNo", sFactoryNo); //工厂代码：0
        SharedPreferencesUtils.saveValue(getBaseContext(), "sLanguage", sLanguage); //保存语言至本地-(默认S)语言：S/T/E
        SharedPreferencesUtils.saveValue(getBaseContext(), "sClientIp", sClientIp); //保存IP至本地
        SharedPreferencesUtils.saveValue(getBaseContext(), "sMac", sMac); //保存MAC至本地

        SharedPreferencesUtils.saveValue(getBaseContext(), "sServiceBese", sServiceBese); //IP+端口
        SharedPreferencesUtils.saveValue(getBaseContext(), "sWeService", sWeService);
        SharedPreferencesUtils.saveValue(getBaseContext(), "sUrl", sUrl); //WebService地址
        SharedPreferencesUtils.saveValue(getBaseContext(), "sDownloadUrl", sDownloadUrl); //APK下载地址
        SharedPreferencesUtils.saveValue(getBaseContext(), "sVersionXml", sVersionXml); //APK版本地址
        //系统全局变量赋值
        MyApplication.sFactoryNo = sFactoryNo;
        MyApplication.sLanguage = sLanguage;
        MyApplication.sClientIp = sClientIp;
        MyApplication.sMac = sMac;

        MyApplication.sUrl = sUrl; //WebService地址
        MyApplication.sDownloadUrl = sDownloadUrl; //APK下载地址
        MyApplication.sVersionXml = sVersionXml; //APK版本地址
        MyApplication.sApkName = sApkName; //APK名称
        MyApplication.sApkVersionXml = sApkVersionXml; //APK版本XML节点(验证APK版本时传参)
        MyApplication.sCheckVersion = sCheckVersion; //APK版本Check方法(验证APK版本时传入方法名)

        AutoUpdateManager(); //检查app是否需要更新
    }

    //获取客户代码-数据处理
    private void GetMesUser() {
        GetMesUserAsyncTask getMesUserAsyncTask = new GetMesUserAsyncTask();
        getMesUserAsyncTask.execute();
    }

    //获取客户代码
    public class GetMesUserAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
        }

        protected String doInBackground(String... params) {
            return GetData.getDataByJson(null, "GetMesUser");
        }

        protected void onPostExecute(String json) {
            if (null == json || json.equals("")) {
                return;
            }
            try {
                JSONArray jsonArray = new JSONArray(json);
                JSONObject item = jsonArray.getJSONObject(0);
                String sStatus = item.getString("status");
                String sMsg = item.getString("msg");
                if (sStatus.equalsIgnoreCase("OK")) {
                    MyApplication.sMesUser = item.getString("MES_USER");
                    SharedPreferencesUtils.saveValue(getBaseContext(), "MesUser", MyApplication.sMesUser);
                    return;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
