package com.zhangly.zmenu.api.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhangluya on 16/8/8.
 */
public class Response<Result> {

    @SerializedName("resultcode")
    public String resultCode;
    public String reason;
    @SerializedName("error_code")
    public int errorCode;
    public Result result;
}
