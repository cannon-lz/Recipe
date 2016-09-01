package com.zhangly.zmenu.module.menus;

import com.zhangly.zmenu.api.bean.Menus;
import com.zhangly.zmenu.module.BasePresenter;
import com.zhangly.zmenu.module.BaseView;

import java.util.List;

/**
 * Created by zhangluya on 16/8/8.
 */
public interface MenusContract {

    interface View extends BaseView<Presenter> {

        void showMenus(List<Menus.Menu> menus);
    }

    interface Presenter extends BasePresenter {

        void loadMenus(int pn, boolean isRefresh);

        void loadMore();

        boolean isLoadMore();
    }
}
