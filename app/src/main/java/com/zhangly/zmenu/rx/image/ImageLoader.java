package com.zhangly.zmenu.rx.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.ArrayMap;
import android.widget.ImageView;

import com.zhangly.zmenu.R;
import com.zhangly.zmenu.api.ApiHelper;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by zhangluya on 16/8/12.
 */
public class ImageLoader {

    private static final ArrayMap<Integer, String> CACHE_KEYS_MAP = new ArrayMap<>();

    private volatile static ImageLoader sInstance;

    private final DiskCacheObservable mDiskCache;
    private final MemoryCacheObservable mMemoryCache;

    public static ImageLoader getInstance() {
        if (sInstance == null) {
            synchronized (ImageLoader.class) {
                if (sInstance == null) {
                    sInstance = new ImageLoader();
                }
            }
        }
        return sInstance;
    }

    private ImageLoader () {
        mDiskCache = new DiskCacheObservable();
        mMemoryCache = new MemoryCacheObservable();
    }

    public DiskCacheObservable getDIskCacheObservable() {
        return mDiskCache;
    }

    public Observable<Bitmap> load(final String url, final ImageView imageView) {
        if (imageView != null) {
            imageView.setImageResource(R.drawable.perch_bg);
            CACHE_KEYS_MAP.put(imageView.hashCode(), url);
        }

        Observable<Bitmap> netCacheObs = ApiHelper.API.getApi().loadImage(url).map(new Func1<ResponseBody, Bitmap>() {
            @Override
            public Bitmap call(ResponseBody responseBody) {
                return BitmapFactory.decodeStream(responseBody.byteStream());
            }
        }).filter(new Func1<Bitmap, Boolean>() {
            @Override
            public Boolean call(Bitmap bitmap) {
                return bitmap != null;
            }
        }).doOnNext(new Action1<Bitmap>() {
            @Override
            public void call(Bitmap bitmap) {
                mDiskCache.put(url, bitmap);
                mMemoryCache.put(url, bitmap);
            }
        }).observeOn(AndroidSchedulers.mainThread());

        Observable<Bitmap> diskObs = mDiskCache.getCacheObservable(url).filter(new Func1<Bitmap, Boolean>() {
            @Override
            public Boolean call(Bitmap bitmap) {
                return bitmap != null;
            }
        }).doOnNext(new Action1<Bitmap>() {
            @Override
            public void call(Bitmap bitmap) {
                mMemoryCache.put(url, bitmap);
            }
        });

        return Observable.concat(mMemoryCache.getCacheObservable(url), diskObs, netCacheObs)
                .first(new Func1<Bitmap, Boolean>() {
                    @Override
                    public Boolean call(Bitmap bitmap) {
                        return bitmap != null;
                    }
                })
                .doOnNext(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        if (url != null  && imageView != null && url.equals(CACHE_KEYS_MAP.get(imageView.hashCode()))) {
                            imageView.setImageBitmap(bitmap);
                        }
                    }
                });
    }
}
