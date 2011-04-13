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

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Server data at a certain time.
 * 
 * @author Bin Liu
 *         <p>
 *         <br>
 *         time The minute this data is for. <br>
 *         cpu The CPU value of this server in percent. <br>
 *         memory The memory usage of this server in bytes. <br>
 *         disk A mapping of hard disk drive names (mount points) to their
 *         current free space in megabytes. <br>
 *         disk_percent The total hard disk usage in percent. <br>
 *         page_faults The number of page faults of the server. <br>
 *         process_num The number of processes running on the server. <br>
 *         thread_num The number of threads running on the server. <br>
 *         disk_percent_part A mapping of hard disk drive names (mount points)
 *         to their current usage in percent.<br>
 *         disk_busy	A mapping of each physcial disk name to its busy percentage.<br>
 *         cpu_cores	A mapping of each CPU core to its usage in percent. <br>
 *         </p>
 * 
 */
public class SystemData extends BaseResourceData {

	/**
	 * @param jsonObject
	 *            a system data object in {@link JSONObject}.
	 */
	public SystemData(JSONObject systemData) {
		// TODO Auto-generated constructor stub
		setTime(BaseResourceData.getIntField("time", systemData));
		cpu = BaseResourceData.getDoubleField("cpu", systemData);
		memory = BaseResourceData.getDoubleField("memory", systemData);
		thread_num = BaseResourceData.getIntField("thread_num", systemData);
		process_num = BaseResourceData.getIntField("process_num", systemData);
		page_faults = BaseResourceData.getIntField("page_faults", systemData);
		disk_percent = BaseResourceData.getDoubleField("disk_percent",
				systemData);

		disk = getListData(systemData, "disk");
		cpu_cores = getListData(systemData, "cpu_cores");
		disk_busy = getListData(systemData, "disk_busy");
		disk_percent_part = getListData(systemData, "disk_percent_part");
	}

	private List<BasicNameValuePair> getListData(JSONObject systemData,
			String fieldName) {
		List<BasicNameValuePair> parsedObjects = new ArrayList<BasicNameValuePair>();
		try {
			JSONObject objects = systemData.getJSONObject(fieldName);
			for (int i = 0; i < objects.names().length(); i++) {
				String name = objects.names().getString(i);
				String value = String.format("%f", objects.getDouble(name));
				parsedObjects.add(new BasicNameValuePair(name, value));
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return parsedObjects;
	}

	/*
	 * Default constructor.
	 */
	public SystemData() {

	}

	public double getCpu() {
		return cpu;
	}

	public void setCpu(double cpu) {
		this.cpu = cpu;
	}

	public double getMemory() {
		return memory;
	}

	public void setMemory(double memory) {
		this.memory = memory;
	}

	public List<BasicNameValuePair> getDisk() {
		return disk;
	}

	public void setDisk(List<BasicNameValuePair> disk) {
		this.disk = disk;
	}

	public double getDisk_percent() {
		return disk_percent;
	}

	public void setDisk_percent(double diskPercent) {
		disk_percent = diskPercent;
	}

	public int getPage_faults() {
		return page_faults;
	}

	public void setPage_faults(int pageFaults) {
		page_faults = pageFaults;
	}

	public int getProcess_num() {
		return process_num;
	}

	public void setProcess_num(int processNum) {
		process_num = processNum;
	}

	public int getThread_num() {
		return thread_num;
	}

	public void setThread_num(int threadNum) {
		thread_num = threadNum;
	}

	public List<BasicNameValuePair> getDisk_busy() {
		return disk_busy;
	}

	public void setDisk_busy(List<BasicNameValuePair> diskBusy) {
		disk_busy = diskBusy;
	}

	public List<BasicNameValuePair> getDisk_percent_part() {
		return disk_percent_part;
	}

	public void setDisk_percent_part(List<BasicNameValuePair> diskPercentPart) {
		disk_percent_part = diskPercentPart;
	}

	public List<BasicNameValuePair> getCpu_cores() {
		return cpu_cores;
	}

	public void setCpu_cores(List<BasicNameValuePair> cpuCores) {
		cpu_cores = cpuCores;
	}

	private double cpu;
	private double memory;
	private List<BasicNameValuePair> disk;
	private double disk_percent;
	private int page_faults;
	private int process_num;
	private int thread_num;
	private List<BasicNameValuePair> disk_busy;
	private List<BasicNameValuePair> disk_percent_part;
	private List<BasicNameValuePair> cpu_cores;
}
