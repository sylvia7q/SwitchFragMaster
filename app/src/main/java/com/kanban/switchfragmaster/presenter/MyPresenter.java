package com.kanban.switchfragmaster.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;


import com.kanban.switchfragmaster.data.ChatMsgInfo;
import com.kanban.switchfragmaster.data.MessageInfo;
import com.kanban.switchfragmaster.data.User;
import com.kanban.switchfragmaster.database.DBUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MyPresenter {

    private final static String TAG = MyPresenter.class.getName();
    private static MyPresenter mInitData;
    private Context mContext;

    public MyPresenter(Context mContext) {
        this.mContext = mContext;
    }

    public static MyPresenter getInstance(Context mContext) {
        if (mInitData == null) {
            mInitData = new MyPresenter(mContext);
        }
        return mInitData;
    }

    private Handler messageHandler;
    private Handler chatMessageHandler;
    private int unReadCount = 0;

    // 删除ArrayList中重复元素，保持顺序
    public static ArrayList<User> getSingleUser(ArrayList<User> list) {
        ArrayList<User> newList = new ArrayList();
        for (Iterator it = list.iterator(); it.hasNext(); ) {
            User user = (User) it.next();
            if (!newList.contains(user)) {
                newList.add(user);
            }
        }
        return newList;
    }


    /**
     * 连接socket成功之后解析json数据
     *
     * @param json
     */
    public void connSuccess(String json) {
        List<MessageInfo> mMessageEntities = new ArrayList<>();// messageFragment显示的列表
        List<ChatMsgInfo> chatList = new ArrayList<>();// messageFragment显示的列表
        DBUtil dbUtil = new DBUtil(mContext);
        User mUser = dbUtil.getLatestUser();
        if (!TextUtils.isEmpty(json.trim())) {
            try {
                JSONArray jsonArray = new JSONArray(json);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                String data = jsonObject.getString("messageInfo");
                JSONArray messageTypeArray = new JSONArray(data);
                for (int i = 0; i < messageTypeArray.length(); i++) {
                    JSONObject messageTypeJSONObject = messageTypeArray.getJSONObject(i);
                    int messageType = messageTypeJSONObject.getInt("messageType");
                    String chatInfo = messageTypeJSONObject.getString("ChatMsgInfo");
                    JSONArray chatArray = new JSONArray(chatInfo);
                    MessageInfo messageInfo = new MessageInfo();
                    messageInfo.setUserId(mUser.getUserId());
                    messageInfo.setUserName(mUser.getUserName());
                    messageInfo.setMessageType(messageType);
                    unReadCount = chatArray.length();
                    int count = dbUtil.getUnReadCountById(mUser.getUserId(), messageType);
                    messageInfo.setUnreadCount(unReadCount + count);//未读数 = 刚推送过来的消息数 + 已经推送过来但是没有读取的数量
                    mMessageEntities.add(messageInfo);//先存储消息类型，
                    for (int j = 0; j < chatArray.length(); j++) {
                        JSONObject chatObject = chatArray.getJSONObject(j);
                        String content = chatObject.getString("content");
                        String sendTime = chatObject.getString("sendTime");
                        ChatMsgInfo chatMsgInfo = new ChatMsgInfo();
                        chatMsgInfo.setUserId(mUser.getUserId());
                        chatMsgInfo.setUserName(mUser.getUserName());
                        chatMsgInfo.setContent(content);
                        chatMsgInfo.setSendTime(sendTime);
                        chatMsgInfo.setMessageType(messageType);
                        chatList.add(chatMsgInfo);//再存储消息类型的具体消息到数据库
                    }
                }
                if (chatList != null && chatList.size() >= 1) {
                    dbUtil.saveChat(chatList);

                }
                for (int i = 0; i < mMessageEntities.size(); i++) {
                    ChatMsgInfo chatMsgInfo = dbUtil.getFristChat(mUser.getUserId(), mMessageEntities.get(i).getMessageType());//根据消息类型获取具体内容并存放到数据库
                    if (chatMsgInfo != null) {
                        MessageInfo messageInfo = new MessageInfo();
                        messageInfo.setUserId(chatMsgInfo.getUserId());
                        messageInfo.setUserName(chatMsgInfo.getUserName());
                        messageInfo.setMessageType(chatMsgInfo.getMessageType());
                        messageInfo.setContent(chatMsgInfo.getContent());
                        messageInfo.setSendTime(chatMsgInfo.getSendTime());
                        messageInfo.setUnreadCount(mMessageEntities.get(i).getUnreadCount());
                        dbUtil.saveMessage(messageInfo);
                    }

                }
                //之所以先存储具体内容，再存储列表的消息类型，是为了每种类型每次都显示最新的消息在消息列表
                //存储成功之后发送消息队列，告知更新界面
                if (messageHandler != null) {
                    Message message = new Message();
                    message.what = 2;
                    messageHandler.sendMessage(message);
                }
                if (chatMessageHandler != null) {
                    Message message = new Message();
                    message.what = 1;
                    chatMessageHandler.sendMessage(message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 每次登陆先获取数据库内容显示
     *
     * @return
     */
    public List<MessageInfo> messageArrived() {
        DBUtil dbUtil = new DBUtil(mContext);
        User mUser = dbUtil.getLatestUser();
        if(mUser != null){
            List<MessageInfo> tempMessageInfos = dbUtil.getAllMessage(mUser.getUserId());
            if (tempMessageInfos != null && tempMessageInfos.size() >= 1) {
                for (int i = 0; i < tempMessageInfos.size(); i++) {
                    ChatMsgInfo chatMsgInfo = dbUtil.getFristChat(mUser.getUserId(), tempMessageInfos.get(i).getMessageType());
                    if (chatMsgInfo != null) {
                        MessageInfo messageInfo = new MessageInfo();
                        messageInfo.setUserId(chatMsgInfo.getUserId());
                        messageInfo.setUserName(chatMsgInfo.getUserName());
                        messageInfo.setMessageType(chatMsgInfo.getMessageType());
                        messageInfo.setContent(chatMsgInfo.getContent());
                        messageInfo.setSendTime(chatMsgInfo.getSendTime());
                        int count = dbUtil.getUnReadCountById(mUser.getUserId(), chatMsgInfo.getMessageType());
                        messageInfo.setUnreadCount(count);
                        dbUtil.saveMessage(messageInfo);
                    }
                }
            }
            if (messageHandler != null) {
                Message message = new Message();
                message.what = 2;
                messageHandler.sendMessage(message);
            }
        }
        return dbUtil.getAllMessage(mUser.getUserId());
    }

    /**
     * 获取数据库消息类型具体内容
     *
     * @param messageType
     * @return
     */
    public List<ChatMsgInfo> chatMsgArrived(int messageType) {
        DBUtil dbUtil = new DBUtil(mContext);
        User mUser = dbUtil.getLatestUser();
        if (chatMessageHandler != null) {
            Message message = new Message();
            message.what = 1;
            chatMessageHandler.sendMessage(message);
        }
        return dbUtil.getAllChatMsg(mUser.getUserId(), messageType);
    }

    public List<ChatMsgInfo> deleteChatMsg(ChatMsgInfo chatMsgInfo){
        DBUtil dbUtil = new DBUtil(mContext);
        User mUser = dbUtil.getLatestUser();
        int messageType = chatMsgInfo.getMessageType();
        List<ChatMsgInfo> info = dbUtil.deleteChat(chatMsgInfo);
        if (chatMessageHandler != null) {
            Message message = new Message();
            message.what = 1;
            chatMessageHandler.sendMessage(message);
        }
        if(info.size() == 0){
            deleteMessage(messageType);
        }
        return info;
    }

    /**
     * 更新消息状态
     *
     * @param info
     */
    public void updateMessage(MessageInfo info) {
        DBUtil dbUtil = new DBUtil(mContext);
        dbUtil.updateMessageStatus(info);
        if (messageHandler != null) {
            Message message = new Message();
            message.what = 2;
            messageHandler.sendMessage(message);
        }
    }
    public void deleteMessage(int messageType){
        DBUtil dbUtil = new DBUtil(mContext);
        User mUser = dbUtil.getLatestUser();
        dbUtil.deleteMessage(mUser.getUserId(),messageType);
        if (messageHandler != null) {
            Message message = new Message();
            message.what = 2;
            messageHandler.sendMessage(message);
        }
    }

    /**
     * 获取未读数量
     *
     * @return
     */
    public int getUnReadcount() {
        DBUtil dbUtil = new DBUtil(mContext);
        User mUser = dbUtil.getLatestUser();
        List<MessageInfo> messageInfos = null;
        int num = 0;
        if(mUser != null ){
            messageInfos = dbUtil.getAllMessage(mUser.getUserId());

            if (messageInfos != null && messageInfos.size() >= 1) {
                for (int i = 0; i < messageInfos.size(); i++) {
                    int unReadCount = dbUtil.getUnReadCountById(mUser.getUserId(), messageInfos.get(i).getMessageType());
                    num += unReadCount;
                }
            }else {
                return 0;
            }
        }

        return num;
    }

    public void setMessageHandler(Handler handler) {
        this.messageHandler = handler;
    }

    public void setChatHandler(Handler handler) {
        this.chatMessageHandler = handler;
    }
}
