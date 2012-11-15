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
import android.widget.Toast;

import com.appfirst.activities.details.AFPolledDataDetail;
import com.appfirst.activities.lists.AFListActivity.ResourceLoader;
import com.appfirst.monitoring2.R;
import com.appfirst.monitoring2.MainApplication;
import com.appfirst.types.Alert;
import com.appfirst.types.PolledDataObject;
import com.appfirst.types.Server;
import com.appfirst.utils.DoubleLineLayoutArrayAdapter;
import com.appfirst.utils.DynamicComparator;

/**
 * @author Bin Liu
 * 
 */
public class AFPolledDataList extends AFListActivity {

	/** Called when the activity is first created. */
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setObjectClass(PolledDataObject.class);
		// Create an array of Strings, that will be put to our ListActivity
		setCurrentView();
		mTitle.setText("Polled datas: ");
		showDialog(PROGRESS_DIALOG);
		if (MainApplication.getPolledDatas() == null || MainApplication.isPolledDataNeedRefresh()) {
			new ResourceLoader().execute();
			MainApplication.setPolledDataNeedRefresh(false);
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
		mSortName = "name";
		if (sortField != null) {
			mSortName = sortField.getName();
		}
		mSortText.setText(getSortAndFilter());
		DynamicComparator.sort(MainApplication.getPolledDatas(), mSortName, true);
		List<String> names = new ArrayList<String>();
		List<String> serverNames = new ArrayList<String>();
		List<Integer> ids = new ArrayList<Integer>();
		List<PolledDataObject> items = MainApplication.getPolledDatas();
		for (int i = 0; i < items.size(); i++) {
			PolledDataObject item = items.get(i);
			String name = item.getName();
			if (this.filterString != ""
					&& !this.filterString.toLowerCase().contains(
							name.toLowerCase())) { // apply the filter
				continue;
			}
			names.add(name);
			ids.add(item.getId());
			serverNames
					.add(MainApplication.getServerNameMap().get(item.getServer()));
		}
		// Create an ArrayAdapter, that will actually make the Strings above
		// appear in the ListView
		mListView.setAdapter(new DoubleLineLayoutArrayAdapter(this, names,
				serverNames, ids, AFPolledDataDetail.class));
	}

	/**
	 *
	 */
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// Get the item that was clicked
		Intent intent = new Intent(AFPolledDataList.this,
				AFPolledDataDetail.class);
		intent.putExtra(PolledDataObject.class.getName() + ".selected",
				position);
		startActivity(intent);
	}

	@Override
	public void sortListItems() {
		displayList();
	}

	/**
	 * 
	 * 
	 * @see com.appfirst.activities.lists.AFListActivity#loadResource()
	 */
	@Override
	public void loadResource() {
		String url = String.format("%s%s",
				getString(R.string.frontend_address),
				getString(R.string.api_polled_datas));
		if (MainApplication.getPolledDatas() == null) {
			MainApplication.loadPolledDataList(url);
		}

	}
}
