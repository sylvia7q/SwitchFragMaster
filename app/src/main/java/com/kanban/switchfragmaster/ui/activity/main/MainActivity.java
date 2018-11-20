package com.kanban.switchfragmaster.ui.activity.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.device.ScanManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.kanban.switchfragmaster.R;
import com.kanban.switchfragmaster.autoupdate.UpdateManager;
import com.kanban.switchfragmaster.data.MyApplication;
import com.kanban.switchfragmaster.data.User;
import com.kanban.switchfragmaster.database.DBUtil;
import com.kanban.switchfragmaster.http.service.GetData;
import com.kanban.switchfragmaster.http.service.ScanReceiver;
import com.kanban.switchfragmaster.ui.HomeActivity;
import com.kanban.switchfragmaster.utils.AppUtil;
import com.kanban.switchfragmaster.utils.DateUtils;
import com.kanban.switchfragmaster.utils.DialogUtil;
import com.kanban.switchfragmaster.utils.SharedPreferencesUtils;
import com.kanban.switchfragmaster.utils.SoundPlayUtil;
import com.kanban.switchfragmaster.utils.TextViewUtils;
import com.kanban.switchfragmaster.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getName();
    @BindView(R.id.main_et_login_user_name)
    EditText etUserName;//账号
    @BindView(R.id.main_et_login_user_password)
    EditText etUserPassword;//密码
    @BindView(R.id.main_btn_login)
    Button btnLogin;//登录
    @BindView(R.id.main_btn_setting)
    Button btnSetting;

    //m60
    private static final String SCAN_ACTION_M60 = "com.mobilead.tools.action.scan_result";
    //i6200A
    private static final String SCAN_ACTION_I6002A = "android.intent.ACTION_DECODE_DATA";
    //i6200S
    private final static String SCAN_ACTION_I6002S = "urovo.rcv.message";
    private ScanReceiver mScanReceiver;
    private ScanManager mScanManager;
    private SoundPool soundpool = null;
    private int soundid;
    private boolean isScanReceiver = true; //定义跳转页面后是否执行doScan，true 执行 / false 不执行
    private boolean isScaning = false;

    private Context context;

    private String sFactoryNo = "0"; //工厂代码
    private String sLanguage = "S"; //语言
    private String sClientIp = ""; //语言
    private String sMac = ""; //语言

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        context = MainActivity.this;
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
    private void initData(){
        //获取本地文件数据
        String sServerIp = SharedPreferencesUtils.getValue(getBaseContext(), "sServerIp", "");
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
        initUser();
    }
    private void initUser(){
        DBUtil dbUtil = new DBUtil(context);
        User user = dbUtil.getLatestUser();
        if(user != null){
            etUserName.setText(user.getUserId());
            etUserName.requestFocus();
            etUserName.setSelection(0, etUserName.getText().length());
            etUserPassword.setText("");
        }else {
            Intent intent = getIntent();
            if(intent != null){
                etUserName.setText(intent.getStringExtra("userId"));
                etUserName.requestFocus();
                etUserName.setSelection(0, etUserName.getText().length());
                etUserPassword.setText("");
            }
        }


    }
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        initData();
        try {
            isScanReceiver = true; //定义是否执行doScan
            initScan();
            //PDA是否授权
//            if(!isAuthorization){
//                getPdaRegistrationCodeInfo();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        isScanReceiver = false; //定义是否执行doScan
    }
    private void initScan() {
        String TAG = "Topcee";
        try {
            if (mScanManager != null) {
                mScanManager.stopDecode();
                isScaning = false;
            }
            if (mScanReceiver != null) {            //注销短信监听广播
                Log.i("Topcee", android.os.Build.MODEL + "注销广播");
                this.unregisterReceiver(mScanReceiver);
            }

            String SCAN_ACTION = "";
            Log.i("Topcee", "PDA型号：" + android.os.Build.MODEL);
            switch (android.os.Build.MODEL) {
                case "i6200S":
                    Log.i("Topcee", "i6200S-PDA扫描注册");
                    //扫描头管理类 ScanManager
                    mScanManager = new ScanManager();
                    //打开扫描头电源,如果扫描头的系统服务未启动,会自动启动扫描头的系统服务。调用成功返回 true ,失败返回 false。
                    mScanManager.openScanner();
                    /**
                     * 此程序用来更改扫描头的输出模式。
                     * 如果扫描头为键盘输出模式,则所有扫描结果会被转换为键盘输入,发送到焦点编辑框
                     *内。如果扫描头为广播模式,应用程序需要注册 action 为 urovo.rcv.message 的广播接收
                     *器在广播机的 onReceive(Context context, Intent arg1) 方法中,通过如下语句
                     *byte[] barocode=arg1.getByteArrayExtra("barocode");
                     *int barocodelen=arg1.getIntExtra("length",0);
                     *byte temp=arg1.getByteExtra("barcodeType",(byte)0);
                     *分别获得 条码值,条码长度,条码类型
                     */
                    mScanManager.switchOutputMode(0);
                    soundpool = new SoundPool(1, AudioManager.STREAM_NOTIFICATION, 100); // MODE_RINGTONE
                    soundid = soundpool.load("/etc/Scan_new.ogg", 1);
                    SCAN_ACTION = SCAN_ACTION_I6002S;
                    break;
                case "m60":
                    Log.i("Topcee", "m60--PDA扫描注册");
                    SCAN_ACTION = SCAN_ACTION_M60;
                    break;
                case "SQ42":   //PDA送修，重刷系统导致版本问题
                    Log.i("Topcee", "SQ42--PDA扫描注册");
                    SCAN_ACTION = SCAN_ACTION_I6002A;
                    break;
                case "SQ43":   //新买的PDA
                    Log.i("Topcee", "SQ43-PDA扫描注册");
                    SCAN_ACTION = SCAN_ACTION_I6002A;
                    break;
                case "i6200A":
                    Log.i("Topcee", "i6200A-PDA扫描注册");
                    SCAN_ACTION = SCAN_ACTION_I6002A;
                    break;
                case "i6300A":  //i6300A广播动作 及 广播数据标签，与i6200A一样
                    Log.i("Topcee", "i6300A-PDA扫描注册");
                    SCAN_ACTION = SCAN_ACTION_I6002A;
                    break;
                default:
                    break;
            }
            //生成广播处理
            mScanReceiver = new ScanReceiver();

            //实例化过滤器并设置要过滤的广播
            IntentFilter intentFilter = new IntentFilter(SCAN_ACTION);
            intentFilter.setPriority(Integer.MAX_VALUE);
            //注册广播
            this.registerReceiver(mScanReceiver, intentFilter);
            Log.i("Topcee", android.os.Build.MODEL + "注册广播");
            mScanReceiver.setOnReceivedMessageListener(new ScanReceiver.MessageListener() {
                @Override
                public void onReceived(String barcodeStr) {
                    isScaning = false;
                    if(!isScaning){
                        isScaning = true;
                        if (isScanReceiver) {  //定义是否执行doScan
                            Log.i("Topcee", "LoginActivity-PDA扫描数据" + barcodeStr);
                            doScan(barcodeStr);
                        }
                    }

                }
            });
        } catch (Exception e) {
            Log.e(TAG, "openScanner...", e);
            e.printStackTrace();
        }
    }
    //扫描SN
    private void doScan(String barcodeStr) {
        if (etUserName.isFocused()) {
            etUserName.setText(barcodeStr);
            etUserPassword.requestFocus();
            play(true);
        } else if (etUserPassword.isFocused()) {
            etUserPassword.setText(barcodeStr);
            etUserPassword.setSelection(barcodeStr.length());
            play(true);
        }
    }
    private void play(boolean isOkSound) {
        SoundPlayUtil.playOkNg(isOkSound);
    }
    //检查app是否需要更新
    private void AutoUpdateManager() {
        UpdateManager mUpdateManager = new UpdateManager(this);
        mUpdateManager.checkUpdateInfo(false);//获取APP版本(本地与服务器)比对，是否需要更新
    }


    @OnClick({R.id.main_btn_login, R.id.main_btn_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.main_btn_login:
                login();
                break;
            case R.id.main_btn_setting:
                set();
                break;
        }
    }
    private void set(){
        Intent functionIntent = new Intent(context, IpSettingActivity.class);
        functionIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(functionIntent);
    }

    private boolean login(){
        String sUserNo = etUserName.getText().toString();
        String sPassword = etUserPassword.getText().toString();
        if (sUserNo.equals("")) {
            ToastUtil.showShortToast(this, getString(R.string.lb_prompt_user_no));
            etUserName.requestFocus();
            return false;
        }
        if (sPassword.equals("")) {
            ToastUtil.showShortToast(this, getString(R.string.lb_prompt_user_pwd));
            etUserPassword.requestFocus();
            return false;
        }
        LoginAsyncTask loginAsyncTask = new LoginAsyncTask();
        loginAsyncTask.execute(sUserNo,sPassword);
        return true;
    }
    private void saveUserInfo(String userNo,String pwd){
        User user = new User();
        user.setUserId(userNo);
        user.setUserName(userNo);
        user.setPassword(pwd);
        user.setLoginTime(DateUtils.getDateTime());
        user.setCurAccount("Y");
        DBUtil dbUtil = new DBUtil(context);
        dbUtil.saveUser(user);
        dbUtil.updateUserStatus(user);
        SharedPreferencesUtils.saveValue(getBaseContext(), "sUserNo", userNo);
        SharedPreferencesUtils.saveValue(getBaseContext(), "sCurrentUserNo",userNo);//验证权限账号
    }
    private void loginSuccess(){
        Intent functionIntent = new Intent(context, HomeActivity.class);
        functionIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(functionIntent);
    }
    //登陆-数据处理
    public class LoginAsyncTask extends AsyncTask<String, Void, String> {
        String sUserNo;
        String sPassword;

        @Override
        protected void onPreExecute() {
            sFactoryNo = MyApplication.sFactoryNo;
            sLanguage = MyApplication.sLanguage;
            sMac = MyApplication.sMac;
            sClientIp = MyApplication.sClientIp;
            TextViewUtils.setClickableShow(btnLogin,false);
            TextViewUtils.setClickableShow(btnSetting,false);
        }

        @Override
        protected String doInBackground(String... params) {
            String methodname = "GetLoginUserInfo";
            JSONObject jsonObject = new JSONObject();
            sUserNo = params[0];
            sPassword = params[1];
            try {
                jsonObject.put("sUserNo", sUserNo);
                jsonObject.put("sPassword", sPassword);
                jsonObject.put("sModelNo", "");
                jsonObject.put("sControlNo", "");
                jsonObject.put("sFactoryNo", sFactoryNo);
                jsonObject.put("sLanguage", sLanguage);
                jsonObject.put("sMac", sMac);
                jsonObject.put("sClientIp", sClientIp);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return GetData.getDataByJson(jsonObject.toString(), methodname);
        }

        @Override
        protected void onPostExecute(String string) {
            TextViewUtils.setClickableShow(btnLogin,true);
            TextViewUtils.setClickableShow(btnSetting,true);
            if (string.equals("") || string.equals(null)) {
                DialogUtil.showStatusDialog(context, getString(R.string.lb_prompt), getString(R.string.lb_interface_returns_failed));
                return;
            } else {
                try {
                    JSONArray jsonArray = new JSONArray(string);
                    JSONObject item = jsonArray.getJSONObject(0);
                    String sStatus = item.getString("status");
                    String sMsg = item.getString("msg");
                    if (sStatus.equalsIgnoreCase("OK")) {
                        saveUserInfo(sUserNo,sPassword);
                        loginSuccess(); //登录成功
                    } else if (sStatus.equalsIgnoreCase("ERROR")) {
                        DialogUtil.showStatusDialog(context, "NG", sMsg);
                    } else {
                        DialogUtil.showStatusDialog(context, "NG", sMsg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    DialogUtil.showStatusDialog(context, "", e.getMessage());
                }
                isScaning = false;
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mScanReceiver!=null){
            unregisterReceiver(mScanReceiver);
        }
    }
}
