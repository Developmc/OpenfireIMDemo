package com.example.myopenfiredemo.module.search;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.example.myopenfiredemo.constants.BundleConstant;
import com.example.myopenfiredemo.custom.recyclerview.OnClickListener;
import com.example.myopenfiredemo.im.IMHelper;
import com.example.myopenfiredemo.model.SearchPerson;

import java.util.ArrayList;
import java.util.List;

/**SearchUserPresenter
 * Created by clement on 2017/4/22.
 */

public class SearchUserPresenter implements SearchUserContract.Presenter{
    private Context mContext;
    private SearchUserContract.View mView;
    private SearchUserAdapter adapter;
    private List<SearchPerson> persons = new ArrayList<>();

    public SearchUserPresenter(SearchUserContract.View view,Context context){
        this.mView = view;
        this.mContext = context;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        mView.setTitle("查找用户");
        initRecyclerView();
    }

    private void initRecyclerView(){
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        mView.getRecyclerView().setLayoutManager(manager);
        adapter = new SearchUserAdapter(persons,mContext);
        mView.getRecyclerView().setAdapter(adapter);
        adapter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view, int position) {
                //跳转到用户详细页面
                Bundle bundle = new Bundle();
                bundle.putString(BundleConstant.USER_JID,adapter.getDatas().get(position).getUserJID());
                bundle.putString(BundleConstant.USER_NAME,adapter.getDatas().get(position).getUserName());
                mView.intent2UserAddFragment(bundle);
            }
        });
    }

    @Override
    public void searchUser(@NonNull String searchContent) {
        persons = IMHelper.getInstance().searchUsers(searchContent);
        if(persons.isEmpty()){
            mView.showToast("没有搜索结果");
            //刷新recyclerView
            refreshRecyclerView(persons);
            return;
        }
        //根据userJID获取头像avatar
        for(SearchPerson person:persons){
            byte[] avatar = IMHelper.getInstance().getAvatar(person.getUserJID());
            person.setAvatar(avatar);
        }
        //刷新recyclerView
        refreshRecyclerView(persons);
    }

    @Override
    public void refreshRecyclerView(List<SearchPerson> persons) {
        //清空原数据
        adapter.getDatas().clear();
        //刷新recyclerView
        adapter.getDatas().addAll(persons);
        adapter.notifyDataSetChanged();
    }
}
