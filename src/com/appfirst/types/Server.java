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
 *         Mapping between Server object in Java and JSON object in AppFirst
 *         Public API.
 *         <p>
 *         Each of the server object should have the following fields. <br>
 *         id (read-only) Unique server id <br>
 *         created (read-only) The time the server was created in UTC seconds <br>
 *         hostname (read-only) The hostname for this server <br>
 *         os (read-only) The OS for this server, either Windows or Linux. <br>
 *         running (read-only) Boolean indicating whether the server is
 *         currently uploading data to AppFirst <br>
 *         resource_uri (read-only) The URI to get more information about this
 *         item <br>
 *         capacity_mem (read-only) The total available memory on the server in
 *         bytes <br>
 *         capacity_cpu_num (read-only) The number of CPU cores on the system <br>
 *         capacity_cpu_freq (read-only) The frequency of the CPU in MHz <br>
 *         capacity_disks (read-only) Mapping of the names of the disks (mount
 *         points) to their capacity in MB
 *         </p>
 */
public class Server extends BaseObject {
	public Server() {

	}

	/**
	 * @param jsonObject
	 */
	public Server(JSONObject jsonObject) {
		super(jsonObject);
		// TODO Auto-generated constructor stub
		try {
			hostname = BaseObject.getStringField("hostname", jsonObject);
			capacity_cpu_freq = BaseObject.getIntField("capacity_cpu_freq", jsonObject);
			created = BaseObject.getLongField("created", jsonObject);
			os = BaseObject.getStringField("os", jsonObject);
			running = BaseObject.getBooleanField("running", jsonObject);
			capacity_mem = BaseObject.getLongField("capacity_mem", jsonObject);
			capacity_cpu_num = BaseObject.getIntField("capacity_cpu_num", jsonObject);
			JSONObject disks = jsonObject.getJSONObject("capacity_disks");
			ArrayList<NameValuePair> diskValues = new ArrayList<NameValuePair>();
			for (int i = 0; i < disks.names().length(); i++) {
				String name = disks.names().getString(i);
				String value = String.format("%d", disks.getLong(name));
				diskValues.add(new BasicNameValuePair(name, value));
				totalDisk += disks.getLong(name);
			}
			setCapacity_disks(diskValues);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public Long getCreated() {
		return created;
	}

	public void setCreated(Long created) {
		this.created = created;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public long getCapacity_mem() {
		return capacity_mem;
	}

	public void setCapacity_mem(long capacityMem) {
		capacity_mem = capacityMem;
	}

	public int getCapacity_cpu_num() {
		return capacity_cpu_num;
	}

	public void setCapacity_cpu_num(int capacityCpuNum) {
		capacity_cpu_num = capacityCpuNum;
	}

	public int getCapacity_cpu_freq() {
		return capacity_cpu_freq;
	}

	public void setCapacity_cpu_freq(int capacityCpuFreq) {
		capacity_cpu_freq = capacityCpuFreq;
	}

	public List<NameValuePair> getCapacity_disks() {
		return capacity_disks;
	}

	public void setCapacity_disks(List<NameValuePair> capacityDisks) {
		capacity_disks = capacityDisks;
	}

	/**
	 * @return the running
	 */
	public Boolean getRunning() {
		return running;
	}

	/**
	 * @param running the running to set
	 */
	public void setRunning(Boolean running) {
		this.running = running;
	}

	/**
	 * @return the os
	 */
	public String getOs() {
		return os;
	}

	/**
	 * @param os the os to set
	 */
	public void setOs(String os) {
		this.os = os;
	}
	
	private Long created;
	private String hostname;
	private Boolean running;
	private String os;
	private long capacity_mem;
	private int capacity_cpu_num;
	private int capacity_cpu_freq;
	private List<NameValuePair> capacity_disks;
	private Long totalDisk = 0L;

	public Long getTotalDisk() {
		return totalDisk;
	}

	public void setTotalDisk(Long totalDisk) {
		this.totalDisk = totalDisk;
	}
}
