package com.zhangly.zmenu.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zhangly.zmenu.R;
import com.zhangly.zmenu.api.bean.Menus;
import com.zhangly.zmenu.rx.image.ImageLoader;
import com.zhangly.zmenu.widget.BaseRecyclerView;

/**
 * Created by zhangluya on 16/8/11.
 */
public class MenusAdapter extends BaseAdapter<Menus.Menu> {

    private static final String TAG = "MenusAdapter";
    private final ImageLoader mImageLoader = ImageLoader.getInstance();
    private static final int VIEW_TYPE_FOOTER = 1;
    private static final int VIEW_TYPE_NORMAL = 2;

    public MenusAdapter(Context context) {
        super(context, R.layout.item_menu);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return VIEW_TYPE_FOOTER;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    @Override
    public void onBindViewHolder(BaseRecyclerView.BaseViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_NORMAL) {
            super.onBindViewHolder(holder, position);
        }
    }

    @Override
    public BaseRecyclerView.BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_FOOTER) {
            return new BaseRecyclerView.BaseViewHolder(mInflater.inflate(R.layout.layout_list_footer, parent, false));
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void bindView(BaseRecyclerView.BaseViewHolder holder, int viewType, int position, Menus.Menu item) {
        if (viewType == VIEW_TYPE_NORMAL) {
            final ImageView imageView = holder.getImageView(R.id.id_iv_menu_pic);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ViewCompat.setTransitionName(imageView, String.format("%sMenuPic", position));
            }
            holder.getTextView(R.id.id_tv_menu_name).setText(item.title);
            holder.getTextView(R.id.id_tv_menu_desc).setText(item.tags);
            mImageLoader.load(item.albums.get(0), imageView).subscribe();
        }
    }
}
