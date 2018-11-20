package com.kanban.switchfragmaster;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;


import com.kanban.switchfragmaster.data.MyApplication;
import com.kanban.switchfragmaster.data.UDPData;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class ReportService extends Service {
    public static final String REPORT_REQUEST_SERVER = "REPORT_REQUEST_SERVER";
    private ReportReceiver reportReceiver;

    private WifiManager wifiManager = null;  //用来检测自身网络是否连接
    private boolean isConnected;
    private static int errorNum = 0;//用于一次连接不成功后记录，并且，记录到4次后再对程序进行提示
    private static final String TAG = "ServerHeartService";
    private String getMsg = "";
    private int _getPort = MyApplication.sSocketPort;
    private int _sendPort = MyApplication.sSocketPort;
    private static boolean ServerFlag = false;
    private int num = 1;
    private Thread thread;
    private DatagramSocket sendSocket = null;
    private DatagramSocket receiveSocket = null;
    private boolean passFlag = false;
    private int length = 1;
    private Handler handler;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        reportReceiver = new ReportReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(reportReceiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        //1.自测wifi是否正常
                        isConnected = isWifiConnect();
                        if (isConnected == false) {
                            if (length == 1) {
                                //不能直接在Service上通知，必须经过Handler去提示
//                                Message heartMessage = HomeActivity.myH.obtainMessage();
//                                heartMessage.arg1 = 1;
//                                ServerTestingActivity.heartHandler.sendMessage(heartMessage);
                                //因为是多线程，防止多次发送Handler
                                length++;
                            }
                            break;
                        }
                        SendMsg();
                        GetMsg();
                        //用于调试，可删除
                        String msg = "";
                        if (ServerFlag == false) {
                            msg = "false";
                        } else {
                            msg = "true";
                        }
                        Log.d(TAG, msg);
                        //记录未接收到客户端发来的消息+1


                        if (ServerFlag == false) {
                            errorNum++;
                        } else {
                            errorNum = 0;
                        }
                        if (errorNum == 4) {
//                            ServerHeartMsg("客户端网络故障！");
                            break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override

    public void onStart(Intent intent, int startId) {
        super.onStart(intent,
                startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (reportReceiver != null) {
            unregisterReceiver(reportReceiver);
        }
        Intent intentForService = new Intent(this, ReportService.class);
        intentForService.setAction(ReportService.REPORT_REQUEST_SERVER);
        startService(intentForService);
    }
    private boolean isWifiConnect(){
        try{
            ConnectivityManager conManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo mWifi = conManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            return mWifi.isConnected();
        }catch(Exception e){
            return false;
        }
    }

    //将控制信息已广播的形式发送到Activity.
    private void ServerHeartMsg(String str) {
        final Intent intent = new Intent();
//        intent.setAction(Constants.Controller.UDP_BORADCAST);
        intent.putExtra(UDPData.UDP_HEART_ERROR, str);
        sendBroadcast(intent);
    }

    public void GetMsg(){
        try {
            receiveSocket = new DatagramSocket(_getPort);
            Log.d(TAG, "Server连接到端口");
            byte data[] = new byte[1024];
            DatagramPacket packet = new DatagramPacket(data, data.length);
            if (passFlag == false) {
                receiveSocket.setSoTimeout(100000);
            } else {
                receiveSocket.setSoTimeout(3000);
            }
            receiveSocket.receive(packet);
//            controlFlag = true;
            passFlag = true;
            Log.d(TAG, "Server接收到信息");
            getMsg = new String(packet.getData(),
                    packet.getOffset(), packet.getLength());
            //将收到的消息发给主界面
            Message msg = Message.obtain();
            msg.what = 1;
            msg.obj = getMsg;
            handler.sendMessage(msg);

            receiveSocket.close();
        } catch (SocketException e) {
            Log.d(TAG, "Server未找到服务器");
            receiveSocket.close();
//            controlFlag = false;
            e.printStackTrace();
        } catch (UnknownHostException e) {
            Log.d(TAG, "Server未连接到服务器");
            receiveSocket.close();
//            controlFlag = false;
            e.printStackTrace();

        } catch (IOException e) {
            Log.d(TAG, "Server消息未接收成功");
            receiveSocket.close();
//            controlFlag = false;
            e.printStackTrace();
        }
    }
    public void setHomeHandler(Handler handler) {
        this.handler = handler;
    }

    public void SendMsg(){
        try {
            Thread.sleep(1000);
            sendSocket = new DatagramSocket();
            InetAddress serverAddress = InetAddress.getByName(MyApplication.sSocketIp);
            String str = "服务端网络故障！";
            byte data[] = str.getBytes();
            DatagramPacket packet = new DatagramPacket(data,data.length, serverAddress, _sendPort);
            sendSocket.send(packet);
            Log.d(TAG, "Server消息发送成功");
            sendSocket.close();
        } catch (SocketException e) {
            Log.d(TAG, "Server未找到服务器");
            sendSocket.close();
            e.printStackTrace();
        } catch (UnknownHostException e) {
            Log.d(TAG, "Server未连接到服务器");
            sendSocket.close();
            e.printStackTrace();
        } catch (IOException e) {
            Log.d(TAG, "Server消息未发送成功");
            sendSocket.close();
            e.printStackTrace();
        } catch (InterruptedException e) {
            Log.d(TAG, "Sleep线程");
            sendSocket.close();
            e.printStackTrace();
        }

    }
}


