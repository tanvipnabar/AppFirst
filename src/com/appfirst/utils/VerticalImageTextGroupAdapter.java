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
package com.appfirst.utils;

import com.appfirst.monitoring2.R;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * This class provides the customized rendering of an {@link ImageView} along
 * with a {@link TextView} to be used in {@link AFHomeScreen}.
 * 
 * There's an inner class ViewHolder which's a holder used to reference the
 * ImageView and TextView.
 * 
 * @author Bin Liu
 */
public class VerticalImageTextGroupAdapter extends BaseAdapter {

	static class ViewHolder {
		public ImageView imageView;
		public TextView textView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mThumbIds.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Create the adapter by passing the parent activity.
	 * 
	 * @param activity
	 *            parent activity
	 */
	public VerticalImageTextGroupAdapter(Activity activity) {
		mContext = activity;
	}

	/**
	 * create a new ImageView for each item referenced by the Adapter
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View myView = convertView;
		ViewHolder holder;
		if (convertView == null) { // if it's not recycled, initialize some
			// attributes
			myView = mContext.getLayoutInflater().inflate(
					R.layout.vertical_image_text, null);
			holder = new ViewHolder();
			holder.textView = (TextView) myView.findViewById(R.id.home_screen_label);
			holder.imageView = (ImageView) myView.findViewById(R.id.home_screen_icon);
			myView.setTag(holder);
		} else {
			holder = (ViewHolder) myView.getTag();
		}
		holder.textView.setText(mNames[position]);
		holder.textView.setGravity(3);
		holder.imageView.setImageResource(mThumbIds[position]);
		return myView;
	}

	// references to our images
	private Integer[] mThumbIds = { R.drawable.ic_icon_server,
			R.drawable.ic_icon_application, R.drawable.ic_icon_alert,
			R.drawable.ic_icon_nagios, R.drawable.ic_icon_alerthistory,
			R.drawable.ic_icon_log, R.drawable.ic_icon_setting };
	// references to the names
	private String[] mNames = new String[] { "Servers", "Applications",
			"Alerts", "Polled data", "Alert Histories", "Logs", "Settings" };
	private Activity mContext;
}
