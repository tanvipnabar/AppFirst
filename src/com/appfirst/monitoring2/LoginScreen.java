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

import java.net.URL;

import com.appfirst.activities.lists.AFAlertHistoryList;
import com.appfirst.activities.lists.AFServerList;
import com.appfirst.communication.Helper;
import com.appfirst.monitoring2.R;

import android.app.Activity;
import android.app.Dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Associated with the login activity of the user. Provide the screen of enter
 * username and password for the user and displaying any login errors.
 * 
 * @author Bin Liu
 * 
 */
public class LoginScreen extends Activity {
	private static final int PROGRESS_DIALOG = 0;
	private EditText etUserName;
	private EditText etUserPassword;
	private Button btnLogin;
	private Button btnCancel;
	private TextView lblResult;
	private final String TAG = "LoginScreen";
	protected String mUserName;
	protected String mUserPassword;
	protected Boolean mSigninResult;
	protected ProgressDialog progressDialog;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initViews();
		autoSignin();
	}

	/**
	 * try sign in with saved username and password.
	 */
	private void autoSignin() {
		SharedPreferences preference = LoginScreen.this.getSharedPreferences(
				getString(R.string.appfirst_preference_key),
				Context.MODE_PRIVATE);
		mUserName = preference.getString(getString(R.string.username_key), "");
		mUserPassword = preference.getString(getString(R.string.passowrd_key),
				"");

		if (mUserName != "" && mUserPassword != "") {// login with saved
			etUserName.setText(mUserName);
			etUserPassword.setText(mUserPassword);
			showDialog(PROGRESS_DIALOG);
			new BackgroundWorker().execute();
		}
	}

	/**
	 * Initialize members and events.
	 */
	private void initViews() {
		setContentView(R.layout.main);
		etUserName = (EditText) findViewById(R.id.username);
		etUserPassword = (EditText) findViewById(R.id.password);
		btnLogin = (Button) findViewById(R.id.login_button);
		btnCancel = (Button) findViewById(R.id.cancel_button);
		lblResult = (TextView) findViewById(R.id.result);

		btnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mUserName = etUserName.getText().toString();
				mUserPassword = etUserPassword.getText().toString();
				showDialog(PROGRESS_DIALOG);
				new BackgroundWorker().execute();
			}
		});
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Close the application
				finish();
			}
		});
	}

	/**
	 * Try load the server list.
	 */
	protected void loadResource() {
		// try new user name and password if user entered new ones.
		MainApplication.client.setmAuthString(String.format("%s:%s", mUserName,
				mUserPassword).getBytes());
		MainApplication.loadServerList(Helper.getServerListUrl(this));
		if (MainApplication.getServers() == null
				|| MainApplication.getServers().size() == 0) {
			mSigninResult = false;
		} else {
			mSigninResult = true;
		}
	}

	/**
	 * Do background resource work.
	 * 
	 * @author Bin Liu
	 * 
	 */
	protected class BackgroundWorker extends AsyncTask<URL, Integer, Long> {
		protected Long doInBackground(URL... urls) {
			loadResource();
			return 0L;
		}

		protected void onPostExecute(Long result) {
			signinResult();
		}
	}

	/**
	 * Handle the login result. Save the user name and password if login is
	 * successful and display the error message if login failed. Register the
	 * push notification. It's the call back function from background worker.
	 */
	protected void signinResult() {
		if (mSigninResult) {
			// save login information
			MainApplication.saveUserLogin(LoginScreen.this, mUserName,
					mUserPassword);
			MainApplication.setCurrentUsername(mUserName);
			// always try to register C2DM by default
			MainApplication.registerC2DM(LoginScreen.this);
			// change the view to home screen.
			startNextActivity();

		} else {
			lblResult
					.setText("Login failed, input username or password for AppFirst user, or this is network issue");
		}
		try {
			dismissDialog(PROGRESS_DIALOG);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Start next activity depends on whether it comes from Notification screen.
	 * If it is, go to alert history list else go to home screen.
	 */
	private void startNextActivity() {
		Intent previousIntent = getIntent();
		if (previousIntent != null) {
			String redirect = previousIntent
					.getStringExtra(getString(R.string.redirect_key));

			if (redirect != null) {
				Log.i(TAG, String.format("Redirect to %s", redirect));
				if (redirect.startsWith(AFAlertHistoryList.class.getName())) { // come
					// from
					// new notification
					Intent next = new Intent(LoginScreen.this,
							AFAlertHistoryList.class);
					startActivity(next);
				} else if (redirect.startsWith(AFServerList.class.getName())) {
					Intent next = new Intent(LoginScreen.this,
							AFServerList.class);
					startActivity(next);
				}
			} else {
				Intent home = new Intent(LoginScreen.this, AFHomeScreen.class);
				startActivity(home);
			}
			finish();
		}
	}

	/**
	 * Create the alert box using for showing progress.
	 * 
	 * @return dialog box showing progress.
	 */
	protected ProgressDialog createProcessDialog() {
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Logging in...");
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
	 * Create progress dialog.
	 */
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		switch (id) {
		case PROGRESS_DIALOG:
			dialog = createProcessDialog();
			break;
		default:
			break;
		}

		return dialog;
	}
}