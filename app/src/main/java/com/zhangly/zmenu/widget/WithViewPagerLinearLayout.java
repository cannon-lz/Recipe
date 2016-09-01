package com.zhangly.zmenu.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import rx.Observable;
import rx.functions.Action1;

/**
 * Created by zhangly on 16/8/19.
 */

public class WithViewPagerLinearLayout extends LinearLayout {

    private static final String TAG = "WithViewPagerLinearLayout";
    private ViewPager mViewPager;

    public WithViewPagerLinearLayout(Context context) {
        super(context);
    }

    public WithViewPagerLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WithViewPagerLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Observable.range(0, getChildCount() - 1).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                final View childView = getChildAt(integer);
                if (childView instanceof ViewPager) {
                    mViewPager = (ViewPager) childView;
                }
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mViewPager.onTouchEvent(event);
    }
}
