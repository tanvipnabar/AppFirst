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
import android.widget.TextView;

import com.appfirst.activities.details.AFApplicationDetail;
import com.appfirst.activities.details.AFLogDetail;
import com.appfirst.monitoring.MainApplication;
import com.appfirst.monitoring.R;
import com.appfirst.types.Application;
import com.appfirst.types.Log;
import com.appfirst.utils.DoubleLineLayoutArrayAdapter;
import com.appfirst.utils.DynamicComparator;

/**
 * Display the list of application
 * 
 * @author Bin Liu
 * 
 */
public class AFLogList2 extends AFListActivity {

	/** Called when the activity is first created. */
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setObjectClass(Log.class);
		// Create an array of Strings, that will be put to our ListActivity
		setCurrentView();
		mTitle.setText("Logs: ");
		showDialog(PROGRESS_DIALOG);
		if (MainApplication.getLogs() == null || MainApplication.isLogNeedRefresh()) {
			new ResourceLoader().execute();
			MainApplication.setLogNeedRefresh(false);
		} else {
			displayList();
		}
	}

	@Override
	public void displayList() {
		dismissDialog(PROGRESS_DIALOG);
		mSortName = "name";
		if (sortField != null) {
			mSortName = sortField.getName();
		}
		mSortText.setText(getSortAndFilter());
		DynamicComparator.sort(MainApplication.getLogs(), mSortName, true);
		List<String> names = new ArrayList<String>();
		List<String> details = new ArrayList<String>();
		List<Integer> ids= new ArrayList<Integer>();
		List<Log> items = MainApplication.getLogs();
		for (int i = 0; i < items.size(); i++) {
			Log item = items.get(i);
			String name = item.getType() + "(" + MainApplication.getServerNameById(item.getServerId()) + ")";
			if (this.filterString != ""
					&& !name.toLowerCase().contains(filterString.toLowerCase())) {
				continue;
			}
			names.add(name);
			details.add("");
			ids.add(item.getId());
		}
		// Create an ArrayAdapter, that will actually make the Strings above
		// appear in the ListView
		mListView.setAdapter(new DoubleLineLayoutArrayAdapter(this,
				names, details, ids, AFLogDetail.class));
	}

	/**
	 * Start the detailed application instance.
	 */
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// Get the item that was clicked
		Intent intent = new Intent(AFLogList2.this,
				AFLogDetail.class);
		intent.putExtra(Log.class.getName() + ".selected", position);
		startActivity(intent);
	}

	@Override
	public void sortListItems() {
		displayList();
	}

	/**
	 * @see com.appfirst.activities.lists.AFListActivity#loadResource()
	 */
	@Override
	public void loadResource() {
		String url = String.format("%s%s",
				getString(R.string.frontend_address),
				getString(R.string.api_logs));
		MainApplication.loadLogList(url);
	}

}
