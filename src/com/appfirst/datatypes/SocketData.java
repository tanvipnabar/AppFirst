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
	public long getTime_open() {
		return time_open;
	}
	public void setTime_open(long timeOpen) {
		time_open = timeOpen;
	}
	private String status;
	private String process_name;
	private int peer_port;
	private String peer_ip;
	private int socket_port;
	private String socket_ip;
	private long data_received;
	private long data_sent;
	private String type;
	private long time_open;
}
