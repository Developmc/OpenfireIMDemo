package com.example.myopenfiredemo.module.chats.chat;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myopenfiredemo.R;
import com.example.myopenfiredemo.base.fragment.BaseFragment;
import com.example.myopenfiredemo.module.navigation.NavigationFragment;
import com.example.myopenfiredemo.module.user.detail.UserDetailFragment;
import com.example.myopenfiredemo.util.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**ChatFragment
 * Created by clement on 2017/4/19.
 */

public class ChatFragment extends BaseFragment implements ChatContract.View {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.et_input)
    EditText etInput;
    @BindView(R.id.btn_send)
    Button btnSend;
    @BindView(R.id.other_layout)
    RelativeLayout otherLayout;

    private ChatContract.Presenter mPresenter;
    private final static int REQUEST_CODE = 1;

    @Override
    public int onBindLayoutID() {
        return R.layout.fragment_chat;
    }

    @Override
    public void initBehavior(View rootView) {
        mPresenter = new ChatPresenter(this,getActivity());
        mPresenter.start();
    }

    @Override public boolean onBackPressed() {
        remove2ShowFragment(ChatFragment.class.getSimpleName(),
                NavigationFragment.class.getSimpleName());
        return true;
    }

    @Override
    public void setPresenter(ChatContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    @Override
    public Bundle getBundle() {
        return getArguments();
    }

    @Override
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @OnClick(R.id.btn_send)
    void onSend(){
        String content = getInputContent();
        if(content==null||content.isEmpty()){
            ToastUtil.show(getActivity(),"请输入内容");
            return;
        }
        //发送文本消息
        mPresenter.sendTextMessage(content);
    }

    @OnClick(R.id.iv_other)
    void onOtherClick(){
        //显示隐藏的布局
        otherLayout.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btn_photo)
    void onPhotoClick(){
        //恢复布局
        otherLayout.setVisibility(View.GONE);
        //跳转到相册界面
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,REQUEST_CODE);
    }

    @Override
    public String getInputContent() {
        return etInput.getText().toString();
    }

    /**
     * 清空输入内容
     */
    @Override
    public void clearInput() {
        etInput.setText("");
    }

    /**跳转到用户详情页面
     * @param bundle
     */
    @Override
    public void intent2UserDetailFragment(Bundle bundle) {
        switchFragment(ChatFragment.class.getSimpleName(),new UserDetailFragment(),
                UserDetailFragment.class.getSimpleName(),bundle);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_CODE && resultCode== Activity.RESULT_OK){
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            if(cursor!=null){
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                //发送图片消息
                mPresenter.sendPhotoMessage(picturePath);
                cursor.close();
            }
        }
    }
}
