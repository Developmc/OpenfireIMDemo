package com.example.myopenfiredemo.module.navigation;

import android.app.FragmentTransaction;
import android.os.Bundle;

import com.example.myopenfiredemo.R;
import com.example.myopenfiredemo.base.activity.BaseActivity;
import com.example.myopenfiredemo.base.fragment.BaseFragment;
import com.example.myopenfiredemo.util.ToastUtil;

public class NavigationActivity extends BaseActivity {
    private long pressTime;

    @Override
    public int onBindLayoutID() {
        return R.layout.activity_navigation;
    }

    @Override
    public void initBehavior(Bundle savedInstanceState) {
        if(savedInstanceState==null){
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            NavigationFragment navigationFragment = new NavigationFragment();
            fragmentTransaction.add(R.id.content_layout,navigationFragment,
                    NavigationFragment.class.getSimpleName()).commit();
        }
    }

    @Override
    public void onBackPressed() {
        //获取当前显示的fragment
        BaseFragment baseFragment = (BaseFragment) getFragmentManager().findFragmentById(R.id.content_layout);
        //如果当前的fragment没有处理返回事件,执行activity的默认返回事件
        if(baseFragment!=null && !baseFragment.onBackPressed()){
            // 2.5s内再次点击back键有效
            if (System.currentTimeMillis() - pressTime > 2500) {
                pressTime = System.currentTimeMillis();
                ToastUtil.show(this, "再次点击退出");
            } else {
                // 这里只需退出，非登出
                finish();
            }
        }
    }

}
