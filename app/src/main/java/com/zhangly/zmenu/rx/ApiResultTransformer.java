package com.zhangly.zmenu.rx;

import com.zhangly.zmenu.api.bean.Response;
import com.zhangly.zmenu.api.exception.ServerException;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by zhangluya on 16/8/15.
 */
public class ApiResultTransformer<T> implements Observable.Transformer<Response<T>, T> {

    @Override
    public Observable<T> call(Observable<Response<T>> observable) {
        return observable.flatMap(new Func1<Response<T>, Observable<T>>() {
            @Override
            public Observable<T> call(final Response<T> tResponse) {
                String resultCode = tResponse.resultCode;

                if ("200".equals(resultCode)) {

                    return Observable.create(new Observable.OnSubscribe<T>() {
                        @Override
                        public void call(Subscriber<? super T> subscriber) {
                            try {
                                subscriber.onNext(tResponse.result);
                                subscriber.onCompleted();
                            } catch (Exception e) {
                                subscriber.onError(e);
                            }
                        }
                    });
                } else {

                    return Observable.error(new ServerException(tResponse.reason));
                }
            }
        });
    }
}
