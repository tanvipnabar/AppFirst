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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.appfirst.activities.details.AFAlertHistoryDetail;
import com.appfirst.activities.details.AFServerDetail;
import com.appfirst.communication.Helper;
import com.appfirst.monitoring2.R;
import com.appfirst.monitoring2.LoginScreen;
import com.appfirst.monitoring2.MainApplication;
import com.appfirst.types.AFDevice;
import com.appfirst.types.AlertHistory;
import com.appfirst.utils.DoubleLineLayoutArrayAdapter;
import com.appfirst.utils.DynamicComparator;

/**
 * This activity lists all the alert histories.
 * 
 * Whenever this page is initialized, resumed or onfocus, the badge number will be reseted. 
 * @author Bin Liu
 * 
 */
public class AFAlertHistoryList extends AFListActivity {
	private static final String TAG = "AFAlertHistoryList";

	/** Called when the activity is first created. */
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		if (!MainApplication.checkClientLogin(AFAlertHistoryList.this)) {
			finish();
			Intent intent = new Intent(this,
					LoginScreen.class);
			startActivity(intent);
			return;
		}
		setObjectClass(AlertHistory.class);
		// Create an array of Strings, that will be put to our ListActivity
		setCurrentView();
		mTitle.setText("Alert histories:");

		if (MainApplication.getAlertHistories() == null || MainApplication.isAlertHistoryNeedRefresh()) {
			showDialog(PROGRESS_DIALOG);
			new ResourceLoader().execute();
			MainApplication.setAlertHistoryNeedRefresh(false);
		} else {
			showDialog(PROGRESS_DIALOG);
			displayList();
		}
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus)
	{
		if (hasFocus) {
			new BadgeUpdater().execute();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		new BadgeUpdater().execute();
	}

	@Override
	public void displayList() {
		try {
			dismissDialog(PROGRESS_DIALOG);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<String> names = new ArrayList<String>();
		List<AlertHistory> items = MainApplication.getAlertHistories();
		List<String> details = new ArrayList<String>();
		List<Integer> ids = new ArrayList<Integer>();
		mSortName = "time";
		mSortText.setText(getSortAndFilter());
		for (int i = 0; i < items.size(); i++) {
			AlertHistory item = items.get(i);
			String subject = item.getSubject();
			if (this.filterString != ""
					&& !subject.toLowerCase().contains(
							this.filterString.toLowerCase())) {
				continue;
			}
			names.add(String.format("%s", item.getSubject()));
			details.add("");
			ids.add(item.getId());
		}
		// Create an ArrayAdapter, that will actually make the Strings above
		// appear in the ListView
		mListView.setAdapter(new DoubleLineLayoutArrayAdapter(this, names,
				details, ids, AFAlertHistoryDetail.class));
	}

	/**
	 * Start the activity for the alert history when the list entry is selected.
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// Get the item that was clicked
		Intent intent = new Intent(AFAlertHistoryList.this,
				AFAlertHistoryDetail.class);
		intent.putExtra(AFAlertHistoryDetail.class.getName() + ".selected",
				position);
		startActivity(intent);
		finish();
	}

	@Override
	public void sortListItems() {
		DynamicComparator.sort(MainApplication.getAlertHistories(), sortField
				.getName(), true);
		displayList();
	}

	/**
	 * 
	 * This is called when the alert history page is initialized or reloaded. 
	 * While onresume is called when user come back from a back button. 
	 */
	@Override
	public void loadResource() {
		
		String url = String.format("%s%s",
				getString(R.string.frontend_address),
				getString(R.string.api_alert_histories));
		MainApplication.loadAlertHistory(url);
		new BadgeUpdater().execute();
	}
	
	protected class BadgeUpdater extends AsyncTask<URL, Integer, Long> {
		protected Long doInBackground(URL... urls) {
			MainApplication.updateWidgetBadge(AFAlertHistoryList.this);
			return 0L;
		}

		protected void onPostExecute(Long result) {
			displayList();
		}
	}
}
