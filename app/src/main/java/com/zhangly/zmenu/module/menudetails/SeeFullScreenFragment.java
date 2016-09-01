package com.zhangly.zmenu.module.menudetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhangly.zmenu.BaseFragment;
import com.zhangly.zmenu.R;
import com.zhangly.zmenu.api.bean.Menus;
import com.zhangly.zmenu.utils.StatusBarUtil;
import com.zhangly.zmenu.widget.ViewPagerIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnPageChange;
import rx.Observable;
import rx.functions.Action1;

/**
 * Created by zhangly on 16/8/19.
 */

public class SeeFullScreenFragment extends BaseFragment {

    private static final String TAG = "SeeFullScreen";
    @BindView(R.id.id_vp_step_pic) ViewPager mVpStepPic;
    @BindView(R.id.id_vpi_indicator) ViewPagerIndicator mVpPagerIndicator;
    @BindView(R.id.id_tv_step) TextView mTvStep;

    private ArrayList<Menus.Step> mSteps;

    public static SeeFullScreenFragment newFragment(ArrayList<Menus.Step> steps, int position) {

        final SeeFullScreenFragment seeFullScreenFragment = new SeeFullScreenFragment();
        final Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("steps", steps);
        bundle.putInt("position", position);
        seeFullScreenFragment.setArguments(bundle);
        return seeFullScreenFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_full_screen_menu, container, false);
        StatusBarUtil.fixStatusBar(view);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.id_tl_toolbar);
        getHostActivity().setSupportActionBar(toolbar);
        getHostActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getHostActivity().getSupportActionBar().setTitle("第 1 步");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStackImmediate();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSteps = getArguments().getParcelableArrayList("steps");
        final List<ImageView> ivSteps = new ArrayList<>(mSteps.size());
        Observable.range(0, mSteps.size()).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                final ImageView imageView = new ImageView(getActivity());
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setTag(mSteps.get(integer));
                ivSteps.add(imageView);
            }
        });

        mVpStepPic.setAdapter(new StepViewPagerAdapter(ivSteps));
        mVpPagerIndicator.setUpViewPager(mVpStepPic);
        int position = getArguments().getInt("position");
        mVpStepPic.setCurrentItem(position);
        mTvStep.setText(mSteps.get(position).step);
        getHostActivity().getSupportActionBar().setTitle(String.format("第 %s 步", ++position));
    }

    @OnPageChange(R.id.id_vp_step_pic)
    void onPagerSelected(int position) {
        mTvStep.setText(mSteps.get(position).step);
        getHostActivity().getSupportActionBar().setTitle(String.format("第 %s 步", ++position));
    }
}
