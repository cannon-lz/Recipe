package com.zhangly.zmenu.rx.image;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.zhangly.zmenu.App;
import com.zhangly.zmenu.utils.DiskLruCache;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zhangluya on 16/8/15.
 */
class DiskCacheObservable {

    private static final long MAX_EXTERNAL_CACHE_SPACE = 10 * 1024 * 1024;

    private DiskLruCache mDiskCache;
    private final Lock mInitDiskLock = new ReentrantLock();

    private DiskCacheObservable(final File file) {
        Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                mInitDiskLock.lock();
                File cacheFile = file == null ? getDiskCacheDir(App.getContext(), "") : file;
                try {
                    mDiskCache = DiskLruCache.open(cacheFile, getAppVersion(), 1, MAX_EXTERNAL_CACHE_SPACE);
                    subscriber.onCompleted();
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    mInitDiskLock.unlock();
                }
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    DiskCacheObservable() {
        this(null);
    }

    private File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (context.getExternalCacheDir() != null) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    void put(final String key, final Bitmap bitmap) {
        Observable.create(new Observable.OnSubscribe<Void>() {
            @Override
            public void call(Subscriber<? super Void> subscriber) {
                mInitDiskLock.lock();
                try {
                    DiskLruCache.Editor edit = mDiskCache.edit(md5Url(key));
                    OutputStream outputStream = edit.newOutputStream(0);
                    if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)) {
                        edit.commit();
                    } else {
                        edit.abort();
                    }
                    subscriber.onNext(null);
                    subscriber.onCompleted();
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    mInitDiskLock.unlock();
                }
            }
        }).subscribeOn(Schedulers.io()).subscribe();

    }

    Observable<Bitmap> getCacheObservable(final String key) {
        return Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                mInitDiskLock.lock();
                try {
                    DiskLruCache.Snapshot snapshot = mDiskCache.get(md5Url(key));
                    if (snapshot != null) {
                        InputStream inputStream = snapshot.getInputStream(0);
                        subscriber.onNext(BitmapFactory.decodeStream(inputStream));
                    }
                    subscriber.onCompleted();
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                } finally {
                    mInitDiskLock.unlock();
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

    }

    private String md5Url(String url) {
        String cacheKey = String.valueOf(url.hashCode());
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(url.getBytes());
            cacheKey = bytesToHexString(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                builder.append('0');
            }
            builder.append(hex);
        }
        return builder.toString();
    }

    private int getAppVersion() {
        int versionCode = 0;
        PackageManager packageManager = App.getContext().getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(App.getContext().getPackageName(), 0);

            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionCode;
    }

}
