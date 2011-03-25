package com.appfirst.monitoring;

import java.util.List;
import java.util.TreeMap;

import com.appfirst.communication.AFClient;
import com.appfirst.datatypes.DetailData;
import com.appfirst.types.Alert;
import com.appfirst.types.AlertHistory;
import com.appfirst.types.PolledDataObject;
import com.appfirst.types.Server;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Represents the whole application and serves as a global variable. It holds a
 * {@link AFClient} member to access all the datas.
 * 
 * @author Bin Liu
 * 
 */
public class MainApplication extends Application {
	public static AFClient client = new AFClient();
	private static List<Alert> alerts;
	public static List<com.appfirst.types.Application> applications;
	public static List<Server> servers;
	public static List<PolledDataObject> polledDatas;
	private static List<AlertHistory> alertHistories;
	public static List<com.appfirst.types.Process> processes;
	public static DetailData detailData;
	private static boolean bReceiveNotification = true;
	private static String currentUsername = "";
	public static TreeMap<Integer, String> serverNameMap = new TreeMap<Integer, String>();

	public static synchronized void loadServerList(String url) {
		servers = client.getServerList(url);
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

	public static List<Alert> getAlerts() {
		return alerts;
	}

	public static List<com.appfirst.types.Application> getApplications() {
		return applications;
	}

	public static List<Server> getServers() {
		return servers;
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

	public static synchronized void loadApplicationList(String url) {
		applications = client.getApplicationList(url);
	}

	public static synchronized void loadAlertHistory(String url) {
		alertHistories = client.getAlertHistory(url, 10);
	}

	public static synchronized void loadDetailData(String url) {
		detailData = client.getDetailData(url);
	}

	/**
	 * Unregistrate from push notification service.
	 * 
	 * @param context
	 *            activity
	 */
	public static synchronized void unregistrateC2DM(Context context) {
		Intent unregIntent = new Intent(
				"com.google.android.c2dm.intent.UNREGISTER");
		unregIntent.putExtra("app", PendingIntent.getBroadcast(context, 0,
				new Intent(), 0));
		context.startService(unregIntent);
	}

	/**
	 * Register the push notification service. We are using the minimum API
	 * level of 8.
	 * 
	 * @param context
	 *            current activity
	 */
	public static synchronized void registerC2DM(Context context) {
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
		}

		// update the authorization key.
		client.setmAuthString(String.format("%s:%s", username, password)
				.getBytes());
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

}
