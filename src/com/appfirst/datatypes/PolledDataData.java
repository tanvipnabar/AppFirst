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
package com.appfirst.datatypes;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Bin Liu
 *
 */
public class PolledDataData extends BaseResourceData{
	
	/**
	 * @param jsonObject
	 */
	public PolledDataData(JSONObject jsonObject) {
		// TODO Auto-generated constructor stub
		setTime(BaseResourceData.getIntField("time", jsonObject));
		status = BaseResourceData.getStringField("status", jsonObject);
		text = BaseResourceData.getStringField("text", jsonObject);
		values = BaseResourceData.getJSONObjectField("values", jsonObject);
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public JSONObject getValues() {
		return values;
	}
	public void setValues(JSONObject values) {
		this.values = values;
	}
	
	private String status;
	private String text;
	private JSONObject values;
}
