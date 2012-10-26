package com.appfirst.monitoring;

import java.util.List;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.appfirst.communication.AFClient;
import com.appfirst.communication.Helper;
import com.appfirst.datatypes.DetailData;
import com.appfirst.types.AFDevice;
import com.appfirst.types.Alert;
import com.appfirst.types.AlertHistory;
import com.appfirst.types.PolledDataObject;
import com.appfirst.types.Server;
import com.appfirst.types.Log;

import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.*;

/**
 * Represents the whole application and serves as a global variable. It holds a
 * {@link AFClient} member to access all the datas.
 * 
 * @author Bin Liu
 * 
 */
public class MainApplication extends Application {
	private static final String TAG = "MainApplication";
	private static List<Alert> alerts;
	private static List<com.appfirst.types.Application> applications;
	private static List<Server> servers;
	private static List<com.appfirst.types.Log> logs;
	private static List<PolledDataObject> polledDatas;
	private static List<AlertHistory> alertHistories;
	private static List<com.appfirst.types.Process> processes;
	private static DetailData detailData;
	private static boolean bReceiveNotification = true;
	private static String currentUsername = "";
	private static TreeMap<Integer, String> serverNameMap = new TreeMap<Integer, String>();
	private static String uid = ""; // unique ID provided by Google C2DM.
	private static AFDevice device; // current device status
	private static Boolean firstTimeLogin = false; // per login basic, only set
	private static boolean alertNeedRefresh = false;
	private static boolean alertHistoryNeedRefresh = false;
	private static boolean polledDataNeedRefresh = false;
	private static boolean applicationNeedRefresh = false;
	private static boolean logNeedRefresh = false;
	
	public static boolean isAlertNeedRefresh() {
		return alertNeedRefresh;
	}
	
	public static boolean isLogNeedRefresh() {
		return logNeedRefresh;
	}
	
	public static void setLogNeedRefresh(boolean logNeedRefresh) {
		MainApplication.logNeedRefresh = logNeedRefresh;
	}

	public static void setAlertNeedRefresh(boolean alertNeedRefresh) {
		MainApplication.alertNeedRefresh = alertNeedRefresh;
	}

	public static boolean isAlertHistoryNeedRefresh() {
		return alertHistoryNeedRefresh;
	}

	public static void setAlertHistoryNeedRefresh(boolean alertHistoryNeedRefresh) {
		MainApplication.alertHistoryNeedRefresh = alertHistoryNeedRefresh;
	}

	public static boolean isPolledDataNeedRefresh() {
		return polledDataNeedRefresh;
	}

	public static void setPolledDataNeedRefresh(boolean polledDataNeedRefresh) {
		MainApplication.polledDataNeedRefresh = polledDataNeedRefresh;
	}

	public static boolean isApplicationNeedRefresh() {
		return applicationNeedRefresh;
	}

	public static void setApplicationNeedRefresh(boolean applicationNeedRefresh) {
		MainApplication.applicationNeedRefresh = applicationNeedRefresh;
	}

	// the first time user
	// login.

	public static Boolean getFirstTimeLogin(Context context) {
		JSONArray devices = MainApplication.client.getDeviceList(Helper
				.getDeviceUrl(context, -1));
		if (devices == null || devices.length() == 0) {
			firstTimeLogin = true;
		}
		return firstTimeLogin;
	}

	public static AFClient client = new AFClient();

	public static synchronized String getUid() {
		return uid;
	}

	public static synchronized void setUid(Context context, String newUid) {
		SharedPreferences preference = context.getSharedPreferences(context
				.getString(R.string.appfirst_preference_key),
				Context.MODE_PRIVATE);
		uid = newUid;
		Editor editor = preference.edit();
		editor.putString(context.getString(R.string.uid_key), uid);
		editor.commit();
	}

	/**
	 * Load all the server names and ids when the application is created.
	 * 
	 * @param url
	 *            resource url.
	 */
	public static synchronized void loadServerList(String url) {
		servers = client.getServerList(url);
		if (servers == null)
			return;
		for (int i = 0; i < servers.size(); i++) {
			Server server = servers.get(i);
			if (!serverNameMap.containsKey(server.getId())) {
				serverNameMap.put(server.getId(), server.getHostname());
			}
		}
	}

