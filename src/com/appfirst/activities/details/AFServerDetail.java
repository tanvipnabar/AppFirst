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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.appfirst.communication.Helper;
import com.appfirst.datatypes.SystemData;
import com.appfirst.monitoring.MainApplication;
import com.appfirst.monitoring.R;
import com.appfirst.types.Server;
import com.appfirst.utils.DoubleLineLayoutArrayAdapter;

/**
 * @author Bin Liu
 * 
 */
public class AFServerDetail extends AFDetailActivity {

	private final String TAG = "AFServerDetail";
	private int server_id = 0;
	protected SystemData data;
	protected List<com.appfirst.types.Process> processes;
	private Button mButton;
	private List<SystemData> mGraphData;
	private Boolean bRefreshGraph = false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.server);
		Intent i = getIntent();
		int selected = i.getIntExtra(Server.class.getName() + ".selected", -1);
		if (selected < 0) {
			Log.e(TAG, "Unlikely, nothing selected");
			return;
		}
		updateViewWithSelected(selected);
		setupGraphOptions();
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
		Server server = MainApplication.servers.get(selected);
		String cpuInfo = String.format("%s cores at %s MHZ", server
				.getCapacity_cpu_num(), server.getCapacity_cpu_freq());
		String memInfo = Helper.formatByteValue(server.getCapacity_mem());
		setTextView(this, R.id.serverHostname, server.getHostname());
		setTextView(this, R.id.serverCpuCapacity, cpuInfo);
		setTextView(this, R.id.serverMemCapacity, memInfo);
		setTextView(this, R.id.serverOSType, server.getOs());
		setTextView(this, R.id.serverDiskCapacity, String.format("%d MB",
				server.getTotalDisk()));
		server_id = server.getId();
		new DataUpdater().execute();
		new ProcessListUpdater().execute();
		new GraphUpdater().execute();
		mButton = (Button) findViewById(R.id.serverShowGraph);
		mButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(OPTION_DIALOG);
			}
		});

	}

	/**
	 * Set server data after it's loaded.
	 */
	@Override
	protected void setData() {
		if (data != null) {
			setTextView(this, R.id.serverCpuValue, String.format("%.1f", data
					.getCpu())
					+ "%");
			setTextView(this, R.id.serverMemoryValue, String.format("%.1f MB",
					data.getMemory() / 1000000));
			setTextView(this, R.id.serverDiskValue, String.format("%.1f", data
					.getDisk_percent())
					+ "%");
			setTextView(this, R.id.serverPFValue, String.format("%d", data
					.getPage_faults()));
			setTextView(this, R.id.serverPRValue, String.format("%d", data
					.getProcess_num()));
			setTextView(this, R.id.serverTRValue, String.format("%d", data
					.getThread_num()));
			setTextView(this, R.id.serverResourceUsageLabel, String.format(
					"Resource usage at %s:", Helper.formatLongTime(data.getTime() * 1000)));
		} else {
			setTextView(this, R.id.serverResourceUsageLabel, String.format(
					"Resource usage %s:", "(update failed)"));
		}
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
		List<String> names = new ArrayList<String>();
		List<String> details = new ArrayList<String>();
		List<Integer> ids = new ArrayList<Integer>();
		for (int cnt = 0; cnt < processes.size(); cnt++) {
			com.appfirst.types.Process process = processes.get(cnt);
			names.add(String.format("%s (pid:%d)", process.getName(), process
					.getPid()));
			details.add(process.getArgs());
			ids.add(process.getId());
		}
		ListView lv = (ListView) findViewById(R.id.serverProcessList);
		lv.setAdapter(new DoubleLineLayoutArrayAdapter(this, names, details,
				ids, AFProcessDetail.class));
		setTextView(this, R.id.serverProcessListLabel, String.format(
				"Intercepted processes: %d", names.size()));
		MainApplication.processes = processes;
	}

	@Override
	protected void getGraphData() {
		String url = String.format("%s%s/%d/data/",
				getString(R.string.frontend_address),
				getString(R.string.api_servers), server_id);
		if (mGraphData == null || bRefreshGraph) {
			mGraphData = MainApplication.client.getServerData(url, 30);
		} else {
			setGraphData();
		}
	}

	@Override
	protected void setGraphData() {
		if (mButton.getVisibility() != View.GONE) {
			dismissDialog(PROGRESS_DIALOG);
			Intent intent = ChartFactory.getTimeChartIntent(this,
					getChartDataset(mGraphData), getDemoRenderer(), null);
			startActivity(intent);
		} else {
			if (mGraphData != null) {
				mButton.setVisibility(View.VISIBLE);
			}
		}
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
		mGraphOptions.add(this.PROCESS_DISPLAY_NAME);
		mGraphOptions.add(this.THREAD_DISPLAY_NAME);
		mGraphOptions.add(this.PAGE_FAULT_DISPLAY_NAME);
		mGraphOptions.add(this.DISK_PERCENT_DISPLAY_NAME);

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
}
