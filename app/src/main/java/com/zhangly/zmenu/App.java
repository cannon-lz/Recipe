package com.zhangly.zmenu;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by zhangluya on 16/8/15.
 */
public class App extends Application {

    private static App sApp;
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        sApp = this;
        sContext = getApplicationContext();
    }

    public static App getApp() {
        return sApp;
    }

    public static Context getContext() {
        return sContext;
    }
}
