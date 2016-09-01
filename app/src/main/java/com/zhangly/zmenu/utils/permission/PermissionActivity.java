package com.zhangly.zmenu.utils.permission;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.zhangly.zmenu.utils.permission.callback.OnRequestPermissionListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p/>
 * <p/>
 * Created by zhangluya on 16/8/16.
 */
public class PermissionActivity extends Activity {

    private final static String DEF_RATIONALE_TITLE = "权限申请";
    private final static String DEF_RATIONALE = "需要申请以下权限才可以正常使用此功能";

    private final static int REQUEST_CODE = 1;

    private String mRationaleTitle;
    private String mRationale;
    private String[] mPermissions;
    private boolean mIsShowRationale;

    private final OnRequestPermissionListener mRequestListener = Permission.getDefault().listener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mPermissions = intent.getStringArrayExtra("permissions");
        mRationaleTitle = intent.getStringExtra("title");
        mRationaleTitle = TextUtils.isEmpty(mRationaleTitle) ? DEF_RATIONALE_TITLE : mRationaleTitle;
        mRationale = intent.getStringExtra("rationale");
        mRationale = TextUtils.isEmpty(mRationale) ? DEF_RATIONALE : mRationale;
        mIsShowRationale = intent.getBooleanExtra("isShowRationale", false);
        request();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            handleResult(permissions, grantResults);
        }
    }

    private void request() {
        // 需要申请的全部权限
        final List<String> permissionsList = new ArrayList<>();
        // 需要显示解释的权限
        final List<String> permissionsNeeded = new ArrayList<>();
        for (String permission : mPermissions) {
            if (addPermission(permissionsList, permission)) {
                permissionsNeeded.add(permission);
            }
        }
        int permissionListSize = permissionsList.size();
        int permissionsNeededSize = permissionsNeeded.size();
        if (noNeedRequest(permissionListSize)) {
            if (mRequestListener != null) {
                mRequestListener.onAllowed();
            }
            finish();
            return;
        }

        if (needShowRationale(permissionsNeededSize)) {
            showRationale(permissionsList);
        } else {
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]));
        }
    }

    private void showRationale(final List<String> permissionsList) {
        new AlertDialog.Builder(this)
                .setTitle(mRationaleTitle)
                .setMessage(mRationale)
                .setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        requestPermissions(permissionsList.toArray(new String[permissionsList.size()]));
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        finish();
                    }
                })
                .create().show();
    }

    private void requestPermissions(String[] permissions) {
        ActivityCompat.requestPermissions(
                PermissionActivity.this,
                permissions,
                REQUEST_CODE);
    }

    private boolean needShowRationale(int permissionNeededSize) {
        return permissionNeededSize > 0;
    }

    private boolean noNeedRequest(int permissionListSize) {
        return permissionListSize <= 0;
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            if (mIsShowRationale && ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                return true;
            }
        }
        return false;
    }

    private void handleResult(String[] permissions, int[] grantResults) {
        if (permissions.length > 0) {
            int length = permissions.length;
            ArrayMap<String, Boolean> resultMap = new ArrayMap<>(length);
            for (int i = 0; i < length; i++) {
                String permission = permissions[i];
                int code = grantResults[i];
                resultMap.put(permission, code == PackageManager.PERMISSION_GRANTED);
            }
            if (resultMap.containsValue(false)) {
                List<String> refusedPermission = new ArrayList<>();
                for (Map.Entry<String, Boolean> entry : resultMap.entrySet()) {
                    String key = entry.getKey();
                    Boolean value = entry.getValue();
                    if (!value) {
                        refusedPermission.add(key);
                    }
                }
                if (mRequestListener != null) {
                    mRequestListener.refused(refusedPermission);
                }
            } else {
                if (mRequestListener != null) {
                    mRequestListener.onAllowed();
                }
            }
        }
        if (mRequestListener != null) {
            mRequestListener.complete();
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Permission.getDefault().destroyListener();
    }
}
