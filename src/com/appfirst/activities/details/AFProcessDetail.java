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
import java.util.Date;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;

import com.appfirst.communication.Helper;
import com.appfirst.datatypes.ProcessData;

import com.appfirst.monitoring2.R;
import com.appfirst.monitoring2.MainApplication;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * @author Bin Liu
 * 
 */
public class AFProcessDetail extends AFDetailActivity {
	private final String TAG = "AFProcessDetail";

	private int process_id = 0;
	private ProcessData data;
	private Button mDetailButton;
	private Button mGraphButton;
	private List<ProcessData> mGraphData;
	private Boolean bRefreshGraph = false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.process);
		Intent i = getIntent();
		int selected = Integer.parseInt(i.getStringExtra(AFProcessDetail.class
				.getName()
				+ ".id"));
		if (selected < 0) {
			return;
		}
		updateViewWithSelected(selected);
		setProcessInfo(selected);
	}

	/**
	 * Set the process basic information.
	 * 
	 * @param selected
	 *            index of the process
	 */
	private void setProcessInfo(Integer selected) {
		if (MainApplication.getProcesses() == null)
			return;

		for (int i = 0; i < MainApplication.getProcesses().size(); i++) {
			com.appfirst.types.Process process = MainApplication.getProcesses()
					.get(i);
			if (process.getId() == selected) {
				setTextView(this, R.id.processName, String.format(
						"Process Name: %s", process.getName()));
				// setTextView(this, R.id.processServerName, process)
				setTextView(this, R.id.processArgs, String.format(
						"Command line: %s", process.getArgs()));
				setTextView(this, R.id.processStart, Helper.formatLongTime(process
						.getStart() * 1000));
				setTextView(this, R.id.processEnd, Helper.formatLongTime(process
						.getEnd() * 1000));
				break;
			}
		}
	}

	/**
	 * Create the dialog boxes for displaying progress and options.
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
		default:
			break;
		}

		return dialog;
	}

	/**
	 * @see com.appfirst.activities.details.AFDetailActivity#updateViewWithSelected(int)
	 */
	@Override
	protected void updateViewWithSelected(int selected) {
		// TODO Auto-generated method stub
		process_id = selected;

		mDetailButton = (Button) findViewById(R.id.processMoreDetail);
		mDetailButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(AFProcessDetail.this,
						AFMinuteDetail.class);
				startActivity(intent);
			}
		});

		mGraphButton = (Button) findViewById(R.id.processShowGraph);
		mGraphButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(OPTION_DIALOG);
			}
		});
		setupGraphOptions();
		setLongClickEvent();
		new DataUpdater().execute();
		new DetailUpdater().execute();
		new GraphUpdater().execute();

	}
	
	
	protected void setLongClickEvent() {
		setRowLongClickEvent(R.id.processTableRow01, 0);
		setRowLongClickEvent(R.id.processTableRow02, 1);
		setRowLongClickEvent(R.id.processTableRow03, 2);
		setRowLongClickEvent(R.id.processTableRow04, 3);
		setRowLongClickEvent(R.id.processTableRow05, 4);
		setRowLongClickEvent(R.id.processTableRow06, 5);
		setRowLongClickEvent(R.id.processTableRow07, 6);
		setRowLongClickEvent(R.id.processTableRow08, 7);
		setRowLongClickEvent(R.id.processTableRow09, 8);
		setRowLongClickEvent(R.id.processTableRow10, 9);
		setRowLongClickEvent(R.id.processTableRow11, 10);
		setRowLongClickEvent(R.id.processTableRow12, 11);
		setRowLongClickEvent(R.id.processTableRow13, 12);
		setRowLongClickEvent(R.id.processTableRow14, 13);
		setRowLongClickEvent(R.id.processTableRow15, 14);
	}

	/**
	 * 
	 * 
	 * @see com.appfirst.activities.details.AFDetailActivity#getData()
	 */
	@Override
	protected void getData() {
		// TODO Auto-generated method stub
		String url = String.format("%s%s/%d/data/",
				getString(R.string.frontend_address),
				getString(R.string.api_processes), process_id);
		List<ProcessData> dataArray = MainApplication.client
				.getProcessData(url);
		if (dataArray.size() > 0) {
			data = dataArray.get(0);
		}
	}

	/**
	 * 
	 * @see com.appfirst.activities.details.AFDetailActivity#getProcessList()
	 */
	@Override
	protected void getProcessList() {

	}

	/**
	 * 
	 * @see com.appfirst.activities.details.AFDetailActivity#setData()
	 */
	@Override
	protected void setData() {
		// TODO Auto-generated method stub
		if (data != null) {
			setTextView(this, R.id.processCpuValue, Helper.formatCpuValue(data
					.getCpu()));
			setTextView(this, R.id.processMemoryValue, Helper
					.formatByteValue(data.getMemory()));
			setTextView(this, R.id.processFileReadValue, Helper
					.formatByteValue(data.getFile_read()));
			setTextView(this, R.id.processFileWriteValue, Helper
					.formatByteValue(data.getFile_write()));
			setTextView(this, R.id.processSocketReadValue, Helper
					.formatByteValue(data.getSocket_read()));
			setTextView(this, R.id.processSocketWriteValue, Helper
					.formatByteValue(data.getSocket_write()));
			setTextView(this, R.id.processFileNumValue, Helper.formatValue(data
					.getFile_num()));
			setTextView(this, R.id.processThreadNumValue, Helper
					.formatValue(data.getThread_num()));
			setTextView(this, R.id.processSocketNumValue, Helper
					.formatValue(data.getSocket_num()));
			setTextView(this, R.id.processRegistryNumValue, Helper
					.formatValue(data.getRegistry_num()));
			setTextView(this, R.id.processResponseNumValue, Helper
					.formatValue(data.getResponse_num()));
			setTextView(this, R.id.processPageFaultValue, Helper
					.formatValue(data.getPage_faults()));
			setTextView(this, R.id.processCriticalLogValue, Helper
					.formatValue(data.getCritical_log()));
			setTextView(this, R.id.processTotalLogValue, Helper
					.formatValue(data.getTotal_log()));
			setTextView(this, R.id.processAvgResponseTimeValue, Helper
					.formatTimeValue(data.getAvg_response_time()));
			setTextView(this, R.id.processResourceUsageLabel, String.format(
					"Resource usage at %s:", Helper.formatLongTime(data.getTime() * 1000)));
		}
	}

	/**
	 * Add the process list data to a ListView.
	 * 
	 * @see com.appfirst.activities.details.AFDetailActivity#setProcessList()
	 */
	@Override
	protected void setProcessList() {

	}

	protected class DetailUpdater extends AsyncTask<URL, Integer, Long> {
		protected Long doInBackground(URL... urls) {
			getDetail();
			return 0L;
		}

		protected void onPostExecute(Long result) {
			updateDetail();
		}
	}

	/**
	 * Get the detail data.
	 */
	public void getDetail() {
		// TODO Auto-generated method stub
		String url = String.format("%s%s/%d/detail/",
				getString(R.string.frontend_address),
				getString(R.string.api_processes), process_id);
		MainApplication.loadDetailData(url);
	}

	/**
	 * Enable the show detail button after data is loaded.
	 */
	public void updateDetail() {
		// TODO Auto-generated method stub
		if (MainApplication.hasValidDetailData()) {
			mDetailButton.setVisibility(View.VISIBLE);
		}	
	}

	@Override
	protected void setGraphData() {
		mGraphButton.setVisibility(View.VISIBLE);
	}

	@Override
	protected void getGraphData() {
		String url = String.format("%s%s/%d/data/",
				getString(R.string.frontend_address),
				getString(R.string.api_processes), process_id);
		if (mGraphData == null || bRefreshGraph) {
			mGraphData = MainApplication.client.getProcessData(url, 60);
		}
	}

	protected XYMultipleSeriesDataset getChartDataset(
			List<ProcessData> graphData) {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

		for (int i = 0; i < mGraphOptions.size(); i++) {
			if (mGraphResource.get(i)) {
				TimeSeries series = new TimeSeries(mGraphOptions.get(i));
				for (int k = 0; k < graphData.size(); k++) {
					ProcessData data = graphData.get(k);
					series.add(new Date(data.getTime() * 1000), getDataValue(
							data, mGraphOptions.get(i)));
				}
				dataset.addSeries(series);
			}
		}
		return dataset;
	}

	/**
	 * Get the value of each resource from {@link ProcessData}, doing some value
	 * factoring for displaying them in the graph.
	 * 
	 * @param data
	 *            a data object to be parsed
	 * @param field
	 *            which field to parse
	 * @return value of field, all converted to double type.
	 */
	private double getDataValue(ProcessData data, String field) {
		if (field == this.CPU_DISPALY_NAME) {
			return data.getCpu();
		} else if (field == this.MEMORY_DISPLAY_NAME) {
			return data.getMemory() / 1000000;
		} else if (field == this.THREAD_DISPLAY_NAME) {
			return (double) data.getThread_num();
		} else if (field == this.PAGE_FAULT_DISPLAY_NAME) {
			return (double) data.getPage_faults();
		} else if (field == this.AVG_RESPONSE_DISPLAY_NAME) {
			return data.getAvg_response_time();
		} else if (field == this.SOCKET_NUM_DISPLAY_NAME) {
			return data.getSocket_num();
		} else if (field == this.SOCKET_READ_DISPLAY_NAME) {
			return (double) data.getSocket_read() / 1000;
		} else if (field == this.SOCKET_WRITE_DISPLAY_NAME) {
			return (double) data.getSocket_write();
		} else if (field == this.FILE_NUM_DISPLAY_NAME) {
			return (double) data.getFile_num();
		} else if (field == this.FILE_READ_DISPLAY_NAME) {
			return (double) data.getFile_read();
		} else if (field == this.FILE_WRITE_DISPLAY_NAME) {
			return (double) data.getFile_write();
		} else if (field == this.REGISTRY_DISPLAY_NAME) {
			return (double) data.getRegistry_num();
		} else if (field == this.INCIDENTS_DISPLAY_NAME) {
			return (double) data.getTotal_log();
		} else if (field == this.CRITICAL_INCIDENTS_DISPLAY_NAME) {
			return (double) data.getCritical_log();
		} else if (field == this.RESPONSE_NUM_DISPLAY_NAME) {
			return (double) data.getRegistry_num();
		}

		return 0;
	}

	/**
	 * set up what resources can be displayed in the graph. Make sure this is
	 * consistent with anywhere else in the product.
	 */
	@Override
	protected void setupGraphOptions() {
		mGraphOptions.add(this.CPU_DISPALY_NAME);
		mGraphOptions.add(this.MEMORY_DISPLAY_NAME);
		mGraphOptions.add(this.PAGE_FAULT_DISPLAY_NAME);
		mGraphOptions.add(this.THREAD_DISPLAY_NAME);
		mGraphOptions.add(this.SOCKET_NUM_DISPLAY_NAME);
		mGraphOptions.add(this.SOCKET_READ_DISPLAY_NAME);
		mGraphOptions.add(this.SOCKET_WRITE_DISPLAY_NAME);
		mGraphOptions.add(this.FILE_NUM_DISPLAY_NAME);
		mGraphOptions.add(this.FILE_READ_DISPLAY_NAME);
		mGraphOptions.add(this.FILE_WRITE_DISPLAY_NAME);
		mGraphOptions.add(this.REGISTRY_DISPLAY_NAME);
		mGraphOptions.add(this.INCIDENTS_DISPLAY_NAME);
		mGraphOptions.add(this.CRITICAL_INCIDENTS_DISPLAY_NAME);
		mGraphOptions.add(this.AVG_RESPONSE_DISPLAY_NAME);
		mGraphOptions.add(this.RESPONSE_NUM_DISPLAY_NAME);

		mGraphResource.add(true);
		mGraphResource.add(false);
		mGraphResource.add(false);
		mGraphResource.add(false);
		mGraphResource.add(false);
		mGraphResource.add(false);
		mGraphResource.add(false);
		mGraphResource.add(false);
		mGraphResource.add(false);
		mGraphResource.add(false);
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

}
