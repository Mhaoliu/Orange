package com.liuhao.orange.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.liuhao.orange.R;
import com.liuhao.orange.activity.PlayerActivity;
import com.liuhao.orange.adapter.base.RecycleViewHolder;
import com.liuhao.orange.adapter.base.RecyclerBaseAdapter;
import com.liuhao.orange.base.BaseActivity;
import com.liuhao.orange.http.video.VideoInfo;
import com.liuhao.orange.utils.network.NetworkConnection;
import com.liuhao.orange.width.CustomDialog;

import java.util.List;

/**
 * Created by liuhao on 2016/10/26.
 */
public class VideoListAdapter extends RecyclerBaseAdapter<VideoInfo.VideosBean> {
    public VideoListAdapter(Context mContext, List<VideoInfo.VideosBean> mDatas, int mLayoutId) {
        super(mContext, mDatas, mLayoutId);
    }

    @Override
    public void convert(RecycleViewHolder holder, final VideoInfo.VideosBean model, final int position) {
        holder.setText(model.getTitle(), R.id.tv_title)
                .setText(model.getSeconds(), R.id.tv_seconds)
                .setText(model.getUser().getUser_name(), R.id.tv_user_name)
                .setText(model.getCategory(), R.id.category);
        Glide.with(super.mContext).load(model.getThumbnail()).placeholder(R.drawable.default_error)
                .error(R.drawable.default_error).into((ImageView) holder.getView(R.id.iv_thumbnail));
        final ImageView paly = (ImageView) holder.getView(R.id.iv_paly);
        paly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetworkConnection.isNetworkConnected(VideoListAdapter.super.mContext)) {
                    ((BaseActivity) VideoListAdapter.super.mContext).showToast("网络没用连接", 1000);
                    return;
                } else if (NetworkConnection.getAPNType(VideoListAdapter.super.mContext) == 2) {
                    CustomDialog.Builder customBuilder = new
                            CustomDialog.Builder(VideoListAdapter.super.mContext);
                    customBuilder.setTitle("警告")
                            .setMessage("当前为移动网络，是否播放")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton("确定",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            Intent i = new Intent(mContext, PlayerActivity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putParcelable("videobean", model);
                                            i.putExtras(bundle);
                                            mContext.startActivity(i);
                                        }
                                    });
                    CustomDialog dialog = customBuilder.create();
                    dialog.show();
                } else {
                    Intent i = new Intent(mContext, PlayerActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("videobean", model);
                    i.putExtras(bundle);
                    mContext.startActivity(i);
                }
            }
        });
    }
}
