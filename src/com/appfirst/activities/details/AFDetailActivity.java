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
import java.util.ArrayList;
import java.util.List;

import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer.Orientation;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.appfirst.datatypes.BaseResourceData;
import com.appfirst.datatypes.PolledDataData;
import com.appfirst.datatypes.ProcessData;
import com.appfirst.datatypes.SystemData;
import com.appfirst.monitoring2.MainHelper;
import com.appfirst.views.Helper;

/**
 * Detail view for display all the aspects for Server, Application, Poll data,
 * Tag and process.
 * 
 * @author Bin Liu
 * 
 */
public abstract class AFDetailActivity extends Activity {
	/**
	 * Update the view according to which item has been selected in the previous
	 * activities. Mostly coming from any list activities.
	 * 
	 * @param selected
	 *            the index of data object, for querying the data.
	 */
	protected abstract void updateViewWithSelected(int selected);

	/**
	 * Get the process list data for an application or a server.
	 */
	protected abstract void getProcessList();

	/**
	 * Set the process list data.
	 */
	protected abstract void setProcessList();

	/**
	 * Set the data according to the type of extended object.
	 */
	protected abstract void setData();

	/**
	 * Get the data for the current minute, which could be {@link SystemData} or
	 * {@link ProcessData} or {@link PolledDataData}
	 */
	protected abstract void getData();

	/**
	 * Get the graph data, or historical data for a process, application, polled
	 * data or server.
	 */
	protected abstract void getGraphData();

	/**
	 * Display the data in a graph. Data should be a list of
	 * {@link ResourceDataBase} in the time increasing order.
	 */
	protected abstract void setGraphData();

	/**
	 * List the resource options that are graphable.
	 */
	protected abstract void setupGraphOptions();

	protected ProgressDialog progressDialog;
	protected List<String> mGraphOptions = new ArrayList<String>();
	protected List<Boolean> mGraphResource = new ArrayList<Boolean>();
	protected int[] mGraphColors = { 0xff4bacc6, 0xffbf44ba, 0xff9dbb61,
			0xff8066a0, 0xffe1b10d, 0xff5c83b4, 0xffbb8892, 0xfff59d56,
			0xffc0504d, 0xff7dcc5e, 0xffd196e7, 0xff00ff00, 0xffff0000,
			0xff29a235, 0xff949494, 0xffc06f4a, 0xffcfc800, 0xffcb810e,
			0xff343434, 0xffffd751, 0xff8c8ab4, 0xff2f89b8 };

	static final int PROGRESS_DIALOG = 0;
	static final int OPTION_DIALOG = 1;
	static final int DISK_DIALOG = 2;
	static final int CPU_DIALOG = 3;
	static final int DISK_BUSY_DIALOG = 4;
	public final String CPU_DISPALY_NAME = "CPU (%)";
	public final String MEMORY_DISPLAY_NAME = "Memory (MB)";
	public final String THREAD_DISPLAY_NAME = "Thread";
	public final String PROCESS_DISPLAY_NAME = "Process";
	public final String DISK_PERCENT_DISPLAY_NAME = "Disk (%)";
	public final String PAGE_FAULT_DISPLAY_NAME = "Page fault";
	public final String FILE_READ_DISPLAY_NAME = "File read (KB)";
	public final String FILE_WRITE_DISPLAY_NAME = "File write (KB)";
	public final String FILE_NUM_DISPLAY_NAME = "File accessed";
	public final String SOCKET_READ_DISPLAY_NAME = "Socket read (KB)";
	public final String SOCKET_WRITE_DISPLAY_NAME = "Socket write (KB)";
	public final String SOCKET_NUM_DISPLAY_NAME = "Network connections";
	public final String CRITICAL_INCIDENTS_DISPLAY_NAME = "Critical incidents reports";
	public final String INCIDENTS_DISPLAY_NAME = "Incidents resports";
	public final String RESPONSE_NUM_DISPLAY_NAME = "Response number";
	public final String AVG_RESPONSE_DISPLAY_NAME = "AVG response time (us)";
	public final String REGISTRY_DISPLAY_NAME = "Registry accessed";

	protected int currentDialogId = -1;
	protected int dialogMaxInnerSpace = 180;

