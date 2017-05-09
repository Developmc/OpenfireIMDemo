package com.example.myopenfiredemo.module.search;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myopenfiredemo.R;
import com.example.myopenfiredemo.base.fragment.BaseFragment;
import com.example.myopenfiredemo.module.user.add.UserAddFragment;
import com.example.myopenfiredemo.util.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**SearchUserFragment搜索用户
 * Created by clement on 2017/4/22.
 */

public class SearchUserFragment extends BaseFragment implements SearchUserContract.View{
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_search)
    Button btnSearch;
    @BindView(R.id.et_input)
    EditText etInput;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private SearchUserContract.Presenter mPresenter;
    @Override
    public int onBindLayoutID() {
        return R.layout.fragment_search_user;
    }

    @Override
    public void initBehavior(View rootView) {
        mPresenter = new SearchUserPresenter(this,getActivity());
        mPresenter.start();
    }

    @Override
    public void setPresenter(SearchUserContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    @Override
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    public String getSearchContent() {
        return etInput.getText().toString();
    }

    @Override
    public void showToast(String content) {
        ToastUtil.show(getActivity(),content);
    }

    @OnClick(R.id.btn_search)
    void onSearchClick(){
        if(getSearchContent().isEmpty()){
            ToastUtil.show(getActivity(),"请先输入查询内容");
            return;
        }
        //执行查询
        mPresenter.searchUser(getSearchContent());
    }

    @Override
    public void intent2UserAddFragment(Bundle bundle) {
        //从搜索页面跳转到用户添加页面
        switchFragment(SearchUserFragment.class.getSimpleName(),new UserAddFragment(),
                UserAddFragment.class.getSimpleName(),bundle);
    }
}
