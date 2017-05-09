package com.example.myopenfiredemo.module.user.add;

import android.os.Bundle;

import com.example.myopenfiredemo.base.mvp.BasePresenter;
import com.example.myopenfiredemo.base.mvp.BaseView;

/**UserAddContract
 * Created by clement on 2017/4/24.
 */

public class UserAddContract {
    interface Presenter extends BasePresenter{
        void initData();
        void initView();
        void addFriend();
    }

    interface View extends BaseView<Presenter>{
        Bundle getBundle();
        void setUserName(String userName);
        String getNickName();
        void setNickName(String nickName);
        String getGroup();
        void showToast(String content);
        void finish();
    }
}
