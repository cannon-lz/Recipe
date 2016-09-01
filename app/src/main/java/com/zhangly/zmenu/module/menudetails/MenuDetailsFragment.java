package com.zhangly.zmenu.module.menudetails;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhangly.zmenu.BaseFragment;
import com.zhangly.zmenu.FragmentHelper;
import com.zhangly.zmenu.R;
import com.zhangly.zmenu.adapter.BaseAdapter;
import com.zhangly.zmenu.api.bean.Menus;
import com.zhangly.zmenu.rx.image.ImageLoader;
import com.zhangly.zmenu.utils.StatusBarUtil;
import com.zhangly.zmenu.widget.BaseRecyclerView;
import com.zhangly.zmenu.widget.LinearItemDecoration;
import com.zhangly.zmenu.widget.WithRecyclerViewScrollView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhangluya on 16/8/18.
 */
public class MenuDetailsFragment extends BaseFragment {


    private static final String TAG = "MenuDetailsFragment";
    @BindView(R.id.id_scv_root) WithRecyclerViewScrollView mScvRoot;
    @BindView(R.id.id_iv_menu_pic) ImageView mIvMenuPic;
    @BindView(R.id.id_tv_menu_name) TextView mTvMenuName;
    @BindView(R.id.id_tv_menu_desc) TextView mTvMenuDesc;
    @BindView(R.id.id_rcl_needed_material) BaseRecyclerView mRclNeededMaterial;
    @BindView(R.id.id_rcl_method) BaseRecyclerView mRclMethod;

    private Menus.Menu mMenus;

    public static MenuDetailsFragment newFragment(Menus.Menu menu) {
        MenuDetailsFragment menuDetailsFragment = new MenuDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("menu", menu);
        menuDetailsFragment.setArguments(bundle);
        return menuDetailsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_menu_details, container, false);
        StatusBarUtil.fixStatusBar(view);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.id_tl_toolbar);
        getHostActivity().setSupportActionBar(toolbar);
        getHostActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getHostActivity().getSupportActionBar().setTitle("做法");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getChildFragmentManager().popBackStackImmediate();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMenus = getArguments().getParcelable("menu");
        ImageLoader.getInstance().load(mMenus.albums.get(0), mIvMenuPic).subscribe();
        mTvMenuName.setText(mMenus.title);
        mTvMenuDesc.setText(mMenus.imtro);
        mRclNeededMaterial.addItemDecoration(new LinearItemDecoration(getResources().getColor(R.color.colorDivider)));
        mRclNeededMaterial.setAdapter(
            new BaseAdapter<MaterialConverter.MaterialPair>(getActivity(), MaterialConverter.convert(mMenus), R.layout.item_materail) {
                @Override
                public void bindView(BaseRecyclerView.BaseViewHolder holder, int viewType, int position, MaterialConverter.MaterialPair item) {
                    holder.getTextView(R.id.id_tv_key).setText(item.key);
                    holder.getTextView(R.id.id_tv_value).setText(item.value);
                }
            }
        );
        mRclMethod.setAdapter(
            new BaseAdapter<Menus.Step>(getActivity(), mMenus.steps, R.layout.item_method) {
                @Override
                public void bindView(BaseRecyclerView.BaseViewHolder holder, int viewType, int position, Menus.Step item) {
                    final ImageView methodItemPic = holder.getImageView(R.id.id_iv_method_rationale_pic);
                    ViewCompat.setTransitionName(methodItemPic, String.format("%sMenuStepPager", position));
                    holder.getTextView(R.id.id_tv_method_index).setText(String.valueOf(++position));
                    ImageLoader.getInstance().load(item.img, methodItemPic).subscribe();
                    holder.getTextView(R.id.id_tv_method_desc).setText(item.step);
                }
            }
        );
        mRclMethod.setOnItemClickListener(new BaseRecyclerView.OnItemClickListener<Menus.Step>() {
            @Override
            public void onItemClick(int position, BaseRecyclerView.BaseViewHolder v, Menus.Step item) {
                final SeeFullScreenFragment seeFullScreenFragment = SeeFullScreenFragment.newFragment(mMenus.steps, position);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    seeFullScreenFragment.setSharedElementEnterTransition(new FragmentHelper.DetailTransition());
                    seeFullScreenFragment.setEnterTransition(new Slide());
                    startFragment(seeFullScreenFragment, v.getImageView(R.id.id_iv_method_rationale_pic), "MenuStepPager");
                } else {
                    startFragment(seeFullScreenFragment, null, null);
                }
            }
        });
    }

    @OnClick(R.id.id_tv_click_full)
    void seeFull(View v) {
        startFragment(SeeFullScreenFragment.newFragment(mMenus.steps, 0), null, null);
    }
}
