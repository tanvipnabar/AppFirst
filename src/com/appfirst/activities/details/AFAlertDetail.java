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

import java.text.DateFormat;
import java.util.Date;

import com.appfirst.communication.Helper;
import com.appfirst.monitoring2.R;
import com.appfirst.monitoring2.MainApplication;
import com.appfirst.types.Alert;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.Toast;

/**
 * @author Bin Liu6
 * 
 */
public class AFAlertDetail extends AFDetailActivity {
	private CheckBox mAlertActive;
	private int mAlertId;
	private final String TAG = "AFAlertDetail";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alert);
		Intent i = getIntent();
		int selected = i.getIntExtra(AFAlertDetail.class.getName() + ".selected", -1);
		if (selected < 0) {
			return;
		}
		updateViewWithSelected(selected);
	}
	
	/**
	 *
	 * 
	 * @see
	 * com.appfirst.activities.details.AFDetailActivity#updateViewWithSelected
	 * (int)
	 */
	@Override
	protected void updateViewWithSelected(final int selected) {
		// TODO Auto-generated method stub
		Alert alert = null; 
		try {
			alert = MainApplication.getAlerts().get(selected);
		} catch (Exception e) {
			Log.e(TAG, "Something is wrong. Alert object doesn't exist anymore.");
			e.printStackTrace();
		}
		if (alert == null) {
			Toast toast = Toast.makeText(this, "Error loading data.", Toast.LENGTH_LONG);
			toast.show();
			finish();
		}
		int lastTrigger = alert.getLast_triggered();
		String triggerString = "N/A";
		if (lastTrigger > 0) {
			triggerString = Helper.formatLongTime(Long.parseLong(String.format("%d", lastTrigger)) * 1000);
		}
		setTextView(this, R.id.alertName, alert.getName());
		setTextView(this, R.id.alertTrigger, alert.getTrigger());
		setTextView(this, R.id.alertType, alert.getType());
		setTextView(this, R.id.alertTarget, alert.getTarget());
		setTextView(this, R.id.lastTriggered, triggerString);
		setTextView(this, R.id.inIncident, alert.getIn_incident());
		this.setCheckBox(this, R.id.alertActive, alert.getActive());
		mAlertId = alert.getId();

		mAlertActive = (CheckBox) findViewById(R.id.alertActive);
		mAlertActive.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Alert newAlert = MainApplication.client.updateAlertStatus(Helper.getAlertUrl(
						AFAlertDetail.this, mAlertId), mAlertId, ((CheckBox) v)
						.isChecked());
				CharSequence message;
				if (newAlert.getActive()) {
					message = "Alert has been enabled";
				} else {
					message = "Alert has been disabled";
				}
				Toast.makeText(AFAlertDetail.this, message, 100).show();
				MainApplication.updateCachedAlerts(selected, newAlert);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.appfirst.activities.details.AFDetailActivity#getData()
	 */
	@Override
	protected void getData() {
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
	 * @see com.appfirst.activities.details.AFDetailActivity#getGraphData()
	 */
	@Override
	protected void getGraphData() {
		// TODO Auto-generated method stub

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
	 * @see com.appfirst.activities.details.AFDetailActivity#setupGraphOptions()
	 */
	@Override
	protected void setupGraphOptions() {
		// TODO Auto-generated method stub

	}
}
