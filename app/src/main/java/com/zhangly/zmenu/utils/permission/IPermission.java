package com.zhangly.zmenu.utils.permission;

import android.content.Context;

import com.zhangly.zmenu.utils.permission.callback.OnRequestPermissionListener;

/**
 * Created by zhangluya on 16/8/17.
 */
public interface IPermission {

    void request(Context context, String[] permission, OnRequestPermissionListener listener);
}
