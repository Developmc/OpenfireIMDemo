package com.example.myopenfiredemo.model;

/**查找到的用户
 * Created by clement on 2017/4/22.
 */

public class SearchPerson {

    private String email;
    private String userJID;
    //用户名（这个值是创建用户时的用户名，创建后不允许修改）
    private String userName;
    //名称（这个值是用户名称，运行自由修改），注意：这个名称也不是用户昵称
    private String name;
    //这个值需要先拿到userJID，再根据userJID才能得到
    private byte[] avatar;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserJID() {
        return userJID;
    }

    public void setUserJID(String userJID) {
        this.userJID = userJID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }
}
