package com.kanban.switchfragmaster.data;

/**
 * Created by LQ on 2017/8/15.
 * 看板异常信息
 */

public class ExceptionShowInfo {

    private String exception_type;//异常类型
    private String exception_info;//异常信息

    public ExceptionShowInfo() {
    }

    public ExceptionShowInfo(String exception_type, String exception_info) {
        this.exception_type = exception_type;
        this.exception_info = exception_info;
    }

    public String getException_type() {
        return exception_type;
    }

    public void setException_type(String exception_type) {
        this.exception_type = exception_type;
    }

    public String getException_info() {
        return exception_info;
    }

    public void setException_info(String exception_info) {
        this.exception_info = exception_info;
    }
}
