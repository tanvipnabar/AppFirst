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
 *  time	The minute this data is for.
	num_info	The number of INFO log messages in this time period.
	num_warning	The number of WARNING log messages in this time period.
	num_critical	The number of CRITICAL log messages in this time period.
 * </p>
 * @author Bin Liu
 *
 */
public class LogData2 extends BaseResourceData {
	private long time;
	private int num_info;
	private int num_warning;
	private int num_critical;

	
	public LogData2(JSONObject dataObject) {
		time = BaseResourceData.getLongField("time", dataObject);
		num_info = BaseResourceData.getIntField("num_info", dataObject);
		num_warning = BaseResourceData.getIntField("num_warning", dataObject);
		num_critical = BaseResourceData.getIntField("num_critical", dataObject);
	}
	
	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getNum_info() {
		return num_info;
	}

	public void setNum_info(int num_info) {
		this.num_info = num_info;
	}

	public int getNum_warning() {
		return num_warning;
	}

	public void setNum_warning(int num_warning) {
		this.num_warning = num_warning;
	}

	public int getNum_critical() {
		return num_critical;
	}

	public void setNum_critical(int num_critical) {
		this.num_critical = num_critical;
	}

	public LogData2() {
		
	}
}
