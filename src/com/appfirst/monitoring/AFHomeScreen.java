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

//import org.json.JSONException;
import org.json.JSONObject;

import com.appfirst.activities.lists.AFAlertHistoryList;
import com.appfirst.activities.lists.AFAlertList;
import com.appfirst.activities.lists.AFApplicationList;
import com.appfirst.activities.lists.AFPolledDataList;
import com.appfirst.activities.lists.AFServerList;
import com.appfirst.animations.ExpandAnimation;
import com.appfirst.animations.CollapseAnimation;

import com.appfirst.communication.Helper;
import com.appfirst.types.AFDevice;
import com.appfirst.utils.VerticalImageTextGroupAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
//import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.util.Log;
import android.view.View;
import android.util.DisplayMetrics;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;


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
	private static final int SUBSCRIBE_DIALOG = 1;
	private static final String TAG = "AFHomeScreen";
	private static Boolean subscribeAll = false;
	private Boolean firstLogin = false;
	private LinearLayout MenuList;
	private Button btnToggleMenuList;
	private int screenWidth;
	private boolean isExpanded = false;


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home2);
		MenuList = (LinearLayout) findViewById(R.id.linearLayout2);
		btnToggleMenuList = (Button) findViewById(R.id.button1);
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		screenWidth = metrics.widthPixels;
		     
		//GridView gridview = (GridView) findViewById(R.id.homeview);
		//gridview.setAdapter(new VerticalImageTextGroupAdapter(this));
		ListView listView = (ListView) findViewById(R.id.homeview);
		listView.setAdapter(new VerticalImageTextGroupAdapter(this));
		
		//gridview.setOnItemClickListener(new OnItemClickListener() {
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				navigateToView(position);
			}
		});

		if (MainApplication.getDevice() == null) {// only does this when
			// application starts
			firstLogin = MainApplication.getFirstTimeLogin(this);
			if (firstLogin) { // only does this when
				// user first log in
				showDialog(SUBSCRIBE_DIALOG);
			}
			initializeApplication();
		}
		handleIntent(getIntent());
		
		btnToggleMenuList.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isExpanded) {
        			isExpanded = false;
        			MenuList.startAnimation(new CollapseAnimation(MenuList, 0,(int)(screenWidth*0.7), 20));
        		}else {
            		isExpanded = true;
            		MenuList.startAnimation(new ExpandAnimation(MenuList, 0,(int)(screenWidth*0.7), 20));
        		}
			}
        });
	}
	

	private void initializeApplication() {
		showDialog(PROGRESS_DIALOG);
		new ResourceLoader().execute();
	}

	/**
	 * Load the selected view.
	 * 
	 * @param position
	 *            the index of the view have been selected
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
			Intent polledData = new Intent(AFHomeScreen.this,
					AFPolledDataList.class);
			startActivity(polledData);
			break;
		case 4:
			Intent alertHistory = new Intent(AFHomeScreen.this,
					AFAlertHistoryList.class);
			startActivity(alertHistory);
			break;
		case 5:
			Intent account = new Intent(AFHomeScreen.this,
					AFAccountManagement.class);
			startActivity(account);
			break;
		default:
			break;
		}
	}

	/**
	 * Register device and get the device id from the database. This function is
	 * always called when home screen is loaded.
	 */
	protected void loadResource() {
		if (MainApplication.getUid() != "") {
			JSONObject deviceObject = null;
			try {
				if (firstLogin) {
					deviceObject = MainApplication.client.saveDeviceInfo(Helper
							.getDeviceUrl(this, -1), getString(R.string.brand),
							MainApplication.getUid(), subscribeAll);
				} else {
					deviceObject = MainApplication.client.saveDeviceInfo(Helper
							.getDeviceUrl(this, -1), getString(R.string.brand),
							MainApplication.getUid());
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				Toast toast = Toast
						.makeText(
								this,
								"Can't save device information, check your network connection",
								Toast.LENGTH_LONG);
				toast.show();
				finish();
			}
			if (deviceObject != null) {
				try {
					MainApplication.setDevice(new AFDevice(deviceObject));
					Log.i(TAG, String.format("Get new device id: %d",
							MainApplication.getDevice().getId()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	protected void displayResult() {
		try {
			dismissDialog(PROGRESS_DIALOG);
		} catch (Exception e) {
			// TODO: handle exception
		}
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
	 * 
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
		case SUBSCRIBE_DIALOG:
			dialog = createSubscribeDialog();
			break;
		default:
			break;
		}

		return dialog;
	}

	/**
	 * Create the alert box to allow user to select whether receive all
	 * notifications from this device. This only happens the first time user
	 * login, or change its current login to another one.
	 * 
	 * @return an alert dialog box to choose "YES" or "NO".
	 */
	private Dialog createSubscribeDialog() {
		AlertDialog.Builder builder;
		AlertDialog alertDialog;

		builder = new AlertDialog.Builder(this);
		builder.setMessage("Receive all alerts through this device?")
				.setCancelable(false).setPositiveButton("YES",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dismissDialog(SUBSCRIBE_DIALOG);
								subscribeAll = true;
							}
						}).setNegativeButton("NO",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
								subscribeAll = false;
							}
						});

		alertDialog = builder.create();
		return alertDialog;
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// Because this activity has set launchMode="singleTop", the system
		// calls this method
		// to deliver the intent if this actvity is currently the foreground
		// activity when
		// invoked again (when the user executes a search from this activity, we
		// don't create
		// a new instance of this activity, so the system delivers the search
		// intent here)
		handleIntent(intent);
	}

	private void handleIntent(Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			// handles a search query
			//String query = intent.getStringExtra(SearchManager.QUERY);
			// showResults(query);
		}
	}
}
