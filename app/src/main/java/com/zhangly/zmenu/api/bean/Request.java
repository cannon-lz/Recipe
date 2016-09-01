package com.zhangly.zmenu.api.bean;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by zhangluya on 16/8/8.
 */
public class Request {

    public static final int ARRAY = 0;
    public static final int STRING = 1;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ARRAY, STRING})
    public @interface AlbumType {}

    public final String key = "fe7c57e83f7d1badf64e8c2d49ca5c22";
    public String menu;
    public String dtype;
    public String pn;
    public String rn;
    @AlbumType public int albums;
}
