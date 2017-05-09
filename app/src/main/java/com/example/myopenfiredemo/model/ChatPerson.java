package com.example.myopenfiredemo.model;

/**ChatPerson
 * Created by clement on 2017/4/21.
 */

public class ChatPerson {
    //标识用户
    private String userJID;
    //用户头像
    private byte[] avatar;
    //用户昵称
    private String nickName;

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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
