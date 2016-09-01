package com.zhangly.zmenu.api;

import com.zhangly.zmenu.rx.ApiResultTransformer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import rx.Observable;

/**
 * Created by zhangluya on 16/8/11.
 */
public class ApiProxy {

    private Api mApiProxy;

    ApiProxy (final Api origin) {

        mApiProxy = (Api) Proxy.newProxyInstance(Api.class.getClassLoader(), new Class[]{Api.class},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                        String name = method.getName();

                        if ("loadImage".equals(name)) {
                            return method.invoke(origin, objects);
                        } else {
                            return ((Observable)method.invoke(origin, objects)).compose(new ApiResultTransformer());
                        }
                    }
                });
    }

    Api getProxyApi() {
        return mApiProxy;
    }
}
