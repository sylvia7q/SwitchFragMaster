package com.kanban.switchfragmaster;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

import com.kanban.switchfragmaster.http.service.UDPClient;


public class ReportReceiver extends BroadcastReceiver {
    private static final String TAG = ReportReceiver.class.getSimpleName();

    private static final String ACTION_RECEIVER_BOOT = "com.topcee.report.receiver.action.boot";
    private UDPClient client;
    private Context context;

    @Override
    public void onReceive(Context ctx, Intent intent) {

        //因为只有连接失败才会发送广播，所以getStringExtra 一定有值，所以不需要去 getStringExtra == null去做判断
//        String  udpHeart = intent.getStringExtra(UDPData.UDP_HEART_ERROR);
        //msgHeart = udpHeart;
//        Toast.makeText(context, udpHeart,Toast.LENGTH_SHORT).show();

        if (intent != null) {
            Log.i(TAG, "action: " + intent.getAction());
            String action = intent.getAction();
            if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()) || ACTION_RECEIVER_BOOT.equals(action)){//开机
                boot(context);
            } else if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {//网络变化
                startService(context);
            }
        }
    }
    /**
     * 开机
     * @param context
     */
    private void boot(Context context){
        startService(context);
    }

    /**
     * 启动服务,注册广播
     * @param context
     */
    private void startService(Context context){

        Intent intentForService = new Intent(context, ReportService.class);
        intentForService.setAction(ReportService.REPORT_REQUEST_SERVER);
        context.startService(intentForService);
    }
}
