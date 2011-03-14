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

import java.util.List;

/**
 * Detail data at a certain time. 
 * @author Bin Liu
 * <p>
 * Each of detail object may have the following data field. 
 * <br> time	The time this data is for.
 * <br> files	A list of files accessed.
 * <br> registries	A list of registries accessed.
 * <br> threads	A list of threads in the process.
 * <br> sockets	A list of sockets.
 * <br> logs	A list of incident reports.
 * </p>
 */
public class DetailData extends BaseResourceData{
	
	public List<FileData> getFiles() {
		return files;
	}
	public void setFiles(List<FileData> files) {
		this.files = files;
	}
	public List<RegistryData> getRegistries() {
		return registries;
	}
	public void setRegistries(List<RegistryData> registries) {
		this.registries = registries;
	}
	public List<ThreadData> getThreads() {
		return threads;
	}
	public void setThreads(List<ThreadData> threads) {
		this.threads = threads;
	}
	public List<SocketData> getSockets() {
		return sockets;
	}
	public void setSockets(List<SocketData> sockets) {
		this.sockets = sockets;
	}
	public List<LogData> getLogs() {
		return logs;
	}
	public void setLogs(List<LogData> logs) {
		this.logs = logs;
	}
	private List<FileData> files;
	private List<RegistryData> registries;
	private List<ThreadData> threads;
	private List<SocketData> sockets;
	private List<LogData> logs;
}
