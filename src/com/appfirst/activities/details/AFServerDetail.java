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


import java.util.Date;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.apache.http.message.BasicNameValuePair;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.appfirst.activities.lists.AFProcessList;
import com.appfirst.communication.Helper;
import com.appfirst.datatypes.SystemData;
import com.appfirst.monitoring2.R;
import com.appfirst.monitoring2.MainApplication;
import com.appfirst.monitoring2.MainHelper;
import com.appfirst.types.Server;
import com.appfirst.views.AFBarView;
import com.appfirst.views.AFPieView;

/**
 * Display the server detail. Including system information, current system
 * resource usage, process list and ability to show historical data.
 * 
 * @author Bin Liu
 * 
 */
public class AFServerDetail extends AFDetailActivity {

	private final String TAG = "AFServerDetail";
	private Button mButton;
	private Button mShowProcesses;
	private Button mAllCpuCoreButton;
	private Button mAllDiskBusyButton;
	private Button mAllDiskUsageButton;

	private List<SystemData> mGraphData;
	private Boolean bRefreshGraph = false;
	private Server mServer;

	private int server_id = 0;
	protected SystemData data;
	protected List<com.appfirst.types.Process> processes;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.server);
		Intent i = getIntent();
		int selected = i.getIntExtra(AFServerDetail.class.getName()
				+ ".selected", -1);
		if (selected < 0) {
			Log.e(TAG, "Unlikely, nothing selected");
			return;
		}
		updateViewWithSelected(selected);
		setDialogMaxInnerSpace();
		setupGraphOptions();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 0, "CPU cores").setIcon(R.drawable.ic_menu_cpu);
		menu.add(0, 1, 0, "Disk usage").setIcon(R.drawable.ic_menu_disk);
		menu.add(0, 1, 0, "Disk busy").setIcon(R.drawable.ic_menu_diskbusy);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		String fieldName = item.toString();
		if (data == null) {
			Toast.makeText(this, "Data is not available, please wait.",
					Toast.LENGTH_LONG);
			return super.onOptionsItemSelected(item);
		}
		Log.i(TAG, String.format("Field change to: %s", fieldName));
		if (fieldName == "Disk usage") {
			showDialog(DISK_DIALOG);
		} else if (fieldName == "CPU cores") {
			showDialog(CPU_DIALOG);
		} else if (fieldName == "Disk busy") {
			showDialog(DISK_BUSY_DIALOG);
		}
		return super.onOptionsItemSelected(item);
	}
	
	

	protected XYMultipleSeriesDataset getChartDataset(List<SystemData> graphData) {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

		for (int i = 0; i < mGraphOptions.size(); i++) {
			if (mGraphResource.get(i)) {
				TimeSeries series = new TimeSeries(mGraphOptions.get(i));
				for (int k = 0; k < graphData.size(); k++) {
					SystemData data = graphData.get(k);
					series.add(new Date(data.getTime() * 1000), getDataValue(
							data, mGraphOptions.get(i)));
				}
				dataset.addSeries(series);
			}
		}
		return dataset;
	}

	@Override
	protected void updateViewWithSelected(int selected) {
		try {
			mServer = MainApplication.getServers().get(selected);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (mServer == null) {
			toastErrorMessage();
			finish();
		}
		
		String cpuInfo = String.format("%s cores at %s MHZ", mServer
				.getCapacity_cpu_num(), mServer.getCapacity_cpu_freq());
		String memInfo = Helper.formatByteValue(mServer.getCapacity_mem());
		setTextView(this, R.id.serverHostname, mServer.getHostname());
		setTextView(this, R.id.serverCpuCapacity, cpuInfo);
		setTextView(this, R.id.serverMemCapacity, memInfo);
		setTextView(this, R.id.serverOSType, mServer.getOs());
		setTextView(this, R.id.serverDiskCapacity, Helper.formatByteValue(
				mServer.getTotalDisk() * 1000000));
		server_id = mServer.getId();
		setClickEvents();

		new DataUpdater().execute();
		new ProcessListUpdater().execute();
		new GraphUpdater().execute();
	}

	/**
	 * Set up all the click events that this view can trigger.
	 */
	private void setClickEvents() {
		mButton = (Button) findViewById(R.id.serverShowGraph);
		mButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(OPTION_DIALOG);
			}
		});

		mShowProcesses = (Button) findViewById(R.id.serverShowProcesses);
		mShowProcesses.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showRunningProcesses();
			}
		});

		mAllCpuCoreButton = (Button) findViewById(R.id.serverShowAllCPUCores);
		mAllCpuCoreButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(CPU_DIALOG);
			}
		});

		mAllDiskBusyButton = (Button) findViewById(R.id.serverShowAllDiskBusy);
		mAllDiskBusyButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(DISK_BUSY_DIALOG);
			}
		});

		mAllDiskUsageButton = (Button) findViewById(R.id.serverShowAllDiskUsage);
		mAllDiskUsageButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(DISK_DIALOG);
			}
		});

		setRowLongClickEvent(R.id.serverCpuValueRow, 0);
		setRowLongClickEvent(R.id.serverMemoryValueRow, 1);
		setRowLongClickEvent(R.id.serverDiskValueRow, 2);
		setRowLongClickEvent(R.id.serverPFValueRow, 3);
		setRowLongClickEvent(R.id.serverPRValueRow, 4);
		setRowLongClickEvent(R.id.serverTRValueRow, 5);
	}

	

	private void showRunningProcesses() {
		Intent intent = new Intent(this, AFProcessList.class);
		startActivity(intent);
	}

	/**
	 * Set server data after it's loaded.
	 */
	@Override
	protected void setData() {
		if (data != null) {
			setResourceUsages();
			mAllCpuCoreButton.setVisibility(View.VISIBLE);
			mAllDiskBusyButton.setVisibility(View.VISIBLE);
			mAllDiskUsageButton.setVisibility(View.VISIBLE);
		} else {
			setTextView(this, R.id.serverResourceUsageLabel, String.format(
					"Resource usage %s:", "(update failed)"));
		}
	}

	/**
	 * 
	 */
	private void setResourceUsages() {
		setCpuData();
		setMemoryData();
		setDiskData();
		setTextView(this, R.id.serverPFValue, String.format("%d", data
				.getPage_faults()));
		setTextView(this, R.id.serverPRValue, String.format("%d", data
				.getProcess_num()));
		setTextView(this, R.id.serverTRValue, String.format("%d", data
				.getThread_num()));
		setTextView(this, R.id.serverResourceUsageLabel, String.format(
				"Resource usage at %s:", Helper
						.formatLongTime(data.getTime() * 1000)));
	}

	/**
	 * Display the Disk data along with the graph.
	 */
	private void setDiskData() {
		LinearLayout container = (LinearLayout) findViewById(R.id.serverDiskContainer);
		AFBarView barView = new AFBarView(this, data.getDisk_percent() / 100);
		barView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		container.addView(barView);
	}

	/**
	 * Display the CPU data along with the graph.
	 */
	private void setCpuData() {
		LinearLayout cpuCores = (LinearLayout) findViewById(R.id.serverCpuContainer);
		AFBarView barView = new AFBarView(this, data.getCpu() / 100);
		barView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		cpuCores.addView(barView);
	}

	/**
	 * Display the memory data along with the graph.
	 */
	private void setMemoryData() {
		Double capMemory = Double.MAX_VALUE;
		if (mServer.getCapacity_mem() != 0) {
			capMemory = Double.parseDouble(String.format("%d", mServer
					.getCapacity_mem()));
		}
		Double percentage = data.getMemory() / capMemory;
		LinearLayout container = (LinearLayout) findViewById(R.id.serverMemoryContainer);
		AFBarView barView = new AFBarView(this, percentage);
		barView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		container.addView(barView);
	}

	/**
	 * Get the server data in the background.
	 */
	@Override
	protected void getData() {
		String url = String.format("%s%s/%d/data/",
				getString(R.string.frontend_address),
				getString(R.string.api_servers), server_id);
		List<SystemData> dataArray = MainApplication.client.getServerData(url);
		if (dataArray != null && dataArray.size() > 0) {
			data = dataArray.get(0);
		}
	}

	/**
	 * Get the process list in background
	 */
	@Override
	protected void getProcessList() {
		String url = String.format("%s%s/%d/processes/",
				getString(R.string.frontend_address),
				getString(R.string.api_servers), server_id);
		processes = MainApplication.client.getServerProcessList(url);
	}

	/**
	 * Set the process list for a server.
	 */
	@Override
	protected void setProcessList() {
		if (processes == null)
			return;
		mShowProcesses.setVisibility(View.VISIBLE);
		this.mShowProcesses.setText(String.format("%d intercepted processes",
				processes.size()));
		MainApplication.setProcesses(processes);
	}

	@Override
	protected void getGraphData() {
		String url = String.format("%s%s/%d/data/",
				getString(R.string.frontend_address),
				getString(R.string.api_servers), server_id);
		if (mGraphData == null || bRefreshGraph) {
			mGraphData = MainApplication.client.getServerData(url, 60);
		}
	}

	@Override
	protected void setGraphData() {
		mButton.setVisibility(View.VISIBLE);
	}

	@Override
	protected void displayGraphData() {
		if (mGraphData == null) {
			Toast.makeText(this, "Please wait data to be loaded", 200).show();
			return;
		}
		Intent intent = ChartFactory.getTimeChartIntent(this,
				getChartDataset(mGraphData), getDemoRenderer(), null);
		startActivity(intent);
	}

	private double getDataValue(SystemData data, String field) {
		if (field == this.CPU_DISPALY_NAME) {
			return data.getCpu();
		} else if (field == this.MEMORY_DISPLAY_NAME) {
			return data.getMemory() / 1000000;
		} else if (field == this.THREAD_DISPLAY_NAME) {
			return (double) data.getThread_num();
		} else if (field == this.PAGE_FAULT_DISPLAY_NAME) {
			return (double) data.getPage_faults();
		} else if (field == this.PROCESS_DISPLAY_NAME) {
			return (double) data.getProcess_num();
		} else {
			return data.getDisk_percent();
		}
	}

	@Override
	protected void setupGraphOptions() {
		mGraphOptions.add(this.CPU_DISPALY_NAME);
		mGraphOptions.add(this.MEMORY_DISPLAY_NAME);
		mGraphOptions.add(this.DISK_PERCENT_DISPLAY_NAME);
		mGraphOptions.add(this.PAGE_FAULT_DISPLAY_NAME);
		mGraphOptions.add(this.PROCESS_DISPLAY_NAME);
		mGraphOptions.add(this.THREAD_DISPLAY_NAME);
		
		mGraphResource.add(true);
		mGraphResource.add(false);
		mGraphResource.add(false);
		mGraphResource.add(false);
		mGraphResource.add(false);
		mGraphResource.add(false);
	}

	@Override
	public List<String> getGraphOptions() {
		return mGraphOptions;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	protected Dialog createCPUDialog() {
		AlertDialog.Builder builder;
		AlertDialog alertDialog;

		builder = new AlertDialog.Builder(this);
		builder.setView(createCPUListDialog());

		builder.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						removeDialog(CPU_DIALOG);
					}
				});
		if (MainHelper.isScreenVertical(this, WINDOW_SERVICE)) {
			builder.setMessage("CPU usages");
		}
		alertDialog = builder.create();
		alertDialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface arg0) {
				// TODO Auto-generated method stub
				currentDialogId = -1;
			}
		});
		return alertDialog;
	}

	/**
	 * Draw a ScrollView to display the CPU value for each core.
	 * 
	 * @return a ScrollView containing a list of BarView.
	 */
	private View createCPUListDialog() {
		ScrollView container = createOuterContainer();
		LinearLayout innerContainer = createInnerContainer();

		List<BasicNameValuePair> items = data.getCpu_cores();
		for (int i = 0; i < items.size(); i++) {
			BasicNameValuePair item = items.get(i);
			Double value = Double.parseDouble(item.getValue());
			LinearLayout row = createTableRow(LinearLayout.HORIZONTAL);
			AFBarView barView = createBarView(value);
			row.addView(barView);
			innerContainer.addView(row);
		}
		container.addView(innerContainer);
		innerContainer.invalidate();
		container.invalidate();
		return container;
	}

	/**
	 * @param value
	 * @return
	 */
	private AFBarView createBarView(Double value) {
		AFBarView barView = new AFBarView(this, value / 100);
		barView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 18));
		return barView;
	}

	/**
	 * Draw a view to display disk status.
	 * 
	 * @return a AlertDialog containing the view.
	 */
	@Override
	protected Dialog createDiskDialog() {
		AlertDialog.Builder builder;
		AlertDialog alertDialog;

		builder = new AlertDialog.Builder(this);
		builder.setView(createDiskListDialog());

		builder.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						removeDialog(DISK_DIALOG);
					}
				});
		if (MainHelper.isScreenVertical(this, WINDOW_SERVICE)) {
			builder.setMessage("Disk usage");
		}
		alertDialog = builder.create();
		alertDialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface arg0) {
				// TODO Auto-generated method stub
				currentDialogId = -1;
			}
		});
		return alertDialog;
	}

	/**
	 * Draw a view to display disk status.
	 * 
	 * @return a AlertDialog containing the view.
	 */
	@Override
	protected Dialog createDiskBusyDialog() {
		AlertDialog.Builder builder;
		AlertDialog alertDialog;

		builder = new AlertDialog.Builder(this);
		builder.setView(createDiskBusyListDialog());	

		builder.setCancelable(false).setPositiveButton(
				"OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						removeDialog(DISK_BUSY_DIALOG);
					}
				});
		if (MainHelper.isScreenVertical(this, WINDOW_SERVICE)) {
			builder.setMessage("Disk busy");
		}
		
		alertDialog = builder.create();
		alertDialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface arg0) {
				// TODO Auto-generated method stub
				currentDialogId = -1;
			}
		});
		return alertDialog;
	}

	/**
	 * Draw a ScrollView to display the Disk usage for each disk.
	 * 
	 * @return a ScrollView containing a list of AFBarView and AFPieView.
	 */
	private View createDiskListDialog() {
		// use the scroll view to scale
		ScrollView container = createOuterContainer();
		LinearLayout innerContainer = createInnerContainer();

		List<BasicNameValuePair> items = data.getDisk_percent_part();
		for (int i = 0; i < items.size(); i++) {
			BasicNameValuePair item = items.get(i);
			Double value = Double.parseDouble(item.getValue());
			String name = item.getName();
			LinearLayout row = createTableRow(LinearLayout.HORIZONTAL);
			AFPieView pieView = createPieView(value);
			row.addView(pieView);
			TextView text = new TextView(this);
			text.setPadding(10, 0, 0, 0);
			text.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));
			text.setText(String.format("%s - %.1f %s used", name, value, "%"));

			row.addView(text);
			innerContainer.addView(row);
		}
		container.addView(innerContainer);
		innerContainer.invalidate();
		container.invalidate();
		return container;
	}

	/**
	 * @param value
	 * @return
	 */
	private AFPieView createPieView(Double value) {
		AFPieView pieView = new AFPieView(this, value / 100);
		pieView.setLayoutParams(new LayoutParams(40, 40));
		return pieView;
	}

	/**
	 * @return
	 */
	private ScrollView createOuterContainer() {
		ScrollView container = new ScrollView(this);
		container.setVerticalScrollBarEnabled(true);
		container.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				this.dialogMaxInnerSpace));
		
		return container;
	}

	/**
	 * @return
	 */
	private LinearLayout createTableRow(int orientation) {
		LinearLayout row = new LinearLayout(this);
		row.setPadding(0, 0, 0, 5);
		row.setOrientation(orientation);
		row.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		return row;
	}

	/**
	 * @return
	 */
	private LinearLayout createInnerContainer() {
		LinearLayout innerContainer = new LinearLayout(this);
		innerContainer.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		innerContainer.setOrientation(LinearLayout.VERTICAL);
		innerContainer.setPadding(5, 0, 5, 5);
		return innerContainer;
	}

	/**
	 * Draw a ScrollView to display the Disk busy for each disk.
	 * 
	 * @return a ScrollView containing a list of AFBarView and AFPieView.
	 */
	private View createDiskBusyListDialog() {
		ScrollView container = createOuterContainer();

		LinearLayout innerContainer = createInnerContainer();

		List<BasicNameValuePair> items = data.getDisk_busy();
		for (int i = 0; i < items.size(); i++) {
			BasicNameValuePair item = items.get(i);
			Double value = 0.0;
			try {
				value = Double.parseDouble(item.getValue());
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			String name = item.getName();
			LinearLayout row = createTableRow(LinearLayout.VERTICAL);
			TextView text = new TextView(this);
			text.setPadding(5, 0, 0, 0);
			text.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.WRAP_CONTENT));
			text.setText(name);
			AFBarView barView = createBarView(value);

			row.addView(barView);
			row.addView(text);
			innerContainer.addView(row);
		}
		container.addView(innerContainer);
		innerContainer.invalidate();
		container.invalidate();
		return container;
	}
}
