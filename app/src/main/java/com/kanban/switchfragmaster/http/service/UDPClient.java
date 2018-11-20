package com.kanban.switchfragmaster.http.service;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;


import com.kanban.switchfragmaster.data.MyApplication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UDPClient implements Runnable {

    private final static String TAG = UDPClient.class.getName();
    private int udpPort = MyApplication.sSocketPort;
    private String hostIp = MyApplication.sSocketIp;
    private DatagramSocket sendSocket = null;
    private DatagramSocket receiveSocket = null;
    private DatagramPacket packetSend = null;
    private DatagramPacket packetRcv= null ;
    private boolean udpLife = true; //udp生命线程
    private byte[] msgRcv = new byte[2048]; //接收消息
    private Handler handler;
    private String RcvMsg;
    public UDPClient(){
        super();
    }

    //返回udp生命线程因子是否存活
    public boolean isUdpLife(){
        if (udpLife){
            return true;
        }

        return false;
    }

    //更改UDP生命线程因子
    public void setUdpLife(boolean b){
        udpLife = b;
    }

    //发送消息
    public String send(String msgSend){

        try {
            sendSocket = new DatagramSocket();
            InetAddress hostAddress = InetAddress.getByName(hostIp);
            packetSend = new DatagramPacket(msgSend.getBytes() , msgSend.getBytes().length,hostAddress,udpPort);
            receiveSocket.send(packetSend);
        } catch (UnknownHostException e) {
            Log.e(TAG,"客户端未找到服务器");
            sendSocket.close();
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
            Log.e(TAG,"客户端建立发送数据报失败");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,"客户端发送失败");
        }
        return msgSend;
    }

    @Override
    public void run() {
        try {

            receiveSocket = new DatagramSocket();
            receiveSocket.setSoTimeout(3000);
            packetRcv = new DatagramPacket(msgRcv,msgRcv.length);
        } catch (SocketException e) {
            Log.e(TAG,"建立接收数据报失败");
            e.printStackTrace();
        }
        while (udpLife){
            try {
                receiveSocket.receive(packetRcv);
                byte[] bytes = packetRcv.getData();
                int len = packetRcv.getLength();
                RcvMsg = new String(bytes,0,len);
                Log.e(TAG,"RcvMsg =  "  + RcvMsg);
                if(!TextUtils.isEmpty(RcvMsg.trim())){//将收到的消息发给主界面
                    Message msg = Message.obtain();
                    msg.what = 1;
                    msg.obj = RcvMsg;
                    handler.sendMessage(msg);
                }

            } catch (UnknownHostException e) {
                Log.e(TAG,"server未找到服务器 UnknownHostException");
                e.printStackTrace();
            } catch (SocketException e) {
                e.printStackTrace();
                Log.e(TAG,"server建立发送数据报失败");
            }catch (IOException e){
                e.printStackTrace();
            }
        }


        if(sendSocket!=null){
            sendSocket.close();
              Log.e(TAG,"UDP监听关闭");
        }
    }
    public void setHomeHandler(Handler handler) {
        this.handler = handler;
    }
}
