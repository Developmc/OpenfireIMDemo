package com.example.myopenfiredemo.module.chats;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myopenfiredemo.R;
import com.example.myopenfiredemo.base.fragment.LazyFragment;
import com.example.myopenfiredemo.module.chats.chat.ChatFragment;
import com.example.myopenfiredemo.module.navigation.NavigationFragment;
import com.example.myopenfiredemo.module.search.SearchUserFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**ChatListFragment
 * Created by clement on 2017/4/18.
 */

public class ChatListFragment extends LazyFragment implements ChatListContract.View{

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_add)
    ImageView ivAdd;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private ChatListContract.Presenter mPresenter;

    @Override
    public int onBindLayoutID() {
        return R.layout.fragment_chat_list;
    }

    @Override
    protected void initLazyBehavior() {
        mPresenter = new ChatListPresenter(this,getActivity());
        mPresenter.start();
    }

    @Override
    public void setPresenter(ChatListContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    @OnClick(R.id.iv_add)
    void onAddClick(){
        //跳转到搜索页面
        toSearchUserFragment();
    }

    @Override
    public void toChatFragment(Bundle bundle) {
        switchFragment(NavigationFragment.class.getSimpleName(),new ChatFragment(),
                ChatFragment.class.getSimpleName(),bundle);
    }

    @Override
    public void toSearchUserFragment() {
        switchFragment(NavigationFragment.class.getSimpleName(),new SearchUserFragment(),
                SearchUserFragment.class.getSimpleName(),null);
    }

    @Override
    public void need2Refresh() {
        //通知fragment刷新列表
        mPresenter.refreshList();
    }

    /**屏蔽当前fragment的返回事件，让activity处理
     * @return
     */
    @Override
    public boolean onBackPressed() {
        return false;
    }

}
