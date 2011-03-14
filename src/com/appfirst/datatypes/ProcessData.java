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
	
	public float getCpu() {
		return cpu;
	}
	public void setCpu(float cpu) {
		this.cpu = cpu;
	}
	public float getMemory() {
		return memory;
	}
	public void setMemory(float memory) {
		this.memory = memory;
	}
	public int getPage_faults() {
		return page_faults;
	}
	public void setPage_faults(int pageFaults) {
		page_faults = pageFaults;
	}
	public int getThread_num() {
		return thread_num;
	}
	public void setThread_num(int threadNum) {
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
	public int getSocket_num() {
		return socket_num;
	}
	public void setSocket_num(int socketNum) {
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
	public int getFile_num() {
		return file_num;
	}
	public void setFile_num(int fileNum) {
		file_num = fileNum;
	}
	public int getRegistry_num() {
		return registry_num;
	}
	public void setRegistry_num(int registryNum) {
		registry_num = registryNum;
	}
	public int getCritical_log() {
		return critical_log;
	}
	public void setCritical_log(int criticalLog) {
		critical_log = criticalLog;
	}
	public int getTotal_log() {
		return total_log;
	}
	public void setTotal_log(int totalLog) {
		total_log = totalLog;
	}
	public float getAvg_response_time() {
		return avg_response_time;
	}
	public void setAvg_response_time(float avgResponseTime) {
		avg_response_time = avgResponseTime;
	}
	public int getResponse_num() {
		return response_num;
	}
	public void setResponse_num(int responseNum) {
		response_num = responseNum;
	}
	private float cpu;
	private float memory;
	private int page_faults;
	private int thread_num;
	private long socket_write;
	private long socket_read;
	private int socket_num;
	private long file_write;
	private long file_read;
	private int file_num;
	private int registry_num;
	private int critical_log;
	private int total_log;
	private float avg_response_time;
	private int response_num;
}
