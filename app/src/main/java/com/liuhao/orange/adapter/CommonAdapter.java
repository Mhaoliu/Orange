package com.liuhao.orange.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class CommonAdapter<T> extends BaseAdapter {
    private Context context;
    // 为丰富程序功能，提供了两种常见的数据类型
    private List<T> dataList = null;// 数据源List<T>
    private T[] dataArray = null;// 数据源T[]
    // 布局文件ID
    private int layoutId;
    protected MultiItemTypeSupportListener multiItemSupportListener;

    /**
     * 构造方法
     *
     * @param context
     * @param layoutId
     * @param dataList
     */
    public CommonAdapter(Context context, int layoutId, List<T> dataList) {
        this.context = context;
        this.dataList = dataList;
        this.layoutId = layoutId;
    }

    /**
     * 构造方法(与上一个只有数据源不同)
     *
     * @param context
     * @param layoutId
     * @param dataArray
     */
    public CommonAdapter(Context context, int layoutId, T[] dataArray) {
        this.context = context;
        this.dataArray = dataArray;
        this.layoutId = layoutId;
    }

    /**
     * 设置多布局监听接口
     *
     * @param multiItemSupportListener
     */
    public void setMultiItemTypeSupportListener(MultiItemTypeSupportListener multiItemSupportListener) {
        this.multiItemSupportListener = multiItemSupportListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (multiItemSupportListener != null) {
            return multiItemSupportListener.getItemViewType(position);
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        if (multiItemSupportListener != null) {
            return multiItemSupportListener.getViewTypeCount();
        }
        return super.getViewTypeCount();
    }

    @Override
    public int getCount() {
        if (dataList != null) {
            return dataList.size();
        } else {
            return dataArray.length;
        }
    }

    @Override
    public T getItem(int position) {
        if (dataList != null) {
            return dataList.get(position);
        } else {
            return dataArray[position];
        }
    }

    @Override
    public long getItemId(int position) {
        if (multiItemSupportListener != null) {
            return multiItemSupportListener.getLayoutId(position);
        }
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int layoutId;
        if (multiItemSupportListener != null) {
            layoutId = (int) getItemId(position);
        } else {
            layoutId = this.layoutId;
        }
        CommonViewHolder holder = CommonViewHolder.get(context, convertView, parent, layoutId, position);
        convert(holder, getItem(position), position);
        return holder.getConvertView();
    }

    /**
     * 需实现的抽象方法
     *
     * @param holder
     * @param model
     */
    public abstract void convert(CommonViewHolder holder, T model, int position);

    /**
     * 修改数据源
     *
     * @param data
     */
    public void updateData(List<T> data) {
        dataList.clear();
        dataList.addAll(data);
        notifyDataSetChanged();
    }

    /**
     * 添加数据源
     *
     * @param data
     */
    public void addAll(List<T> data) {
        dataList.addAll(data);
        notifyDataSetChanged();
    }

    /**
     * 支持多布局的item接口
     *
     * @author zlp
     */
    public interface MultiItemTypeSupportListener {
        int getItemViewType(int position);

        int getViewTypeCount();

        int getLayoutId(int position);

    }
}