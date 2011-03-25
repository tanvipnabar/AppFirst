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

import org.json.JSONObject;

/**
 * Map to the alert history message which's sent in the email. 
 * The following fields are presented in the message. 
 * <p>
 *  "html": "<table>Email content in html format here</table>",
    "text": "Email content in text format here",
    "sms": "Short content"
 * </p>
 * @author Bin Liu
 *
 */
public class AlertHistoryData extends BaseResourceData {
	public AlertHistoryData() {
		
	}
	
	public AlertHistoryData(JSONObject dataObject) {
		text = BaseResourceData.getStringField("text", dataObject);
		html = BaseResourceData.getStringField("html", dataObject);
		sms = BaseResourceData.getStringField("sms", dataObject);
	}
	
	
	/**
	 * @return the html
	 */
	public String getHtml() {
		return html;
	}
	/**
	 * @param html the html to set
	 */
	public void setHtml(String html) {
		this.html = html;
	}
	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}
	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}
	/**
	 * @return the sms
	 */
	public String getSms() {
		return sms;
	}
	/**
	 * @param sms the sms to set
	 */
	public void setSms(String sms) {
		this.sms = sms;
	}
	
	private String html;
	private String text;
	private String sms;
}
