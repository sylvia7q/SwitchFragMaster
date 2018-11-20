package com.kanban.switchfragmaster.data;

import java.util.List;

public class Result {
    private String status;
    private String msg;
    private List<MessageInfo> messageInfos;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<MessageInfo> getMessageInfos() {
        return messageInfos;
    }

    public void setMessageInfos(List<MessageInfo> messageInfos) {
        this.messageInfos = messageInfos;
    }

    @Override
    public String toString() {
        return "ResultInfo{" +
                "status='" + status + '\'' +
                ", msg='" + msg + '\'' +
                ", messageInfos=" + messageInfos +
                '}';
    }
}
