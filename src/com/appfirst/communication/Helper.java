package com.appfirst.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.appfirst.datatypes.PolledDataData;
import com.appfirst.datatypes.ProcessData;
import com.appfirst.datatypes.SystemData;
import com.appfirst.monitoring2.R;
import com.appfirst.monitoring2.MainApplication;
import com.appfirst.types.Alert;
import com.appfirst.types.AlertHistory;
import com.appfirst.types.Application;
import com.appfirst.types.Log;
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
	private final static String HTTP_OK = "HTTP/1.1 200 OK";

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
		if (jsonArray == null)
			return serverList;
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
		if (jsonArray == null)
			return serverList;
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
		if (jsonArray == null)
			return processDataList;
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
		if (jsonArray == null) 
			return processList;
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
		if (jsonArray == null) {
			return alertList;
		}
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
	
	public static List<Log> convertLogList(JSONArray jsonArray) {
		List<Log> logList = new ArrayList<Log>();
		if (jsonArray == null) {
			return logList;
		}
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				JSONObject jsonObject = (JSONObject) jsonArray.get(i);
				Log process = new Log(jsonObject);
				if(MainApplication.getServerNameById(process.getServerId()) != null)
					logList.add(process);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return logList;
	}

	public static List<AlertHistory> convertAlertHistoryList(JSONArray jsonArray) {
		List<AlertHistory> list = new ArrayList<AlertHistory>();
		if (jsonArray == null) {
			return list;
		}
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
		if (jsonArray == null) {
			return list;
		}
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
		if (jsonArray == null) {
			return list;
		}
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

	/**
	 * @param jsonArray
	 * @return
	 */
	public static List<Application> convertApplicationList(JSONArray jsonArray) {
		// TODO Auto-generated method stub
		List<Application> list = new ArrayList<Application>();
		if (jsonArray == null) {
			return list;
		}
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				JSONObject jsonObject = (JSONObject) jsonArray.get(i);
				Application item = new Application(jsonObject);
				list.add(item);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return list;
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public static String formatCpuValue(double value) {
		return String.format("%.1f", value) + "%";
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public static String formatMemoryValue(double value) {
		return String.format("%.0f MB", value / 1000000);
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public static String formatByteValue(long value) {
		String valueString = String.format("%d", value);
		String units = "B";
		if (valueString.length() > 9) {
			valueString = String.format("%d", value / 1000000000);
			units = "GB";
		}
		else if (valueString.length() > 6) {
			valueString = String.format("%d", value / 1000000);
			units = "MB";
		} else if (valueString.length() > 3) {
			valueString = String.format("%d", value / 1000);
			units = "KB";
		}
		return String.format("%s %s", valueString, units);
	}
	
	public static String toRelativeTimeString(long timestamp) {
		Date convertedDate = new Date(timestamp);
	    Date todayDate = new Date();
	    double ti = System.currentTimeMillis()/1000 - timestamp;
	    if (ti < 1) {
	        return "never";
	    } else if (ti < 60) {
	        int diff = (int)ti;
	        return diff + " seconds ago";
	    } else if (ti < 7200) {
	        int diff = (int) Math.round(ti / 60);
	        return diff + " minutes ago";
	    } else if (ti < 86400) {
	        int diff = (int) Math.round(ti / 60 / 60);
	        return diff + " hours ago";
	    } else if (ti < 2629743) {
	        int diff = (int) Math.round(ti / 60 / 60 / 24);
	        return diff + " days ago";
	    } else {
	        return "never";
	    }
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public static String formatTimeValue(double value) {
		if (Double.isNaN(value))
			return "N/A";

		String valueString = String.format("%.0f", value);
		String units = "us";
		if (valueString.length() > 8) {
			valueString = String.format("%.1f", value / 1000000);
			units = "s";
		} else if (valueString.length() > 5) {
			valueString = String.format("%.1f", value / 1000);
			units = "ms";
		}
		return String.format("%s %s", valueString, units);
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public static String formatTimeValue(long value) {
		String valueString = String.format("%d", value);
		String units = "us";
		if (valueString.length() > 8) {
			valueString = String.format("%d", value / 1000000);
			units = "s";
		} else if (valueString.length() > 5) {
			valueString = String.format("%d", value / 1000);
			units = "ms";
		}
		return String.format("%s %s", valueString, units);
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public static String formatValue(long value) {
		return String.format("%d", value);
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public static String formatValue(int value) {
		return String.format("%d", value);
	}

	/**
	 * 
	 * @param time
	 *            number of epoch second
	 * @return formated time
	 */
	public static String formatTime(long time) {
		String ret = "";
		if (time > 0) {
			Date date = new Date(time);
			SimpleDateFormat df = new SimpleDateFormat("MMM d HH:mm");
			ret = df.format(date);
		} else {
			ret = "N/A";
		}
		return ret;
	}

	/**
	 * 
	 * @param time
	 *            number of epoch second
	 * @return formated time
	 */
	public static String formatLongTime(long time) {
		String ret = "";
		if (time > 0) {
			Date date = new Date(time);
			SimpleDateFormat df = new SimpleDateFormat("MMM d HH:mm yyyy");
			ret = df.format(date);
		} else {
			ret = "N/A";
		}
		return ret;
	}

	/**
	 * Get the url of server list query.
	 * 
	 * @param context
	 *            current activity.
	 * @return the formatted url string.
	 */
	public static String getServerListUrl(Context context) {
		return String.format("%s%s", context
				.getString(R.string.frontend_address), context
				.getString(R.string.api_servers));

	}

	/**
	 * Get the url of the polled data list query.
	 * 
	 * @param context
	 *            current activity.
	 * @return the formatted url string.
	 */
	public static String getPollDataList(Context context) {
		return String.format("%s%s", context
				.getString(R.string.frontend_address), context
				.getString(R.string.api_polled_datas));
	}
	
	/**
	 * Get the url of the alert query. 
	 * @param context current activity.
	 * @param id alert id
	 * @return the formatted url String. 
	 */
	public static String getLogUrl(Context context, int id) {
		String ret = String.format("%s%s", context
				.getString(R.string.frontend_address), context
				.getString(R.string.api_logs));
		if (id > 0) {
			ret = String.format("%s%d/", ret, id); 
		}
		return ret;
	}
	
	/**
	 * Get the url of the alert query. 
	 * @param context current activity.
	 * @param id alert id
	 * @return the formatted url String. 
	 */
	public static String getAlertUrl(Context context, int id) {
		String ret = String.format("%s%s", context
				.getString(R.string.frontend_address), context
				.getString(R.string.api_alerts));
		if (id > 0) {
			ret = String.format("%s%d/", ret, id); 
		}
		return ret;
	}
	
	/**
	 * Get the url of the mobile device.  
	 * @param context current activity. 
	 * @param id device id
	 * @return the formatted url String. 
	 */
	public static String getDeviceUrl(Context context, int id) {
		String ret = String.format("%s%s", context
				.getString(R.string.frontend_address), context
				.getString(R.string.api_device));
		if (id > 0) {
			ret = String.format("%s/%d/", ret, id); 
		}
		return ret;
	}
	

	/**
	 * Check whether request is successful.
	 * 
	 * @param response
	 *            the HTTP Response
	 * @return true if successful, false otherwise
	 */
	public static Boolean checkStatus(HttpResponse response) {
		return response.getStatusLine().toString().equals(HTTP_OK);
	}
}
