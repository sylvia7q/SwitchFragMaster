package com.kanban.switchfragmaster.data;

import java.io.Serializable;

/**
 * Created by LQ on 2017/8/7.
 * 生产图表表格信息
 */

public class ProductTableInfo implements Serializable {

    private String time;
    private String really;
    private String standand;

    public ProductTableInfo() {
    }

    public ProductTableInfo(String time, String really, String standand) {
        this.time = time;
        this.really = really;
        this.standand = standand;
    }

    public String getTime() {
        return time;
    }

    public String getReally() {
        return really;
    }

    public String getStandand() {
        return standand;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setReally(String really) {
        this.really = really;
    }

    public void setStandand(String standand) {
        this.standand = standand;
    }

    @Override
    public String toString() {
        return "Info{" +
                "time='" + time + '\'' +
                ", really='" + really + '\'' +
                ", standand='" + standand + '\'' +
                '}';
    }
}
