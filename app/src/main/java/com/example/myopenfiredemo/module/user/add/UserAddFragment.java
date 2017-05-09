package com.example.myopenfiredemo.module.user.add;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myopenfiredemo.R;
import com.example.myopenfiredemo.base.fragment.BaseFragment;
import com.example.myopenfiredemo.module.navigation.NavigationFragment;
import com.example.myopenfiredemo.util.ToastUtil;

import org.jivesoftware.smackx.search.UserSearch;

import butterknife.BindView;
import butterknife.OnClick;

/**UserAddFragment:添加好友的页面
 * Created by clement on 2017/4/24.
 */

public class UserAddFragment extends BaseFragment implements UserAddContract.View{

    @BindView(R.id.et_user_name)
    EditText etUserName;
    @BindView(R.id.et_nick_name)
    EditText etNickName;
    @BindView(R.id.et_group)
    EditText etGroup;
    @BindView(R.id.btn_add)
    Button btnAdd;

    private UserAddContract.Presenter mPresenter;

    @Override
    public int onBindLayoutID() {
        return R.layout.fragment_user_add;
    }

    @Override
    public void initBehavior(View rootView) {
        mPresenter = new UserAddPresenter(this,getActivity());
        mPresenter.start();
    }

    @Override
    public void setPresenter(UserAddContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public Bundle getBundle() {
        return getArguments();
    }

    @Override
    public void setUserName(String userName) {
        etUserName.setText(userName);
    }

    @Override
    public String getNickName() {
        return etNickName.getText().toString();
    }

    @Override
    public void setNickName(String nickName) {
        etNickName.setText(nickName);
    }

    @Override
    public String getGroup() {
        return etGroup.getText().toString();
    }

    @OnClick(R.id.btn_add)
    void onAddFriend(){
        mPresenter.addFriend();
    }

    @Override
    public void showToast(String content) {
        ToastUtil.show(getActivity(),content);
    }

    @Override
    public void finish() {
        //移除当前页面并跳转到聊天列表界面,传递bundle通知刷新
        Bundle bundle = new Bundle();
        remove2ShowFragment(UserAddFragment.class.getSimpleName(),
                NavigationFragment.class.getSimpleName(),bundle);
        //移除中间的fragment:移除用户添加页面
        removeFragment(UserAddFragment.class.getSimpleName());
        //移除用户搜索界面
        removeFragment(UserSearch.class.getSimpleName());
    }

}
