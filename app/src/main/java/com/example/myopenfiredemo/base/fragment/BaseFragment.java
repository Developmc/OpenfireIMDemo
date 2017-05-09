package com.example.myopenfiredemo.base.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.example.myopenfiredemo.R;
import com.example.myopenfiredemo.base.interfaces.FragmentBackHandler;
import com.example.myopenfiredemo.base.interfaces.FragmentInitializer;
import com.example.myopenfiredemo.constants.BundleConstant;

import butterknife.ButterKnife;

/**
 * Base Fragment
 * Created by clement on 2017/4/17
 */

public abstract class BaseFragment extends android.app.Fragment implements FragmentInitializer,FragmentBackHandler {
    private Context mContext;
    //保存fragment的view
    protected View rootView;
    private Bundle bundle;
    //fragment的容器view，这里基本固定是 R.id.content_layout
    private int containerViewId = R.id.content_layout;

    /**
     * 绑定activity的操作在onAttach执行
     */
    @TargetApi(23) @Override public void onAttach(Context context) {
        super.onAttach(context);
        onAttach2Context(context);
    }

    /**
     * 兼容android6.0以下
     */
    @SuppressWarnings("deprecation") @Override public void onAttach(Activity activity) {
        super.onAttach(activity);
        onAttach2Context(activity);
    }

    /**
     * 兼容android6.0
     */
    protected void onAttach2Context(Context context) {
        mContext = context;
    }

