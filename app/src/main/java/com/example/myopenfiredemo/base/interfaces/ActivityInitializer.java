package com.example.myopenfiredemo.base.interfaces;

import android.os.Bundle;
import android.support.annotation.LayoutRes;

/**implement by activity
 * Created by clement on 2017/4/17
 */

public interface ActivityInitializer {
    @LayoutRes
    int onBindLayoutID();

    void initBehavior(Bundle savedInstanceState);
}
