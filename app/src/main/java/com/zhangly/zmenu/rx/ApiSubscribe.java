package com.zhangly.zmenu.rx;

import com.zhangly.zmenu.api.exception.ServerException;

import rx.Subscriber;

/**
 * Created by zhangluya on 16/8/8.
 */
public abstract class ApiSubscribe<T> extends Subscriber<T> {

    @Override
    public void onNext(T t) {

        onRealNext(t);
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (e instanceof ServerException) {
            onRealError(e.getMessage());
        } else {
            onRealError("网络暂时不可用");
        }
    }

    protected abstract void onRealNext(T t);

    protected abstract void onRealError(String t);
}
