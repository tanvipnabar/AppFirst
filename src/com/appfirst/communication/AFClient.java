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
package com.appfirst.communication;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.appfirst.datatypes.AlertHistoryData;
import com.appfirst.datatypes.DetailData;
import com.appfirst.datatypes.PolledDataData;
import com.appfirst.datatypes.ProcessData;
import com.appfirst.datatypes.SystemData;
import com.appfirst.monitoring.R;
import com.appfirst.types.Alert;
import com.appfirst.types.AlertHistory;
import com.appfirst.types.Application;
import com.appfirst.types.PolledDataObject;
import com.appfirst.types.Server;
import com.appfirst.types.Process;

import android.util.Log;
import android.util.Base64;

/**
 * Client library for accessing AppFirst Monitoring public API.
 * <p>
 * It provides most of the interface of getting the current status for servers,
 * applications, tags, processes, polled datas and alerts. At the same time, it
 * doesn't implement the interface of accessing he functionalities like deleting
 * or adding.
 * </p>
 * 
 * @author Bin Liu
 * 
 */
public class AFClient {
	private DefaultHttpClient mClient;/* the actual http client */
	private String TAG = "AFClient";/* tag for logging */
	private List<Cookie> _cookies;/* keep cookies after login */
	private String mEncodedAuthString;/* the key for access the public API */
	private String mAuthName = "Authorization";/*
												 * name of the header field for
												 * authorization
												 */
	private byte[] mAuthString = "".getBytes();/*
												 * value of authorization header
												 */

	public byte[] getmAuthString() {
		return mAuthString;
	}

	public void setmAuthString(byte[] mAuthString) {
		this.mAuthString = mAuthString;
	}

	/**
	 * My default constructor. It create an instance of {@link AFHttpClient} as
	 * its member to access all the get/post HTTP method.
	 */
	public AFClient() {
		AFHttpClient afHttpClient = new AFHttpClient();
		this.mClient = afHttpClient.getAFHttpClient();
	}

	/**
	 * @param apiKey
	 *            the new api key
	 */
	public void setApiKeyByString(String apiKey) {
		this.mAuthString = apiKey.getBytes();
	}

