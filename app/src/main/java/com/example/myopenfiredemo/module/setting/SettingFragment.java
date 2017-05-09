package com.example.myopenfiredemo.module.setting;

import android.content.Intent;
import android.widget.Button;

import com.example.myopenfiredemo.R;
import com.example.myopenfiredemo.base.fragment.LazyFragment;
import com.example.myopenfiredemo.custom.view.ItemSpinnerView;
import com.example.myopenfiredemo.module.login.LoginActivity;
import com.example.myopenfiredemo.module.navigation.NavigationFragment;
import com.example.myopenfiredemo.module.setting.changepassword.ChangePasswordFragment;
import com.example.myopenfiredemo.module.user.detail.UserDetailFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**SettingFragment
 * Created by clement on 2017/4/18.
 */

public class SettingFragment extends LazyFragment implements SettingContract.View{

    @BindView(R.id.btn_logout)
    Button btnLogout;
    @BindView(R.id.btn_change_password)
    Button btnChangePassword;
    @BindView(R.id.btn_detail)
    Button btnDetail;
    @BindView(R.id.item_status)
    ItemSpinnerView statusSpinnerView;

    private SettingContract.Presenter mPresenter;

    @Override
    public int onBindLayoutID() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void initLazyBehavior() {
        mPresenter = new SettingPresenter(this,getActivity());
        mPresenter.start();
    }

    @OnClick(R.id.btn_logout)
    void onLogout(){
        mPresenter.logout();
    }

    @OnClick(R.id.btn_change_password)
    void onChangePassword(){
        mPresenter.changePassword();
    }

    @OnClick(R.id.btn_detail)
     void onDetail(){
            mPresenter.personalDetail();
        }

    @Override
    public ItemSpinnerView getStatusSpinnerView() {
        return statusSpinnerView;
    }

    @Override
    public void setPresenter(SettingContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void intent2Login() {
        //跳转到登录界面
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void intent2ChangePassword() {
        switchFragment(NavigationFragment.class.getSimpleName(),new ChangePasswordFragment(),
                ChangePasswordFragment.class.getSimpleName(),null);
    }

    @Override
    public void intent2PersonalDetail() {
        switchFragment(NavigationFragment.class.getSimpleName(),new UserDetailFragment(),
                UserDetailFragment.class.getSimpleName(),null);
    }

    /**屏蔽当前fragment的返回事件，让activity处理
     * @return
     */
    @Override
    public boolean onBackPressed() {
        return false;
    }

}
