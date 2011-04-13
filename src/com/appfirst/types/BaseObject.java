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
package com.appfirst.types;

import java.net.URI;
import java.net.URISyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Base object for any other types. 
 * Map from JSON type to JAVA object. 
 * @author Bin Liu
 *
 */
public class BaseObject {
	public BaseObject() {
		
	}
	/**
	 * @param jsonObject
	 */
	public BaseObject(JSONObject jsonObject) {
		// TODO Auto-generated constructor stub
		id = BaseObject.getIntField("id", jsonObject);
		name = BaseObject.getStringField("name", jsonObject);
		resource_uri = BaseObject.getURIField("resource_uri", jsonObject);
	}

	/**
	 * 
	 * @param field
	 *            the name of field in a {@link JSONObject}.
	 * @param dataObject
	 *            a {@link JSONObject}
	 * @return an Integer
	 */
	public static Integer getIntField(String field, JSONObject dataObject) {
		Integer ret = 0;
		if (dataObject == null || !dataObject.has(field)) {
			return ret;
		}
		try {
			Object data = dataObject.get(field);
			if (data != null && data.toString() != "null") {
				ret = Integer.parseInt(data.toString());
			}
		} catch (JSONException je) {
			je.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * 
	 * @param field
	 *            the name of field in a {@link JSONObject}.
	 * @param dataObject
	 *            a {@link JSONObject}
	 * @return a Boolean
	 */
	public static Boolean getBooleanField(String field, JSONObject dataObject) {
		Boolean ret = false;
		if (dataObject == null || !dataObject.has(field)) {
			return ret;
		}
		try {
			Object data = dataObject.get(field);
			if (data != null && data.toString() != "null") {
				ret = Boolean.parseBoolean(data.toString());
			}
		} catch (JSONException je) {
			je.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * 
	 * @param field
	 *            the name of field in a {@link JSONObject}.
	 * @param dataObject
	 *            a {@link JSONObject}
	 * @return a Long
	 */
	public static Long getLongField(String field, JSONObject dataObject) {
		Long ret = 0L;
		if (dataObject == null || !dataObject.has(field)) {
			return ret;
		}
		try {
			Object data = dataObject.get(field);
			if (data != null && data.toString() != "null") {
				ret = Long.parseLong(data.toString());
			}
		} catch (JSONException je) {
			je.printStackTrace();
		}
		return ret;
	}

	/**
	 * 
	 * @param field
	 *            the name of field in a {@link JSONObject}.
	 * @param dataObject
	 *            a {@link JSONObject}
	 * @return a double
	 */
	public static double getDoubleField(String field, JSONObject dataObject) {
		double ret = Double.NaN;
		if (dataObject == null || !dataObject.has(field)) {
			return ret;
		}
		try {
			Object data = dataObject.get(field);
			if (data != null && data.toString() != "null") {
				ret = Double.parseDouble(data.toString());
			}
		} catch (JSONException je) {
			je.printStackTrace();
		}
		return ret;
	}

	/**
	 * 
	 * @param field
	 *            the name of field in a {@link JSONObject}.
	 * @param dataObject
	 *            a {@link JSONObject}
	 * @return a String
	 */
	public static String getStringField(String field, JSONObject dataObject) {
		String ret = "";
		if (dataObject == null || !dataObject.has(field)) {
			return ret;
		}
		try {
			Object data = dataObject.get(field);
			if (data != null && data != "null") {
				ret = data.toString();
			}
		} catch (JSONException je) {
			je.printStackTrace();
		}
		return ret;
	}

	/**
	 * 
	 * @param field
	 *            the name of field in a {@link JSONObject}.
	 * @param dataObject
	 *            a {@link JSONObject}
	 * @return a {@link JSONObject}
	 */
	public static JSONObject getJSONObjectField(String field,
			JSONObject dataObject) {
		JSONObject jsonObject = new JSONObject();
		if (dataObject == null || !dataObject.has(field)) {
			return jsonObject;
		}
		try {
			Object object = dataObject.get(field);
			if (object.toString() != "null")
				jsonObject = dataObject.getJSONObject(field);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
	/**
	 * 
	 * @param field
	 *            the name of field in a {@link JSONObject}.
	 * @param dataObject
	 *            a {@link JSONObject}
	 * @return a {@link JSONArray}
	 */
	public static JSONArray getJSONArrayField(String field,
			JSONObject dataObject) {
		JSONArray jsonArray = new JSONArray();
		if (dataObject == null || !dataObject.has(field)) {
			return jsonArray;
		}
		try {
			Object object = dataObject.get(field);
			if (object.toString() != "null")
				jsonArray = dataObject.getJSONArray(field);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonArray;
	}
	/**
	 * 
	 * @param field
	 *            the name of field in a {@link JSONObject}.
	 * @param dataObject
	 *            a {@link JSONObject}
	 * @return a String
	 */
	public static URI getURIField(String field, JSONObject dataObject) {
		URI ret = null;
		if (dataObject == null || !dataObject.has(field)) {
			return ret;
		}
		try {
			Object data = dataObject.get(field);
			if (data != null && data != "null") {
				ret = new URI(data.toString());
			}
		} catch (JSONException je) {
			je.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}
	
	private int id;
	private String name;
	private URI resource_uri;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public URI getResource_uri() {
		return resource_uri;
	}
	public void setResource_uri(URI resourceUri) {
		resource_uri = resourceUri;
	}
}
