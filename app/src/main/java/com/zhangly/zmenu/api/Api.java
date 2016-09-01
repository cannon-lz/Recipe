package com.zhangly.zmenu.api;

import com.zhangly.zmenu.api.bean.Menus;
import com.zhangly.zmenu.api.bean.Response;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by zhangluya on 16/8/8.
 */
public interface Api {

    @FormUrlEncoded
    @POST("http://apis.juhe.cn/cook/query.php")
    Observable<Response<Menus>> getMenus(@FieldMap Map<String, String> request);

    @GET
    Observable<ResponseBody> loadImage(@Url String url);
}
