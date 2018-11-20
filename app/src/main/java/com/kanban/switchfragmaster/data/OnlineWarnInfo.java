package com.kanban.switchfragmaster.data;

/**
 * Created by LQ on 2017/8/14.
 * 物料预警信息
 */

public class OnlineWarnInfo{

    private String sMachine; //设备
    private String sFid; //站位
    private String sReelNo; //料卷号
    private String sPartNo; //物料号
    private String sPartDesc; //物料描述（规格）
    private String sConsumeQty; //用量
    private String sFeedQty; //上料数量
    private String sCurrentQty; //在线数量
    private String sQtyNeed; //需求数量
    private String sTime; //可用时间（剩余时间）
    private String sUsablePercent; //可用百分比

    public String getsMachine() {
        return sMachine;
    }

    public void setsMachine(String sMachine) {
        this.sMachine = sMachine;
    }

    public String getsFid() {
        return sFid;
    }

    public void setsFid(String sFid) {
        this.sFid = sFid;
    }

    public String getsReelNo() {
        return sReelNo;
    }

    public void setsReelNo(String sReelNo) {
        this.sReelNo = sReelNo;
    }

    public String getsPartNo() {
        return sPartNo;
    }

    public void setsPartNo(String sPartNo) {
        this.sPartNo = sPartNo;
    }

    public String getsPartDesc() {
        return sPartDesc;
    }

    public void setsPartDesc(String sPartDesc) {
        this.sPartDesc = sPartDesc;
    }

    public String getsConsumeQty() {
        return sConsumeQty;
    }

    public void setsConsumeQty(String sConsumeQty) {
        this.sConsumeQty = sConsumeQty;
    }

    public String getsFeedQty() {
        return sFeedQty;
    }

    public void setsFeedQty(String sFeedQty) {
        this.sFeedQty = sFeedQty;
    }

    public String getsCurrentQty() {
        return sCurrentQty;
    }

    public void setsCurrentQty(String sCurrentQty) {
        this.sCurrentQty = sCurrentQty;
    }

    public String getsQtyNeed() {
        return sQtyNeed;
    }

    public void setsQtyNeed(String sQtyNeed) {
        this.sQtyNeed = sQtyNeed;
    }

    public String getsTime() {
        return sTime;
    }

    public void setsTime(String sTime) {
        this.sTime = sTime;
    }

    public String getsUsablePercent() {
        return sUsablePercent;
    }

    public void setsUsablePercent(String sUsablePercent) {
        this.sUsablePercent = sUsablePercent;
    }
}
