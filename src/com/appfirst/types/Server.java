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
import org.json.JSONArray;
import org.json.JSONObject;


/**
 * 
 * @author Bin Liu
 *
 * Mapping between Server object in Java and JSON object in AppFirst Public API.  
 * <p> 
 * Each of the server object should have the following fields. 
 * <br>id (read-only)	Unique server id
 * <br>created (read-only)	The time the server was created in UTC seconds
 * <br>hostname (read-only)	The hostname for this server
 * <br>resource_uri (read-only)	The URI to get more information about this item
 * <br>capacity_mem (read-only)	The total available memory on the server in bytes
 * <br>capacity_cpu_num (read-only)	The number of CPU cores on the system
 * <br>capacity_cpu_freq (read-only)	The frequency of the CPU in MHz
 * <br>capacity_disks (read-only)	Mapping of the names of the disks (mount points) 
 * to their capacity in MB
 * </p>
 */
public class Server extends BaseObject{
	public Server() {
		
	}
	
	/**
	 * @param jsonObject
	 */
	public Server(JSONObject jsonObject) {
		// TODO Auto-generated constructor stub
		try {
			setHostname(jsonObject.getString("hostname"));
			setId(jsonObject.getInt("id"));
			setCapacity_cpu_freq(jsonObject.getInt("capacity_cpu_freq"));
			setCreated(jsonObject.getInt("created"));
			setCapacity_mem(jsonObject.getInt("capacity_mem"));
			setCapacity_cpu_num(jsonObject.getInt("capacity_cpu_num"));
			setResource_uri(URI.create(jsonObject.getString("resource_uri")));
			JSONObject disks =  jsonObject.getJSONObject("capacity_disks");
			ArrayList<NameValuePair> diskValues = new ArrayList<NameValuePair>();
			for (int i = 0; i < disks.names().length(); i ++) {
				String name = disks.names().getString(i);
				String value = String.format("%d",disks.getInt(name));
				diskValues.add(new BasicNameValuePair(name, value));
			}
			setCapacity_disks(diskValues);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public int getCreated() {
		return created;
	}
	public void setCreated(int created) {
		this.created = created;
	}
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public int getCapacity_mem() {
		return capacity_mem;
	}
	public void setCapacity_mem(int capacityMem) {
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
	private int created;
	private String hostname;
	private int capacity_mem;
	private int capacity_cpu_num;
	private int capacity_cpu_freq;
	private List<NameValuePair> capacity_disks; 
}
