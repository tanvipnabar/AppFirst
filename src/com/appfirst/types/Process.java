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

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * @author Bin Liu
 * 
 *         Mapping between Process object in Java and JSON object in AppFirst
 *         Public API.
 *         <p>
 *         Each process will have the following fields. <br>
 *         id (read-only) Unique id for the process in our system <br>
 *         start (read-only) The time the process was created <br>
 *         end (read-only) The time the process stopped, null if still running <br>
 *         name (read-only) The name for this process <br>
 *         pid (read-only) The operating system assigned process id (commonly
 *         called pid) for this process <br>
 *         args (read-only) The command line args used when starting this
 *         process <br>
 *         resource_uri (read-only) The URI to get more information about this
 *         item
 *         </p>
 */
public class Process extends BaseObject {

	/**
	 * Default constructor.
	 */
	public Process() {

	}

	/**
	 * Create a {@link Process} object from json object.
	 * 
	 * @param process
	 *            JSON object
	 */
	public Process(JSONObject process) {
		super(process);
		try {
			setResource_uri(new URI(process.getString("resource_uri")));
			setStart(process.getLong("start"));
			setPid(process.getInt("pid"));
			setArgs(process.getString("args"));
			Object end = process.get("end");
			if (end != null && end.toString() != "null") {
				setEnd(Long.parseLong(end.toString()));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public long getEnd() {
		return end;
	}

	public void setEnd(long end) {
		this.end = end;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public String getArgs() {
		return args;
	}

	public void setArgs(String args) {
		this.args = args;
	}

	private long start;
	private long end;
	private int pid;
	private String args;
}
