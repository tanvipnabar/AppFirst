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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.appfirst.monitoring.R;
import com.appfirst.monitoring.MainApplication;
import com.appfirst.types.AlertHistory;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;

/**
 * This class provides the customized rendering of an {@link ImageView} along
 * with a {@link TextView} to be used in {@link AFHomeScreen}.
 * 
 * There's an inner class ViewHolder which's a holder used to reference the
 * ImageView and TextView.
 * 
 * @author Bin Liu
 */
public class HomePageAlertListPopulator extends BaseAdapter {

	private ArrayList<AlertHistory> alertHistories = new ArrayList<AlertHistory>();
	
	static class ViewHolder {
		public ImageView icon;
		public TextView timeLine;
		public TextView subjectLine;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return alertHistories.size();//mThumbIds.length;
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
	public HomePageAlertListPopulator(Activity activity) {
		mContext = activity;
		List<AlertHistory> items;
		if (MainApplication.getAlertHistories() == null || MainApplication.isAlertHistoryNeedRefresh()) {
			String url = "https://173.192.88.66/api/v1/alert-histories/";//String.format("%s%s",getString(R.string.frontend_address),getString(R.string.api_alert_histories));
			MainApplication.loadAlertHistory(url);
		}
		items = MainApplication.getAlertHistories();
		//System.out.println("AH SIZZZEEEE *****---->  " + items.get(0).getSubject());
		for(int i=0; i<5; i++) {
			alertHistories.add((AlertHistory)items.get(i));
		}
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
					R.layout.home_page_alert_list_populator, null);
			holder = new ViewHolder();
			holder.icon = (ImageView) myView.findViewById(R.id.icon);
			holder.timeLine = (TextView) myView.findViewById(R.id.timeLine);
			holder.subjectLine = (TextView) myView.findViewById(R.id.subjectLine);
			myView.setTag(holder);
		} else {
			holder = (ViewHolder) myView.getTag();
		}
		Date date = new Date(System.currentTimeMillis() - alertHistories.get(position).getStart());
		holder.timeLine.setText(date.toString());
		holder.subjectLine.setText(alertHistories.get(position).getSubject()); //mNames[position]);
		//holder.textView.setGravity(3);
		holder.icon.setImageResource(checkIcon(alertHistories.get(position).getSubject()));//mThumbIds[position]);
		return myView;
	}
	
	public int checkIcon(String subject) {
		subject = subject.toUpperCase();
		if(subject.contains("POLLED DATA"))
			return R.drawable.ic_icon_nagios;
		if(subject.contains("PROCESSES IN APPLICATION") && subject.contains("TERMINATED"))
			return R.drawable.ic_icon_processes_terminated;
		if(subject.contains("NO DATA RECEIVED"))
			return R.drawable.ic_icon_no_data;
		if(subject.contains("ACTION REQUIRED"))
			return R.drawable.ic_icon_alert;
		if(subject.contains("LOG CONTENT"))
			return R.drawable.ic_icon_log;
		if(subject.contains("NETWORK"))
			return R.drawable.ic_icon_network;
		if(subject.contains("CPU ALERT"))
			return R.drawable.ic_icon_cpu;
		if(subject.contains("DISK"))
			return R.drawable.ic_icon_disk;
		return R.drawable.ic_icon_server;
	}

	// references to our images
	/*private Integer[] mThumbIds = { R.drawable.ic_icon_server,
			R.drawable.ic_icon_application, R.drawable.ic_icon_alert,
			R.drawable.ic_icon_nagios, R.drawable.ic_icon_alerthistory,
			R.drawable.ic_icon_setting };
	// references to the names
	private String[] mNames = new String[] { "Servers", "Applications",
			"Alerts", "Polled data", "Alert Histories", "Settings" };*/
	private Activity mContext;
}
