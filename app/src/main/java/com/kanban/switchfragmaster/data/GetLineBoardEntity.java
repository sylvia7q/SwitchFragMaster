package com.kanban.switchfragmaster.data;

/**
 * [获取表头制令单信息(生产进度、完成率、总产时间、异常时间、直通率)]
 * Created by LQ on 2017/10/18.
 */

public class GetLineBoardEntity {
    private String sWo; //制令单
    private String sPartNo; //产品号
    private String sPartDesc; //产品描述
    private String sQtyPlan; //计划数量
    private String sBoardCount; //拼版数量
    private String sSide; //板面
    private String sPlanProcessingDate; //计划开始时间
    private String sPlanFinishedDate; //计划结束时间
    private String sCustomerNo; //客户信息
    private String sNextWo; //下一个制令单  (查询同线已检验制令单排序)
    private String sWorkingSchedule; //生产进度
    private String sFinishingRate; //完成率
    private String sCO; //总产时间
    private String sOE; //异常时间
    private String sFTQ; //直通率

    public String getsWo() {
        return sWo;
    }

    public void setsWo(String sWo) {
        this.sWo = sWo;
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

    public String getsQtyPlan() {
        return sQtyPlan;
    }

    public void setsQtyPlan(String sQtyPlan) {
        this.sQtyPlan = sQtyPlan;
    }

    public String getsBoardCount() {
        return sBoardCount;
    }

    public void setsBoardCount(String sBoardCount) {
        this.sBoardCount = sBoardCount;
    }

    public String getsSide() {
        return sSide;
    }

    public void setsSide(String sSide) {
        this.sSide = sSide;
    }

    public String getsPlanProcessingDate() {
        return sPlanProcessingDate;
    }

    public void setsPlanProcessingDate(String sPlanProcessingDate) {
        this.sPlanProcessingDate = sPlanProcessingDate;
    }

    public String getsPlanFinishedDate() {
        return sPlanFinishedDate;
    }

    public void setsPlanFinishedDate(String sPlanFinishedDate) {
        this.sPlanFinishedDate = sPlanFinishedDate;
    }

    public String getsCustomerNo() {
        return sCustomerNo;
    }

    public void setsCustomerNo(String sCustomerNo) {
        this.sCustomerNo = sCustomerNo;
    }

    public String getsNextWo() {
        return sNextWo;
    }

    public void setsNextWo(String sNextWo) {
        this.sNextWo = sNextWo;
    }

    public String getsWorkingSchedule() {
        return sWorkingSchedule;
    }

    public void setsWorkingSchedule(String sWorkingSchedule) {
        this.sWorkingSchedule = sWorkingSchedule;
    }

    public String getsFinishingRate() {
        return sFinishingRate;
    }

    public void setsFinishingRate(String sFinishingRate) {
        this.sFinishingRate = sFinishingRate;
    }

    public String getsCO() {
        return sCO;
    }

    public void setsCO(String sCO) {
        this.sCO = sCO;
    }

    public String getsOE() {
        return sOE;
    }

    public void setsOE(String sOE) {
        this.sOE = sOE;
    }

    public String getsFTQ() {
        return sFTQ;
    }

    public void setsFTQ(String sFTQ) {
        this.sFTQ = sFTQ;
    }
}
