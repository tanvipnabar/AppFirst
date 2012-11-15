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
import android.widget.ListView;

import com.appfirst.activities.details.AFProcessDetail;
import com.appfirst.activities.lists.AFListActivity.ResourceLoader;
import com.appfirst.monitoring2.R;
import com.appfirst.monitoring2.MainApplication;
import com.appfirst.types.Application;
import com.appfirst.utils.DoubleLineLayoutArrayAdapter;
import com.appfirst.utils.DynamicComparator;

/**
 * @author Bin Liu
 * 
 */
public class AFProcessList extends AFListActivity {
	protected List<com.appfirst.types.Process> processes;

	/** Called when the activity is first created. */
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setObjectClass(com.appfirst.types.Process.class);
		// Create an array of Strings, that will be put to our ListActivity
		setCurrentView();
		mTitle.setText("Processes: ");
		processes = MainApplication.getProcesses();
		displayList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.appfirst.activities.lists.AFListActivity#displayList()
	 */
	@Override
	public void displayList() {
		// TODO Auto-generated method stub
		mSortName = "name";
		if (sortField != null) {
			mSortName = sortField.getName();
		}
		mSortText.setText(getSortAndFilter());
		DynamicComparator.sort(processes, mSortName, true);
		List<String> names = new ArrayList<String>();
		List<String> details = new ArrayList<String>();
		List<Integer> ids = new ArrayList<Integer>();
		for (int cnt = 0; cnt < processes.size(); cnt++) {
			com.appfirst.types.Process process = processes.get(cnt);
			String name = process.getName();
			if (this.filterString != ""
					&& !name.toLowerCase().contains(filterString.toLowerCase())) {
				continue;
			}
			names.add(String.format("%s (pid:%d)", name, process.getPid()));
			details.add(process.getArgs());
			ids.add(process.getId());
		}
		// ListView lv = (ListView) findViewById(R.id.serverProcessList);
		mListView.setAdapter(new DoubleLineLayoutArrayAdapter(this, names,
				details, ids, AFProcessDetail.class));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.appfirst.activities.lists.AFListActivity#loadResource()
	 */
	@Override
	public void loadResource() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.appfirst.activities.lists.AFListActivity#sortListItems()
	 */
	@Override
	public void sortListItems() {
		// TODO Auto-generated method stub

	}

}
