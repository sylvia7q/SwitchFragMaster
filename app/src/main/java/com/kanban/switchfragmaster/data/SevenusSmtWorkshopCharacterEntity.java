package com.kanban.switchfragmaster.data;

/**
 * Created by LQ on 2017/2/8.
 */

public class SevenusSmtWorkshopCharacterEntity {
    private String sLineNo; //线别
    private String sRty; //RTY
    private String sHrowingRate1;  //1#抛料率
    private String sHrowingRate2;  //2#抛料率
    private String sHrowingRate3;  //3#抛料率
    private String sProductionState; //生产状态代码
    private String sProductionStateName; //生产状态名称

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

    public String getsRty() {
        return sRty;
    }

    public void setsRty(String sRty) {
        this.sRty = sRty;
    }

    public String getsHrowingRate2() {
        return sHrowingRate2;
    }

    public void setsHrowingRate2(String sHrowingRate2) {
        this.sHrowingRate2 = sHrowingRate2;
    }

    public String getsHrowingRate3() {
        return sHrowingRate3;
    }

    public void setsHrowingRate3(String sHrowingRate3) {
        this.sHrowingRate3 = sHrowingRate3;
    }

    public String getsProductionState() {
        return sProductionState;
    }

    public void setsProductionState(String sProductionState) {
        this.sProductionState = sProductionState;
    }

    public String getsHrowingRate1() {
        return sHrowingRate1;
    }

    public void setsHrowingRate1(String sHrowingRate1) {
        this.sHrowingRate1 = sHrowingRate1;
    }
}
