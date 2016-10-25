package com.liuhao.orange.adapter;

import android.content.Context;

import com.liuhao.orange.R;
import com.liuhao.orange.adapter.base.RecycleViewHolder;
import com.liuhao.orange.adapter.base.RecyclerBaseAdapter;

import java.util.List;

/**
 * Created by liuhao on 2016/10/25.
 */
public class HotCityAdapter extends RecyclerBaseAdapter<String> {
    public HotCityAdapter(Context mContext, String[] dataArray, int mLayoutId) {
        super(mContext, dataArray, mLayoutId);
    }

    public HotCityAdapter(Context mContext, List<String> mDatas, int mLayoutId) {
        super(mContext, mDatas, mLayoutId);
    }


    @Override
    public void convert(RecycleViewHolder holder, String model, int position) {
        holder.setText(model, R.id.tv_city_name);
    }
}
