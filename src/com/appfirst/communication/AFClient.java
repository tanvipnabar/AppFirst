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
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.appfirst.datatypes.SystemData;
import com.appfirst.types.Server;

import android.util.Log;
import android.util.Base64;


/**
 * Client library for accessing AppFirst Monitoring public API.
 * <p>
 * It provides most of the interface of getting the current status for servers, applications, tags, 
 * processes, polled datas and alerts. At the same time, it doesn't implement the interface of 
 * accessing he functionalities like deleting or adding. 
 * </p>
 * @author Bin Liu
 * 
 */
public class AFClient {
	private DefaultHttpClient mClient;/* the actual http client */
	private String TAG = "AFHttpClient";/* tag for logging */
	private List<Cookie> _cookies;/* keep cookies after login */
	private String mEncodedAuthString;/* the key for access the public API */
	private String mAuthName = "Authorization";/* name of the header field for authorization */
	private byte[] mAuthString = "Basic EMAIL:APIKEY".getBytes();/* value of authorization header */
	
	/**
	 * My default constructor. 
	 */
	public AFClient() {
		AFHttpClient afHttpClient = new AFHttpClient();
		this.mClient = afHttpClient.getAFHttpClient();
	}
	
	/**
	 * @param apiKey the new api key
	 */
	public void setApiKeyByString(String apiKey) {
		this.mAuthString = apiKey.getBytes();
	}

	/**
	 * Logs in with input username and password, store the api key 
	 * that's returned for accessing AppFirst public API. 
	 * @param username user's name
	 * @param password user's password
	 * @return true is login succeed, false otherwise
	 */
	public Boolean userLogin(String username, String password) {
		List<NameValuePair> postContent = new ArrayList<NameValuePair>(2);
		postContent.add(new BasicNameValuePair("username", username));
		postContent.add(new BasicNameValuePair("password", password));
		HttpPost post = new HttpPost("https://173.192.88.66/api/iphone/login/");
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
			Log.v(TAG, "Login form get: " + response.getStatusLine());
            if (entity != null) {
            	entity.consumeContent();
            }
			this._cookies = this.mClient.getCookieStore().getCookies();
            if (this._cookies.isEmpty()) {
                Log.v(TAG, "Empty");
            } else {
            	logMyCookies();
            	result = true;
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
	 
	 * @param	url	the url of appfirst public API	
	 * @return a list of {@link Server} object. 
	 */
	public List<Server> getServerList(String url) {
		JSONArray jsonArray = new JSONArray();
		HttpGet getRequest = new HttpGet(url);
		this.mEncodedAuthString = String.format("Basic %s", 
				Base64.encodeToString(this.mAuthString, Base64.DEFAULT).trim());
		getRequest.setHeader(this.mAuthName, this.mEncodedAuthString);
		try {
			HttpResponse response = this.mClient.execute(getRequest);
			Log.i(TAG, response.getStatusLine().toString());
			HttpEntity entity = response.getEntity();
            if (entity != null) {
            	InputStream instream = entity.getContent();
                String result = Helper.convertStreamToString(instream);
                jsonArray = new JSONArray(result);
                instream.close();
            }
		} catch (ClientProtocolException cpe) {
			cpe.printStackTrace();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		} catch(JSONException je) {
			je.printStackTrace();
		}
		
		return Helper.convertServerList(jsonArray);
	}
	
	/**
	 * Gets the capacity data for a server. 
	 * @param url the url for the query
	 * @return a {@link Server} object. 
	 */
	public Server getServer(String url) {
		JSONObject jsonObject = null;
		HttpGet getRequest = new HttpGet(url);
		this.mEncodedAuthString = String.format("Basic %s", 
				Base64.encodeToString(this.mAuthString, Base64.DEFAULT).trim());
		getRequest.setHeader(this.mAuthName, this.mEncodedAuthString);
		try {
			HttpResponse response = this.mClient.execute(getRequest);
			Log.i(TAG, response.getStatusLine().toString());
			HttpEntity entity = response.getEntity();
            if (entity != null) {
            	InputStream instream = entity.getContent();
                String result = Helper.convertStreamToString(instream);
                jsonObject = new JSONObject(result);
                instream.close();
            }
		} catch (ClientProtocolException cpe) {
			cpe.printStackTrace();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		} catch(JSONException je) {
			je.printStackTrace();
		}
		
		return new Server(jsonObject);
	}
	
	public List<SystemData> getServerData(String url) {
		JSONArray jsonArray = null;
		
		HttpGet getRequest = new HttpGet(url);
		this.mEncodedAuthString = String.format("Basic %s", 
				Base64.encodeToString(this.mAuthString, Base64.DEFAULT).trim());
		getRequest.setHeader(this.mAuthName, this.mEncodedAuthString);
		try {
			HttpResponse response = this.mClient.execute(getRequest);
			Log.i(TAG, response.getStatusLine().toString());
			HttpEntity entity = response.getEntity();
            if (entity != null) {
            	InputStream instream = entity.getContent();
                String result = Helper.convertStreamToString(instream);
                jsonArray = new JSONArray(result);
                instream.close();
            }
		} catch (ClientProtocolException cpe) {
			cpe.printStackTrace();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		} catch(JSONException je) {
			je.printStackTrace();
		}
		
		return Helper.convertServerDataList(jsonArray);
		
	}
	
	private void logMyCookies() {
		for (int i = 0; i < this._cookies.size(); i++) {
            Log.v(TAG, "- " + this._cookies.get(i).toString());
        }
	}

}
