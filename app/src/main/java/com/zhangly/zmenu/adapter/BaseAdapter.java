package com.zhangly.zmenu.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.zhangly.zmenu.widget.BaseRecyclerView;

import java.util.List;

/**
 *  RecyclerView 通用Adapter，只需实现binView方法利用BaseViewHolder将对象信息设置到ItemView上。
 *
 * @author zhangluya
 * @version 1.0 2015/11/3.
 */
public abstract class BaseAdapter<D> extends RecyclerView.Adapter<BaseRecyclerView.BaseViewHolder> {

    private static final String TAG = "BaseAdapter";
    protected Context mContext;
    protected List<D> itemList;
    protected LayoutInflater mInflater;
    private int mLayoutId;

    public BaseAdapter(Context context, List<D> data, int layoutId) {
        mContext = context;
        itemList = data;
        mLayoutId = layoutId;
        mInflater = LayoutInflater.from(context);
    }

    public BaseAdapter(Context context, int layoutId) {
        this(context, null, layoutId);
    }

    public BaseAdapter(Context context) {
        this(context, 0);
    }

    public LayoutInflater getLayoutInflater() {
        return mInflater;
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public BaseRecyclerView.BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseRecyclerView.BaseViewHolder<>(LayoutInflater.from(mContext).inflate(mLayoutId, parent, false));
    }

    @Override
    public void onBindViewHolder(BaseRecyclerView.BaseViewHolder holder, int position) {
        final D d = itemList != null ? itemList.get(position) : null;
        holder.itemView.setTag(d);
        bindView(holder, getItemViewType(position), position, d);
    }

    @Override
    public int getItemCount() {
        return (itemList != null ? itemList.size() : 0);
    }

    public void setData(List<D> newData) {
        itemList = newData;
    }

    /**
     * 刷新list数据
     *
     * @param newData
     *           刷新的数据列表
     */
    public void refreshData(List<D> newData) {
        setData(newData);
        notifyDataSetChanged();
    }

    public void addData(List<D> moreData) {
        itemList.addAll(moreData);
        notifyDataSetChanged();
    }

    /**
     * 给RecyclerView设置Adapter只需要实现该方法，绑定信息到组件
     *
     * @param holder
     *         {@link BaseRecyclerView.BaseViewHolder} ViewHolder对象，该对象可得到ItemView中的所有子View
     * @param item
     *         每一项的数据对象，具体类型由外部设置的泛型决定
     */
    public abstract void bindView(BaseRecyclerView.BaseViewHolder holder, int viewType, int position, D item);
}
