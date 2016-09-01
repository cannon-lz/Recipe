package com.zhangly.zmenu.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * 为RecyclerView增加itemClick。
 *
 * @author android-sh-zhangluya
 * @version 1.0 2015/11/2.
 * @Decription
 */
public class BaseRecyclerView extends RecyclerView {

    private OnItemClickListener mItemClickListener;
    private int mTouchSlop;
    private float mPrevX;

    public interface OnItemClickListener<T> {
        void onItemClick(int position, BaseViewHolder holder, T item);
    }

    public <T> void setOnItemClickListener(OnItemClickListener<T> itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public BaseRecyclerView(Context context) {
        this(context, null);
    }

    public BaseRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                mPrevX = MotionEvent.obtain(ev).getX();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                final float eventX = ev.getX();
                final float xDiff = Math.abs(eventX - mPrevX);
                if (xDiff > mTouchSlop) {
                    return false;
                }
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 用于缓存itemView中的控件对象，可以通过控件的id获取到控件的实例
     * {@link BaseViewHolder#getView(int)}
     */
    public static class BaseViewHolder<T> extends ViewHolder implements OnClickListener {

        private SparseArray<View> mViews = new SparseArray<>();
        private BaseRecyclerView mOwnerCustomRecyclerView;

        public BaseViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOwnerCustomRecyclerView == null) {
                mOwnerCustomRecyclerView = (BaseRecyclerView) getOwnerRecyclerView();
            }
            if (mOwnerCustomRecyclerView != null) {
                final OnItemClickListener<T> listener = mOwnerCustomRecyclerView.mItemClickListener;
                if (listener != null) {
                    listener.onItemClick(getLayoutPosition(), this, (T) v.getTag());
                }
            }
        }

        public ViewPager getViewPager(int viewId) {
            return getView(viewId);
        }

        public FrameLayout getFrameLayout(int viewId) {
            return getView(viewId);
        }

        public RelativeLayout getRelativeLayout(int viewId) {
            return getView(viewId);
        }

        public LinearLayout getLinearLayout(int viewId) {
            return getView(viewId);
        }

        public ProgressBar getProgressBar(int viewId) {
            return getView(viewId);
        }

        public ImageButton getImageButton(int viewId) {
            return getView(viewId);
        }

        public ImageView getImageView(int viewId) {
            return getView(viewId);
        }

        public TextView getTextView(int viewId) {
            return getView(viewId);
        }

        public Button getButton(int viewId) {
            return getView(viewId);
        }

        public <T extends View> T getView(int viewId) {
            View v = mViews.get(viewId);
            if (v == null) {
                v = itemView.findViewById(viewId);
                mViews.put(viewId, v);
            }
            return (T) v;
        }

        /**
         * 通过反射获取ViewHolder中RecyclerView的引用，因为他的访问权限是default，
         * 得到该对像用于得到我们自定义的 {@link OnItemClickListener}
         *
         * @return {@link BaseRecyclerView}
         */
        private RecyclerView getOwnerRecyclerView() {
            try {
                final Field ownerRecyclerView = ViewHolder.class.getDeclaredField("mOwnerRecyclerView");
                ownerRecyclerView.setAccessible(true);
                return (RecyclerView) ownerRecyclerView.get(this);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}