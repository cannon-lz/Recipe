package com.zhangly.zmenu.module.menus;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.zhangly.zmenu.BaseFragment;
import com.zhangly.zmenu.HomeConfig;
import com.zhangly.zmenu.R;
import com.zhangly.zmenu.adapter.HomePageAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnEditorAction;

/**
 * Created by zhangly on 16/8/23.
 */

public class HomeFragment extends BaseFragment {

    @BindView(R.id.id_vp_home_pager) ViewPager mVpHomePager;
    @BindView(R.id.id_tbl_tab) TabLayout mTab;

    public static HomeFragment newFragment() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_test, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initHomeFragment();
    }

    @Override
    public boolean onBackPressed() {
        getHostActivity().finish();
        return true;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @OnEditorAction(R.id.id_et_search)
    boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
        if (id == EditorInfo.IME_ACTION_SEARCH) {
            final InputMethodManager inputMethodManager = (InputMethodManager) textView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            String keyword = textView.getText().toString();
            final MenuListFragment menuListFragment = MenuListFragment.newFragment(keyword, true, true);
            new MenusPresenter(menuListFragment, keyword);
            startFragment(menuListFragment, null, null);
            return true;
        }
        return false;
    }

    private void initHomeFragment() {
        HomePageAdapter homePageAdapter = new HomePageAdapter(getChildFragmentManager(), HomeConfig.TITLES);
        List<Fragment> homeModules = new ArrayList<>();
        for (String title : homePageAdapter.getTitles()) {
            MenuListFragment menuListFragment = MenuListFragment.newFragment(title, false);
            homeModules.add(menuListFragment);
            new MenusPresenter(menuListFragment, title);
        }
        homePageAdapter.setFragments(homeModules);
        mVpHomePager.setAdapter(homePageAdapter);
        mTab.setupWithViewPager(mVpHomePager);
    }
}
