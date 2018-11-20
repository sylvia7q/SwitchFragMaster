package com.kanban.switchfragmaster.data;

import android.app.Application;
import android.graphics.Typeface;

public class MyApplication extends Application {
    public static String sUrl =  ""; //WebService地址
    public static String sDownloadUrl =  ""; //APK下载地址
    public static String sVersionXml =  ""; //APK版本地址
    public static String sApkName =  ""; //APK名称
    public static String sApkVersionXml = ""; //APK版本XML节点(验证APK版本时传参)

    public static String sCheckVersion = ""; //APK版本Check方法(验证APK版本时传入方法名)

    public static String sMesUser = ""; //客户编码(客户代码)
    public static String sFactoryNo = "0"; //工厂代码：0
    public static String sLanguage = "S"; //(默认S)语言：S/T/E
    public static String sLoginUserNo = "";
    public static String sClientIp = "127.0.0.1"; //IP地址
    public static String sMac = "0"; //MAC地址

    public static String sCurrentUserNo="";//验证权限账号
    public static String sUserNo=""; //用户登陆账号
    public static String sUserName =""; //用户登陆名称


    public static int sSocketPort = 60000; //socket端口
    public static String sSocketIp ="192.168.1.36"; //socketIp
    public static String sLineNo =""; //
    public static boolean isBack =  false; //
    //字体
    public static Typeface typeFaceSTZHONGS;  //华文中宋
    public static boolean blPauseScroll=false;    //仓库看板是否暂停滚动

}
