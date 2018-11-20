package com.kanban.switchfragmaster.data;


import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

@Table(name="T_MessageInfo")
public class MessageInfo{

    @Id(column = "messageType")
    @NoAutoIncrement
    private int messageType;

    @Column(column = "userId")
    private String userId;

    @Column(column = "userName")
    private String userName;

    @Column(column = "content")
    private String content;

    @Column(column = "sendTime")
    private String sendTime;

    @Column(column = "unreadCount")
    private int unreadCount;


    public MessageInfo() {
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

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    @Override
    public String toString() {
        return "MessageInfo{" +
                "messageType=" + messageType +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", content='" + content + '\'' +
                ", sendTime='" + sendTime + '\'' +
                ", unreadCount=" + unreadCount +
                '}';
    }
}