    @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(onBindLayoutID(), container, false);
            ButterKnife.bind(this, rootView);
            initBehavior(rootView);
        }
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //在界面销毁的时候，统一处理键盘
        hideKeyboard();
    }

    /**
     * 获取context,避免使用getActivity()
     */
    public Context getContext() {
        if (mContext != null) {
            return mContext;
        } else {
            return getActivity();
        }
    }

    /**
     * fragment从隐藏到显示的回调(当需要传递数据前，和remove2showChildFragment绑定设置bundle)
     *
     * @param bundle 如果为空，表示没有数据传输
     */
    public void onHide2Show(Bundle bundle) {

    }

    /**
     * fragment从显示到隐藏的回调
     */
    public void onShow2Hide() {
    }

    @Override public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        //从hidden->show
        if (!hidden) {
            onHide2Show(bundle);
            //重置参数
            bundle = null;
        } else {
            onShow2Hide();
            //隐藏键盘
            hideKeyboard();
        }
    }

    /**
     * 为fragment设置bundle
     */
    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    /**
     * add fragment :如果没有则新增，如果已存在，则移除再新增
     *
     * @param containerViewId 将要跳转的fragment的容器view
     * @param toFragment 将要跳转的fragment实例
     * @param toFragmentTag 将要跳转的fragment的Tag
     * @param bundle 传递参数(设为null时，表示不传值)
     */
    @Deprecated
    protected void addFragment(int containerViewId, BaseFragment toFragment, String toFragmentTag,
        Bundle bundle) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        //设置跳转动画
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        //传递参数
        if (bundle != null) {
            toFragment.setArguments(bundle);
        }
        //判断将要跳转的fragment是否已经存在，如果已经存在，则先移除旧的fragment
        if (manager.findFragmentByTag(toFragmentTag) != null) {
            removeFragment(toFragmentTag);
        }
        //跳转到指定的fragment
        transaction.add(containerViewId, toFragment, toFragmentTag).commitAllowingStateLoss();
    }

    /**
     * add fragment :如果没有则新增，如果已存在，则移除再新增
     *
     * @param toFragment 将要跳转的fragment实例
     * @param toFragmentTag 将要跳转的fragment的Tag
     * @param bundle 传递参数(设为null时，表示不传值)
     */
    protected void addFragment(BaseFragment toFragment, String toFragmentTag, Bundle bundle) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        //设置跳转动画
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        //传递参数
        if (bundle != null) {
            toFragment.setArguments(bundle);
        }
        //判断将要跳转的fragment是否已经存在，如果已经存在，则先移除旧的fragment
        if (manager.findFragmentByTag(toFragmentTag) != null) {
            removeFragment(toFragmentTag);
        }
        //跳转到指定的fragment
        transaction.add(containerViewId, toFragment, toFragmentTag).commitAllowingStateLoss();
    }

    /**
     * 移除fragment
     *
     * @param removeFragmentTag 将要移除的fragment的Tag
     */
    protected void removeFragment(String removeFragmentTag) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        //设置跳转动画
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        //移除当前fragment
        BaseFragment removeFragment = (BaseFragment) manager.findFragmentByTag(removeFragmentTag);
        if (removeFragment == null) {
            return;
        }
        transaction.remove(removeFragment);
        transaction.commitAllowingStateLoss();
    }

    /**
     * 切换fragment，采取的是隐藏当前fragment，然后跳转到新fragment,如果新的fragment已经存在则移除再add
     *
     * @param fromFragmentTag 将要隐藏的fragment的Tag
     * @param toFragment 将要跳转的fragment实例
     * @param toFragmentTag 将要跳转的fragment的Tag
     * @param bundle 传递参数(设为null时，表示不传值)
     */
    protected void switchFragment(String fromFragmentTag, BaseFragment toFragment,
        String toFragmentTag, Bundle bundle) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        //设置跳转动画
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        //隐藏当前fragment
        transaction.hide(manager.findFragmentByTag(fromFragmentTag));
        //传递参数,添加两个tag
        if(bundle == null){
            bundle = new Bundle();
        }
        bundle.putString(BundleConstant.FROM_TAG,fromFragmentTag);
        bundle.putString(BundleConstant.TO_TAG,toFragmentTag);
        toFragment.setArguments(bundle);
        //判断将要跳转的fragment是否已经存在,如果已存在，则移除
        if (manager.findFragmentByTag(toFragmentTag) != null) {
            //跳转到指定的fragment
            transaction.remove(manager.findFragmentByTag(toFragmentTag));
        }
        transaction.add(containerViewId, toFragment, toFragmentTag).commitAllowingStateLoss();
    }

    /**
     * 移除当前fragment，并显示上一个fragment（该fragment处于隐藏状态）
     *
     * @param removeFragmentTag 将要移除的fragment的Tag
     * @param showFragmentTag 将要显示的fragment的Tag
     */
    protected void remove2ShowFragment(String removeFragmentTag, String showFragmentTag) {
        remove2ShowFragment(removeFragmentTag,showFragmentTag,null);
    }

    /**
     * 移除当前fragment，传递数据，并显示上一个fragment（该fragment处于隐藏状态）,复写onHide2Show接收数据
     *
     * @param removeFragmentTag 将要移除的fragment的Tag
     * @param showFragmentTag 将要显示的fragment的Tag
     * @param bundle 传递的数据
     */
    protected void remove2ShowFragment(String removeFragmentTag, String showFragmentTag, Bundle bundle) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        //设置跳转动画
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        //移除当前fragment
        removeFragment(removeFragmentTag);
        BaseFragment showFragment = (BaseFragment) manager.findFragmentByTag(showFragmentTag);
        if (showFragment == null) {
            return;
        }
        showFragment.setBundle(bundle);
        //显示上一个隐藏的fragment
        transaction.show(showFragment).commitAllowingStateLoss();
    }

    /**
     * 隐藏键盘
     */
    protected void hideKeyboard(){
        //如果打开了，则隐藏键盘
        InputMethodManager imm =  (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null) {
            imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    /**处理返回事件,返回true表示处理了该事件，false表示没处理，事件会传递到activity
     * @return
     */
    @Override
    public boolean onBackPressed() {
        remove2ShowFragment(getArguments().getString(BundleConstant.TO_TAG),
                getArguments().getString(BundleConstant.FROM_TAG));
        return true;
    }

}
