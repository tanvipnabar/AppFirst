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
 * Process data at a certain minute. 
 * @author Bin Liu
 *
 * <p>
 * <br> time	The minute this data is for.
 * <br> cpu	The CPU value in percent.
 * <br> memory	The memory usage in bytes.
 * <br> page_faults	The number of page faults.
 * <br> thread_num	The number of threads.
 * <br> socket_write	Outbound network traffic in bytes.
 * <br> socket_read	Inbound network traffic in bytes.
 * <br> socket_num	The number of network connections.
 * <br> file_read	Data written to files in bytes.
 * <br> file_write	Data read from files in bytes.
 * <br> file_num	The number of files accessed.
 * <br> registry_num	The number of registries accessed.
 * <br> critical_log	The number of critical incident reports logged.
 * <br> total_log	The number of total incident reports logged.
 * <br> avg_response_time	The average response time of socket transactions in microseconds.
 * <br> response_num	The number of socket responses sent (you can have multiple responses on
 * a single network/socket connection).
 * </p>
 */
public class ProcessData extends BaseResourceData{
	/**
	 * Default constructor. 
	 */
	public ProcessData() {
		
	}
	
	/**
	 * 
	 * @param processData a process data object in {@link JSONObject}. 
	 */
	public ProcessData(JSONObject processData) {
		cpu = BaseResourceData.getDoubleField("cpu", processData);
		memory = BaseResourceData.getLongField("memory", processData);
		avg_response_time = BaseResourceData.getDoubleField("avg_response_time", processData);
		thread_num = BaseResourceData.getLongField("thread_num", processData);
		page_faults = BaseResourceData.getLongField("page_faults", processData);
		socket_write = BaseResourceData.getLongField("socket_write", processData);
		socket_read = BaseResourceData.getLongField("socket_read", processData);
		response_num = BaseResourceData.getLongField("response_num", processData);
		total_log = BaseResourceData.getLongField("total_log", processData);
		registry_num = BaseResourceData.getLongField("registry_num", processData);
		socket_num = BaseResourceData.getLongField("socket_num", processData);
		critical_log = BaseResourceData.getLongField("critical_log", processData);
		file_read = BaseResourceData.getLongField("file_read", processData);
		file_write = BaseResourceData.getLongField("file_write", processData);
		file_num = BaseResourceData.getLongField("file_num", processData);
		setTime(BaseResourceData.getIntField("time", processData));
	}
	public double getCpu() {
		return cpu;
	}
	public void setCpu(double cpu) {
		this.cpu = cpu;
	}
	public long getMemory() {
		return memory;
	}
	public void setMemory(long memory) {
		this.memory = memory;
	}
	public long getPage_faults() {
		return page_faults;
	}
	public void setPage_faults(long pageFaults) {
		page_faults = pageFaults;
	}
	public long getThread_num() {
		return thread_num;
	}
	public void setThread_num(long threadNum) {
		thread_num = threadNum;
	}
	public long getSocket_write() {
		return socket_write;
	}
	public void setSocket_write(long socketWrite) {
		socket_write = socketWrite;
	}
	public long getSocket_read() {
		return socket_read;
	}
	public void setSocket_read(long socketRead) {
		socket_read = socketRead;
	}
	public long getSocket_num() {
		return socket_num;
	}
	public void setSocket_num(long socketNum) {
		socket_num = socketNum;
	}
	public long getFile_write() {
		return file_write;
	}
	public void setFile_write(long fileWrite) {
		file_write = fileWrite;
	}
	public long getFile_read() {
		return file_read;
	}
	public void setFile_read(long fileRead) {
		file_read = fileRead;
	}
	public long getFile_num() {
		return file_num;
	}
	public void setFile_num(long fileNum) {
		file_num = fileNum;
	}
	public long getRegistry_num() {
		return registry_num;
	}
	public void setRegistry_num(long registryNum) {
		registry_num = registryNum;
	}
	public long getCritical_log() {
		return critical_log;
	}
	public void setCritical_log(long criticalLog) {
		critical_log = criticalLog;
	}
	public long getTotal_log() {
		return total_log;
	}
	public void setTotal_log(long totalLog) {
		total_log = totalLog;
	}
	public double getAvg_response_time() {
		return avg_response_time;
	}
	public void setAvg_response_time(double avgResponseTime) {
		avg_response_time = avgResponseTime;
	}
	public long getResponse_num() {
		return response_num;
	}
	public void setResponse_num(long responseNum) {
		response_num = responseNum;
	}
	private double cpu;
	private long memory;
	private long page_faults;
	private long thread_num;
	private long socket_write;
	private long socket_read;
	private long socket_num;
	private long file_write;
	private long file_read;
	private long file_num;
	private long registry_num;
	private long critical_log;
	private long total_log;
	private double avg_response_time;
	private long response_num;
}
