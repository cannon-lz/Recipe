package com.zhangly.zmenu.utils.permission.callback;

import java.util.List;

/**
 * Created by zhangluya on 16/8/17.
 */
public interface OnRequestPermissionListener {

    /**
     * 允许授权时
     *
     */
    void onAllowed();

    /**
     * 拒绝某个权限
     *
     * @param refusedPermissions 被拒绝的权限
     */
    void refused(List<String> refusedPermissions);

    /**
     * 权限申请完成，批量申请权限时无论中途是否有被拒绝的权限都会回调该方法
     *
     */
    void complete();
}
