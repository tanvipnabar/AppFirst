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

import org.json.JSONObject;

/**
 * 
 * <p>
 * Mapping between Polled Data object in Java Class and JSON object in AppFirst public API.
 * </p>
 * @author Bin Liu
 * <p>
 * Each Polled data object should have the following field. 
 * <br> id (read-only)	Unique id for the polled data item in our system
 * <br> name (read-only)	The name for this polled data item
 * <br> server (read-only)	The id of the server this item is running on
 * <br> server_uri (read-only)	The URI of the server this item is running on
 * <br> alert (read-only)	The id of the alert on this item
 * <br> alert_uri (read-only)	The URI for the alert on this item
 * <br> resource_uri (read-only)	The URI to get more information about this item
 * </p>
 */
public class PolledDataObject extends BaseObject{
	/**
	 * @param jsonObject
	 */
	public PolledDataObject(JSONObject jsonObject) {
		super(jsonObject);
		server = BaseObject.getIntField("server", jsonObject);
		server_uri = BaseObject.getURIField("server_uri", jsonObject);
		alert_uri = BaseObject.getURIField("alert_uri",jsonObject);
		alert = BaseObject.getIntField("alert", jsonObject);
		// TODO Auto-generated constructor stub
	}
	public int getServer() {
		return server;
	}
	public void setServer(int server) {
		this.server = server;
	}
	public URI getServer_uri() {
		return server_uri;
	}
	public void setServer_uri(URI serverUri) {
		server_uri = serverUri;
	}
	public int getAlert() {
		return alert;
	}
	public void setAlert(int alert) {
		this.alert = alert;
	}
	public URI getAlert_uri() {
		return alert_uri;
	}
	public void setAlert_uri(URI alertUri) {
		alert_uri = alertUri;
	}
	private int server;
	private URI server_uri;
	private int alert;
	private URI alert_uri;
}
