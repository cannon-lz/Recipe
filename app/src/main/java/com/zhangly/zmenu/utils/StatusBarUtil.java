package com.zhangly.zmenu.utils;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zhangly on 16/8/25.
 */

public final class StatusBarUtil {

    private StatusBarUtil () {}

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static void fixStatusBar(View root) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final View placeholderView = new View(root.getContext());
            placeholderView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(root.getContext())));
            if (root instanceof ViewGroup) {
                ViewGroup rootGroup = (ViewGroup) root;
                rootGroup.addView(placeholderView, 0);
            }
        }
    }
}
