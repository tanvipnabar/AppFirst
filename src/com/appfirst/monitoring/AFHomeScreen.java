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
package com.appfirst.monitoring;

import java.net.URL;

import com.appfirst.activities.lists.AFAlertHistoryList;
import com.appfirst.activities.lists.AFAlertList;
import com.appfirst.activities.lists.AFApplicationList;
import com.appfirst.activities.lists.AFPolledDataList;
import com.appfirst.activities.lists.AFServerList;
import com.appfirst.communication.Helper;
import com.appfirst.utils.VerticalImageTextGroupAdapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View;

/**
 * AFHomeScreen holds a {@link GridView} which displays each of AppFirst's
 * monitoring solutions with an icon and a title.
 * <p>
 * It uses {@link VerticalImageTextGroupAdapter} to render each of the
 * solutions.
 * </p>
 * 
 * @author Bin Liu
 */
public class AFHomeScreen extends Activity {
	private static final int PROGRESS_DIALOG = 0;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		GridView gridview = (GridView) findViewById(R.id.homeview);
		gridview.setAdapter(new VerticalImageTextGroupAdapter(this));

		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				navigateToView(position);
			}
		});
		initializeApplication();
	}
	
	private void initializeApplication() {
		if (MainApplication.servers == null) {
			showDialog(PROGRESS_DIALOG);
			new ResourceLoader().execute();
		}	
	}
	
	/**
	 * Load the selected view. 
	 * @param position the index of the view have been selected
	 */
	private void navigateToView(int position) {
		switch (position) {
		case 0:
			Intent server = new Intent(AFHomeScreen.this, AFServerList.class);
			startActivity(server);
			break;
		case 1:
			Intent application = new Intent(AFHomeScreen.this,
					AFApplicationList.class);
			startActivity(application);
			break;
		case 2:
			Intent alert = new Intent(AFHomeScreen.this, AFAlertList.class);
			startActivity(alert);
			break;
		case 3:
			Intent polledData = new Intent(AFHomeScreen.this, AFPolledDataList.class);
			startActivity(polledData);
			break;
		case 4:
			Intent alertHistory = new Intent(AFHomeScreen.this, AFAlertHistoryList.class);
			startActivity(alertHistory);
			break;
		case 5:
			Intent account = new Intent(AFHomeScreen.this, AFAccountManagement.class);
			startActivity(account);
			break;
		default:
			break;
		}
	}
	
	protected void loadResource() {
		MainApplication.loadServerList(Helper.getServerListUrl(this));
	}
	
	protected void displayResult() {
		dismissDialog(PROGRESS_DIALOG);
	}
	
	protected class ResourceLoader extends AsyncTask<URL, Integer, Long> {
		protected Long doInBackground(URL... urls) {
			loadResource();
			return 0L;
		}

		protected void onPostExecute(Long result) {
			displayResult();
		}
	}
	
	protected ProgressDialog progressDialog;
	/**
	 * Create the alert box using for showing progress.
	 * @return dialog box showing progress. 
	 */
	protected ProgressDialog createProcessDialog() {
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Initializing...");
		return progressDialog;
	}
	
	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case PROGRESS_DIALOG:
			progressDialog.setProgress(0);
		}
	}
	
	/**
	 * 
	 */
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		switch (id) {
		case PROGRESS_DIALOG:
			dialog = createProcessDialog();
			break;
		default:
			break;
		}

		return dialog;
	}
}
