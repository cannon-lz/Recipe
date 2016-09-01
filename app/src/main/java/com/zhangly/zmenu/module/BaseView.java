package com.zhangly.zmenu.module;

/**
 * Created by zhangluya on 16/8/8.
 */
public interface BaseView<Presenter> {

    void showLoading();

    void closeLoading();

    void toast(String msg);

    void showDialog(String msg);

    void setPresenter(Presenter presenter);
}
