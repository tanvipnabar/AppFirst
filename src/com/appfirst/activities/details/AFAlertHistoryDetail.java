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
import com.appfirst.monitoring2.R;
import com.appfirst.monitoring2.MainApplication;
import com.appfirst.types.AlertHistory;

/**
 * Display the alert history detail.
 * 
 * @author Bin Liu
 * 
 */
public class AFAlertHistoryDetail extends AFDetailActivity {
	private AlertHistoryData mAlertMessage;
	private AlertHistory mAlertHistory;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alert_history);
		Intent i = getIntent();
		int selected = i.getIntExtra(AFAlertHistoryDetail.class.getName()
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
		String url = String.format("%s%s/%d/message/",
				getString(R.string.frontend_address),
				getString(R.string.api_alert_histories), mAlertHistory.getId());
		mAlertMessage = MainApplication.client.getAlertHistoryData(url);
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
		setTextView(this, R.id.alertHistoryMessage, mAlertMessage.getText());
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
			mAlertHistory = MainApplication.getAlertHistories().get(selected);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (mAlertHistory == null) {
			toastErrorMessage();
			finish();
		}
	}
}
