package com.example.myopenfiredemo.module.user.detail;

import android.os.Bundle;

import com.example.myopenfiredemo.base.mvp.BasePresenter;
import com.example.myopenfiredemo.base.mvp.BaseView;

/**UserDetailContract
 * Created by clement on 2017/4/21.
 */

public class UserDetailContract {

    interface Presenter extends BasePresenter{
        void initView();
        void updateDetail();
    }

    interface View extends BaseView<Presenter>{
        Bundle getBundle();
        void setAvatar(byte[] imageBytes);
        byte[] getAvatar();
        void setTitleName(String titleName);
        String getFirstName();
        void setFirstName(String firstName);
        String getMiddleName();
        void setMiddleName(String middleName);
        String getLastName();
        void setLastName(String lastName);
        String getNickName();
        void setNickName(String nickName);
        String getEmail();
        void setEmail(String email);
        void showToast(String message);
        void hideUpdateButton();
        void finish();
    }

}
