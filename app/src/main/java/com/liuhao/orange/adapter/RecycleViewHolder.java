package com.liuhao.orange.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by liuhao on 2016/10/9.
 */
public class RecycleViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews;
    View mItemView;

    public RecycleViewHolder(View itemView) {
        super(itemView);
        mItemView = itemView;
        mViews = new SparseArray<>();
    }

    /**
     * 获取VIIEW
     *
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mItemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * @param text
     * @param id   控件ID
     * @return
     */
    public RecycleViewHolder setText(String text, int id) {
        TextView tv = getView(id);
        tv.setText(text);
        return this;
    }

    /**
     * @param viewId
     * @param id
     * @return
     */
    public RecycleViewHolder setImageResource(int viewId, int id) {
        ImageView iv = getView(viewId);
        iv.setImageResource(id);
        return this;
    }

    /**
     * @param viewId
     * @param bitmap
     * @return
     */
    public RecycleViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView iv = getView(viewId);
        iv.setImageBitmap(bitmap);
        return this;
    }

}
