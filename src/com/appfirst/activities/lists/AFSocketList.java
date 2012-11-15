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
import com.appfirst.datatypes.SocketData;
import com.appfirst.monitoring2.MainApplication;
import com.appfirst.utils.DoubleLineLayoutArrayAdapter;
import com.appfirst.utils.DynamicComparator;

/**
 * @author Bin Liu
 * 
 */
public class AFSocketList extends AFListActivity {

	/** Called when the activity is first created. */
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setObjectClass(SocketData.class);
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
		List<SocketData> items = MainApplication.getDetailData().getSockets();
		if (items != null) {
			for (int i = 0; i < items.size(); i++) {
				SocketData item = items.get(i);
				String socketName = formatSocketName(item);
				String socketDetail = formatSocketDetail(item);
				if (this.filterString != ""
						&& !socketName.toLowerCase().contains(
								this.filterString.toLowerCase())
						&& !socketDetail.toLowerCase().contains(
								this.filterString.toLowerCase())) {
					continue;
				}
				names.add(formatSocketName(item));
				details.add(formatSocketDetail(item));
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
		DynamicComparator.sort(MainApplication.getDetailData().getSockets(),
				sortField.getName(), true);
		displayList();
	}

	/**
	 * 
	 * @param item
	 * @return
	 */
	private String formatSocketName(SocketData item) {
		return String.format("%s From %s:%d to %s:%d", item.getType(), item
				.getSocket_ip(), item.getSocket_port(), item.getPeer_ip(), item
				.getPeer_port());
	}

	/**
	 * 
	 * @param item
	 * @return
	 */
	private String formatSocketDetail(SocketData item) {
		String ret = "";
		ret = String.format(
				"Status: %s, Received: %s Sent: %s Time open (sec): %s Process: %s"
						+ " Thread id: %d", item.getStatus(), Helper
						.formatByteValue(item.getData_received()), Helper
						.formatByteValue(item.getData_sent()), item
						.getTime_open(), item.getProcess_name(), item
						.getThread_id());
		return ret;
	}
}
