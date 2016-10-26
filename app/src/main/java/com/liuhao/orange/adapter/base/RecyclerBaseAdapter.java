package com.liuhao.orange.adapter.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by liuhao on 2016/10/9.
 */
public abstract class RecyclerBaseAdapter<T> extends RecyclerView.Adapter<RecycleViewHolder> {

    protected Context mContext;
    //集合数据源
    protected List<T> mDatas;
    // 数组数据源T[]
    protected T[] mDataArray;
    private int mLayoutId;

    private MultiItemTypeSupportListener multiItemTypeSupportListener;
    private OnItemClickListener onItemClickListener;

    public RecyclerBaseAdapter(Context mContext, List<T> mDatas, int mLayoutId) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        this.mLayoutId = mLayoutId;
    }

    public RecyclerBaseAdapter(Context mContext, T[] dataArray, int mLayoutId) {
        this.mContext = mContext;
        this.mDataArray = dataArray;
        this.mLayoutId = mLayoutId;
    }

    public void setMultiItemTypeSupportListener(MultiItemTypeSupportListener multiItemTypeSupportListener) {
        this.multiItemTypeSupportListener = multiItemTypeSupportListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId;
        if (multiItemTypeSupportListener != null) {
            layoutId = multiItemTypeSupportListener.getLayoutId(viewType);
        } else {
            layoutId = this.mLayoutId;
        }
        View view = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
        RecycleViewHolder holder = new RecycleViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final RecycleViewHolder holder, final int position) {
        convert(holder, mDatas != null ? mDatas.get(position) : mDataArray[position], position);

        if (onItemClickListener != null) {
            holder.mItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(holder.mItemView, position);
                }
            });
        }
    }

    /**
     * 需实现的抽象方法
     *
     * @param holder
     * @param model
     */
    public abstract void convert(RecycleViewHolder holder, T model, int position);

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : mDataArray != null ? mDataArray.length : 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (multiItemTypeSupportListener != null) {
            return multiItemTypeSupportListener.getItemViewType(position);
        }
        return super.getItemViewType(position);
    }


    @Override
    public long getItemId(int position) {

        return position;
    }

    /**
     * 支持多布局的item接口
     *
     * @author zlp
     */
    public interface MultiItemTypeSupportListener {
        int getItemViewType(int position);

        int getLayoutId(int viewType);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
