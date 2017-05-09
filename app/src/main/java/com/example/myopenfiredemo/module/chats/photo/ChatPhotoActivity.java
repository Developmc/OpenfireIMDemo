package com.example.myopenfiredemo.module.chats.photo;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.myopenfiredemo.R;
import com.example.myopenfiredemo.base.activity.BaseActivity;
import com.example.myopenfiredemo.constants.BundleConstant;
import com.example.myopenfiredemo.util.BitmapUtil;
import com.github.chrisbanes.photoview.PhotoView;

import butterknife.BindView;
import butterknife.OnClick;

/**聊天记录的图片的详情页
 * Created by clement on 2017/4/25.
 */

public class ChatPhotoActivity extends BaseActivity{
    @BindView(R.id.photo_view)
    PhotoView photoView;

    @Override
    public int onBindLayoutID() {
        //activity全屏显示
        //取消标题栏(如果主题是NO_ACTION_BAR的,这句可去掉)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return R.layout.activity_chat_photo;
    }

    @Override
    public void initBehavior(Bundle savedInstanceState) {
        String photoContent = getIntent().getStringExtra(BundleConstant.PHOTO_CONTENT);
//        Glide.with(this).load(path).into(photoView);
        photoView.setImageBitmap(BitmapUtil.string2Bitmap(photoContent));
    }
    @OnClick(R.id.photo_view)
    void onPhotoViewClick(){
        finish();
    }
}
