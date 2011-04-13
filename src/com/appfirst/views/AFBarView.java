/*
 * Copyright 2009-2011 AppFirst, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.appfirst.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * The single bar to display the visual percentage of a resource that scaled
 * between 0 and 1. It contains a bar chart and TextView displays the actual
 * percentage.
 * 
 * @author Bin Liu
 * 
 */
public class AFBarView extends View {

	private double mFillPercentage = 0;
	private final int BORDER_WIDTH = 1;
	private final int PADDING = 1;
	private final String longestString = "100.0%";
	private final int textPadding = 5;
	private Paint mTextPaint;

	/**
	 * @param context
	 */
	public AFBarView(Context context) {
		super(context);
		initPaint();
	}

	/**
	 * @param context
	 */
	public AFBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initPaint();
	}

	/**
	 * @param context
	 * @param percentage
	 */
	public AFBarView(Context context, double percentage) {
		super(context);
		if (percentage <= 1) {
			mFillPercentage = percentage;
		} else {
			mFillPercentage = 1;
		}
		initPaint();
		setWillNotDraw(false);
	}

	/**
	 * Set the percentage of the chart after creation.
	 * 
	 * @param percentage
	 */
	public void setPercentage(double percentage) {
		if (percentage <= 1) {
			mFillPercentage = percentage;
		} else {
			mFillPercentage = 1;
		}
		invalidate();
	}

	private final void initPaint() {
		mTextPaint = new Paint();
		mTextPaint.setAntiAlias(true);
		mTextPaint.setTextSize(Helper.convertDpToPx(16f, getContext()));
		mTextPaint.setColor(0xFFffffff);
	}

	/**
	 * @see android.view.View#measure(int, int)
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(measureWidth(widthMeasureSpec),
				measureHeight(heightMeasureSpec));

	}

	/**
	 * Determines the width of this view
	 * 
	 * @param measureSpec
	 *            A measureSpec packed into an int
	 * @return The width of the view, honoring constraints from measureSpec
	 */
	private int measureWidth(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			// We were told how big to be
			result = specSize;
		} else {
			// Measure the text
			result = specSize;
			if (specMode == MeasureSpec.AT_MOST) {
				// Respect AT_MOST value if that was what is called for by
				// measureSpec
				result = Math.min(result, specSize);
			}
		}

		return result;
	}

	/**
	 * Determines the height of this view
	 * 
	 * @param measureSpec
	 *            A measureSpec packed into an int
	 * @return The height of the view, honoring constraints from measureSpec
	 */
	private int measureHeight(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			// We were told how big to be
			result = specSize;
		} else {
			// Measure the text (beware: ascent is a negative number)
			result = specSize;
			if (specMode == MeasureSpec.AT_MOST) {
				// Respect AT_MOST value if that was what is called for by
				// measureSpec
				result = Math.min(result, specSize);
			}
		}
		return result;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint paint = new Paint();

		int width = this.getWidth();
		int height = this.getHeight();
		paint.setTextSize(10);
		String percentage = String.format("%.1f", this.mFillPercentage * 100)
				+ "%";

		int textWidth = Helper.convertDpToPx((float)paint.measureText(this.longestString)
				+ textPadding, getContext());
		
		width -= textWidth;
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.GRAY);
		paint.setStrokeWidth(BORDER_WIDTH);
		canvas.drawRect(0, PADDING, width, height - PADDING * 2, paint);

		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Helper.barBackgroundColor);
		canvas.drawRect(BORDER_WIDTH, BORDER_WIDTH + PADDING, width
				- BORDER_WIDTH * 2, height - BORDER_WIDTH * 2 - 2 * PADDING,
				paint);

		if (mFillPercentage < 0.9) {
			paint.setColor(Helper.barFillColor);	
		} else {
			paint.setColor(Color.RED);
		}
		canvas.drawRect(BORDER_WIDTH, BORDER_WIDTH + PADDING,
				convertFillWidth(width - BORDER_WIDTH * 2 - 2 * PADDING),
				height - BORDER_WIDTH * 2, paint);

		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.WHITE);
		paint.setStrokeWidth(0.25f);
		paint.setTextSize(Helper.convertDpToPx(12, getContext()));
		paint.setAntiAlias(true);
		
		// align the text to the right side. 
		int realTextWidth = (int) paint.measureText(percentage);
		canvas.drawText(percentage, this.getWidth() - realTextWidth, height
				- PADDING * 2 - BORDER_WIDTH * 2, paint);
	}

	private int convertFillWidth(int fillWidth) {
		float dWidth = Float.parseFloat(String.format("%d", fillWidth));
		dWidth *= mFillPercentage;
		return Math.round(dWidth);
	}
}