	/**
	 * @param currentUsername
	 *            the currentUsername to set
	 */
	public static void setCurrentUsername(String currentUsername) {
		MainApplication.currentUsername = currentUsername;
	}

	/**
	 * @return the currentUsername
	 */
	public static String getCurrentUsername() {
		return currentUsername;
	}

	public static synchronized List<Alert> getAlerts() {
		return alerts;
	}

	public static List<com.appfirst.types.Application> getApplications() {
		return applications;
	}

	public static List<Server> getServers() {
		return servers;
	}
	
	public static List<Log> getLogs() {
		return logs;
	}

	public static List<PolledDataObject> getPolledDatas() {
		return polledDatas;
	}

	public static List<AlertHistory> getAlertHistories() {
		return alertHistories;
	}

	public static List<com.appfirst.types.Process> getProcesses() {
		return processes;
	}

	public static void setProcesses(
			List<com.appfirst.types.Process> newProcesses) {
		processes = newProcesses;
	}

	public static DetailData getDetailData() {
		return detailData;
	}

	public static TreeMap<Integer, String> getServerNameMap() {
		return serverNameMap;
	}

	public static synchronized void setReceiveNotification(Boolean bool) {
		bReceiveNotification = bool;
	}

	public static synchronized Boolean getReceiveNotification() {
		return bReceiveNotification;
	}

	public static synchronized String getServerNameById(Integer id) {
		if (id < 0)
			return "";
		else
			return serverNameMap.get(id);
	}

	public static synchronized void loadAlertList(String url) {
		alerts = client.getAlertList(url);
	}

	public static synchronized void loadPolledDataList(String url) {
		polledDatas = client.getPollDataList(url);
	}
	
	public static synchronized void loadLogList(String url) {
		logs = client.getLogList(url);
	}

	public static synchronized void loadApplicationList(String url) {
		applications = client.getApplicationList(url);
	}

	public static synchronized void loadAlertHistory(String url) {
		alertHistories = client.getAlertHistory(url, 50);
	}

	public static synchronized void loadDetailData(String url) {
		detailData = client.getDetailData(url);
	}

