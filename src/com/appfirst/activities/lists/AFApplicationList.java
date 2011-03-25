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

import com.appfirst.activities.details.AFApplicationDetail;
import com.appfirst.monitoring.MainApplication;
import com.appfirst.monitoring.R;
import com.appfirst.types.Application;
import com.appfirst.utils.DynamicComparator;

/**
 * Display the list of application 
 * 
 * @author Bin Liu
 * 
 */
public class AFApplicationList extends AFListActivity {

	/** Called when the activity is first created. */
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setObjectClass(Application.class);
		// Create an array of Strings, that will be put to our ListActivity

		if (MainApplication.applications == null) {
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
		String sortName = "name";
		if (sortField != null) {
			sortName = sortField.getName();
		}
		DynamicComparator.sort(MainApplication.applications, sortName, true);
		List<String> names = new ArrayList<String>();
		List<Application> items = MainApplication.applications;
		for (int i = 0; i < items.size(); i++) {
			Application item = items.get(i);
			names.add(item.getName());
		}
		// Create an ArrayAdapter, that will actually make the Strings above
		// appear in the ListView
		this.setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_checked, names));
	}

	/**
	 * Start the detailed application instance. 
	 */
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// Get the item that was clicked
		Intent intent = new Intent(AFApplicationList.this,
				AFApplicationDetail.class);
		intent.putExtra(Application.class.getName() + ".selected", position);
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
				getString(R.string.api_applications));
		MainApplication.loadApplicationList(url);
	}

}
