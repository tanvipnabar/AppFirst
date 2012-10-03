package com.tutorial.menuList;

import com.tutorial.menuList.animations.ExpandAnimation;
import com.tutorial.menuList.animations.CollapseAnimation;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
	private LinearLayout MenuList;
	private Button btnToggleMenuList;
	private int screenWidth;
	private boolean isExpanded;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        MenuList = (LinearLayout) findViewById(R.id.linearLayout2);
        btnToggleMenuList = (Button) findViewById(R.id.button1);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        btnToggleMenuList.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		if (isExpanded) {
        			isExpanded = false;
        			MenuList.startAnimation(new CollapseAnimation(MenuList, 0,(int)(screenWidth*0.7), 20));
        		}else {
            		isExpanded = true;
            		MenuList.startAnimation(new ExpandAnimation(MenuList, 0,(int)(screenWidth*0.7), 20));
        		}
        		}
        });
    }
}