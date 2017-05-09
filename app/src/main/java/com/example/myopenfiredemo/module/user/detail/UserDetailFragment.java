package com.example.myopenfiredemo.module.user.detail;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.myopenfiredemo.R;
import com.example.myopenfiredemo.base.fragment.BaseFragment;
import com.example.myopenfiredemo.util.BitmapUtil;
import com.example.myopenfiredemo.util.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**用户个人信息详情页
 * Created by clement on 2017/4/20.
 */

public class UserDetailFragment extends BaseFragment implements UserDetailContract.View{

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.et_first_name)
    EditText etFirstName;
    @BindView(R.id.et_middle_name)
    EditText etMiddleName;
    @BindView(R.id.et_last_name)
    EditText etLastName;
    @BindView(R.id.et_nick_name)
    EditText etNickName;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.btn_update)
    Button btnUpdate;

    private UserDetailContract.Presenter mPresenter;
    private final static int REQUEST_CODE = 2;

    @Override
    public int onBindLayoutID() {
        return R.layout.fragment_user_detail;
    }

    @Override
    public void initBehavior(View rootView) {
        mPresenter = new UserDetailPresenter(this,getActivity());
        mPresenter.start();
    }

    @Override
    public void setPresenter(UserDetailContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public Bundle getBundle() {
        return getArguments();
    }

    @Override
    public void setAvatar(byte[] imageBytes) {
        Glide.with(getActivity()).load(imageBytes).into(ivAvatar);
    }

    @Override
    public byte[] getAvatar() {
        //获取imageView的图片
        ivAvatar.setDrawingCacheEnabled(true);
        Bitmap tempBitmap = Bitmap.createBitmap(ivAvatar.getDrawingCache());
        //清空
        ivAvatar.setDrawingCacheEnabled(false);
        return BitmapUtil.bitmap2Bytes(tempBitmap);
    }

    @Override
    public void setTitleName(String titleName) {
        tvTitle.setText(titleName);
    }

    @Override
    public String getFirstName() {
        return etFirstName.getText().toString();
    }

    @Override
    public void setFirstName(String firstName) {
        etFirstName.setText(firstName);
    }

    @Override
    public String getMiddleName() {
        return etMiddleName.getText().toString();
    }

    @Override
    public void setMiddleName(String middleName) {
        etMiddleName.setText(middleName);
    }

    @Override
    public String getLastName() {
        return etLastName.getText().toString();
    }

    @Override
    public void setLastName(String lastName) {
        etLastName.setText(lastName);
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
    public String getEmail() {
        return etEmail.getText().toString();
    }

    @Override
    public void setEmail(String email) {
        etEmail.setText(email);
    }

    @OnClick(R.id.btn_update)
    void onUpdateDetail(){
        mPresenter.updateDetail();
    }

    @OnClick(R.id.iv_avatar)
    void onAvatarClick(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,REQUEST_CODE);
    }

    @Override
    public void showToast(String message) {
        ToastUtil.show(getActivity(),message);
    }

    @Override
    public void hideUpdateButton() {
        btnUpdate.setVisibility(View.GONE);
    }

    @Override
    public void finish() {
        //结束当前fragment,相当于触发点击回退
        onBackPressed();
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
                Glide.with(getActivity()).load(picturePath).into(ivAvatar);
                cursor.close();
            }
        }
    }
}
