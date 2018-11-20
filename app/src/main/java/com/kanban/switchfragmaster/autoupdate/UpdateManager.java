package com.kanban.switchfragmaster.autoupdate;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;


import com.kanban.switchfragmaster.R;
import com.kanban.switchfragmaster.data.MyApplication;
import com.kanban.switchfragmaster.http.service.GetData;
import com.kanban.switchfragmaster.utils.AppUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * [在此写上功能描述]
 * Created by YANGT on 2017/11/15.
 */

public class UpdateManager {
    private Context mContext;
    boolean blShowNoUpdate;
    //提示语
    private Dialog noticeDialog;
    private Dialog downloadDialog;
    /* 下载包安装路径 */
    private static String savePath = android.os.Environment.getExternalStorageDirectory().getPath() + File.separator;
    //APK名称
    static String sApkName = MyApplication.sApkName;
    private static String saveFileName = savePath + sApkName;
    private String sDownloadUrl = MyApplication.sDownloadUrl; //APK下载地址
    /* 进度条与通知ui刷新的handler和msg常量 */
    private ProgressBar mProgress;

    private static final int DOWN_UPDATE = 1;

    private static final int DOWN_OVER = 2;

    private int progress;

    private Thread downLoadThread;

    private boolean interceptFlag = false;

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:   //返回版本信息
                    String show_result = (String)msg.obj;
                    try {
                        JSONArray jsonArray = new JSONArray(show_result);
                        JSONObject item = jsonArray.getJSONObject(0);
                        String sStatus = item.getString("status");
                        String sIsUpdate= item.getString("IsUpdate");
                        if(sStatus.equals("OK")) {
                            if (sIsUpdate.equals("Y")) {
                                showNoticeDialog(); //需要更新
                            }
                            else {
                                if(blShowNoUpdate) {
                                    notNewVersionShow(); //未需更新
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case DOWN_UPDATE:
                    mProgress.setProgress(progress);
                    break;
                case DOWN_OVER:
                    installApk();
                    break;
                default:
                    break;
            }
        }

    };

    /**
     * 检查APK是否有更新版本
     * @return
     */
    private void isUpdate(final boolean blShowNoUpdateTemp)
    {
        blShowNoUpdate = blShowNoUpdateTemp;

        final String version_Str =  Integer.toString(AppUtil.getVersionCode(mContext));
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {

                JSONObject jsonObj = new JSONObject();
                try {
                    jsonObj.put("sAppType", MyApplication.sApkVersionXml);
                    jsonObj.put("sClientVersionCode",version_Str);
                }
                catch (JSONException ex) {
                    ex.printStackTrace();
                }
                String show_result = GetData.getInstance(mContext).getDataByJson(jsonObj.toString(), MyApplication.sCheckVersion);

                //需要数据传递，用下面方法；
                Message msg =new Message();
                msg.obj = show_result;//可以是基本类型，可以是对象，可以是List、map等；
                mHandler.sendMessage(msg);
            }
        });
        t.start();

    }

    public UpdateManager(Context context) {
        this.mContext = context;
//        getDomain(); //初始化数
    }

    //外部接口让主Activity调用
    public void checkUpdateInfo(boolean blShowNoUpdate){
        isUpdate(blShowNoUpdate); //检查APK是否有更新版本
    }


    private void showNoticeDialog(){
        String updateMsg = mContext.getString(R.string.lb_have_the_new_version_on_the_server_please_update);
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mContext.getString(R.string.lb_software_version_update));
        builder.setMessage(updateMsg);
        builder.setPositiveButton(mContext.getString(R.string.lb_now_update), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showDownloadDialog();

            }
        });
        builder.setNegativeButton(mContext.getString(R.string.lb_later_update), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        noticeDialog = builder.create();
        noticeDialog.show();
    }

    private void notNewVersionShow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mContext.getString(R.string.lb_software_is_the_latest_version));
        int verCode = AppUtil.getVersionCode(mContext);
        String verName = AppUtil.getVersionName(mContext);
        StringBuffer sb = new StringBuffer();
        sb.append(mContext.getString(R.string.lb_the_current_version)+":");
        sb.append(verName);

        sb.append(";\n"+mContext.getString(R.string.lb_is_the_latest_version_no_need_to_update)+"!");
        builder.setMessage(sb.toString()); ///设置内容
        builder.setPositiveButton(mContext.getString(R.string.lb_btn_confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        noticeDialog = builder.create();
        noticeDialog.show();
    }



    private void showDownloadDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mContext.getString(R.string.lb_software_version_update));

        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.lb_activity_update_manager, null);
        mProgress = (ProgressBar)v.findViewById(R.id.progress);

        builder.setView(v);
        builder.setNegativeButton(mContext.getString(R.string.lb_btn_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                interceptFlag = true;
            }
        });
        downloadDialog = builder.create();
        downloadDialog.show();

        downloadApk(); //下载_apk安装包
    }

//    public final void getDomain() {
//        //APK名称
//        String sApkName = MyApplication.sApkName;
//        saveFileName = savePath + sApkName;
//        //APK下载地址
//        sDownloadUrl = MyApplication.sDownloadUrl;
//    }

    private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL(sDownloadUrl);

                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();

                File file = new File(savePath);
                if(!file.exists()){
                    file.mkdir();
                }
                String apkFile = saveFileName;
                File ApkFile = new File(apkFile);
                FileOutputStream fos = new FileOutputStream(ApkFile);
                int count = 0;
                byte buf[] = new byte[1024];
                do{
                    int numread = is.read(buf);
                    count += numread;
                    progress =(int)(((float)count / length) * 100);
                    //更新进度
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    if(numread <= 0){
                        //下载完成通知安装
                        mHandler.sendEmptyMessage(DOWN_OVER);
                        break;
                    }
                    fos.write(buf,0,numread);
                }while(!interceptFlag);//点击取消就停止下载.

                fos.close();
                is.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch(IOException e){
                e.printStackTrace();
            }

        }
    };

    //下载_apk安装包
    private void downloadApk(){
        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }

    //安装_apk
    private void installApk(){
        File apkfile = new File(saveFileName);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        mContext.startActivity(i);

    }
}
