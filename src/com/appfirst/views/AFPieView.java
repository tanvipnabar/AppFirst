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
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author Bin Liu
 * 
 */
public class AFPieView extends View {

	private double mFillPercentage = 0;
	private int PADDING = 0;

	/**
	 * @param context
	 */
	public AFPieView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 */
	public AFPieView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * @param context
	 * @param percentage
	 */
	public AFPieView(Context context, double percentage) {
		super(context);
		if (percentage <= 1) {
			mFillPercentage = percentage;
		} else {
			mFillPercentage = 1;
		}
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
		paint.setAntiAlias(true);
		paint.setStrokeWidth(0.5f);
		
		paint.setColor(Helper.barBackgroundColor);
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setStrokeWidth(1);
		canvas.drawCircle(width / 2, height / 2, width / 2 - PADDING, paint);

		if (mFillPercentage < 0.9) {
			paint.setColor(Helper.barFillColor);
		} else {
			paint.setColor(Color.RED);
		}
		paint.setStyle(Paint.Style.FILL);
		RectF container = new RectF(PADDING, PADDING, width - PADDING * 2,
				width - PADDING * 2);
		canvas.drawArc(container, new Float(0), Float.parseFloat(String.format(
				"%f", 360 * mFillPercentage)), true, paint);

	}
}
