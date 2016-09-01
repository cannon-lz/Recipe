package com.zhangly.zmenu;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionSet;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * Created by zhangly on 16/8/23.
 */

public class FragmentHelper {

    private final static String TAG = "FragmentHelper";

    private FragmentManager mManager;
    private int mContainerId;

    public FragmentHelper(FragmentManager manager, int containerId) {
        this.mManager = manager;
        mContainerId = containerId;
    }

    public void attachHost(Fragment target) {
        start(null, target, null, null);
    }

    public void start(Fragment from, Fragment target, View sharedView, String sharedName) {
        final FragmentTransaction fts = mManager.beginTransaction();
        if (sharedView != null) {
            fts.addSharedElement(sharedView, sharedName);
        } else {
            fts.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        }
        if (from != null) {
            fts.hide(from);
        }
        fts.add(mContainerId, target).addToBackStack(target.getClass().getName()).commit();
    }

    public Fragment getCurrentFragment() {
        final List<Fragment> fragments = mManager.getFragments();
        if (fragments == null) {
            return null;
        }
        for (int i = fragments.size() - 1; i >= 0; i--) {
            final Fragment fragment = fragments.get(i);
            if (fragment != null) {
                return fragment;
            }
        }
        return null;
    }


    /**
     * 转换动画
     * Created by wangchenlong on 15/11/5.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static class DetailTransition extends TransitionSet {
        public DetailTransition() {
            init();
        }

        // 允许资源文件使用
        public DetailTransition(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        private void init() {
            setOrdering(ORDERING_TOGETHER);
            addTransition(new ChangeBounds()).
                    addTransition(new ChangeTransform()).
                    addTransition(new ChangeImageTransform());
        }
    }
}
