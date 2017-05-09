package com.example.myopenfiredemo.base.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.myopenfiredemo.base.interfaces.ActivityInitializer;

import butterknife.ButterKnife;

/**Base Activity
 * Created by clement on 2017/4/17
 */

public abstract class BaseActivity extends AppCompatActivity implements ActivityInitializer {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(onBindLayoutID());
        ButterKnife.bind(this);
        initBehavior(savedInstanceState);
        ActivityManager.getInstance().putActivity(this.getClass().getSimpleName(),this);
    }

    @Override protected void onResume() {
        super.onResume();
    }

    @Override protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getInstance().removeActivity(this.getClass().getSimpleName());
    }
}
