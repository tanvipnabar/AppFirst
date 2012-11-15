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

import com.appfirst.communication.Helper;
import com.appfirst.datatypes.ThreadData;
import com.appfirst.monitoring2.MainApplication;
import com.appfirst.utils.DoubleLineLayoutArrayAdapter;
import com.appfirst.utils.DynamicComparator;

/**
 * @author Bin Liu
 * 
 */
public class AFThreadList extends AFListActivity {

	/** Called when the activity is first created. */
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setObjectClass(ThreadData.class);
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
		List<ThreadData> items = MainApplication.getDetailData().getThreads();
		if (items != null) {
			for (int i = 0; i < items.size(); i++) {
				ThreadData item = items.get(i);
				String threadName = formatThreadName(item);
				String threadDetail = formatThreadDetail(item);
				if (this.filterString != ""
						&& !threadName.toLowerCase().contains(
								this.filterString.toLowerCase())
						&& !threadDetail.toLowerCase().contains(
								this.filterString.toLowerCase())) {
					continue;
				}
				names.add(formatThreadName(item));
				details.add(formatThreadDetail(item));
			}
		}
		mListView.setAdapter(new DoubleLineLayoutArrayAdapter(this, names,
				details));
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
		DynamicComparator.sort(MainApplication.getDetailData().getThreads(),
				sortField.getName(), true);
		displayList();
	}

	/**
	 * Format thread name as Thread id and Thread created time.
	 * 
	 * @param item
	 *            a {@link ThreadData} object
	 * @return formatted thread name
	 */
	private String formatThreadName(ThreadData item) {
		return String.format("Thread %d - created %s", item.getId(), item
				.getCreate_time());
	}

	/**
	 * Format thread detail with other members of {@link ThreadData}
	 * 
	 * @param item
	 *            a {@link ThreadData} object
	 * @return formatted thread detail
	 */
	private String formatThreadDetail(ThreadData item) {
		String detail = String.format("Kernel time: %s User time: %s, "
				+ "Stack size: %s", Helper.formatTimeValue(item
				.getKernel_time()),
				Helper.formatTimeValue(item.getUser_time()), Helper
						.formatByteValue(item.getStack_size()));
		if (item.getExit_time() != "") {
			detail += String.format("Exit time: %s", item.getExit_time());
		}
		return detail;
	}
}
