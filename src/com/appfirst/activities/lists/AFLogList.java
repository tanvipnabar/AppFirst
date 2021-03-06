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

import android.os.Bundle;

import com.appfirst.datatypes.LogData;
import com.appfirst.monitoring2.MainApplication;

import com.appfirst.utils.DoubleLineLayoutArrayAdapter;
import com.appfirst.utils.DynamicComparator;

/**
 * @author Bin Liu
 * 
 */
public class AFLogList extends AFListActivity {
	/** Called when the activity is first created. */
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setObjectClass(LogData.class);
		setCurrentView();
		// Create an array of Strings, that will be put to our ListActivity
		displayList();
	}

	/**
	 * @see com.appfirst.activities.lists.AFListActivity#displayList()
	 */
	@Override
	public void displayList() {
		// TODO Auto-generated method stub
		List<String> names = new ArrayList<String>();
		List<String> details = new ArrayList<String>();
		List<LogData> items = MainApplication.getDetailData().getLogs();

		if (items != null) {
			for (int i = 0; i < items.size(); i++) {
				LogData item = items.get(i);
				if (this.filterString != ""
						&& ! item.getMessage().toLowerCase().contains(
								this.filterString.toLowerCase())) {
					continue;
				}
				names.add(String.format("Message: %s", item.getMessage()));
				details.add(String.format("Severity: %s", item.getSeverity()));
			}
		}
		mListView.setAdapter(new DoubleLineLayoutArrayAdapter(this, details,
				names));
	}

	/**
	 * @see com.appfirst.activities.lists.AFListActivity#loadResource()
	 */
	@Override
	public void loadResource() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see com.appfirst.activities.lists.AFListActivity#sortListItems()
	 */
	@Override
	public void sortListItems() {
		// TODO Auto-generated method stub
		DynamicComparator.sort(MainApplication.getDetailData().getLogs(), sortField
				.getName(), true);
		displayList();
	}
}
