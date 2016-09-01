package com.zhangly.zmenu.module;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zhangly.zmenu.BaseFragment;
import com.zhangly.zmenu.FragmentHelper;
import com.zhangly.zmenu.R;
import com.zhangly.zmenu.module.menus.HomeFragment;

public class HostActivity extends AppCompatActivity {

    private static final String TAG = "FragmentHelper";
    private FragmentHelper mFragmentation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_host);
        mFragmentation = new FragmentHelper(getSupportFragmentManager(), R.id.id_rl_host_container);
        if (savedInstanceState == null) {
            mFragmentation.attachHost(HomeFragment.newFragment());
        }
    }

    public FragmentHelper getFragmentation() {
        return mFragmentation;
    }

    @Override
    public void onBackPressed() {
        final Fragment currentFragment = mFragmentation.getCurrentFragment();
        if (currentFragment instanceof BaseFragment) {
            BaseFragment current = (BaseFragment) currentFragment;
            if (current.onBackPressed()) {
                return;
            }
        }
        super.onBackPressed();
    }

}
