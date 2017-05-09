package com.example.myopenfiredemo.module.chats.chat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.example.myopenfiredemo.base.mvp.BasePresenter;
import com.example.myopenfiredemo.base.mvp.BaseView;

/**ChatContract
 * Created by clement on 2017/4/19.
 */

public class ChatContract {

    public interface Presenter extends BasePresenter{
        void initData();
        void initView();
        void addMessageListener();
        void sendTextMessage(@NonNull String textContent);
        void sendPhotoMessage(@NonNull String photoPath);
    }

    public interface View extends BaseView<Presenter>{
        void setTitle(String title);
        Bundle getBundle();
        //获取RecyclerView的实例
        RecyclerView getRecyclerView();
        String getInputContent();
        void clearInput();
        //跳转到用户详情页面
        void intent2UserDetailFragment(Bundle bundle);
    }

}
