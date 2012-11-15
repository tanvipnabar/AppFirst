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
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @author Bin Liu
 * 
 *         Mapping between Log object in Java and JSON object in AppFirst
 *         Public API.
 *         <p>
 *         Each of the server object should have the following fields. <br>
 *         id (read-only)	Unique id for the log in our system. <br>
 *         server	The id of the server this log is on. <br>
 *         type	The type of the log, one of: EVENTLOG, SYSLOG, FILE. <br>
 *         critical (optional)	For FILE type logs, a string that is used to label a log message as CRITICAL. 
 *         If both warning and critical match, the message is labeled CRITICAL. Default: None. <br>
 *         running (read-only) Boolean indicating whether the server is
 *         currently uploading data to AppFirst <br>
 *         resource_uri (read-only) The URI to get more information about this
 *         item <br>
 *         filter (optional)	Only include log messages that match this filter regular expression. 
 *         If not given, all messages are included. Default: Empty <br>
 *         </p>
 */
public class Log extends BaseObject {
	private int serverId;
	private String critical;
	private String filter;
	private String source;
	private String type;

	/**
	 * @param jsonObject
	 */
	public Log(JSONObject jsonObject) {
		super(jsonObject);
		// TODO Auto-generated constructor stub
		try {
			type = BaseObject.getStringField("type", jsonObject);
			serverId = BaseObject.getIntField("server", jsonObject);
			critical = BaseObject.getStringField("critical", jsonObject);
			filter = BaseObject.getStringField("filter", jsonObject);
			source = BaseObject.getStringField("source", jsonObject);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public String getCritical() {
		return critical;
	}

	public void setCritical(String critical) {
		this.critical = critical;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
