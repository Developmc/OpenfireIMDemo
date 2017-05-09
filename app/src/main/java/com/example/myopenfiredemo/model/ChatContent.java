package com.example.myopenfiredemo.model;

/**回话内容
 * Created by clement on 2017/4/19.
 */

public class ChatContent {
    public final static int SEND = 1;
    public final static int RECEIVE = 2;
    private String content;
    private int flag;
    private MessageType messageType;

    /**
     * 用来识别当前对话内容所属哪个用户：不知道如何获取当前登录的userJID，用空或者null表示该对象属于当前登录的账号
     */
    private String userJID;
    //头像
    private byte[] avatar;

    public ChatContent(){}
    public ChatContent(String content,MessageType messageType,int flag,String userJID,byte[] avatar){
        this.content = content;
        this.messageType = messageType;
        this.flag = flag;
        this.userJID = userJID;
        this.avatar = avatar;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getUserJID() {
        return userJID;
    }

    public void setUserJID(String userJID) {
        this.userJID = userJID;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    /**
     * 消息的类型：文本，图片等
     */
    public enum MessageType {
        TEXT,PHOTO
    }
}
