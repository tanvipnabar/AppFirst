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

import com.appfirst.activities.lists.AFFileList;
import com.appfirst.activities.lists.AFLogList;
import com.appfirst.activities.lists.AFRegistryList;
import com.appfirst.activities.lists.AFSocketList;
import com.appfirst.activities.lists.AFThreadList;
import com.appfirst.monitoring2.R;
import com.appfirst.monitoring2.MainApplication;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

/**
 * Minute detail view for application and process. It consists of 5 parts
 * (connections, threads, logs, files and registries) which are showing in the
 * tab.
 * 
 * @author Bin Liu
 * 
 */
public class AFMinuteDetail extends TabActivity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_tab);
		// Initialize a TabSpec for each tab and add it to the TabHost
		addSocketTab();
		addThreadTab();
		addFileTab();
		addLogTab();
		addRegistryTab();
	}

	/**
	 * Add a tab to display log data based on whether data is available.
	 */
	private void addLogTab() {
		TabHost tabHost = getTabHost(); // The activity TabHost
		TabHost.TabSpec spec; // Resusable TabSpec for each tab
		Intent intent; // Reusable Intent for each tab
		if (MainApplication.getDetailData().getLogs() != null
				&& MainApplication.getDetailData().getLogs().size() > 0) {
			// Create an Intent to launch an Activity for the tab (to be reused)
			intent = new Intent().setClass(this, AFLogList.class);

			// Initialize a TabSpec for each tab and add it to the TabHost
			spec = tabHost.newTabSpec("Logs").setIndicator("Logs",
					getResources().getDrawable(R.drawable.ic_tab_logs))
					.setContent(intent);
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
		
		if (MainApplication.getDetailData().getFiles() != null
				&& MainApplication.getDetailData().getFiles().size() > 0) {
			intent = new Intent().setClass(this, AFFileList.class);
			// Initialize a TabSpec for each tab and add it to the TabHost
			spec = tabHost.newTabSpec("Files").setIndicator("Files",
					getResources().getDrawable(R.drawable.ic_tab_files))
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

		if (MainApplication.getDetailData().getThreads() != null
				&& MainApplication.getDetailData().getThreads().size() > 0) {
			intent = new Intent().setClass(this, AFThreadList.class);

			// Initialize a TabSpec for each tab and add it to the TabHost
			spec = tabHost.newTabSpec("Threads").setIndicator("Threads",
					getResources().getDrawable(R.drawable.ic_tab_threads))
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

		if (MainApplication.getDetailData().getSockets() != null
				&& MainApplication.getDetailData().getSockets().size() > 0) {
			intent = new Intent().setClass(this, AFSocketList.class);
			spec = tabHost.newTabSpec("Sockets").setIndicator("Sockets",
					getResources().getDrawable(R.drawable.ic_tab_connections))
					.setContent(intent);
			tabHost.addTab(spec);
		}
	}
	
	/**
	 * Add a tab to display Registry data based on whether data is available.
	 */
	private void addRegistryTab() {
		TabHost tabHost = getTabHost(); // The activity TabHost
		TabHost.TabSpec spec; // Resusable TabSpec for each tab
		Intent intent; // Reusable Intent for each tab

		if (MainApplication.getDetailData().getRegistries() != null
				&& MainApplication.getDetailData().getRegistries().size() > 0) {
			intent = new Intent().setClass(this, AFRegistryList.class);
			spec = tabHost.newTabSpec("Registries").setIndicator("Registries",
					getResources().getDrawable(R.drawable.ic_tab_registries))
					.setContent(intent);
			tabHost.addTab(spec);
		}
	}
}
