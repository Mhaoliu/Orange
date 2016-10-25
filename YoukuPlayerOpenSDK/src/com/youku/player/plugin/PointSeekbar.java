package com.youku.player.plugin;

import java.util.ArrayList;

import com.youku.player.goplay.Point;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.SeekBar;

public class PointSeekbar extends SeekBar {

	ArrayList<Point> points = new ArrayList<Point>();

	public PointSeekbar(Context context) {
		super(context);
	}

	public PointSeekbar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PointSeekbar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setPoints(ArrayList<Point> points) {
		if (points != null) {
			this.points.clear();
			this.points.addAll(points);
		}
	}
	
	@Override
	protected synchronized void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		drawPoints();
		
		
	}

	private void drawPoints() {
		
	}

}
