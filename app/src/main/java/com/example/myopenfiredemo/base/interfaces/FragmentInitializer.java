package com.example.myopenfiredemo.base.interfaces;

import android.support.annotation.LayoutRes;
import android.view.View;

/**implement by fragment
 * Created by clement on 2017/4/17
 */

public interface FragmentInitializer {
    @LayoutRes
    int onBindLayoutID();

    void initBehavior(View rootView);
}
