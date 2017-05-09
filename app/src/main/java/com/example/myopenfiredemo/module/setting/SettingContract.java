package com.example.myopenfiredemo.module.setting;

import com.example.myopenfiredemo.base.mvp.BasePresenter;
import com.example.myopenfiredemo.base.mvp.BaseView;
import com.example.myopenfiredemo.custom.view.ItemSpinnerView;

/**SettingContract
 * Created by clement on 2017/4/18.
 */

public class SettingContract {

    interface Presenter extends BasePresenter {
        void initView();
        //退出登录
        void logout();
        //更改密码
        void changePassword();
        //查看个人详细
        void personalDetail();
    }

    interface View extends BaseView<Presenter> {
        //跳转到登录页面
        void intent2Login();
        //跳转到更改密码页面
        void intent2ChangePassword();
        //跳转到个人详细页面
        void intent2PersonalDetail();
        ItemSpinnerView getStatusSpinnerView();
    }
}