	/**
	 * Default constuctor.
	 */
	public AFDetailActivity() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case PROGRESS_DIALOG:
			progressDialog.setProgress(0);
		}
		currentDialogId = id;
	}

	protected void setTextView(Activity context, int id, String content) {
		TextView view = (TextView) context.findViewById(id);
		if (content != null) {
			view.setText(content);
		}
	}

	protected void setTextView(Activity context, int id, Boolean bool) {
		TextView view = (TextView) context.findViewById(id);
		if (bool) {
			view.setText("YES");
		} else {
			view.setText("No");
		}
	}

	protected void setCheckBox(Activity context, int id, Boolean checked) {
		CheckBox view = (CheckBox) context.findViewById(id);
		view.setChecked(checked);
	}

	/**
	 * Nested class for updating process list.
	 * 
	 * @author Bin Liu
	 */
	protected class ProcessListUpdater extends AsyncTask<URL, Integer, Long> {
		protected Long doInBackground(URL... urls) {
			getProcessList();
			return 0L;
		}

		protected void onPostExecute(Long result) {
			setProcessList();
		}
	}

	/**
	 * Nested class for updating data.
	 * 
	 * @author Bin Liu
	 * 
	 */
	protected class DataUpdater extends AsyncTask<URL, Integer, Long> {
		protected Long doInBackground(URL... urls) {
			getData();
			return 0L;
		}

		protected void onPostExecute(Long result) {
			setData();
		}
	}

	/**
	 * Nested class for updating graph data.
	 * 
	 * @author Bin Liu
	 * 
	 */
	protected class GraphUpdater extends AsyncTask<URL, Integer, Long> {
		protected Long doInBackground(URL... urls) {
			getGraphData();
			return 0L;
		}

		protected void onPostExecute(Long result) {
			setGraphData();
		}
	}

	/**
	 * 
	 * @return a list of resources name to be graphed.
	 */
	public List<String> getGraphOptions() {
		return new ArrayList<String>();
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// SubMenu sortMenuItem = menu.addSubMenu(0, 1, 0, "graph");
	// List<String> options = getGraphOptions();
	// int cnt = 0;
	// for (cnt = 0; cnt < options.size(); cnt++) {
	// sortMenuItem.add(1, cnt, 0, options.get(cnt)).setCheckable(true);
	// }
	// sortMenuItem.add(1, cnt, 0, "OK").setCheckable(false);
	// return super.onCreateOptionsMenu(menu);
	// }
	//
	// @Override
	// public boolean onOptionsItemSelected(MenuItem item) {
	// if (item.isChecked()) {
	// item.setChecked(false);
	// } else {
	// item.setChecked(true);
	// }
	// String fieldName = item.toString();
	// if (fieldName == "OK") {
	// return true;// super.onOptionsItemSelected(item);
	// } else {
	// return false;
	// }
	// }

	/**
	 * Nested class that performs progress calculations (counting)
	 */
	class ProgressThread extends Thread {
		Handler mHandler;
		final static int STATE_DONE = 0;
		final static int STATE_RUNNING = 1;
		int mState;
		int total;

		ProgressThread(Handler h) {
			mHandler = h;
		}

		public void run() {
			mState = STATE_RUNNING;
			total = 0;

			while (mState == STATE_RUNNING) {
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					Log.e("ERROR", "Thread Interrupted");
				}
				Message msg = mHandler.obtainMessage();
				msg.arg1 = total;
				mHandler.sendMessage(msg);
				total++;
			}
		}

		/*
		 * sets the current state for the thread, used to stop the thread
		 */
		public void setState(int state) {
			mState = state;
		}
	}

	/**
	 * Create the graph resource options dialog according to the extended class.
	 * 
	 * @return the view created dynamically.
	 */
	protected View createGraphOptionDialog() {
		LinearLayout outerContainer = new LinearLayout(this);
		ScrollView container = new ScrollView(this);
		container.setVerticalScrollBarEnabled(true);
		container.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				Helper.convertDpToPx(200, this)));
		LinearLayout linear = new LinearLayout(this);
		linear.setOrientation(LinearLayout.VERTICAL);
		linear.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		linear.setVerticalScrollBarEnabled(true);

		List<String> options = getGraphOptions();
		for (int cnt = 0; cnt < options.size(); cnt++) {
			CheckBox newRadioButton = new CheckBox(this);
			newRadioButton.setChecked(mGraphResource.get(cnt));
			newRadioButton.setText(options.get(cnt));
			newRadioButton.setTextSize(Helper.convertDpToPx(15, this));
			newRadioButton.setLayoutParams(new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			newRadioButton
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							String text = (String) buttonView.getText();

							for (int i = 0; i < mGraphOptions.size(); i++) {
								if (text == mGraphOptions.get(i)) {
									mGraphResource.set(i, isChecked);
								}
							}
						}
					});
			linear.addView(newRadioButton);
		}
		container.addView(linear);
		outerContainer.addView(container);
		return outerContainer;
	}

	/**
	 * Create the alert box using for showing progress.
	 * 
	 * @return dialog box showing progress.
	 */
	protected ProgressDialog createProcessDialog() {
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Querying...");
		return progressDialog;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		
		if (currentDialogId == DISK_DIALOG
				|| currentDialogId == DISK_BUSY_DIALOG
				|| currentDialogId == CPU_DIALOG) {
			int backupId = currentDialogId;
			removeDialog(currentDialogId);
			setDialogMaxInnerSpace();
			showDialog(backupId);
		} else {
			setDialogMaxInnerSpace();
		}
		
		
	}

	/**
	 * 
	 */
	protected void setDialogMaxInnerSpace() {
		/* First, get the Display from the WindowManager */
		Display display = ((WindowManager) getSystemService(WINDOW_SERVICE))
				.getDefaultDisplay();

		/* Now we can retrieve all display-related infos */
		int height = display.getHeight();
		dialogMaxInnerSpace = height / 3;
	}

	/**
	 * Create the alert box of choosing graph resource options. The inner view
	 * is from the function createGraphOptionDialog.
	 * 
	 * @return alert dialog box.
	 */
	protected AlertDialog createOptionDialog() {
		AlertDialog.Builder builder;
		AlertDialog alertDialog;

		builder = new AlertDialog.Builder(this);
		builder.setView(createGraphOptionDialog());

		builder.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						removeDialog(OPTION_DIALOG);
						displayGraphData();
					}
				}).setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		
		if (MainHelper.isScreenVertical(this, WINDOW_SERVICE)) {
			builder.setMessage("Choose resources");
		}

		alertDialog = builder.create();
		return alertDialog;
	}

	protected double getDataValue(BaseResourceData data, String field) {
		return 0;
	}

	/**
	 * Create dialogs.
	 */
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		switch (id) {
		case PROGRESS_DIALOG:
			dialog = createProcessDialog();
			break;
		case OPTION_DIALOG:
			dialog = createOptionDialog();
			break;
		case DISK_DIALOG:
			dialog = createDiskDialog();
			break;
		case CPU_DIALOG:
			dialog = createCPUDialog();
			break;
		case DISK_BUSY_DIALOG:
			dialog = createDiskBusyDialog();
			break;
		default:
			break;
		}

		currentDialogId = id;

		return dialog;
	}

	/**
	 * Need to be overwritten.
	 * 
	 * @return
	 */
	protected Dialog createDiskBusyDialog() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Need to be overwritten.
	 * 
	 * @return
	 */
	protected Dialog createCPUDialog() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Need to be overwritten.
	 * 
	 * @return
	 */
	protected Dialog createDiskDialog() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Define the renderer style of each series.
	 * 
	 * @return renderer object to be used in the extended class.
	 */
	protected XYMultipleSeriesRenderer getDemoRenderer() {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		renderer.setAxisTitleTextSize(Helper.convertDpToPx(12, this));
		renderer.setChartTitleTextSize(Helper.convertDpToPx(15, this));
		renderer.setLabelsTextSize(Helper.convertDpToPx(12, this));
		renderer.setLegendTextSize(Helper.convertDpToPx(12, this));
		renderer.setPointSize(Helper.convertDpToPx(12, this));
		renderer.setYAxisMin(0.0);
		renderer.setOrientation(Orientation.HORIZONTAL);
		renderer.setMargins(new int[] { Helper.convertDpToPx(15, this),
				Helper.convertDpToPx(30, this), Helper.convertDpToPx(15, this),
				0 });
		for (int cnt = 0; cnt < mGraphOptions.size(); cnt++) {
			if (mGraphResource.get(cnt)) {
				XYSeriesRenderer r = new XYSeriesRenderer();
				r.setLineWidth(Helper.convertDpToPx(3, this));
				r.setColor(mGraphColors[cnt % mGraphColors.length]);
				renderer.addSeriesRenderer(r);
			}
		}

		renderer.setAxesColor(Color.DKGRAY);
		renderer.setLabelsColor(Color.WHITE);
		renderer.setZoomEnabled(false, false);
		return renderer;
	}

	/**
	 * 
	 */
	protected void displayGraphData() {
		// TODO Auto-generated method stub

	}

	protected void resetGraphResources(int index) {
		for (int cnt = 0; cnt < mGraphResource.size(); cnt++) {
			if (index == cnt) {
				mGraphResource.set(cnt, true);
			} else {
				mGraphResource.set(cnt, false);
			}
		}
	}

	protected void setRowLongClickEvent(int id, final int index) {
		TableRow tableRow = (TableRow) findViewById(id);
		tableRow.setLongClickable(true);
		tableRow.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				resetGraphResources(index);
				displayGraphData();
				//return false;
			}
		});
	}
	
	protected void toastErrorMessage() {
		Toast toast = Toast.makeText(this, "Error loading data", Toast.LENGTH_LONG);
		toast.show();
	}
	
	
}
