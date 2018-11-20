package com.kanban.switchfragmaster.http.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * [广播接收]
 * Created by YANGT on 2017/11/13.
 */

public class ScanReceiver extends BroadcastReceiver {

    private static MessageListener mMessageListener;
    public static final String SCAN_M60_RECEIVED_ACTION = "com.mobilead.tools.action.scan_result";
    public static final String SCAN_I6002S_RECEIVED_ACTION = "urovo.rcv.message";
    public static final String SCAN_I6002A_RECEIVED_ACTION = "android.intent.ACTION_DECODE_DATA";

    public ScanReceiver() {
        super();
    }

    //重写父类onReceive()
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(SCAN_M60_RECEIVED_ACTION)) {
            String content = intent.getStringExtra("decode_rslt").trim();
            Log.i("debug", "----barcodeStr--" + content);
            mMessageListener.onReceived(content);
        }
        else if(intent.getAction().equals(SCAN_I6002S_RECEIVED_ACTION))
        {
            //获得到条码值
            byte[] barcode = intent.getByteArrayExtra("barocode");
            //获得到条码长度
            int barocodelen = intent.getIntExtra("length", 0);
            //获得到条码类型
            byte temp = intent.getByteExtra("barcodeType", (byte) 0);
            Log.i("debug", "----codetype--" + temp);
            String barcodeStr = new String(barcode, 0, barocodelen);
            Log.i("debug", "----barcodeStr--" + barcodeStr);
            mMessageListener.onReceived(barcodeStr);
        }
        else if(intent.getAction().equals(SCAN_I6002A_RECEIVED_ACTION))
        {
            //获得到条码值
            String content = intent.getStringExtra("barcode_string").trim();
            Log.i("debug", "----barcodeStr--" + content);
            mMessageListener.onReceived(content);
        } else {}
        abortBroadcast();

    }

    //回调接口
    public interface MessageListener {
        public void onReceived(String message);
    }

    public void setOnReceivedMessageListener(MessageListener messageListener) {
        this.mMessageListener = messageListener;
    }
}
