package com.zhangly.zmenu.widget;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhangly.zmenu.R;

/**
 * Created by zhangly on 16/8/22.
 */

public class ViewPagerIndicator extends LinearLayout {

    private static final String TAG = "ViewPagerIndicator";
    private TextView mTvIndicatorText;
    private ViewPager mViewPager;
    private int mTabWidth;

    public ViewPagerIndicator(Context context) {
        this(context, null);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.layout_page_indicator, this);
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (mViewPager != null) {
                    mTabWidth = mViewPager.getWidth() / mViewPager.getAdapter().getCount();
                    final ViewGroup.LayoutParams lp = getLayoutParams();
                    lp.width = mTabWidth;
                    setLayoutParams(lp);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && mViewPager != null) {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTvIndicatorText = (TextView) findViewById(R.id.id_tv_indicator_text);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mViewPager != null) {
            mViewPager.removeOnPageChangeListener(mOnPageChangeListener);
        }
    }

    public void setUpViewPager(ViewPager viewPager) {
        mViewPager = viewPager;
        mViewPager.addOnPageChangeListener(mOnPageChangeListener);
        mOnPageChangeListener.onPageSelected(0);
    }

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (position > 0) {
                if (mTabWidth == 0) {
                    mTabWidth = mViewPager.getWidth() / mViewPager.getAdapter().getCount();
                }
                float translationX = mTabWidth * (positionOffset + position);
                setTranslationX(translationX);
            }

        }

        @Override
        public void onPageSelected(int position) {
            mTvIndicatorText.setText(String.valueOf(++position));
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

}
