package com.appfirst.animations;

import com.appfirst.monitoring2.R;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.widget.ListView;

public class ExpandAnimation extends Animation implements Animation.AnimationListener {
	private View view;
	private static int ANIMATION_DURATION;
	private int LastWidth;
	private int FromWidth;
	private int ToWidth;
	private static int STEP_SIZE=30;
	public ExpandAnimation(View v, int FromWidth, int ToWidth, int Duration) {
		
		this.view = v;
		ANIMATION_DURATION = 2;
		this.FromWidth = FromWidth;
		this.ToWidth = ToWidth;
		setDuration(ANIMATION_DURATION);
		setRepeatCount(20);
		setFillAfter(false);
		setInterpolator(new AccelerateInterpolator());
		setAnimationListener(this);
	}

	public void onAnimationEnd(Animation anim) {
		// TODO Auto-generated method stub
	}

	public void onAnimationRepeat(Animation anim) {
		// TODO Auto-generated method stub
		LayoutParams lyp =  view.getLayoutParams();
		lyp.width = LastWidth +=ToWidth/20;
		view.setLayoutParams(lyp);
	}

	public void onAnimationStart(Animation anim) {
		// TODO Auto-generated method stub
		LayoutParams lyp =  view.getLayoutParams();
		lyp.width = 0;
		view.setLayoutParams(lyp);
		LastWidth = 0;
	}

}
