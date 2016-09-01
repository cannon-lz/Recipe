package com.zhangly.zmenu.rx;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.zhangly.zmenu.App;

import rx.Observable;
import rx.functions.Func0;

/**
 * Created by zhangly on 16/8/19.
 */

public final class RxSharedPreference {

    private RxSharedPreference() {
    }

    public static Observable<String> getCache(final String key) {
        return Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {
                final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(App.getContext());
                return Observable.just(sharedPreferences.getString(key, ""));
            }
        });
    }

    public static void cache(String key, String value) {
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(App.getContext());
        sp.edit().putString(key, value).apply();
    }

    public static void clearCache() {
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(App.getContext());
        sp.edit().clear().commit();
    }
}
