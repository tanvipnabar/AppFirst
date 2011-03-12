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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

import android.util.Log;
import android.util.Base64;


/**
 * Client lib for accessing AppFirst Monitoring public API. 
 * @author Bin Liu
 *
 */
public class AFClient {
	private DefaultHttpClient _client;/* the actual http client */
	private String TAG = "AFHttpClient";/* tag for logging */
	private List<Cookie> _cookies;/* keep cookies after login */
	private String _encodedAuthString;/* the key for access the public API */
	private String _authName = "Authorization";
	private byte[] _authString = "YOUR_EMAIL:YOUR_API_KEY".getBytes();
	/**
	 * My default constructor. 
	 */
	public AFClient() {
		AFHttpClient afHttpClient = new AFHttpClient();
		this._client = afHttpClient.getAFHttpClient();
	}

	/**
	 * User log in with this function. 
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
			HttpResponse response = this._client.execute(post);
			entity = response.getEntity();
			Log.v(TAG, "Login form get: " + response.getStatusLine());
            if (entity != null) {
            	entity.consumeContent();
            }
			this._cookies = this._client.getCookieStore().getCookies();
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
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public Boolean getApiKey(String username, String password) {
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
			HttpResponse response = this._client.execute(post);
			entity = response.getEntity();
			Log.v(TAG, "Login form get: " + response.getStatusLine());
            if (entity != null) {
            	entity.consumeContent();
            }
			this._cookies = this._client.getCookieStore().getCookies();
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
	 * 
	 * @param String url
	 * @return JSONString an array of json object, each is the data of a server. 
	 */
	public JSONArray getServerList(String url) {
		JSONArray json = new JSONArray();
		HttpGet getRequest = new HttpGet(url);
		this._encodedAuthString = Base64.encodeToString(this._authString, Base64.DEFAULT);
		getRequest.setHeader(this._authName, "Basic " + this._encodedAuthString.trim());
		try {
			HttpResponse response = this._client.execute(getRequest);
			Log.i(TAG, response.getStatusLine().toString());
			HttpEntity entity = response.getEntity();
            if (entity != null) {
            	InputStream instream = entity.getContent();
                String result = convertStreamToString(instream);
                json = new JSONArray(result);
                instream.close();
            }
		} catch (ClientProtocolException cpe) {
			cpe.printStackTrace();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		} catch(JSONException je) {
			je.printStackTrace();
		}
		return json;
	}
	
	private void logMyCookies() {
		for (int i = 0; i < this._cookies.size(); i++) {
            Log.v(TAG, "- " + this._cookies.get(i).toString());
        }
	}
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
}
