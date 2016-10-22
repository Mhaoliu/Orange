package com.liuhao.orange.adapter.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class CommonViewHolder {
    private final SparseArray<View> mViews;
    public int mPosition;
    private View mConvertView;
    public int layoutId;

    CommonViewHolder(Context context, ViewGroup parent, int layoutId,
                     int position) {
        this.mPosition = position;
        this.layoutId = layoutId;
        this.mViews = new SparseArray<View>();
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,
                false);
        // setTag
        mConvertView.setTag(this);
    }

    /**
     * 拿到一个ViewHolder对象
     *
     * @param context
     * @param convertView
     * @param parent
     * @param layoutId
     * @param position
     * @return
     */
    public static CommonViewHolder get(Context context, View convertView,
                                       ViewGroup parent, int layoutId, int position) {

        if (convertView == null) {
            return new CommonViewHolder(context, parent, layoutId, position);
        }

        // 复用convertView
        CommonViewHolder existingHelper = (CommonViewHolder) convertView
                .getTag();
        return existingHelper;
    }

    public View getConvertView() {
        return mConvertView;
    }

    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public int getPosition() {
        return mPosition;
    }

    /**
     * @param text
     * @param id   控件ID
     * @return
     */
    public CommonViewHolder setText(String text, int id) {
        TextView tv = getView(id);
        tv.setText(text);
        return this;
    }

    /**
     * @param viewId
     * @param id
     * @return
     */
    public CommonViewHolder setImageResource(int viewId, int id) {
        ImageView iv = getView(viewId);
        iv.setImageResource(id);
        return this;
    }

    /**
     *
     * @param viewId
     * @param bitmap
     * @return
     */
    public CommonViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView iv = getView(viewId);
        iv.setImageBitmap(bitmap);
        return this;
    }

}
