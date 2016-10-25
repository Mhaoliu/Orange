package com.youku.player.plugin;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.baseproject.utils.Logger;
import com.youku.player.goplay.Point;
import com.youku.player.ui.R;

public class HotPointView extends RelativeLayout {

	private static final String TAG = HotPointView.class.getSimpleName();

	private ArrayList<Point> points = new ArrayList<Point>();
	private int seekbar_thumb_img_width;
	private int seekbar_thumb_img_width_half;
	private int hotpoint_img_width;
	private int hotpoint_img_width_half;

	public HotPointView(Context context) {
		super(context);
		init();
	}

	public HotPointView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public HotPointView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {

		Drawable seekbar_thumb_img = getContext().getResources().getDrawable(
				R.drawable.icon_scrubbarslider);
		seekbar_thumb_img_width = seekbar_thumb_img.getIntrinsicWidth();
		seekbar_thumb_img_width_half = seekbar_thumb_img_width / 2;
		Drawable hotpoint_img = getContext().getResources().getDrawable(
				R.drawable.hotpoint_img);
		hotpoint_img_width = hotpoint_img.getIntrinsicWidth();
		hotpoint_img_width_half = hotpoint_img_width / 2;

	}

	public void setPoints(ArrayList<Point> points) {
		if (points != null) {
			this.points.clear();
			this.points.addAll(points);
		}

		updatePointsPos();
	}

	private int max = 0;

	public void setMax(int max) {
		this.max = max;
	}

	private boolean showed = false;
	private int oldWidth = 0;

	private void updatePointsPos() {

		Logger.d(TAG, "updatePointsPos：");

		showed = false;
		removeAllViews();

		oldWidth = getWidth();
		if (oldWidth == 0) {
			Logger.e(TAG, "viewWidth = 0：");
			return;
		}

		if (max == 0) {
			Logger.e(TAG, "max  = 0：");
			throw new IllegalStateException("have not set Max value");
		}

		int length = points == null ? 0 : points.size();
		Logger.d(TAG, "精彩点个数：" + length);
		for (int i = 0; i < length; i++) {
			ImageView hotpoint_img = new ImageView(getContext());
			hotpoint_img.setTag(points.get(i));
			hotpoint_img.setScaleType(ScaleType.CENTER_INSIDE);
			hotpoint_img.setImageResource(R.drawable.hotpoint_img);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.MATCH_PARENT);
			params.addRule(RelativeLayout.CENTER_VERTICAL);
			int point_pos = (int) (1f * (getWidth() - seekbar_thumb_img_width)
					* points.get(i).start / max);
			params.setMargins(point_pos + seekbar_thumb_img_width_half
					- hotpoint_img_width_half, params.topMargin,
					params.rightMargin, params.bottomMargin);
			addView(hotpoint_img, params);
		}

		showed = true;
	}

	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		checkNeedUpdatePos();
	}
	
	public void setProgress(int currentPosition) {
		if(getWidth() ==0){
			return ;
		}
		
		checkNeedUpdatePos();
		
		int NEAR_POINT_MULTIPLE = seekbar_thumb_img_width / getWidth()  ;
		
		for (int i = 0; i < getChildCount(); i++) {
			ImageView hotpoint_img = (ImageView) getChildAt(i);
			Point point = (Point) hotpoint_img.getTag();
			if (point != null) {

				boolean clickable = !(Math.abs(point.start - currentPosition) <= max
						/ NEAR_POINT_MULTIPLE);
				if (clickable) {
					hotpoint_img.setClickable(true);
					hotpoint_img.setVisibility(View.VISIBLE);
				} else {
					hotpoint_img.setClickable(false);
					hotpoint_img.setVisibility(View.INVISIBLE);
				}
			}
		}
	}

	private void checkNeedUpdatePos() {
		if (!showed) {
			updatePointsPos();
		}

		if (oldWidth != getWidth()) {
			updatePointsPos();
		}
	}

}
