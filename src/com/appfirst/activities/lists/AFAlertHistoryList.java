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

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.appfirst.activities.details.AFAlertDetail;
import com.appfirst.activities.details.AFAlertHistoryDetail;
import com.appfirst.activities.lists.AFListActivity.ResourceLoader;
import com.appfirst.monitoring.MainApplication;
import com.appfirst.monitoring.R;
import com.appfirst.types.Alert;
import com.appfirst.types.AlertHistory;
import com.appfirst.utils.DynamicComparator;

/**
 * This activity lists all the alert histories.
 * 
 * @author Bin Liu
 * 
 */
public class AFAlertHistoryList extends AFListActivity {

	/** Called when the activity is first created. */
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setObjectClass(AlertHistory.class);
		// Create an array of Strings, that will be put to our ListActivity

		if (MainApplication.getAlertHistories() == null) {
			showDialog(PROGRESS_DIALOG);
			new ResourceLoader().execute();
		} else {
			showDialog(PROGRESS_DIALOG);
			displayList();
		}
	}

	@Override
	public void displayList() {
		dismissDialog(PROGRESS_DIALOG);
		List<String> names = new ArrayList<String>();
		List<AlertHistory> items = MainApplication.getAlertHistories();
		for (int i = 0; i < items.size(); i++) {
			AlertHistory item = items.get(i);
			// TODO: change to use subject name
			names.add(String.format("%s", item.getSubject()));
		}
		// Create an ArrayAdapter, that will actually make the Strings above
		// appear in the ListView
		this.setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, names));
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
	 * @see com.appfirst.activities.lists.AFListActivity#loadResource()
	 */
	@Override
	public void loadResource() {
		String url = String.format("%s%s",
				getString(R.string.frontend_address),
				getString(R.string.api_alert_histories));
		MainApplication.loadAlertHistory(url);
	}
}
