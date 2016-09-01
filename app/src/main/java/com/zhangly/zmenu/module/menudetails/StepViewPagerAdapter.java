package com.zhangly.zmenu.module.menudetails;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import com.zhangly.zmenu.api.bean.Menus;
import com.zhangly.zmenu.rx.image.ImageLoader;

import java.util.List;

/**
 * Created by zhangly on 16/8/19.
 */

public class StepViewPagerAdapter extends PagerAdapter {

    private List<ImageView> mIvSteps;

    public StepViewPagerAdapter(List<ImageView> ivSteps) {
        this.mIvSteps = ivSteps;
    }

    @Override
    public int getCount() {
        return mIvSteps.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final ImageView stepView = mIvSteps.get(position);
        final ViewParent parent = stepView.getParent();
        if (parent != null) {
            ViewGroup group = (ViewGroup) parent;
            group.removeView(stepView);
        }
        Menus.Step step = (Menus.Step) stepView.getTag();
        ImageLoader.getInstance().load(step.img, stepView).subscribe();
        container.addView(stepView);
        return stepView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mIvSteps.get(position));
    }


}
