package com.kanban.switchfragmaster.data;

import java.io.Serializable;

/**
 * Created by LQ on 2017/8/15.
 * 供料器位置抛料率图表信息
 */

public class FeedersMaterielInfo implements Serializable {

    private String feeders_equipmentCoding;//设备编码
    private String feeders_lineNum;//行号
    private String feeders_location;//位置
    private String feeders_absorbNum;//吸取数
    private String feeders_rejectNum;//抛料数量
    private String feeders_rejectRate;//抛料率


    public FeedersMaterielInfo() {
    }

    public FeedersMaterielInfo(String feeders_equipmentCoding, String feeders_lineNum, String feeders_location, String feeders_absorbNum, String feeders_rejectNum, String feeders_rejectRate) {
        this.feeders_equipmentCoding = feeders_equipmentCoding;
        this.feeders_lineNum = feeders_lineNum;
        this.feeders_location = feeders_location;
        this.feeders_absorbNum = feeders_absorbNum;
        this.feeders_rejectNum = feeders_rejectNum;
        this.feeders_rejectRate = feeders_rejectRate;
    }

    public String getFeeders_equipmentCoding() {
        return feeders_equipmentCoding;
    }

    public void setFeeders_equipmentCoding(String feeders_equipmentCoding) {
        this.feeders_equipmentCoding = feeders_equipmentCoding;
    }

    public String getFeeders_lineNum() {
        return feeders_lineNum;
    }

    public void setFeeders_lineNum(String feeders_lineNum) {
        this.feeders_lineNum = feeders_lineNum;
    }

    public String getFeeders_location() {
        return feeders_location;
    }

    public void setFeeders_location(String feeders_location) {
        this.feeders_location = feeders_location;
    }

    public String getFeeders_absorbNum() {
        return feeders_absorbNum;
    }

    public void setFeeders_absorbNum(String feeders_absorbNum) {
        this.feeders_absorbNum = feeders_absorbNum;
    }

    public String getFeeders_rejectRate() {
        return feeders_rejectRate;
    }

    public void setFeeders_rejectRate(String feeders_rejectRate) {
        this.feeders_rejectRate = feeders_rejectRate;
    }

    public String getFeeders_rejectNum() {
        return feeders_rejectNum;
    }

    public void setFeeders_rejectNum(String feeders_rejectNum) {
        this.feeders_rejectNum = feeders_rejectNum;
    }
}
