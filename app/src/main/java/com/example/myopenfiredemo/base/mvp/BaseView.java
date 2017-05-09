package com.example.myopenfiredemo.base.mvp;

/**
 * MVP Base View
 * Created by Clement on 2017/4/17
 */

public interface BaseView<T> {
    void setPresenter(T presenter);
}
