package com.example.myopenfiredemo.module.chats;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.example.myopenfiredemo.base.mvp.BasePresenter;
import com.example.myopenfiredemo.base.mvp.BaseView;
import com.example.myopenfiredemo.model.ChatPerson;

import java.util.List;

/**ChatListContract
 * Created by clement on 2017/4/18.
 */

public class ChatListContract {

    public interface Presenter extends BasePresenter{
        List<ChatPerson> getCharPersons();
        void refreshList();
    }

    public interface View extends BaseView<Presenter>{
        //获取RecyclerView的实例
        RecyclerView getRecyclerView();
        //跳转到ChatFragment
        void toChatFragment(Bundle bundle);
        //跳转到搜索页面
        void toSearchUserFragment();
        void setTitle(String title);
        void need2Refresh();
    }

}
