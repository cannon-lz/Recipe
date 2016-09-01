package com.zhangly.zmenu.module.menus;

import android.util.Log;

import com.zhangly.zmenu.api.ApiHelper;
import com.zhangly.zmenu.api.bean.Menus;
import com.zhangly.zmenu.rx.ApiSubscribe;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.functions.Action0;

/**
 * Created by zhangluya on 16/8/8.
 */
public class MenusPresenter implements MenusContract.Presenter {

    private static final String TAG = "MenusPresenter";
    private String mMenuType;
    private MenusContract.View mView;
    private Subscription mSubscription;
    private final MenusCase mUseCase;

    private int mCurPn = 1;
    private final List<Menus.Menu> mMenus = new ArrayList<>();

    public MenusPresenter(MenusContract.View view, String menuType) {
        mUseCase = new MenusCase(ApiHelper.API.getApi());
        this.mView = view;
        mMenuType = menuType;
        mView.setPresenter(this);
    }

    @Override
    public void preLoad() {

    }

    @Override
    public void onViewDestroy() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    @Override
    public void loadMenus(int pn, boolean isRefresh) {
        mCurPn = pn;
        mSubscription = mUseCase.loadMenus(mMenuType, String.valueOf(mCurPn), isRefresh).doOnSubscribe(new Action0() {
            @Override
            public void call() {
                mView.showLoading();
            }
        }).subscribe(new ApiSubscribe<Menus>() {

            @Override
            protected void onRealNext(Menus menus) {
                Log.i(TAG, String.format("实际接收的数据为 %s", menus.data.size()));
                mView.showMenus(menus.data);
            }

            @Override
            protected void onRealError(String t) {
                mView.closeLoading();
                mView.toast(t);
            }

            @Override
            public void onCompleted() {
                mView.closeLoading();
            }
        });
    }

    @Override
    public void loadMore() {
        loadMenus(++mCurPn, false);
    }

    @Override
    public boolean isLoadMore() {
        return mCurPn != 1;
    }
}
