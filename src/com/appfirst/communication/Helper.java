package com.appfirst.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.appfirst.datatypes.PolledDataData;
import com.appfirst.datatypes.ProcessData;
import com.appfirst.datatypes.SystemData;
import com.appfirst.types.Alert;
import com.appfirst.types.AlertHistory;
import com.appfirst.types.PolledDataObject;
import com.appfirst.types.Server;
import com.appfirst.types.Process;

/**
 * 
 * 
 * @author Bin Liu
 * 
 */
public class Helper {
	/**
	 * 
	 * @param is
	 * @return one line of string
	 */
	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public static List<Server> convertServerList(JSONArray jsonArray) {
		List<Server> serverList = new ArrayList<Server>();
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				JSONObject jsonObject = (JSONObject) jsonArray.get(i);
				Server server = new Server(jsonObject);
				serverList.add(server);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return serverList;
	}

	public static List<SystemData> convertServerDataList(JSONArray jsonArray) {
		List<SystemData> serverList = new ArrayList<SystemData>();
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				JSONObject jsonObject = (JSONObject) jsonArray.get(i);
				SystemData systemData = new SystemData(jsonObject);
				serverList.add(systemData);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return serverList;
	}

	public static List<ProcessData> convertProcessDataList(JSONArray jsonArray) {
		List<ProcessData> processDataList = new ArrayList<ProcessData>();
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				JSONObject jsonObject = (JSONObject) jsonArray.get(i);
				ProcessData systemData = new ProcessData(jsonObject);
				processDataList.add(systemData);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return processDataList;
	}

	public static List<Process> convertProcessList(JSONArray jsonArray) {
		List<Process> processList = new ArrayList<Process>();
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				JSONObject jsonObject = (JSONObject) jsonArray.get(i);
				Process process = new Process(jsonObject);
				processList.add(process);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return processList;
	}

	public static List<Alert> convertAlertList(JSONArray jsonArray) {
		List<Alert> alertList = new ArrayList<Alert>();
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				JSONObject jsonObject = (JSONObject) jsonArray.get(i);
				Alert process = new Alert(jsonObject);
				alertList.add(process);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return alertList;
	}

	public static List<AlertHistory> convertAlertHistoryList(JSONArray jsonArray) {
		List<AlertHistory> list = new ArrayList<AlertHistory>();
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				JSONObject jsonObject = (JSONObject) jsonArray.get(i);
				AlertHistory item = new AlertHistory(jsonObject);
				list.add(item);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return list;
	}

	/**
	 * @param dataObject
	 * @return
	 */
	public static List<PolledDataObject> convertPolledDataList(
			JSONArray jsonArray) {
		List<PolledDataObject> list = new ArrayList<PolledDataObject>();
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				JSONObject jsonObject = (JSONObject) jsonArray.get(i);
				PolledDataObject item = new PolledDataObject(jsonObject);
				list.add(item);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return list;
	}

	/**
	 * @param dataObject
	 * @return
	 */
	public static List<PolledDataData> convertPolledDataDataList(
			JSONArray jsonArray) {
		// TODO Auto-generated method stub
		List<PolledDataData> list = new ArrayList<PolledDataData>();
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				JSONObject jsonObject = (JSONObject) jsonArray.get(i);
				PolledDataData item = new PolledDataData(jsonObject);
				list.add(item);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return list;
	}
}
