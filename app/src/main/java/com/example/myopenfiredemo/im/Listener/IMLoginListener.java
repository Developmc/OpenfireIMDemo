package com.example.myopenfiredemo.im.Listener;

import android.support.annotation.Nullable;

/**IM登录回调
 * Created by clement on 2017/4/18.
 */

public interface IMLoginListener {
    /**
     * 登录回调，如果true表示成功。失败时，返回exception
     * @param isLogin
     * @param exception
     */
    void onLogin(boolean isLogin,@Nullable Exception exception);
}
