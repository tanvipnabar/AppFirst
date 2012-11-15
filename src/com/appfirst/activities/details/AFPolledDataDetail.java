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
import java.util.Iterator;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.appfirst.communication.Helper;
import com.appfirst.datatypes.PolledDataData;
import com.appfirst.datatypes.SystemData;
import com.appfirst.monitoring2.R;
import com.appfirst.monitoring2.MainApplication;
import com.appfirst.types.PolledDataObject;

/**
 * Display the PollData detail, the message that's originally sent in the
 * alerting email.
 * 
 * @author Bin Liu
 * 
 */
public class AFPolledDataDetail extends AFDetailActivity {
	private final String TAG = "AFPolledDataDetail";
	private int polled_data_id;
	private PolledDataData data;
	private Button mButton;
	private List<PolledDataData> mGraphData;
	private Boolean bRefreshGraph = false;
	private List<String> mUnits = new ArrayList<String>();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.polled_data);
		Intent i = getIntent();
		int selected = i.getIntExtra(AFPolledDataDetail.class.getName()
				+ ".selected", -1);
		if (selected < 0) {
			return;
		}
		updateViewWithSelected(selected);
	}

	/**
	 * Get polled data data.
	 * 
	 * @see com.appfirst.activities.details.AFDetailActivity#getData()
	 */
	@Override
	protected void getData() {
		// TODO Auto-generated method stub
		String url = String.format("%s%s/%d/data/",
				getString(R.string.frontend_address),
				getString(R.string.api_polled_datas), polled_data_id);
		List<PolledDataData> dataArray = MainApplication.client
				.getPollDataDataList(url);
		if (dataArray != null && dataArray.size() > 0) {
			data = dataArray.get(0);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.appfirst.activities.details.AFDetailActivity#getProcessList()
	 */
	@Override
	protected void getProcessList() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.appfirst.activities.details.AFDetailActivity#setData()
	 */
	@Override
	protected void setData() {
		// TODO Auto-generated method stub
		if (data != null) {
			setTextView(this, R.id.polledDataStatus, String.format(
					"Status at %s: %s", Helper
							.formatLongTime(data.getTime() * 1000), data
							.getStatus()));
			setTextView(this, R.id.polledDataMessage, String.format(
					"Message: %s", data.getText()));
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.appfirst.activities.details.AFDetailActivity#setProcessList()
	 */
	@Override
	protected void setProcessList() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.appfirst.activities.details.AFDetailActivity#updateViewWithSelected
	 * (int)
	 */
	@Override
	protected void updateViewWithSelected(int selected) {
		PolledDataObject item = null;
		try {
			item = MainApplication.getPolledDatas().get(selected);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (item == null) {
			toastErrorMessage();
			finish();
		}
		
		polled_data_id = item.getId();
		setTextView(this, R.id.polledDataName, String.format("Script name: %s",
				item.getName()));
		setTextView(this, R.id.polledDataServerName, String.format(
				"Server: %s", MainApplication.getServerNameById(item
						.getServer())));
		new DataUpdater().execute();
		new GraphUpdater().execute();
		mButton = (Button) findViewById(R.id.polledDataShowHistory);
		mButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(OPTION_DIALOG);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.appfirst.activities.details.AFDetailActivity#getGraphData()
	 */
	@Override
	protected void getGraphData() {
		// TODO Auto-generated method stub
		String url = String.format("%s%s/%d/data/",
				getString(R.string.frontend_address),
				getString(R.string.api_polled_datas), polled_data_id);
		if (mGraphData == null || bRefreshGraph) {
			mGraphData = MainApplication.client.getPollDataDataList(url, 60);
			setupGraphOptions();
		}
	}

	/**
	 * @see com.appfirst.activities.details.AFDetailActivity#setGraphData()
	 */
	@Override
	protected void setGraphData() {
		// TODO Auto-generated method stub
		if (mButton.getVisibility() != View.GONE) {
			try {
				dismissDialog(PROGRESS_DIALOG);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		} else {
			mButton.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public List<String> getGraphOptions() {
		return mGraphOptions;
	}

	private double getDataValue(PolledDataData graphData, String field) {
		double ret = 0;

		if (graphData.getValues().has(field)) {
			try {
				JSONArray array = graphData.getValues().getJSONArray(field);
				if (array.length() > 2) {
					JSONObject valueObject = array.getJSONObject(1);
					if (valueObject.has("val")) {
						ret = valueObject.getDouble("val");
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return ret;
	}

	protected XYMultipleSeriesDataset getChartDataset(
			List<PolledDataData> graphData) {
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();

		for (int i = 0; i < mGraphOptions.size(); i++) {
			if (mGraphResource.get(i)) {
				TimeSeries series = new TimeSeries(String.format("%s (%s)",
						mGraphOptions.get(i), mUnits.get(i)));
				for (int k = 0; k < graphData.size(); k++) {
					PolledDataData data = graphData.get(k);
					series.add(new Date(data.getTime() * 1000), getDataValue(
							data, mGraphOptions.get(i)));
				}
				dataset.addSeries(series);
			}
		}
		return dataset;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.appfirst.activities.details.AFDetailActivity#setupGraphOptions()
	 */
	@Override
	protected void setupGraphOptions() {
		// TODO Auto-generated method stub
		mGraphOptions.add("status");
		mGraphResource.add(true);
		mUnits.add("");

		for (int cnt = 0; cnt < mGraphData.size(); cnt++) {
			PolledDataData data = mGraphData.get(cnt);
			JSONObject values = data.getValues();
			Iterator<?> it = values.keys();
			while (it.hasNext()) {
				String key = (String) it.next();
				if (!mGraphOptions.contains(key)) {
					String unit = "";
					try {
						JSONArray array = data.getValues().getJSONArray(key);
						if (array.length() > 2) {
							unit = array.getString(2);
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					mGraphOptions.add(key);
					mGraphResource.add(false);
					mUnits.add(unit);
				}
			}
		}
		Log.i(TAG, mGraphOptions.toString());
	}
	
	@Override
	protected void displayGraphData() {
		if (mGraphData == null) {
			return;
		}
		Intent intent = ChartFactory.getTimeChartIntent(this,
				getChartDataset(mGraphData), getDemoRenderer(), null);
		startActivity(intent);
	}
}