	public static synchronized Boolean hasValidDetailData() {
		if (detailData != null && !detailData.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	public static synchronized Boolean checkClientLogin(Context context) {
		if (client.getmAuthString() == null
				|| client.getmAuthString().length == 0) {
			client.setmAuthString(MainApplication.getSavedLogin(context));
		}
		
		if (client.getmAuthString() == null
				|| client.getmAuthString().length == 0)
			return false;
		else
			return true;
	}

	/**
	 * Unregistrate from push notification service.
	 * 
	 * @param context
	 *            activity
	 */
	public static synchronized void unregistrateC2DM(Context context) {
		// remove current uid.
		SharedPreferences preference = context.getSharedPreferences(context
				.getString(R.string.appfirst_preference_key),
				Context.MODE_PRIVATE);
		Editor editor = preference.edit();
		editor.remove(context.getString(R.string.uid_key));
		editor.commit();

		Intent unregIntent = new Intent(
				"com.google.android.c2dm.intent.UNREGISTER");
		unregIntent.putExtra("app", PendingIntent.getBroadcast(context, 0,
				new Intent(), 0));
		context.startService(unregIntent);
	}

	/**
	 * Register the push notification service. We are using the minimum API
	 * level of 8. Only register again if we don't have a current one.
	 * 
	 * @param context
	 *            current activity
	 */
	public static synchronized void registerC2DM(Context context) {
		SharedPreferences preference = context.getSharedPreferences(context
				.getString(R.string.appfirst_preference_key),
				Context.MODE_PRIVATE);
		String savedUid = (preference.getString(context
				.getString(R.string.uid_key), ""));
		if (savedUid != "") {// already saved in the database.
			uid = savedUid;
			return;
		}

		android.util.Log.i(TAG, "Try to register c2dm");
		Intent registrationIntent = new Intent(
				"com.google.android.c2dm.intent.REGISTER");
		registrationIntent.putExtra("app", PendingIntent.getBroadcast(context,
				0, new Intent(), 0));
		registrationIntent.putExtra("sender", context
				.getString(R.string.sender_email));
		context.startService(registrationIntent);
	}

	/**
	 * Save user name and password after login is successful
	 * 
	 * @param context
	 *            current activity
	 * @param username
	 *            new user name
	 * @param password
	 *            new user password
	 */
	public static synchronized void saveUserLogin(Context context,
			String username, String password) {
		SharedPreferences preference = context.getSharedPreferences(context
				.getString(R.string.appfirst_preference_key),
				Context.MODE_PRIVATE);
		String oldUserName = preference.getString(context
				.getString(R.string.username_key), "");
		String oldPassword = preference.getString(context
				.getString(R.string.passowrd_key), "");

		if (oldUserName != username || oldPassword != password) { // user name
			// and
			// password
			// is not saved
			Editor editor = preference.edit();
			editor
					.putString(context.getString(R.string.username_key),
							username);
			editor
					.putString(context.getString(R.string.passowrd_key),
							password);
			editor.commit();
			setNeedRefreshData();
		}
		
		// update the authorization key.
		client.setmAuthString(String.format("%s:%s", username, password)
				.getBytes());
	}
	
	private static void setNeedRefreshData() {
		alertNeedRefresh = true;
		alertHistoryNeedRefresh = true;
		applicationNeedRefresh = true;
		polledDataNeedRefresh = true;
	}

	/**
	 * Delete saved user name and password.
	 * 
	 * @param context
	 *            current activity
	 */
	public static synchronized void deleteUserLogin(Context context) {
		SharedPreferences preference = context.getSharedPreferences(context
				.getString(R.string.appfirst_preference_key),
				Context.MODE_PRIVATE);
		Editor editor = preference.edit();
		editor.remove(context.getString(R.string.username_key));
		editor.remove(context.getString(R.string.passowrd_key));
		editor.commit();
	}

	/**
	 * Get saved login.
	 * 
	 * @param context
	 *            activity.
	 * @return saved login string.
	 */
	public static synchronized byte[] getSavedLogin(Context context) {
		String ret = "";
		SharedPreferences preference = context.getSharedPreferences(context
				.getString(R.string.appfirst_preference_key),
				Context.MODE_PRIVATE);
		String oldUserName = preference.getString(context
				.getString(R.string.username_key), "");
		String oldPassword = preference.getString(context
				.getString(R.string.passowrd_key), "");

		if (oldUserName != "" && oldPassword != "") {
			ret = String.format("%s:%s", oldUserName, oldPassword);
		}

		return ret.getBytes();
	}
	
	public static synchronized int getAlertIncidents(Context context) {
		int incidents = 0;
		MainApplication.loadAlertList(Helper.getAlertUrl(context, -1));
		if (alerts != null) {
			for (int cnt = 0; cnt < alerts.size(); cnt ++) {
				if (alerts.get(cnt).getIn_incident()) {
					incidents ++;
				}
			}
		}
		
		return incidents;
	}

	/**
	 * @param device
	 *            the device to set
	 */
	public static synchronized void setDevice(AFDevice device) {
		MainApplication.device = device;
	}

	/**
	 * @return the device
	 */
	public static synchronized AFDevice getDevice() {
		return device;
	}

	public static synchronized void updateCachedAlerts(int index, Alert newAlert) {
		alerts.set(index, newAlert);
	}

	/**
	 * update widget badge and reset widget message when user are viewing alert
	 * history.
	 */
	public static void updateWidgetBadge(Context context) {
		if (MainApplication.getDevice() == null) {
			initDeviceId(context);
		}
		MainApplication.client.updateDeviceBadge(Helper.getDeviceUrl(context,
				MainApplication.getDevice().getId()), 0, MainApplication
				.getUid());
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(ns);
		mNotificationManager.cancelAll();
		android.util.Log.i(TAG, "Badge has been resetted. ");
	}

	/**
	 * Initialize device id if user click from widget.
	 */
	private static void initDeviceId(Context context) {
		SharedPreferences preference = context.getSharedPreferences(context
				.getString(R.string.appfirst_preference_key),
				Context.MODE_PRIVATE);
		String savedUid = (preference.getString(context
				.getString(R.string.uid_key), ""));
		JSONObject deviceObject = null;
		deviceObject = MainApplication.client.saveDeviceInfo(Helper
				.getDeviceUrl(context, -1), context.getString(R.string.brand),
				savedUid);// query the device object

		if (deviceObject != null) {// only reset the device id here, may have to
									// be changed
			try {
				MainApplication.setDevice(new AFDevice(deviceObject));
				android.util.Log.i(TAG, String.format("Get new device id: %d",
						MainApplication.getDevice().getId()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
