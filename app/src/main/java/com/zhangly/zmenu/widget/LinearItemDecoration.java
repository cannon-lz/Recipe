package com.zhangly.zmenu.widget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by zhangly on 16/8/19.
 */

public class LinearItemDecoration extends RecyclerView.ItemDecoration {

    private final Paint mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public LinearItemDecoration(int color) {
        mLinePaint.setColor(color);
        mLinePaint.setStrokeWidth(3);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        final RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager manager = (LinearLayoutManager) layoutManager;
            final int childCount = manager.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childView = manager.getChildAt(i);
                if (i == 0) {
                    c.drawLine(childView.getLeft(), childView.getTop(), childView.getRight(), childView.getTop(), mLinePaint);
                    c.drawLine(childView.getLeft(), childView.getBottom(), childView.getRight(), childView.getBottom(), mLinePaint);
                } else {
                    c.drawLine(childView.getLeft(), childView.getBottom(), childView.getRight(), childView.getBottom(), mLinePaint);
                }
            }
        }
    }
}
