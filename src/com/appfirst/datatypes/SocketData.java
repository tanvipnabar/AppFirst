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
 * @author Bin Liu
 * <br>Example:
 * <code>
 * "Status":"Closed",
 * "Process Name":"procname:pid",
 * "Peer Port":2093,
 * "Peer IP":"51.146.225.231",
 * "Data Received (bytes)":32,
 * "Data Sent (bytes)":16,
 * "Socket IP":"161.80.233.244",
 * "Thread Id":69,
 * "Socket Port":7168,
 * "Type":"TCP",
 * "Time Open (sec)":455
 * </code>
 */
public class SocketData {
	
	/**
	 * @param jsonObject
	 */
	public SocketData(JSONObject jsonObject) {
		// TODO Auto-generated constructor stub
		status = BaseResourceData.getStringField(getFieldFullName("status"), jsonObject);
		process_name = BaseResourceData.getStringField(getFieldFullName("process_name"), jsonObject);
		type = BaseResourceData.getStringField(getFieldFullName("type"), jsonObject);
		peer_ip = BaseResourceData.getStringField(getFieldFullName("peer_ip"), jsonObject);
		socket_ip = BaseResourceData.getStringField(getFieldFullName("socket_ip"), jsonObject);
		socket_port = BaseResourceData.getIntField(getFieldFullName("socket_port"), jsonObject);
		peer_port = BaseResourceData.getIntField(getFieldFullName("peer_port"), jsonObject);
		data_received = BaseResourceData.getIntField(getFieldFullName("data_received"), jsonObject);
		data_sent = BaseResourceData.getIntField(getFieldFullName("data_sent"), jsonObject);
		thread_id = BaseResourceData.getIntField(getFieldFullName("thread_id"), jsonObject);
		time_open = BaseResourceData.getDoubleField(getFieldFullName("time_open"), jsonObject);
	}
	
	public static String getFieldFullName(String field) {
		if (field == "status") {
			return "Status";
		} else if (field == "process_name") {
			return "Process Name";
		} else if (field == "peer_port") {
			return "Peer Port";
		} else if (field == "peer_ip") {
			return "Peer IP";
		} else if (field == "socket_port") {
			return "Socket Port";
		} else if (field == "socket_ip") {
			return "Socket IP";
		} else if (field == "data_received") {
			return "Data Received (bytes)";
		} else if (field == "data_sent") {
			return "Data Sent (bytes)";
		} else if (field == "type") {
			return "Type";
		} else if (field == "time_open") {
			return "Time Open (sec)";
		} else if (field == "thread_id") {
			return "Thread Id";
		} else {
			return "";
		}
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getProcess_name() {
		return process_name;
	}
	public void setProcess_name(String processName) {
		process_name = processName;
	}
	public int getPeer_port() {
		return peer_port;
	}
	public void setPeer_port(int peerPort) {
		peer_port = peerPort;
	}
	public String getPeer_ip() {
		return peer_ip;
	}
	public void setPeer_ip(String peerIp) {
		peer_ip = peerIp;
	}
	public int getSocket_port() {
		return socket_port;
	}
	public void setSocket_port(int socketPort) {
		socket_port = socketPort;
	}
	public String getSocket_ip() {
		return socket_ip;
	}
	public void setSocket_ip(String socketIp) {
		socket_ip = socketIp;
	}
	public long getData_received() {
		return data_received;
	}
	public void setData_received(long dataReceived) {
		data_received = dataReceived;
	}
	public long getData_sent() {
		return data_sent;
	}
	public void setData_sent(long dataSent) {
		data_sent = dataSent;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public double getTime_open() {
		return time_open;
	}
	public void setTime_open(double timeOpen) {
		time_open = timeOpen;
	}
	public int getThread_id() {
		return thread_id;
	}
	public void setThread_id(int threadId) {
		thread_id = threadId;
	}

	private String status;
	private String process_name;
	private int peer_port;
	private int thread_id;
	private String peer_ip;
	private int socket_port;
	private String socket_ip;
	private long data_received;
	private long data_sent;
	private String type;
	private double time_open;
}
