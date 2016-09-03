package com.zhangly.zmenu.module.menus;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.TransitionSet;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zhangly.zmenu.BaseFragment;
import com.zhangly.zmenu.FragmentHelper;
import com.zhangly.zmenu.R;
import com.zhangly.zmenu.adapter.MenusAdapter;
import com.zhangly.zmenu.api.bean.Menus;
import com.zhangly.zmenu.module.menudetails.MenuDetailsFragment;
import com.zhangly.zmenu.utils.StatusBarUtil;
import com.zhangly.zmenu.widget.BaseRecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhangluya on 16/8/8.
 */
public class MenuListFragment extends BaseFragment implements MenusContract.View {

    private static final String TAG = "MenuListFragment";
    @BindView(R.id.id_sr_refresh) SwipeRefreshLayout mSrRefreshLayout;
    @BindView(R.id.id_rcl_menu_list) BaseRecyclerView mRclMenuList;

    private MenusContract.Presenter mPresenter;

    public static MenuListFragment newFragment(String menu, boolean isHome, boolean isAddToolbar) {
        Bundle bundle = new Bundle();
        bundle.putString("menu", menu);
        bundle.putBoolean("flag", isHome);
        bundle.putBoolean("isAddToolbar", isAddToolbar);
        MenuListFragment menuListFragment = new MenuListFragment();
        menuListFragment.setArguments(bundle);
        return menuListFragment;
    }

    public static MenuListFragment newFragment(String menu, boolean isHome) {
        return newFragment(menu, isHome, false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final boolean isAddToolbar = getArguments().getBoolean("isAddToolbar");
        int targetLayout = isAddToolbar ? R.layout.fragment_menu_list_toolbar : R.layout.fragment_menu_list;
        final View view = inflater.inflate(targetLayout, container, false);
        if (isAddToolbar) {
            StatusBarUtil.fixStatusBar(view);
            Toolbar toolbar = (Toolbar) view.findViewById(R.id.id_tl_toolbar);
            getHostActivity().setSupportActionBar(toolbar);
            getHostActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getHostActivity().getSupportActionBar().setTitle(getArguments().getString("menu"));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager().popBackStackImmediate();
                }
            });
        }
        return view;
    }

    private int[] getSomeVisibleItemPosition(RecyclerView recyclerView) {
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if(layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager manager = (LinearLayoutManager) layoutManager;
            return new int[]{ manager.findFirstCompletelyVisibleItemPosition(), manager.findLastCompletelyVisibleItemPosition() };
        }
        return null;
    }

    private int mFirstVisibleItemPosition;
    private int mLastVisibleItemPosition;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.i(TAG, String.format("%s Fragment 创建。", getArguments().getString("menu")));

        ButterKnife.bind(this, view);
        MenusAdapter menusAdapter = new MenusAdapter(getActivity());
        mSrRefreshLayout.setOnRefreshListener(mOnRefreshListener);
        mRclMenuList.setAdapter(menusAdapter);
        mRclMenuList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                mFirstVisibleItemPosition = getSomeVisibleItemPosition(recyclerView)[0];
                mLastVisibleItemPosition = getSomeVisibleItemPosition(recyclerView)[1];
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mLastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1
                        && mFirstVisibleItemPosition != 0 && newState == RecyclerView.SCROLL_STATE_IDLE) {

                    mPresenter.loadMore();
                }

            }
        });
        mRclMenuList.setOnItemClickListener(new BaseRecyclerView.OnItemClickListener<Menus.Menu>() {
            @Override
            public void onItemClick(int position, BaseRecyclerView.BaseViewHolder holder, Menus.Menu item) {
                final MenuDetailsFragment fragment = MenuDetailsFragment.newFragment(item);
                Fragment thisFragment = getParentFragment() == null ? MenuListFragment.this : getParentFragment();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    thisFragment.setExitTransition(new Slide());
                    fragment.setEnterTransition(new Slide());
                    fragment.setSharedElementEnterTransition(new FragmentHelper.DetailTransition());
                    startFragment(thisFragment, fragment, holder.getImageView(R.id.id_iv_menu_pic), "MenuPic");
                } else {
                    startFragment(thisFragment, fragment, null ,null);
                }
            }
        });
        if (getArguments().getBoolean("flag")) {
            onViewCreatedOrVisible();
        }
    }

    @Override
    protected void onViewCreatedOrVisible() {
        //mOnRefreshListener.onRefresh();
        mPresenter.loadMenus(1, false);
    }

    @Override
    public void showMenus(List<Menus.Menu> menus) {
        mSrRefreshLayout.setRefreshing(false);
        MenusAdapter adapter = (MenusAdapter) mRclMenuList.getAdapter();
        if (mPresenter.isLoadMore()) {
            adapter.addData(menus);
        } else {
            adapter.refreshData(menus);
        }
    }

    @Override
    public void showLoading() {
        //mLoading.show();
    }

    @Override
    public void closeLoading() {
        //mLoading.hide();
    }

    @Override
    public void toast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showDialog(String msg) {

    }

    @Override
    public void setPresenter(MenusContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private SwipeRefreshLayout.OnRefreshListener mOnRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            mPresenter.loadMenus(1, true);
        }
    };


}
