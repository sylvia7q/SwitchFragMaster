package com.kanban.switchfragmaster.database;

import android.content.Context;
import android.util.Log;

import com.kanban.switchfragmaster.data.ChatMsgInfo;
import com.kanban.switchfragmaster.data.MessageInfo;
import com.kanban.switchfragmaster.data.User;
import com.kanban.switchfragmaster.utils.LogUtil;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;

import java.util.List;

public class DBUtil {

    private final static String TAG = DBUtil.class.getName();
    public static final String DB_NAME = "report";
    public static final int DB_VERSION = 1;
    private DbUtils mDbUtils;
    private Context mContext;

    public DBUtil(Context context) {
        super();
        mContext = context;
        //mDbUtils = DbUtils.create(context, DBName);
        mDbUtils = DbUtils.create(context, DB_NAME, DB_VERSION, new DbUtils.DbUpgradeListener() {

            @Override
            public void onUpgrade(DbUtils dbUtils, int oldVersion, int newVersion) {
                // TODO Auto-generated method stub
                Log.e(TAG, "-----------------oldVersion:" + oldVersion + "   newVersion:" + newVersion);
            }
        });
        mDbUtils.configAllowTransaction(false);
    }

    /**
     * 新增用户
     * @param user
     * @return
     */
    public boolean saveUser(User user) {
        boolean flag = false;
        try {
            User object = getUserById(user.getUserId());
            if (object == null) {
                LogUtil.e("add user");
                mDbUtils.save(user);
            } else {
                LogUtil.e("update user");
                mDbUtils.update(user, "userId", "userName", "password", "loginTime","curAccount");
            }
            flag = true;
        } catch (DbException e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }
    /**
     * 更新用户状态
     *
     * @param curUser
     * @return
     */
    public boolean updateUserStatus(User curUser) {
        boolean flag = false;
        try {
            List<User> userList = mDbUtils.findAll(Selector.from(User.class).where("userId","!=",curUser.getUserId()));
            if (userList != null) {
                for (int i = 0; i < userList.size(); i++) {
                    User updateUser = new User();
                    updateUser.setUserId(userList.get(i).getUserId());
                    updateUser.setUserName(userList.get(i).getUserName());
                    updateUser.setPassword(userList.get(i).getPassword());
                    updateUser.setLoginTime(userList.get(i).getLoginTime());
                    updateUser.setCurAccount("N");
                    mDbUtils.update(updateUser, "userId", "userName", "password", "loginTime","curAccount");
                }
            }
            flag = true;
        } catch (DbException e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }

    public User getLatestUser() {
        User user = null;
        try {
            user = mDbUtils.findFirst(Selector.from(User.class).where("curAccount","=","Y"));
            if(user != null){
                return user;
            }else {
                return null;
            }

        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }


    /**
     * 根据ID获取用户
     *
     * @param userId
     * @return
     */
    public User getUserById(String userId) {
        try {
            return mDbUtils.findFirst(Selector.from(User.class).where("userId", "=", userId));
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    /**
     * 新增消息
     *
     * @param info
     * @return
     */
    public boolean saveMessage(MessageInfo info) {
        boolean flag = false;
        try {
            MessageInfo object = getMessageById(info.getUserId(), info.getMessageType());
            if (object == null) {
                LogUtil.e("add MessageInfo");
                mDbUtils.save(info);
            } else {
                mDbUtils.update(info
                        , "messageType", "userId", "userName", "content", "sendTime", "unreadCount");
                LogUtil.e("update MessageInfo");
            }
            flag = true;
        } catch (DbException e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 新增消息
     *
     * @param info
     * @return
     */
    public boolean saveMessage(List<MessageInfo> info) {
        boolean flag = false;
        try {
            mDbUtils.saveAll(info);
            flag = true;
        } catch (DbException e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 根据ID获取消息对象
     *
     * @param messageType
     * @return
     */
    public MessageInfo getMessageById(String userId, int messageType) {
        try {
            return mDbUtils.findFirst(Selector.from(MessageInfo.class)
                    .where(WhereBuilder.b("userId", "=", userId).and("messageType", "=", messageType)));
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    /**
     * 根据ID获取消息对象
     *
     * @param messageType
     * @return
     */
    public int getUnReadCountById(String userId, int messageType) {
        try {
            MessageInfo info = mDbUtils.findFirst(Selector.from(MessageInfo.class)
                    .where(WhereBuilder.b("userId", "=", userId).and("messageType", "=", messageType)));
            if (info != null) {
                return info.getUnreadCount();
            } else {
                return 0;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return 0;
    }

    /**
     * 新增消息
     *
     * @param info
     * @return
     */
    public boolean saveChat(ChatMsgInfo info) {
        boolean flag = false;
        try {
            mDbUtils.save(info);
            flag = true;
        } catch (DbException e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }

    public boolean saveChat(List<ChatMsgInfo> info) {
        boolean flag = false;
        try {
            mDbUtils.saveAll(info);
            flag = true;
        } catch (DbException e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 根据ID获取消息对象
     *
     * @param messageType
     * @return
     */
    public ChatMsgInfo getChatById(String userId, int messageType) {
        try {
            return mDbUtils.findFirst(Selector.from(ChatMsgInfo.class)
                    .where(WhereBuilder.b("userId", "=", userId).and("messageType", "=", messageType)));
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    public List<ChatMsgInfo> getAllChatMsg(String userId, int messageType) {
        try {
            return mDbUtils.findAll(Selector.from(ChatMsgInfo.class)
                    .where(WhereBuilder.b("userId", "=", userId).and("messageType", "=", messageType)));
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    public List<MessageInfo> getAllMessage(String userId) {
        List<MessageInfo> infoList = null;
        try {
            infoList = mDbUtils.findAll(Selector.from(MessageInfo.class)
                    .where(WhereBuilder.b("userId", "=", userId))
                    .orderBy("sendTime", true));
        } catch (Exception e) {
            // TODO: handle exception
        }
        return infoList;
    }

    public List<MessageInfo> getAllMessage(String userId, int messageType) {
        List<MessageInfo> infoList = null;
        try {
            infoList = mDbUtils.findAll(Selector.from(MessageInfo.class)
                    .where(WhereBuilder.b("userId", "=", userId).and("messageType", "=", messageType)));

        } catch (Exception e) {
            // TODO: handle exception
        }
        return infoList;
    }

    /**
     * 删除
     *
     * @param messageType
     * @return
     */
    public boolean deleteMessage(String userId, int messageType) {
        boolean flag = false;
        try {
            List<ChatMsgInfo> chatMsgInfos = mDbUtils.findAll(Selector.from(ChatMsgInfo.class).where("userId", "=", userId).and("messageType", "=", messageType));
            if(chatMsgInfos != null && chatMsgInfos.size() > 0){
                mDbUtils.deleteAll(chatMsgInfos);
            }
            List<MessageInfo> messageInfos = mDbUtils.findAll(Selector.from(MessageInfo.class).where("userId", "=", userId).and("messageType", "=", messageType));
            if(messageInfos != null && messageInfos.size() > 0){
                mDbUtils.deleteAll(messageInfos);
            }
            flag = true;
        } catch (DbException e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    /**
     * 更新消息状态，已读或者未读
     *
     * @param info
     * @return
     */
    public boolean updateMessageStatus(MessageInfo info) {
        boolean flag = false;
        try {
            mDbUtils.update(info, "unreadCount");
            flag = true;
        } catch (DbException e) {
            flag = false;
            e.printStackTrace();
        }
        return flag;
    }


    /**
     * 获取第一条消息的时间
     *
     * @return
     */
    public ChatMsgInfo getFristChat(String userId, int messageType) {
        try {
            ChatMsgInfo info = mDbUtils.findFirst(Selector.from(ChatMsgInfo.class)
                    .where(WhereBuilder.b("userId", "=", userId).and("messageType", "=", messageType))
                    .orderBy("id", true));
            if (info != null) {
                return info;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    public List<ChatMsgInfo> deleteChat(ChatMsgInfo chatMsgInfo){
        List<ChatMsgInfo> info = null;
        try {
//            mDbUtils.delete(ChatMsgInfo.class,WhereBuilder.b("userId", "=", chatMsgInfo.getUserId())
//                    .and("messageType", "=", chatMsgInfo.getMessageType())
//                            .and("content", "=", chatMsgInfo.getContent()));
            mDbUtils.delete(chatMsgInfo);
            info = mDbUtils.findAll(Selector.from(ChatMsgInfo.class)
                    .where(WhereBuilder.b("userId", "=", chatMsgInfo.getUserId())
                            .and("messageType", "=", chatMsgInfo.getMessageType())));
        } catch (DbException e) {
            e.printStackTrace();
            return null;
        }
        return info;
    }
}

