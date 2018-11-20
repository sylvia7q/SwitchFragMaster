package com.kanban.switchfragmaster.data;


/**
 * Created by LQ on 2016/6/7.
 */
public class WoInfoWhEntity {

    private String sLineNo;   //线别
    private String sLineStatusName;  //线体状态
    private String sWo;  //制令单
    private String sWoPlanQty;   //制令单计划数量
    private String sIsPrepareSufficient;   //是否齐套
    private String sQtyTotal;   //完成数

    public String getLineNo() {
        return sLineNo;
    }

    public void setLineNo(String sLineNo) {
        this.sLineNo = sLineNo;
    }

    public String getLineStatusName() {
        return sLineStatusName;
    }

    public void setLineStatusName(String sLineStatusName) {
        this.sLineStatusName = sLineStatusName;
    }

    public String getWo() {
        return sWo;
    }

    public void setWo(String sWo) {
        this.sWo = sWo;
    }

    public String getWoPlanQty() {
        return sWoPlanQty;
    }

    public void setWoPlanQty(String sWoPlanQty) {
        this.sWoPlanQty = sWoPlanQty;
    }

    public String getIsPrepareSufficient() {
        return sIsPrepareSufficient;
    }

    public void setIsPrepareSufficient(String sIsPrepareSufficient) {
        this.sIsPrepareSufficient = sIsPrepareSufficient;
    }

    public String getQtyTotal() {
        return sQtyTotal;
    }

    public void setQtyTotal(String sQtyTotal) {
        this.sQtyTotal = sQtyTotal;
    }
}
