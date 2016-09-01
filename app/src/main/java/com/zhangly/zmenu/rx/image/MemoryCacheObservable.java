package com.zhangly.zmenu.rx.image;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import rx.Observable;

/**
 * Created by zhangluya on 16/8/15.
 */
class MemoryCacheObservable {

    private static final int MEMORY_MAX_SIZE = (int) (Runtime.getRuntime().maxMemory() / 1024 / 8);
    private LruCache<String, Bitmap> mMemoryCache;

    MemoryCacheObservable() {
        mMemoryCache = new LruCache<String, Bitmap>(MEMORY_MAX_SIZE) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() / 1024;//（单位：KB）
            }
        };
    }

    Observable<Bitmap> getCacheObservable(String key) {
        return Observable.just(mMemoryCache.get(key));
    }

    void put(String key, Bitmap bitmap) {

        mMemoryCache.put(key, bitmap);
    }
}
