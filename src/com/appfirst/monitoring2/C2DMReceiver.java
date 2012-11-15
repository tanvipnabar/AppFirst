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

/**
 * @author Bin Liu
 *
 */

import com.appfirst.activities.lists.AFAlertHistoryList;
import com.appfirst.monitoring2.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class C2DMReceiver extends BroadcastReceiver {
	private Context context;
	private static String TAG = "C2DMReceiver";

	public C2DMReceiver() {

	}

	/**
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context,
	 *      android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		this.context = context;
		if (intent.getAction().equals(
				"com.google.android.c2dm.intent.REGISTRATION")) {
			handleRegistration(context, intent);
		} else if (intent.getAction().equals(
				"com.google.android.c2dm.intent.RECEIVE")) {
			handleMessage(context, intent);
		}
	}

	/**
	 * @param context2
	 *            current activity
	 * @param intent
	 */
	private void handleMessage(Context context2, Intent intent) {
		// TODO Auto-generated method stub
		Log.v(TAG, "I got something from the server. ");
		String message = intent.getStringExtra(context2
				.getString(R.string.notification_key));
		addNotification(context2, message);
	}

	/**
	 * @param context2
	 *            current activity
	 * @param message
	 *            the content of the notification
	 */
	private void addNotification(Context context2, String message) {
		long when = System.currentTimeMillis();
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) context2
				.getSystemService(ns);

		Notification notification = new Notification(
				R.drawable.ic_launcher_appfirst_flat, message, when);
		CharSequence contentTitle = context2
				.getString(R.string.notification_title);
		CharSequence contentText = message;
		Intent notificationIntent = new Intent(context2,
				AFAlertHistoryList.class);
		// redirect to alert history directly
		notificationIntent.putExtra(context2.getString(R.string.redirect_key),
				AFAlertHistoryList.class.getName().toString());
		PendingIntent contentIntent = PendingIntent.getActivity(context2, 0,
				notificationIntent, 0);
		notification.setLatestEventInfo(context, contentTitle, contentText,
				contentIntent);
		mNotificationManager.notify(1, notification);
	}

	/**
	 * @param context2
	 * @param intent
	 */
	private void handleRegistration(Context context2, Intent intent) {
		// TODO Auto-generated method stub

		String registration = intent.getStringExtra("registration_id");
		if (registration == "") {
			Log.e(TAG, "Failed to registrate.");
			return;
		}
		if (intent.getStringExtra("error") != null) {
			// Registration failed, should try again later.
			Log.d(TAG, "registration failed");
			String error = intent.getStringExtra("error");
			System.out.println("REGERROR ----> " + error);
			if (error == "SERVICE_NOT_AVAILABLE") {
				Log.d(TAG, "SERVICE_NOT_AVAILABLE");
			} else if (error == "ACCOUNT_MISSING") {
				Log.d(TAG, "ACCOUNT_MISSING");
			} else if (error == "AUTHENTICATION_FAILED") {
				Log.d(TAG, "AUTHENTICATION_FAILED");
			} else if (error == "TOO_MANY_REGISTRATIONS") {
				Log.d(TAG, "TOO_MANY_REGISTRATIONS");
			} else if (error == "INVALID_SENDER") {
				Log.d(TAG, "INVALID_SENDER");
			} else if (error == "PHONE_REGISTRATION_ERROR") {
				Log.d(TAG, "PHONE_REGISTRATION_ERROR");
			}
		} else if (intent.getStringExtra("unregistered") != null) {
			// unregistration done, new messages from the authorized sender will
			// be rejected
			Log.d(TAG, "unregistered");
			MainApplication.setReceiveNotification(false);

		} else if (registration != null) {
			Log.d(TAG, registration);
			MainApplication.setUid(context2, registration);
			MainApplication.setReceiveNotification(true);
		}
	}
}
