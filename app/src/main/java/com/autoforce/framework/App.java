package com.autoforce.framework;

import android.app.Application;
import com.autoforce.common.CommonAppDelegate;

/**
 * Created by xlh on 2019/3/22.
 * description:
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        CommonAppDelegate.init(this);
    }
}
