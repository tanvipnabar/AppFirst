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
 * Mapping between AlertHistory object in Java and JSON object in AppFirst
 * Public API.
 * 
 * @author Bin Liu
 * 
 *         <p>
 *         Each Alert History object should have the following field. <br>
 *         id (read-only) Unique id for the application in our system <br>
 *         start (read-only) The start time of this alert history incident <br>
 *         end (read-only) The end time of this alert history incident, will be
 *         null if still in the incident <br>
 *         alert (read-only) The id of the alert this triggered on <br>
 *         subject (read-only) The subject of the alert email that was sent out.
 *         <br>
 *         message_uri (read-only) The URI of the saved email message that was
 *         generated when the alert triggered <br>
 *         resource_uri (read-only) The URI to get more information about this
 *         item
 *         </p>
 */
public class AlertHistory extends BaseObject {

	/**
	 * @param jsonObject
	 */
	public AlertHistory(JSONObject jsonObject) {
		// TODO Auto-generated constructor stub
		super(jsonObject);
		message_uri = BaseObject.getURIField("message_uri", jsonObject);
		start = BaseObject.getIntField("start", jsonObject);
		end = BaseObject.getIntField("end", jsonObject);
		alert = BaseObject.getIntField("alert", jsonObject);
		subject = BaseObject.getStringField("subject", jsonObject);
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public int getAlert() {
		return alert;
	}

	public void setAlert(int alert) {
		this.alert = alert;
	}

	public URI getMessage_uri() {
		return message_uri;
	}

	public void setMessage_uri(URI messageUri) {
		message_uri = messageUri;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject
	 *            the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	private int start;
	private int end;
	private int alert;
	private URI message_uri;
	private String subject;

}
