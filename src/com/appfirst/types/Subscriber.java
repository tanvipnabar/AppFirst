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
 * Mapping between Subscriber object in Java Class and JSON object in AppFirst public API.
 * @author Bin Liu
 * 
 */
public class Subscriber extends BaseObject{
	/**
	 * @param jsonObject
	 */
	public Subscriber(JSONObject jsonObject) {
		super();
		auth = BaseObject.getStringField("auth", jsonObject);
		type = BaseObject.getStringField("type", jsonObject);
		event = BaseObject.getStringField("event", jsonObject);
		url = BaseObject.getURIField("url", jsonObject);
		
		// TODO Auto-generated constructor stub
	}
	public URI getUrl() {
		return url;
	}
	public void setUrl(URI url) {
		this.url = url;
	}
	public String getAuth() {
		return auth;
	}
	public void setAuth(String auth) {
		this.auth = auth;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	private URI url;
	private String auth;
	private String type;
	private String event;
}
