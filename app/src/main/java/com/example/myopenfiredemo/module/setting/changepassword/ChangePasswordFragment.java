package com.example.myopenfiredemo.module.setting.changepassword;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myopenfiredemo.R;
import com.example.myopenfiredemo.base.fragment.BaseFragment;
import com.example.myopenfiredemo.im.IMHelper;
import com.example.myopenfiredemo.im.Listener.IMChangePasswordListener;
import com.example.myopenfiredemo.util.DialogUtil;
import com.example.myopenfiredemo.util.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**ChangePasswordFragment
 * Created by clement on 2017/4/20.
 */

public class ChangePasswordFragment extends BaseFragment {
    @BindView(R.id.btn_change_password)
    Button btnChangePassword;
    @BindView(R.id.et_change_password)
    EditText etChangePassword;
    @BindView(R.id.et_change_password_confirm)
    EditText etChangePasswordConfirm;

    @Override
    public int onBindLayoutID() {
        return R.layout.fragment_change_password;
    }

    @Override
    public void initBehavior(View rootView) {

    }

    @OnClick(R.id.btn_change_password)
    void onChangePassword(){
        String newPassword1 = etChangePassword.getText().toString();
        String newPassword2 = etChangePasswordConfirm.getText().toString();
        if(newPassword1.isEmpty() || newPassword2.isEmpty()){
            ToastUtil.show(getContext(),"请先输入密码");
            return;
        }
        DialogUtil.showLoadingDialog(getContext(),"修改中");
        IMHelper.getInstance().changePassword(getActivity(), newPassword1, new IMChangePasswordListener() {
            @Override
            public void onChangePassword(boolean isChange, @Nullable Exception exception) {
                DialogUtil.hideLoadingDialog();
                //修改成功
                if(isChange){
                    ToastUtil.show(getActivity(),"密码已修改");
                    //退出当前界面
                    onBackPressed();
                }
                else{
                    ToastUtil.show(getActivity(),exception==null ? "未知异常" : exception.getMessage());
                }
            }
        });
    }

}
