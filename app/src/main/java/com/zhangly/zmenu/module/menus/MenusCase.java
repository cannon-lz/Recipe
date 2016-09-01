package com.zhangly.zmenu.module.menus;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.zhangly.zmenu.App;
import com.zhangly.zmenu.api.Api;
import com.zhangly.zmenu.api.bean.Menus;
import com.zhangly.zmenu.rx.ApiResultTransformer;
import com.zhangly.zmenu.rx.RxSharedPreference;
import com.zhangly.zmenu.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by zhangly on 16/8/19.
 */

class MenusCase {

    private static final String TAG = "MenusCase";
    private Api mApi;
    private final Gson mGson = new Gson();
    private final List<Menus.Menu> mMenuCache = new ArrayList<>();

    MenusCase(Api api) {
        this.mApi = api;
    }

    Observable<Menus> loadMenus(String menu, final String pn, boolean isRefresh) {
        Map<String, String> params = new HashMap<>();
        params.put("key", Constants.API_KEY);
        params.put("menu", menu);
        params.put("rn", String.valueOf(15));
        final Observable<Menus> localCacheObservable = diskCacheObservable(menu);
        final Observable<Menus> netCacheObservable = netCacheObservable(params);

        if (!"1".equals(pn) || isRefresh) {
            if (isRefresh) {
                RxSharedPreference.clearCache();
                mMenuCache.clear();
            }
            return netCacheObservable;
        }

        return Observable.concat(localCacheObservable, netCacheObservable)
                .first(new Func1<Menus, Boolean>() {
                    @Override
                    public Boolean call(Menus menus) {
                        return menus != null && menus.data != null;
                    }
                });
    }

    private Observable<Menus> diskCacheObservable(final String menu) {
        return RxSharedPreference.getCache(menu).map(new Func1<String, Menus>() {
            @Override
            public Menus call(String s) {
                return mGson.fromJson(s, Menus.class);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<Menus> netCacheObservable(final Map<String, String> params) {
        return diskCacheObservable(params.get("menu")).flatMap(new Func1<Menus, Observable<Menus>>() {
            @Override
            public Observable<Menus> call(Menus menus) {
                final String pn = menus == null || TextUtils.isEmpty(menus.pn) ? String.valueOf(1) : String.valueOf(Integer.valueOf(menus.pn) + 1);
                Log.i(TAG, "开始加载第。" + pn + "数据。");
                params.put("pn", pn);
                return mApi.getMenus(params)
                        .compose(new ApiResultTransformer<Menus>())
                        .doOnNext(new Action1<Menus>() {
                            @Override
                            public void call(Menus menus) {
                                mMenuCache.addAll(mMenuCache.size(), menus.data);
                                final Menus cacheMenu = new Menus();
                                cacheMenu.data = mMenuCache;
                                cacheMenu.pn = pn;
                                RxSharedPreference.cache(params.get("menu"), mGson.toJson(cacheMenu));
                                Log.i(TAG, String.format("网络返回的数据大小 %s", menus.data.size()));
                                Log.i(TAG, "从网络加载新数据并缓存。" + mMenuCache.size());
                            }
                        });
            }
        }).observeOn(AndroidSchedulers.mainThread());
    }
}
