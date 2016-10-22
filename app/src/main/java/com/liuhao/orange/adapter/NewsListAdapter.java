package com.liuhao.orange.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.liuhao.orange.R;
import com.liuhao.orange.adapter.base.RecycleViewHolder;
import com.liuhao.orange.adapter.base.RecyclerBaseAdapter;
import com.liuhao.orange.bean.NewsBean;

import java.util.List;

/**
 * Created by liuhao on 2016/10/22.
 */
public class NewsListAdapter extends RecyclerBaseAdapter<NewsBean.DataBean> {

    private Context mContext;

    public NewsListAdapter(Context mContext, List<NewsBean.DataBean> mDatas, int mLayoutId) {
        super(mContext, mDatas, mLayoutId);
        this.mContext = mContext;
    }

    @Override
    public void convert(RecycleViewHolder holder, NewsBean.DataBean model, int position) {
        if (model != null) {
            holder.setText(model.getTitle(), R.id.tv_title).setText(model.getAuthor_name(), R.id.author_name).setText(model.getDate(), R.id.tv_date);
            Glide.with(mContext).load(model.getThumbnail_pic_s()).error(R.drawable.default_error).
                    placeholder(R.drawable.default_error).
                    into((ImageView) holder.getView(R.id.thumbnail_pic_s));
        }
    }
}
