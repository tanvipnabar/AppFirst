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

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.appfirst.activities.details.AFServerDetail;
import com.appfirst.activities.lists.AFListActivity.ResourceLoader;
import com.appfirst.communication.Helper;
import com.appfirst.monitoring2.R;
import com.appfirst.monitoring2.LoginScreen;
import com.appfirst.monitoring2.MainApplication;
import com.appfirst.types.Server;
import com.appfirst.utils.DoubleLineLayoutArrayAdapter;
import com.appfirst.utils.DynamicComparator;

/**
 * @author Bin Liu
 * 
 */
public class AFServerList extends AFListActivity {
	private static final String TAG = "AFServerList";

	/** Called when the activity is first created. */
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		if (!MainApplication.checkClientLogin(this)) {
			finish();
			Intent intent = new Intent(this, LoginScreen.class);
			startActivity(intent);
		}
		setObjectClass(Server.class);
		setCurrentView();
		showDialog(PROGRESS_DIALOG);
		mTitle.setText("Servers: ");

		// Create an array of Strings, that will be put to our ListActivity
		if (MainApplication.getServers() == null
				|| MainApplication.getServers().size() == 0) {
			new ResourceLoader().execute();
		} else {
			displayList();
		}
	}

	@Override
	public void displayList() {
		try {
			dismissDialog(PROGRESS_DIALOG);
		} catch (Exception e) {
			// TODO: handle exception
		}
		List<String> names = new ArrayList<String>();
		List<String> details = new ArrayList<String>();
		List<Integer> images = new ArrayList<Integer>();
		List<Server> items = MainApplication.getServers();
		List<Integer> ids = new ArrayList<Integer>();
		if (items == null) { // nothing is returned
			return;
		}
		mSortName = "running";
		if (sortField != null) {
			mSortName = sortField.getName();
		}
		mSortText.setText(getSortAndFilter());
		DynamicComparator.sort(MainApplication.getServers(), mSortName, true);
		for (int i = 0; i < items.size(); i++) {
			Server item = items.get(i);
			String hostname = item.getHostname();
			if (filterString != "" && !hostname.contains(filterString))
				continue;
			if (item.getRunning()) {
				names.add(item.getHostname());
			} else {
				names.add(String.format("%s (stopped)", item.getHostname()));
			}

			details.add(String.format("%s cores at %s MHZ, %s Memory, %s Disk",
					item.getCapacity_cpu_num(), item.getCapacity_cpu_freq(),
					Helper.formatByteValue(item.getCapacity_mem()), Helper
							.formatByteValue(item.getTotalDisk() * 1000000)));

			if (item.getOs().toString().startsWith("Windows")) {
				images.add(R.drawable.ic_icon_windows);
			} else {
				images.add(R.drawable.ic_icon_linux);
			}

			ids.add(item.getId());
		}

		// Create an ArrayAdapter, that will actually make the Strings above
		// appear in the ListView
		mListView.setAdapter(new DoubleLineLayoutArrayAdapter(this, names,
				details, images, ids, AFServerDetail.class));
	}

	/**
	 *
	 */
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// Get the item that was clicked
		Intent intent = new Intent(AFServerList.this, AFServerDetail.class);
		intent.putExtra(Server.class.getName() + ".selected", position);
		startActivity(intent);
	}

	@Override
	public void sortListItems() {
		displayList();
	}

	@Override
	public void loadResource() {
		String url = String.format("%s%s",
				getString(R.string.frontend_address),
				getString(R.string.api_servers));
		MainApplication.loadServerList(url);
	}

	@Override
	public boolean onSearchRequested() {
		Log.i(TAG, "search box created");
		return super.onSearchRequested();
	}
}
