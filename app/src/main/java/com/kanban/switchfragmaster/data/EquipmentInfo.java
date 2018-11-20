package com.kanban.switchfragmaster.data;

import java.io.Serializable;

/**
 * Created by LQ on 2017/11/27.
 */

public class EquipmentInfo implements Serializable {
    private String time;//时段
    private String runPercent;//运行百分率
    private String stopPercent;//停止百分率
//    private String sMachineNo;//设备代码
//    private String sMachineName;//设备名字

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRunPercent() {
        return runPercent;
    }

    public void setRunPercent(String runPercent) {
        this.runPercent = runPercent;
    }

    public String getStopPercent() {
        return stopPercent;
    }

    public void setStopPercent(String stopPercent) {
        this.stopPercent = stopPercent;
    }

    /*public String getsMachineNo() {
        return sMachineNo;
    }

    public void setsMachineNo(String sMachineNo) {
        this.sMachineNo = sMachineNo;
    }

    public String getsMachineName() {
        return sMachineName;
    }

    public void setsMachineName(String sMachineName) {
        this.sMachineName = sMachineName;
    }*/
}
