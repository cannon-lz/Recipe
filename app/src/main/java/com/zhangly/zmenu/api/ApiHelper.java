package com.zhangly.zmenu.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

/**
 * Created by zhangluya on 16/8/8.
 */
public enum  ApiHelper {

    API;

    private Api mProxyApi;
    private Api mApi;

    ApiHelper() {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = httpClientBuilder.addInterceptor(loggingInterceptor)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .client(httpClient)
                .baseUrl("http://apis.juhe.cn/");
        mApi = builder.build().create(Api.class);
        mProxyApi = new ApiProxy(mApi).getProxyApi();
    }

    public Api getProxyApi() {
        return mProxyApi;
    }

    public Api getApi() {
        return mApi;
    }

}