	/**
	 * Logs in with input username and password, store the api key that's
	 * returned for accessing AppFirst public API.
	 * 
	 * @param username
	 *            user's name
	 * @param password
	 *            user's password
	 * @return true is login succeed, false otherwise
	 */
	public Boolean userLogin(String username, String password, String address) {
		List<NameValuePair> postContent = new ArrayList<NameValuePair>(2);
		postContent.add(new BasicNameValuePair("username", username));
		postContent.add(new BasicNameValuePair("password", password));
		HttpPost post = new HttpPost(address);
		Boolean result = false;
		try {
			post.setEntity(new UrlEncodedFormEntity(postContent));
		} catch (Exception e) {
			return result;
		}
		HttpEntity entity;
		try {
			HttpResponse response = this.mClient.execute(post);
			entity = response.getEntity();
			if (entity != null) {
				entity.consumeContent();
			}
			if (Helper.checkStatus(response)) {
				result = true;
			} else {
				Log.e(TAG, String.format("Log in failed: %s", response
						.getStatusLine()));
				result = false;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * Gets a list of the server for a tenant, including the capacity data.
	 * 
	 * @param url
	 *            the url of appfirst public API
	 * @return a list of {@link Server} object.
	 */
	public List<Server> getServerList(String url) {
		JSONArray jsonArray = new JSONArray();
		HttpGet getRequest = new HttpGet(url);
		jsonArray = makeJsonArrayRequest(getRequest);
		return Helper.convertServerList(jsonArray);
	}

	/**
	 * Gets the capacity data for a server.
	 * 
	 * @param url
	 *            the public api address of the query
	 * @return a {@link Server} object.
	 */
	public Server getServer(String url) {
		JSONObject jsonObject = null;
		HttpGet getRequest = new HttpGet(url);
		jsonObject = makeJsonObjectRequest(getRequest);
		return new Server(jsonObject);
	}

	/**
	 * Gets the server data
	 * 
	 * @param url
	 *            the public api address of the query
	 * @return a list of system data in the time order.
	 */
	public List<SystemData> getServerData(String url) {
		JSONArray jsonArray = null;
		HttpGet getRequest = new HttpGet(url);
		jsonArray = makeJsonArrayRequest(getRequest);
		return Helper.convertServerDataList(jsonArray);
	}

	/**
	 * Gets the server data
	 * 
	 * @param url
	 *            the public api address of the query
	 * @param number
	 *            the many number of points to be retrived. Note that there can
	 *            be gaps in the data if the server has an outage.
	 * @return a list of system data in the time order.
	 */
	public List<SystemData> getServerData(String url, int number) {
		JSONArray jsonArray = null;
		String params = String.format("?num=%d", number);
		HttpGet getRequest = new HttpGet(url + params);
		jsonArray = makeJsonArrayRequest(getRequest);
		return Helper.convertServerDataList(jsonArray);
	}

	/**
	 * Gets the server data
	 * 
	 * @param url
	 *            the public api address of the query
	 * @param start
	 *            Retrieve data from this timestamp backwards. If not given, it
	 *            gets the most recent data.
	 * @param end
	 *            Don't retrieve any points before this date, if given.
	 * @param number
	 *            Number of points to retrive.
	 * @return a list of system data in the time order.
	 */
	public List<SystemData> getServerData(String url, long start, long end,
			int number) {
		JSONArray jsonArray = null;
		String params = String.format("?start=%d&end=%d&num=%d", start, end,
				number);
		HttpGet getRequest = new HttpGet(url + params);
		jsonArray = makeJsonArrayRequest(getRequest);

		return Helper.convertServerDataList(jsonArray);
	}

	/**
	 * Gets the current running processes for a server.
	 * 
	 * @param url
	 *            the public api address of the query
	 * @return a list of {@link Process} object.
	 */
	public List<Process> getServerProcessList(String url) {
		JSONArray jsonArray = null;

		HttpGet getRequest = new HttpGet(url);
		jsonArray = makeJsonArrayRequest(getRequest);

		return Helper.convertProcessList(jsonArray);
	}

	/**
	 * Gets the current running processes for a server.
	 * 
	 * @param url
	 *            the public api address of the query
	 * @param start
	 *            Start date of range (in secs since epoch) to get processes
	 *            for.
	 * @param end
	 *            End date of range (in secs since epoch) to get processes for.
	 * @return a list of {@link Process} object.
	 */
	public List<Process> getServerProcessList(String url, long start, long end) {
		JSONArray jsonArray = null;
		String params = String.format("?start=%d&end=%d", start, end);
		HttpGet getRequest = new HttpGet(url + params);
		jsonArray = makeJsonArrayRequest(getRequest);

		return Helper.convertProcessList(jsonArray);
	}

	/**
	 * Gets the process information.
	 * 
	 * @param url
	 *            the public api address of the query
	 * @return a new {@link Process} object
	 */
	public Process getProcess(String url) {
		JSONObject jsonObject = null;
		HttpGet getRequest = new HttpGet(url);
		jsonObject = makeJsonObjectRequest(getRequest);

		return new Process(jsonObject);
	}

	/**
	 * Gets the process data for the most recent day.
	 * 
	 * @param url
	 *            the public api address of the query
	 * @return a list of {@link ProcessData} object.
	 */
	public List<ProcessData> getProcessData(String url) {
		JSONArray jsonArray = null;

		HttpGet getRequest = new HttpGet(url);
		jsonArray = makeJsonArrayRequest(getRequest);

		return Helper.convertProcessDataList(jsonArray);
	}

	/**
	 * Gets the process data for the most recent day.
	 * 
	 * @param url
	 *            the public api address of the query
	 * @param number
	 *            Retrieve up to this many number of points. Note that there can
	 *            be gaps in the data if the server this process is on has an
	 *            outage.
	 * @return a list of {@link ProcessData} object.
	 */
	public List<ProcessData> getProcessData(String url, int number) {
		JSONArray jsonArray = null;
		String params = String.format("?num=%d", number);
		HttpGet getRequest = new HttpGet(url + params);
		jsonArray = makeJsonArrayRequest(getRequest);

		return Helper.convertProcessDataList(jsonArray);
	}

	/**
	 * Gets the process data for the most recent day.
	 * 
	 * @param url
	 *            the public api address of the query
	 * @param number
	 *            the number of points to retrieve
	 * @param start
	 *            don't retrieve any points before this date, if given.
	 * @param end
	 *            retrieve data from this timestamp backwards. If not given, it
	 *            gets the most recent data.
	 * @return a list of {@link ProcessData} object.
	 */
	public List<ProcessData> getProcessData(String url, long start, long end,
			int number) {
		JSONArray jsonArray = null;
		String params = String.format("?start=%d&end=%d&num=%d", start, end,
				number);
		HttpGet getRequest = new HttpGet(url + params);
		jsonArray = makeJsonArrayRequest(getRequest);

		return Helper.convertProcessDataList(jsonArray);
	}

	/**
	 * Gets the detail data for lastest minute.
	 * 
	 * @param url
	 *            the public api address of the query
	 * @return a {@link DetailData} object.
	 */
	public DetailData getDetailData(String url) {
		JSONObject jsonObject = null;
		HttpGet getRequest = new HttpGet(url);
		jsonObject = makeJsonObjectRequest(getRequest);

		return new DetailData(jsonObject);
	}

	/**
	 * Gets the detail data for specific minute.
	 * 
	 * @param url
	 *            the public api address of the query
	 * @return a {@link DetailData} object.
	 */
	public DetailData getDetailData(String url, long time) {
		JSONObject jsonObject = null;
		String params = String.format("?time=%d", time);
		HttpGet getRequest = new HttpGet(url + params);
		jsonObject = makeJsonObjectRequest(getRequest);

		return new DetailData(jsonObject);
	}

	/**
	 * Gets the detail data for lastest minute.
	 * 
	 * @param url
	 *            the public api address of the query
	 * @return a {@link Application} object.
	 */
	public List<Application> getApplicationList(String url) {
		JSONArray jsonArray = null;
		HttpGet getRequest = new HttpGet(url);
		jsonArray = makeJsonArrayRequest(getRequest);

		return Helper.convertApplicationList(jsonArray);
	}

	/**
	 * Gets the application object.
	 * 
	 * @param url
	 *            the public api address of the query
	 * @return a {@link Application} object.
	 */
	public Application getApplication(String url) {
		JSONObject jsonObject = null;
		HttpGet getRequest = new HttpGet(url);
		jsonObject = makeJsonObjectRequest(getRequest);

		return new Application(jsonObject);
	}

	/**
	 * Gets alert list
	 * 
	 * @param url
	 *            the public api address of the query
	 * @return a list of {@link Alert} object.
	 */
	public List<Alert> getAlertList(String url) {
		JSONArray jsonArray = null;
		HttpGet getRequest = new HttpGet(url);
		jsonArray = makeJsonArrayRequest(getRequest);

		return Helper.convertAlertList(jsonArray);
	}

	/**
	 * Gets an alert with id.
	 * 
	 * @param url
	 *            the public api address of the query
	 * @return a {@link Alert} object.
	 */
	public Alert getAlert(String url) {
		JSONObject jsonObject = null;
		HttpGet getRequest = new HttpGet(url);
		jsonObject = makeJsonObjectRequest(getRequest);

		return new Alert(jsonObject);
	}

	/**
	 * Gets an AlertHistory with id.
	 * 
	 * @param url
	 *            the public api address of the query
	 * @return a {@link AlertHistory} object.
	 */
	public AlertHistory getAlertHistory(String url) {
		JSONObject jsonObject = null;
		HttpGet getRequest = new HttpGet(url);
		jsonObject = makeJsonObjectRequest(getRequest);

		return new AlertHistory(jsonObject);
	}

	/**
	 * Gets a number of AlertHistories.
	 * 
	 * @param url
	 *            the public api address of the query
	 * @return a {@link AlertHistory} object.
	 */
	public List<AlertHistory> getAlertHistory(String url, int number) {
		JSONArray jsonArray = null;
		String params = String.format("?num=%d", number);
		HttpGet getRequest = new HttpGet(url + params);
		jsonArray = makeJsonArrayRequest(getRequest);
		return Helper.convertAlertHistoryList(jsonArray);
	}

	/**
	 * Gets a number of AlertHistories.
	 * 
	 * @param url
	 *            the public api address of the query
	 * @param start
	 *            don't retrieve any points before this date, if given.
	 * @param end
	 *            retrieve data from this timestamp backwards. If not given, it
	 *            gets the most recent data.
	 * @return a {@link AlertHistory} object.
	 */
	public List<AlertHistory> getAlertHistory(String url, long start, long end) {
		JSONArray jsonArray = null;
		String params = String.format("?start=%d&end=%d", start, end);
		HttpGet getRequest = new HttpGet(url + params);
		jsonArray = makeJsonArrayRequest(getRequest);
		return Helper.convertAlertHistoryList(jsonArray);
	}

	/**
	 * Gets the message of a triggered alert.
	 * 
	 * @param url
	 *            the public api address of the query
	 * @return a {@link AlertHistoryData} object
	 */
	public AlertHistoryData getAlertHistoryData(String url) {
		JSONObject jsonObject = null;
		HttpGet getRequest = new HttpGet(url);
		jsonObject = makeJsonObjectRequest(getRequest);
		return new AlertHistoryData(jsonObject);
	}

	/**
	 * Gets an PolledDataObject with id.
	 * 
	 * @param url
	 *            the public api address of the query
	 * @return a {@link PolledDataObject} object.
	 */
	public PolledDataObject getPollData(String url) {
		JSONObject jsonObject = null;
		HttpGet getRequest = new HttpGet(url);
		jsonObject = makeJsonObjectRequest(getRequest);

		return new PolledDataObject(jsonObject);
	}

	

	/**
	 * Gets an PolledDataObject with id.
	 * 
	 * @param url
	 *            the public api address of the query
	 * @return a {@link PolledDataObject} object.
	 */
	public List<PolledDataObject> getPollDataList(String url) {
		JSONArray dataObject = null;
		HttpGet getRequest = new HttpGet(url);
		dataObject = makeJsonArrayRequest(getRequest);
		return Helper.convertPolledDataList(dataObject);
	}

	/**
	 * Gets an PolledDataObject with id.
	 * 
	 * @param url
	 *            the public api address of the query
	 * @return a {@link PolledDataData} object.
	 */
	public List<PolledDataData> getPollDataDataList(String url) {
		JSONArray dataObject = null;
		HttpGet getRequest = new HttpGet(url);
		dataObject = makeJsonArrayRequest(getRequest);
		return Helper.convertPolledDataDataList(dataObject);
	}

	/**
	 * Gets an PolledDataObject with id.
	 * 
	 * @param url
	 *            the public api address of the query
	 * @param number
	 *            the number of points to retrieve
	 * @param start
	 *            don't retrieve any points before this date, if given.
	 * @param end
	 *            retrieve data from this timestamp backwards. If not given, it
	 *            gets the most recent data.
	 * @return a {@link PolledDataData} object.
	 */
	public List<PolledDataData> getPollDataDataList(String url, long start,
			long end, int number) {
		JSONArray dataObject = null;
		URI uri = null;
		try {
			uri = new URI(String.format("%s?num=%d", url, number));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpGet getRequest = new HttpGet(uri);
		dataObject = makeJsonArrayRequest(getRequest);

		return Helper.convertPolledDataDataList(dataObject);
	}

	/**
	 * Gets an PolledDataObject with id.
	 * 
	 * @param url
	 *            the public api address of the query
	 * @return a {@link PolledDataData} object.
	 */
	public List<PolledDataData> getPollDataDataList(String url, int number) {
		JSONArray dataObject = null;
		String params = String.format("?num=%d", number);
		HttpGet getRequest = null;
		try {
			getRequest = new HttpGet(new URI(url + params));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dataObject = makeJsonArrayRequest(getRequest);

		return Helper.convertPolledDataDataList(dataObject);
	}

	

	/**
	 * Change the alert status to be either active or inactive.
	 * 
	 * @param url
	 *            query url
	 * @param id
	 *            the id of the alert
	 * @param active
	 *            Boolean value indicates whether the alert is active or not.
	 * @return the modified Alert object.
	 */
	public Alert updateAlertStatus(String url, int id, Boolean active) {
		JSONObject updatedAlert = new JSONObject();
		HttpPut putRequest = null;
		String params = String
				.format("?id=%d&active=%s", id, active.toString());
		try {
			putRequest = new HttpPut(new URI(url + params));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.mEncodedAuthString = String.format("Basic %s", Base64
				.encodeToString(this.mAuthString, Base64.DEFAULT).trim());
		putRequest.setHeader(this.mAuthName, this.mEncodedAuthString);
		try {
			HttpResponse response = this.mClient.execute(putRequest);
			if (!Helper.checkStatus(response)) {
				Log.e(TAG, String.format("Request failed with :%s", response
						.getStatusLine()));
				return null;
			}
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				String result = Helper.convertStreamToString(instream);
				updatedAlert = new JSONObject(result);
				instream.close();
			}
		} catch (ClientProtocolException cpe) {
			cpe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (JSONException je) {
			je.printStackTrace();
		}
		return new Alert(updatedAlert);
	}
	
	/**
	 * Make a get request and return a JSONArray. 
	 * @param getRequest a HTTPGet request. 
	 * @return JSONArray, null if error occurs. 
	 */
	private JSONArray makeJsonArrayRequest(HttpGet getRequest) {
		JSONArray jsonArray = null;
		this.mEncodedAuthString = String.format("Basic %s", Base64
				.encodeToString(this.mAuthString, Base64.DEFAULT).trim());
		getRequest.setHeader(this.mAuthName, this.mEncodedAuthString);
		try {
			HttpResponse response = this.mClient.execute(getRequest);
			if (!Helper.checkStatus(response)) {
				Log.e(TAG, String.format("Request failed with :%s", response
						.getStatusLine()));
				return null;
			}
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				String result = Helper.convertStreamToString(instream);
				jsonArray = new JSONArray(result);
				instream.close();
			}
		} catch (ClientProtocolException cpe) {
			cpe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (JSONException je) {
			je.printStackTrace();
		}
		return jsonArray;
	}
	/**
	 * Make a get request and return a JSONObject;
	 * @param jsonObject a HttpGet request. 
	 * @return JSONObject, null if error occurs. 
	 */
	private JSONObject makeJsonObjectRequest(HttpGet getRequest) {
		JSONObject jsonObject = null;
		this.mEncodedAuthString = String.format("Basic %s", Base64
				.encodeToString(this.mAuthString, Base64.DEFAULT).trim());
		getRequest.setHeader(this.mAuthName, this.mEncodedAuthString);
		try {
			HttpResponse response = this.mClient.execute(getRequest);
			if (!Helper.checkStatus(response)) {
				Log.e(TAG, String.format("Request failed with :%s", response
						.getStatusLine()));
				return null;
			}
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream instream = entity.getContent();
				String result = Helper.convertStreamToString(instream);
				jsonObject = new JSONObject(result);
				instream.close();
			}
		} catch (ClientProtocolException cpe) {
			cpe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (JSONException je) {
			je.printStackTrace();
		}
		return jsonObject;
	}
}
