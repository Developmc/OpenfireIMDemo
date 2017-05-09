package com.example.myopenfiredemo.module.search;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.example.myopenfiredemo.base.mvp.BasePresenter;
import com.example.myopenfiredemo.base.mvp.BaseView;
import com.example.myopenfiredemo.model.SearchPerson;

import java.util.List;

/**SearchUserContract
 * Created by clement on 2017/4/22.
 */

public class SearchUserContract {

    interface Presenter extends BasePresenter{
        void searchUser(@NonNull String searchContent);
        void refreshRecyclerView(List<SearchPerson> persons);
    }

    interface View extends BaseView<Presenter>{
        void setTitle(String title);
        RecyclerView getRecyclerView();
        String getSearchContent();
        void showToast(String content);
        void intent2UserAddFragment(Bundle bundle);
    }

}
