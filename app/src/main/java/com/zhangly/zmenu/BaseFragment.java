package com.zhangly.zmenu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;

import com.zhangly.zmenu.module.HostActivity;

import butterknife.ButterKnife;

/**
 * Created by zhangluya on 16/8/18.
 */
public class BaseFragment extends Fragment {

    private static final String TAG = "BaseFragment";

    protected boolean mIsViewInitComplete;
    protected boolean mIsVisibleToUser;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mIsViewInitComplete = true;
        if (mIsVisibleToUser) {
            onViewCreatedOrVisible();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mIsVisibleToUser = isVisibleToUser;
        if (mIsVisibleToUser && mIsViewInitComplete) {
            onViewCreatedOrVisible();
        }
    }

    public HostActivity getHostActivity() {
        return (HostActivity) getActivity();
    }

    public FragmentHelper getFragmentation() {
        return getHostActivity().getFragmentation();
    }

    public void startFragment(Fragment to, View sharedView, String sharedName) {
        getFragmentation().start(this, to, sharedView, sharedName);
    }

    public void startFragment(Fragment from, Fragment to, View sharedView, String sharedName) {
        getFragmentation().start(from, to, sharedView, sharedName);
    }

    public boolean onBackPressed() {
        return false;
    }

    protected void onViewCreatedOrVisible() {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.i(TAG, String.format("%s %s", getClass().getSimpleName(), hidden));
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        Log.i(TAG, String.format("%s onCreateAnimation; enter = %s", getClass().getSimpleName(), enter));
        if (nextAnim != 0) {
            log(nextAnim);
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    private static final int FRAGMENT_ENTER = R.anim.fragment_enter;
    private static final int FRAGMENT_EXIT = R.anim.fragment_exit;
    private static final int FRAGMENT_POP_ENTER = R.anim.fragment_pop_enter;
    private static final int FRAGMENT_POP_EXIT = R.anim.fragment_pop_exit;

    private void log(int anim) {
        String curAnim = null;
        switch (anim) {
            case FRAGMENT_ENTER:
                curAnim = "enter";
                break;
            case FRAGMENT_EXIT:
                curAnim = "exit";
                break;
            case FRAGMENT_POP_ENTER:
                curAnim = "popEnter";
                break;
            case FRAGMENT_POP_EXIT:
                curAnim = "popExit";
                break;
        }
        Log.i(TAG, String.format("%s 此时动画为%s", getClass().getSimpleName(), curAnim));
    }
}
