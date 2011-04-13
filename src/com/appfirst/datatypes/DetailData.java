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
 * Detail data at a certain time.
 * 
 * @author Bin Liu
 *         <p>
 *         Each of detail object may have the following data field. <br>
 *         time The time this data is for. <br>
 *         files A list of files accessed. <br>
 *         registries A list of registries accessed. <br>
 *         threads A list of threads in the process. <br>
 *         sockets A list of sockets. <br>
 *         logs A list of incident reports.
 *         </p>
 */
public class DetailData extends BaseResourceData {
	/*
	 * Default constructor.
	 */
	public DetailData() {

	}

	/**
	 * @param detailData
	 *            the data object in {@link JSONObject}
	 */
	public DetailData(JSONObject detailData) {
		setTime(BaseResourceData.getIntField("time", detailData));
		setFiles(BaseResourceData.getJSONArrayField("files", detailData));
		setLogs(BaseResourceData.getJSONArrayField("logs", detailData));
		setSockets(BaseResourceData.getJSONArrayField("sockets", detailData));
		setRegistries(BaseResourceData.getJSONArrayField("registries",
				detailData));
		setThreads(BaseResourceData.getJSONArrayField("threads", detailData));
	}

	public List<FileData> getFiles() {
		return files;
	}

	public void setFiles(List<FileData> files) {
		this.files = files;
	}

	public void setFiles(JSONArray files) {
		this.files = new ArrayList<FileData>();
		for (int cnt = 0; cnt < files.length(); cnt++) {
			if (cnt > 50)
				break;
			try {
				this.files.add(new FileData(files.getJSONObject(cnt)));
			} catch (JSONException je) {
				je.printStackTrace();
			}
		}
	}

	public List<RegistryData> getRegistries() {
		return registries;
	}

	public void setRegistries(List<RegistryData> registries) {
		this.registries = registries;
	}

	public void setRegistries(JSONArray registries) {
		this.registries = new ArrayList<RegistryData>();
		for (int cnt = 0; cnt < registries.length(); cnt++) {
			try {
				this.registries.add(new RegistryData(registries
						.getJSONObject(cnt)));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public List<ThreadData> getThreads() {
		return threads;
	}

	public void setThreads(List<ThreadData> threads) {
		this.threads = threads;
	}

	public void setThreads(JSONArray threads) {
		this.threads = new ArrayList<ThreadData>();
		for (int cnt = 0; cnt < threads.length(); cnt++) {
			if (cnt > 50)
				break;
			try {
				this.threads.add(new ThreadData(threads.getJSONObject(cnt)));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public List<SocketData> getSockets() {
		return sockets;
	}

	public void setSockets(List<SocketData> sockets) {
		this.sockets = sockets;
	}

	public void setSockets(JSONArray sockets) {
		this.sockets = new ArrayList<SocketData>();
		for (int cnt = 0; cnt < sockets.length(); cnt++) {
			if (cnt > 50)
				break;
			try {
				this.sockets.add(new SocketData(sockets.getJSONObject(cnt)));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public List<LogData> getLogs() {
		return logs;
	}

	public void setLogs(List<LogData> logs) {
		this.logs = logs;
	}

	public void setLogs(JSONArray logs) {
		this.logs = new ArrayList<LogData>();
		for (int cnt = 0; cnt < logs.length(); cnt++) {
			if (cnt > 50)
				break;
			try {
				this.logs.add(new LogData(logs.getJSONObject(cnt)));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public Boolean isEmpty() {
		Boolean ret = true;

		if (files != null && files.size() > 0) {
			ret = false;
		}

		if (logs != null && logs.size() > 0) {
			ret = false;
		}

		if (registries != null && registries.size() > 0) {
			ret = false;
		}

		if (sockets != null && sockets.size() > 0) {
			ret = false;
		}

		if (threads != null && threads.size() > 0) {
			ret = false;
		}

		return ret;
	}

	private List<FileData> files;
	private List<RegistryData> registries;
	private List<ThreadData> threads;
	private List<SocketData> sockets;
	private List<LogData> logs;
}
