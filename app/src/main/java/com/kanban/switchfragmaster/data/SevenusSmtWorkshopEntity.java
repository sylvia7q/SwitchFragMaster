package com.kanban.switchfragmaster.data;

/**
 * Created by LQ on 2017/2/8.
 */

public class SevenusSmtWorkshopEntity {
    private String sLineNo; //线别
    private String sCustomerNo; //客户
    private String sProductNo; //客户
    private String so;  //制令工单
    private String sMoPlanQty;   //制令工计划数量
    private String sMoFinishingRate;   // 工单完成率
    private String sActivation;   //稼动率
    private String sProductionState; //生产状态
    private String sProductionStateName; //生产状态名称

    public String getsProductNo() {
        return sProductNo;
    }

    public void setsProductNo(String sProductNo) {
        this.sProductNo = sProductNo;
    }

    public String getsProductionStateName() {
        return sProductionStateName;
    }

    public void setsProductionStateName(String sProductionStateName) {
        this.sProductionStateName = sProductionStateName;
    }

    public String getsLineNo() {
        return sLineNo;
    }

    public void setsLineNo(String sLineNo) {
        this.sLineNo = sLineNo;
    }

    public String getsCustomerNo() {
        return sCustomerNo;
    }

    public void setsCustomerNo(String sCustomerNo) {
        this.sCustomerNo = sCustomerNo;
    }

    public String getSo() {
        return so;
    }

    public void setSo(String so) {
        this.so = so;
    }

    public String getsMoPlanQty() {
        return sMoPlanQty;
    }

    public void setsMoPlanQty(String sMoPlanQty) {
        this.sMoPlanQty = sMoPlanQty;
    }

    public String getsMoFinishingRate() {
        return sMoFinishingRate;
    }

    public void setsMoFinishingRate(String sMoFinishingRate) {
        this.sMoFinishingRate = sMoFinishingRate;
    }
    public String getsProductionState() {
        return sProductionState;
    }

    public void setsProductionState(String sProductionState) {
        this.sProductionState = sProductionState;
    }
}
