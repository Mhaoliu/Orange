package com.liuhao.orange.adapter;

import android.content.Context;
import android.util.Log;

import com.liuhao.orange.R;
import com.liuhao.orange.adapter.base.RecycleViewHolder;
import com.liuhao.orange.adapter.base.RecyclerBaseAdapter;
import com.liuhao.orange.bean.WeatherBean;
import com.liuhao.orange.bean.WeatherLifeBean;

import java.util.List;

/**
 * Created by liuhao on 2016/10/23.
 */
public class WeatherLifeApater extends RecyclerBaseAdapter<WeatherLifeBean> {
    public WeatherLifeApater(Context mContext, List<WeatherLifeBean> mDatas, int mLayoutId) {
        super(mContext, mDatas, mLayoutId);
    }

//    @Override
//    public int getItemCount() {
//        return 6;
//    }
//
//    @Override
//    public void onBindViewHolder(RecycleViewHolder holder, int position) {
//        Log.d("androidxx", mDatas.toString());
//        convert(holder, super.mDatas.get(0), position);
//    }

    @Override
    public void convert(RecycleViewHolder holder, WeatherLifeBean model, int position) {
        if (model != null) {
            switch (position) {
                case 0:
                    holder.setText("穿衣指数", R.id.zhishu_name)
                            .setText(model.getZhiShi(), R.id.zhishu)
                            .setText(model.getJianYi(), R.id.jianyi)
                    ;
                    break;
                case 1:
                    holder.setText("感冒指数", R.id.zhishu_name)
                            .setText(model.getZhiShi(), R.id.zhishu)
                            .setText(model.getJianYi(), R.id.jianyi);
                    break;
                case 2:
                    holder.setText("空调指数", R.id.zhishu_name)
                            .setText(model.getZhiShi(), R.id.zhishu)
                            .setText(model.getJianYi(), R.id.jianyi);
                    break;
                case 3:
                    holder.setText("洗车指数", R.id.zhishu_name)
                            .setText(model.getZhiShi(), R.id.zhishu)
                            .setText(model.getJianYi(), R.id.jianyi);
                    break;
                case 4:
                    holder.setText("运动指数", R.id.zhishu_name)
                            .setText(model.getZhiShi(), R.id.zhishu)
                            .setText(model.getJianYi(), R.id.jianyi);
                    break;
                case 5:
                    holder.setText("紫外线指数", R.id.zhishu_name)
                            .setText(model.getZhiShi(), R.id.zhishu)
                            .setText(model.getJianYi(), R.id.jianyi);
                    break;

            }
        }
    }
}
