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
package com.appfirst.activities.lists;

import java.util.ArrayList;
import java.util.List;

import com.appfirst.activities.details.AFAlertDetail;
import com.appfirst.activities.details.AFServerDetail;
import com.appfirst.activities.lists.AFListActivity.ResourceLoader;
import com.appfirst.communication.Helper;
import com.appfirst.monitoring2.R;
import com.appfirst.monitoring2.AFHomeScreen;
import com.appfirst.monitoring2.LoginScreen;
import com.appfirst.monitoring2.MainApplication;
import com.appfirst.types.Alert;
import com.appfirst.utils.DoubleLineLayoutArrayAdapter;
import com.appfirst.utils.DynamicComparator;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author Bin Liu
 * 
 */
public class AFAlertList extends AFListActivity {

	private static final String TAG = "AFAlertList";

	/** Called when the activity is first created. */
	public void onCreate(Bundle icicle) {
		if (!MainApplication.checkClientLogin(this)) {
			finish();
			Intent intent = new Intent(this, LoginScreen.class);
			startActivity(intent);
		}
		super.onCreate(icicle);
		setObjectClass(Alert.class);
		setCurrentView();
		mTitle.setText("Alerts: ");
		showDialog(PROGRESS_DIALOG);

		// Create an array of Strings, that will be put to our ListActivity
		if (MainApplication.getAlerts() == null
				|| MainApplication.getAlerts().size() == 0 || MainApplication.isAlertNeedRefresh()) {
			new ResourceLoader().execute();
			MainApplication.setAlertNeedRefresh(false);
		} else {
			displayList();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (MainApplication.getAlerts() == null) {
			return;
		}
		showDialog(PROGRESS_DIALOG);
		displayList();
	}

	@Override
	public void displayList() {
		try {
			dismissDialog(PROGRESS_DIALOG);
		} catch (Exception e) {
			// TODO: handle exception
		}
		mSortName = "in_incident";
		if (sortField != null) {
			mSortName = sortField.getName();
		}
		mSortText.setText(getSortAndFilter());
		DynamicComparator.sort(MainApplication.getAlerts(), mSortName, true);
		List<String> names = new ArrayList<String>();
		List<String> details = new ArrayList<String>();
		List<Integer> ids = new ArrayList<Integer>();
		List<Integer> icons = new ArrayList<Integer>();
		List<Alert> items = MainApplication.getAlerts();
		for (int i = 0; i < items.size(); i++) {
			Alert alert = items.get(i);
			String name = alert.getName();
			if (this.filterString != ""
					&& !name.toLowerCase().contains(name.toLowerCase())) {
				continue;
			}
			names.add(name);
			details.add(String.format("Last triggerred: %s, %s", Helper
					.formatTime(alert.getLast_triggered() * 1000), alert
					.getTarget()));
			ids.add(alert.getId());
			Integer resourceId = 0;
			if (alert.getActive()) {
				if (alert.getIn_incident()) {
					resourceId = R.drawable.ic_icon_red_status;
				} else {
					resourceId = R.drawable.ic_icon_green_status;
				}
			} else {
				resourceId = R.drawable.ic_icon_grey_status;
			}
			icons.add(resourceId);
		}
		// Create an ArrayAdapter, that will actually make the Strings above
		// appear in the ListView
		mListView.setAdapter(new DoubleLineLayoutArrayAdapter(this, names,
				details, icons, ids, AFAlertDetail.class));
	}

	/**
	 *
	 */
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// Get the item that was clicked
		Intent intent = new Intent(AFAlertList.this, AFAlertDetail.class);
		intent.putExtra(AFAlertDetail.class.getName() + ".selected", position);
		startActivity(intent);
	}

	@Override
	public void sortListItems() {
		displayList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.appfirst.activities.lists.AFListActivity#loadResource()
	 */
	@Override
	public void loadResource() {

		String url = String.format("%s%s",
				getString(R.string.frontend_address),
				getString(R.string.api_alerts));

		MainApplication.loadAlertList(url);
	}
}
