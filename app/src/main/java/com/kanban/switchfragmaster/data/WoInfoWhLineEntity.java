package com.kanban.switchfragmaster.data;

/**
 * Created by LQ on 2016-08-02.
 */
public class WoInfoWhLineEntity {
    private String sEquipmentId;  //设备
    private String sStration;  //站位
    private String sPartNo;  //物料号
    private String sTotalNO;  //需求总量
    private String sPartStatus;  //物料状态
    private String sQty;  //已用数量
    private String sQuantity;  //拉存数量
    private String sMinutes;  //可用分钟

    public String getsEquipmentId() {
        return sEquipmentId;
    }

    public void setsEquipmentId(String sEquipmentId) {
        this.sEquipmentId = sEquipmentId;
    }

    public String getsStration() {
        return sStration;
    }

    public void setsStration(String sStration) {
        this.sStration = sStration;
    }

    public String getsPartNo() {
        return sPartNo;
    }

    public void setsPartNo(String sPartNo) {
        this.sPartNo = sPartNo;
    }

    public String getsTotalNO() {
        return sTotalNO;
    }

    public void setsTotalNO(String sTotalNO) {
        this.sTotalNO = sTotalNO;
    }

    public String getsPartStatus() {
        return sPartStatus;
    }

    public void setsPartStatus(String sPartStatus) {
        this.sPartStatus = sPartStatus;
    }

    public String getsQty() {
        return sQty;
    }

    public void setsQty(String sQty) {
        this.sQty = sQty;
    }

    public String getsQuantity() {
        return sQuantity;
    }

    public void setsQuantity(String sQuantity) {
        this.sQuantity = sQuantity;
    }

    public String getsMinutes() {
        return sMinutes;
    }

    public void setsMinutes(String sMinutes) {
        this.sMinutes = sMinutes;
    }
}
