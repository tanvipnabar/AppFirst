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
package com.appfirst.monitoring2;

import com.appfirst.monitoring2.R;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * @author Bin Liu
 * 
 */
public class AFAccountManagement extends Activity {
	private CheckBox mReceiveNotification;
	private Button mSignoutButton;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account);

		mSignoutButton = (Button) findViewById(R.id.accountSignoutButton);
		mReceiveNotification = (CheckBox) findViewById(R.id.accountReceiveNotification);
		mReceiveNotification.setChecked(MainApplication.getReceiveNotification());
		mSignoutButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MainApplication.deleteUserLogin(AFAccountManagement.this);
				MainApplication.unregistrateC2DM(AFAccountManagement.this);
				finish();
				Intent intent = new Intent(AFAccountManagement.this, LoginScreen.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
		
		mReceiveNotification.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		        // Perform action on clicks, depending on whether it's now checked
		        if (((CheckBox) v).isChecked()) {
		           MainApplication.registerC2DM(AFAccountManagement.this);
		        } else {
		           MainApplication.unregistrateC2DM(AFAccountManagement.this);
		        }
		    }
		});
		
		TextView userNameText = (TextView) findViewById(R.id.accountUserName);
		userNameText.setText(MainApplication.getCurrentUsername());
	}
}
