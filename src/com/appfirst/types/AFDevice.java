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

import org.json.JSONObject;

/**
 * Mapping of the device object in the database. 
 * <p>
 * 	brand (read-only)	The type of this device: either iOS or Android. <br>
 *  uid (read-only)	The unique id/token for this device given by Apple/Google in their push notification documentation. <br>
 *  badge	The number of unread alert messages this device has. <br>
 *  last_notified (read-only)	The last time this device was sent a push notification. <br>
 *  last_set_uid (read-only)	The last time this device updated it's uid. <br>
 *  resource_uri (read-only)	The URI to get more information about this item <br>
 * </p>
 * @author Bin Liu
 *
 */
public class AFDevice extends BaseObject {
	
	public AFDevice(JSONObject dataObject) {
		super(dataObject);
		brand = BaseObject.getStringField("brand", dataObject);
		uid = BaseObject.getStringField("uid", dataObject);
		badge = BaseObject.getLongField("badge", dataObject);
		last_notified = BaseObject.getLongField("last_notified", dataObject);
		last_set_uid = BaseObject.getStringField("last_set_uid", dataObject);
	}
	
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public Long getBadge() {
		return badge;
	}
	public void setBadge(Long badge) {
		this.badge = badge;
	}
	public Long getLast_notified() {
		return last_notified;
	}
	public void setLast_notified(Long lastNotified) {
		last_notified = lastNotified;
	}
	public String getLast_set_uid() {
		return last_set_uid;
	}
	public void setLast_set_uid(String lastSetUid) {
		last_set_uid = lastSetUid;
	}
	public Boolean getSubscribe_all() {
		return subscribe_all;
	}
	public void setSubscribe_all(Boolean subscribeAll) {
		subscribe_all = subscribeAll;
	}
	
	private String brand;
	private String uid;
	private Long badge;
	private Long last_notified;
	private String last_set_uid;
	private Boolean subscribe_all;
}
