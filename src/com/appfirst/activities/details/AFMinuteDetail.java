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
package com.appfirst.activities.details;

import java.net.URL;

import com.appfirst.activities.lists.AFFileList;
import com.appfirst.activities.lists.AFLogList;
import com.appfirst.activities.lists.AFSocketList;
import com.appfirst.activities.lists.AFThreadList;
import com.appfirst.datatypes.DetailData;
import com.appfirst.monitoring.MainApplication;
import com.appfirst.monitoring.R;
import com.appfirst.types.Alert;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TabHost;

/**
 * @author Bin Liu
 * 
 */
public class AFMinuteDetail extends TabActivity {
	private String url;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_tab);
		// Initialize a TabSpec for each tab and add it to the TabHost
		addSocketTab();
		addThreadTab();
		addFileTab();
		addLogTab();
	}

	/**
	 * Add a tab to display log data based on whether data is available. 
	 */
	private void addLogTab() {
		TabHost tabHost = getTabHost(); // The activity TabHost
		TabHost.TabSpec spec; // Resusable TabSpec for each tab
		Intent intent; // Reusable Intent for each tab
		if (MainApplication.detailData.getLogs() != null
				&& MainApplication.detailData.getLogs().size() > 0) {
			// Create an Intent to launch an Activity for the tab (to be reused)
			intent = new Intent().setClass(this, AFLogList.class);

			// Initialize a TabSpec for each tab and add it to the TabHost
			spec = tabHost.newTabSpec("logs").setIndicator("Logs").setContent(
					intent);
			tabHost.addTab(spec);
		}
	}

	/**
	 * Add a tab to display File data based on whether data is available.  
	 */
	private void addFileTab() {
		TabHost tabHost = getTabHost(); // The activity TabHost
		TabHost.TabSpec spec; // Resusable TabSpec for each tab
		Intent intent; // Reusable Intent for each tab

		if (MainApplication.detailData.getFiles() != null
				&& MainApplication.detailData.getFiles().size() > 0) {
			intent = new Intent().setClass(this, AFFileList.class);
			// Initialize a TabSpec for each tab and add it to the TabHost
			spec = tabHost.newTabSpec("files").setIndicator("files")
					.setContent(intent);
			tabHost.addTab(spec);
		}
	}

	/**
	 * Add a tab to display Thread data based on whether data is available. 
	 */
	private void addThreadTab() {
		TabHost tabHost = getTabHost(); // The activity TabHost
		TabHost.TabSpec spec; // Resusable TabSpec for each tab
		Intent intent; // Reusable Intent for each tab

		if (MainApplication.detailData.getThreads() != null
				&& MainApplication.detailData.getThreads().size() > 0) {
			intent = new Intent().setClass(this, AFThreadList.class);

			// Initialize a TabSpec for each tab and add it to the TabHost
			spec = tabHost.newTabSpec("threads").setIndicator("threads")
					.setContent(intent);
			tabHost.addTab(spec);
		}
	}

	/**
	 * Add a tab to display Socket data based on whether data is available. 
	 */
	private void addSocketTab() {
		TabHost tabHost = getTabHost(); // The activity TabHost
		TabHost.TabSpec spec; // Resusable TabSpec for each tab
		Intent intent; // Reusable Intent for each tab

		if (MainApplication.detailData.getSockets() != null
				&& MainApplication.detailData.getSockets().size() > 0) {
			intent = new Intent().setClass(this, AFSocketList.class);
			spec = tabHost.newTabSpec("sockets").setIndicator("sockets")
					.setContent(intent);
			tabHost.addTab(spec);
		}
	}
}
