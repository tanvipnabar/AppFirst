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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Server data at a certain time. 
 * @author Bin Liu
 * <p>
 * <br> time	The minute this data is for.
 * <br> cpu	The CPU value of this server in percent.
 * <br> memory	The memory usage of this server in bytes.
 * <br> disk	A mapping of hard disk drive names (mount points) to their current free space in megabytes.
 * <br> disk_percent	The total hard disk usage in percent.
 * <br> page_faults	The number of page faults of the server.
 * <br> process_num	The number of processes running on the server.
 * <br> thread_num	The number of threads running on the server.
 * </p>
 *  
 */
public class SystemData extends BaseResourceData{
	
	/**
	 * @param jsonObject
	 */
	public SystemData(JSONObject jsonObject) {
		// TODO Auto-generated constructor stub
		try {
			setTime(jsonObject.getInt("time"));
			setCpu(jsonObject.getDouble("cpu"));
			setMemory(jsonObject.getDouble("memory"));
			setThread_num(jsonObject.getInt("thread_num"));
			setProcess_num(jsonObject.getInt("thread_num"));
			setPage_faults(jsonObject.getInt("page_faults"));
			setDisk_percent(jsonObject.getDouble("disk_percent"));
			JSONObject disks = jsonObject.getJSONObject("disk");
			List<BasicNameValuePair> diskValues = new ArrayList<BasicNameValuePair>();
			for (int i = 0; i < disks.names().length(); i++) {
				String name = disks.names().getString(i);
				String value = String.format("%d", disks.getInt(name));
				diskValues.add(new BasicNameValuePair(name, value));
			}
			setDisk(diskValues);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
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
	private double cpu;
	private double memory;
	private List<BasicNameValuePair> disk;
	private double disk_percent;
	private int page_faults;
	private int process_num;
	private int thread_num;
}
