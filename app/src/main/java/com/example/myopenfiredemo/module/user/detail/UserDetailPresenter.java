package com.example.myopenfiredemo.module.user.detail;

import android.content.Context;

import com.example.myopenfiredemo.constants.BundleConstant;
import com.example.myopenfiredemo.im.IMHelper;

import org.jivesoftware.smackx.vcardtemp.packet.VCard;

/**UserDetailPresenter
 * Created by clement on 2017/4/21.
 */

public class UserDetailPresenter implements UserDetailContract.Presenter {

    private Context mContext;
    private UserDetailContract.View mView;

    private VCard vCard;

    public UserDetailPresenter(UserDetailContract.View view,Context context){
        this.mView = view;
        this.mContext = context;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        //通过传入的userJID,显示对应的用户的详情
        String userJID = mView.getBundle().getString(BundleConstant.USER_JID,"");
        if(userJID == null || userJID.isEmpty()){
            //读取当前登录的用户基本信息
            vCard = IMHelper.getInstance().getSelfVCard();
        }
        else{
            //读取对应JID的用户的基本信息
            vCard = IMHelper.getInstance().getUserVCard(userJID);
            //如果是其他用户不能修改，也没法修改
            mView.hideUpdateButton();
        }
        //初始化视图
        initView();
    }

    @Override
    public void initView() {
        mView.setAvatar(vCard.getAvatar());
        //用户显示的名字都是昵称
        mView.setTitleName(vCard.getNickName());
        mView.setFirstName(vCard.getFirstName());
        mView.setMiddleName(vCard.getMiddleName());
        mView.setLastName(vCard.getLastName());
        mView.setNickName(vCard.getNickName());
        mView.setEmail(vCard.getEmailHome());
    }

    @Override
    public void updateDetail() {
        boolean isSuccess = IMHelper.getInstance().updateVCard(mView.getFirstName(),mView.getMiddleName(),
                mView.getLastName(),mView.getNickName(),mView.getEmail(),mView.getAvatar());
        if(isSuccess){
            mView.showToast("更新成功");
            //退出
            mView.finish();
        }
        else{
            mView.showToast("更新失败");
        }
    }

}
