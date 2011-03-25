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

import java.lang.reflect.Field;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Bin Liu
 * 
 * <br>Example:
 * <code>
 *    "User Time (us)":1486058684791,
 *   "Create Time":1443109011965,
 *   "Kernel Time (us)":1211180777596,
 *   "Exit Time":2555505541592,
 *   "Stack Size (bytes)":4948,
 *   "Thread ID":55
 * </code>
 *
 */
public class ThreadData {
	
	/**
	 * @param jsonObject
	 */
	public ThreadData(JSONObject jsonObject) {
		// TODO Auto-generated constructor stub
		user_time = BaseResourceData.getLongField("User Time (us)", jsonObject);
		create_time = BaseResourceData.getStringField("Create Time", jsonObject);
		kernel_time= BaseResourceData.getLongField("Kernel Time (us)", jsonObject);
		exit_time = BaseResourceData.getStringField("Exit Time", jsonObject);
		stack_size = BaseResourceData.getLongField("Stack Size (bytes)", jsonObject);
		id = BaseResourceData.getLongField("Thread ID", jsonObject);
	}
	public long getUser_time() {
		return user_time;
	}
	public void setUser_time(long userTime) {
		user_time = userTime;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String createTime) {
		create_time = createTime;
	}
	public long getKernel_time() {
		return kernel_time;
	}
	public void setKernel_time(long kernelTime) {
		kernel_time = kernelTime;
	}
	public String getExit_time() {
		return exit_time;
	}
	public void setExit_time(String exitTime) {
		exit_time = exitTime;
	}
	public long getStack_size() {
		return stack_size;
	}
	public void setStack_size(long stackSize) {
		stack_size = stackSize;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	private long user_time; 
	private String create_time;
	private long kernel_time;
	private String exit_time;
	private long stack_size;
	private long id;
}
