package com.example.myopenfiredemo.module.user.add;

import android.content.Context;
import android.os.Bundle;

import com.example.myopenfiredemo.constants.BundleConstant;
import com.example.myopenfiredemo.im.IMHelper;

/**UserAddPresenter
 * Created by clement on 2017/4/24.
 */

public class UserAddPresenter implements UserAddContract.Presenter {
    private Context mContext;
    private UserAddContract.View mView;

    private String userJID,userName;

    public UserAddPresenter(UserAddContract.View view,Context context){
        this.mView = view;
        this.mContext = context;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        initData();
        initView();
    }

    @Override
    public void initData() {
        Bundle bundle = mView.getBundle();
        userJID = bundle.getString(BundleConstant.USER_JID,"");
        userName = bundle.getString(BundleConstant.USER_NAME,"");
    }

    @Override
    public void initView() {
        mView.setUserName(userJID);
        mView.setNickName(userName);
    }

    @Override
    public void addFriend() {
        //添加好友
        String nickName = mView.getNickName();
        String group = mView.getGroup();
        boolean isSuccess = IMHelper.getInstance().addFriend(userJID,nickName,group);
        if(isSuccess){
            mView.showToast("添加好友成功");
            //移除当前界面
            mView.finish();
        }
        else{
            mView.showToast("添加好友失败");
        }
    }
}
