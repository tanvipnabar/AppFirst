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

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.appfirst.activities.lists.AFAlertHistoryList;
import com.appfirst.datatypes.AlertHistoryData;
import com.appfirst.datatypes.LogData2;
import com.appfirst.monitoring2.R;
import com.appfirst.monitoring2.MainApplication;
import com.appfirst.types.AlertHistory;
import com.appfirst.types.Log;

/**
 * Display the alert history detail.
 * 
 * @author Bin Liu
 * 
 */
public class AFLogDetail extends AFDetailActivity {
	private LogData2 mLogData;
	private Log mLog;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.log);
		Intent i = getIntent();
		int selected = i.getIntExtra(AFLogDetail.class.getName()
				+ ".selected", -1);
		if (selected < 0) {
			return;
		}
		updateViewWithSelected(selected);
		showDialog(PROGRESS_DIALOG);
		new DataUpdater().execute();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.appfirst.activities.details.AFDetailActivity#getData()
	 */
	@Override
	protected void getData() {
		// TODO Auto-generated method stub
		// TODO: get alert history detail
		String url = String.format("%s%s/%d/data/",
				getString(R.string.frontend_address),
				getString(R.string.api_logs), mLog.getId());
		mLogData = MainApplication.client.getLogData(url);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.appfirst.activities.details.AFDetailActivity#getGraphData()
	 */
	@Override
	protected void getGraphData() {
		// TODO Auto-generated method stub

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
		try {
			// TODO Auto-generated method stub
			dismissDialog(PROGRESS_DIALOG);
		} catch (Exception e) {
			// TODO: handle exception
		}
		setTextView(this, R.id.server, "Server : " + MainApplication.getServerNameById(mLog.getServerId()));
		setTextView(this, R.id.type, "Type : " + mLog.getType());
		setTextView(this, R.id.source, "Source : " + mLog.getSource());
		setTextView(this, R.id.critical, "Critical : " + mLog.getCritical());
		setTextView(this, R.id.filter, "Filter : " + mLog.getFilter());
		setTextView(this, R.id.time, "Time : " + String.valueOf(mLogData.getTime()));
		setTextView(this, R.id.num_info, "Num. of Info Messages : " + mLogData.getNum_info());
		setTextView(this, R.id.num_warning, "Num. of Warning Messages : " + mLogData.getNum_warning());
		setTextView(this, R.id.num_critical, "Num. of Critical Messages : " + mLogData.getNum_critical());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.appfirst.activities.details.AFDetailActivity#setGraphData()
	 */
	@Override
	protected void setGraphData() {
		// TODO Auto-generated method stub

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
	 * @see com.appfirst.activities.details.AFDetailActivity#setupGraphOptions()
	 */
	@Override
	protected void setupGraphOptions() {
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
		// TODO Auto-generated method stub
		try {
			mLog = MainApplication.getLogs().get(selected);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (mLog == null) {
			toastErrorMessage();
			finish();
		}
	}
}
