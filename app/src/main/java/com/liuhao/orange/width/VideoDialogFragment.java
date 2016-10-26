package com.liuhao.orange.width;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import com.liuhao.orange.R;
import com.liuhao.orange.adapter.base.RecycleViewHolder;
import com.liuhao.orange.adapter.base.RecyclerBaseAdapter;

import java.util.List;


/**
 * Created by liuhao on 2016/10/26.
 */
public class VideoDialogFragment extends DialogFragment {

    private RecyclerView mRecyclerView;
    private VideoDialogAdapter mDialogAdapter;
    private OnItemClickListener mOnItemClickListener;
    private Dialog dialog;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //构造一个自定义style 的 dialog
        dialog = new Dialog(getActivity(), R.style.CustomDialogStyle);
        //设置为无标题
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_view);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        initView();
        return dialog;
    }

    private void initView() {
        mRecyclerView = (RecyclerView) dialog.findViewById(R.id.recycle_qingxidu);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        mDialogAdapter = new VideoDialogAdapter(getActivity(), getResources().getStringArray(R.array.video_qingxidu), R.layout.viode_dialog_item);
        mRecyclerView.setAdapter(mDialogAdapter);
        mDialogAdapter.setOnItemClickListener(new RecyclerBaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClickListener(position);
                    dismiss();
                }
            }
        });
    }

    class VideoDialogAdapter extends RecyclerBaseAdapter<String> {


        public VideoDialogAdapter(Context mContext, String[] dataArray, int mLayoutId) {
            super(mContext, dataArray, mLayoutId);
        }

        @Override
        public void convert(RecycleViewHolder holder, String model, int position) {
            holder.setText(model, R.id.tv_qingxidu);
        }
    }

    public interface OnItemClickListener {
        void onItemClickListener(int position);
    }
}
