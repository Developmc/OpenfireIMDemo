package com.example.myopenfiredemo.im.Listener;

import android.support.annotation.Nullable;

/**IMChangePasswordListener
 * Created by clement on 2017/4/20.
 */

public interface IMChangePasswordListener {
    /**
     * true表示成功，false，返回异常
     * @param isChange
     * @param exception
     */
    void onChangePassword(boolean isChange,@Nullable Exception exception);
}
