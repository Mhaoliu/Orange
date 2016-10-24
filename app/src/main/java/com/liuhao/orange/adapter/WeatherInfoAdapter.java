package com.liuhao.orange.adapter;

import android.content.Context;

import com.liuhao.orange.R;
import com.liuhao.orange.adapter.base.RecycleViewHolder;
import com.liuhao.orange.adapter.base.RecyclerBaseAdapter;
import com.liuhao.orange.bean.WeatherBean;

import java.util.List;

/**
 * Created by liuhao on 2016/10/23.
 */
public class WeatherInfoAdapter extends RecyclerBaseAdapter<WeatherBean.DataBean.WeatherInfo> {

    public WeatherInfoAdapter(Context mContext, List<WeatherBean.DataBean.WeatherInfo> mDatas, int mLayoutId) {
        super(mContext, mDatas, mLayoutId);
    }

    @Override
    public void convert(RecycleViewHolder holder, WeatherBean.DataBean.WeatherInfo model, int position) {
        holder.setText(model.getDate(), R.id.date)
                .setText(model.getNongli(), R.id.nongli)
                .setText("周" + model.getWeek(), R.id.week)
                .setText(model.getInfo().getDay().get(1), R.id.day_weather)
                .setText(model.getInfo().getDay().get(2), R.id.day_t)
                .setText(model.getInfo().getDay().get(3), R.id.day_wind)
                .setText(model.getInfo().getDay().get(4), R.id.day_wind_level)
                .setText(model.getInfo().getNight().get(1), R.id.night_weather)
                .setText(model.getInfo().getNight().get(2), R.id.night_t)
                .setText(model.getInfo().getNight().get(3), R.id.night_wind)
                .setText(model.getInfo().getNight().get(4), R.id.night_wind_level);
    }
}
