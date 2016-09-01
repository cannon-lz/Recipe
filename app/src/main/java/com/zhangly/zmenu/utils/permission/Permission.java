package com.zhangly.zmenu.utils.permission;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.zhangly.zmenu.utils.permission.callback.OnRequestPermissionListener;

import java.lang.ref.WeakReference;

/**
 * Created by zhangluya on 16/8/17.
 */
public class Permission implements IPermission {

    private static Permission sInstance;

    private String mRationaleTitle;
    private String mRationale;
    private boolean mIsShowRationale;
    private WeakReference<OnRequestPermissionListener> mListener;

    private Permission () {}

    public static Permission getDefault() {
        if (sInstance == null) {
            sInstance = new Permission();
        }
        return sInstance;
    }

    public Permission setRationale(String rationale) {
        this.mRationale = rationale;
        return this;
    }

    public Permission setRationaleTitle(String rationaleTitle) {
        this.mRationaleTitle = rationaleTitle;
        return this;
    }

    public Permission setShowRationale(boolean isShowRationale) {
        this.mIsShowRationale = isShowRationale;
        return this;
    }

    @Override
    public void request(Context context, String[] permissions, OnRequestPermissionListener listener) {
        mListener = new WeakReference<>(listener);
        if (Build.VERSION.SDK_INT >= 23) {
            Intent intent = new Intent(context, PermissionActivity.class);
            intent.putExtra("permissions", permissions);
            intent.putExtra("title", mRationaleTitle);
            intent.putExtra("rationale", mRationale);
            intent.putExtra("isShowRationale", mIsShowRationale);
            context.startActivity(intent);
        } else {
            OnRequestPermissionListener requestPermissionListener = listener();
            if (requestPermissionListener != null) {
                requestPermissionListener.onAllowed();
                destroyListener();
            }
        }

    }

    OnRequestPermissionListener listener() {
        return mListener.get();
    }

    void destroyListener() {
        mListener = null;
        mIsShowRationale = false;
        mRationale = null;
        mRationaleTitle = null;
    }
}
