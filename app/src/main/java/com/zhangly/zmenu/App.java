package com.zhangly.zmenu;

import android.app.Application;
import android.content.Context;

/**
 * Created by zhangluya on 16/8/15.
 */
public class App extends Application {

    private static App sApp;
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();

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
