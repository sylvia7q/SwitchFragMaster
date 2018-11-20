package com.kanban.switchfragmaster.data;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

@Table(name="T_ChatMsgInfo")
public class ChatMsgInfo {

    public static final int  RECEIVE = 0;//对方发来的信息
    public static final int SEND = 1;//自己发来的信息

    @Id(column = "id")
    private int id;

    @Column(column = "userId")
    private String userId;

    @Column(column = "userName")
    private String userName;

    @Column(column = "content")
    private String content;//聊天内容

    @Column(column = "sendTime")
    private String sendTime;//发送时间

    @Column(column = "messageType")
    private int messageType; //内容类型

    public ChatMsgInfo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    @Override
    public String toString() {
        return "ChatMsgInfo{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", content='" + content + '\'' +
                ", sendTime='" + sendTime + '\'' +
                ", messageType=" + messageType +
                '}';
    }
}
