package com.example.myopenfiredemo.module.setting;

import android.content.Context;

import com.example.myopenfiredemo.custom.lisenter.OnSpinnerUpdateListener;
import com.example.myopenfiredemo.im.IMHelper;
import com.example.myopenfiredemo.util.ToastUtil;

import java.util.ArrayList;

/**SettingPresenter
 * Created by clement on 2017/4/18.
 */

public class SettingPresenter implements SettingContract.Presenter{
    private Context mContext;
    private SettingContract.View mView;

    public SettingPresenter(SettingContract.View view, Context context){
        this.mView = view;
        this.mContext = context;
    }

    @Override
    public void start() {
        initView();
    }

    @Override
    public void initView() {
        //初始化spinner
        final ArrayList<String> status = new ArrayList<>();
        status.add("在线");
        status.add("离开");
        status.add("空闲");
        status.add("忙碌");
        status.add("长时间离开");
        status.add("离线");
        mView.getStatusSpinnerView().setDatas(status);
        mView.getStatusSpinnerView().setOnSpinnerUpdateListener(new OnSpinnerUpdateListener() {
            @Override
            public void onUpdate(int position) {
                boolean updateSuccess = IMHelper.getInstance().updateUserState(status.get(position));
                if(updateSuccess){
                    ToastUtil.show(mContext,"状态已更新");
                }
                else{
                    ToastUtil.show(mContext,"状态更新出错");
                }
            }
        });
        mView.getStatusSpinnerView().getTvLabel().setText("当前状态");
    }

    @Override
    public void logout() {
        //退出IM
        IMHelper.getInstance().logout();
        //跳转到登录界面
        mView.intent2Login();
    }

    @Override
    public void changePassword() {
        mView.intent2ChangePassword();
    }

    @Override
    public void personalDetail() {
        mView.intent2PersonalDetail();
    }
}
