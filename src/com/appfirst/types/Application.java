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
 * Mapping between Application object in Java and JSON object in AppFirst Public API.
 * @author Bin Liu
 * 
 * <p>
 * Each of Application object will contains the following field. 
 * <br>id (read-only)	Unique id for the application in our system
 * <br>created (read-only)	The time the applications was created
 * <br>name (read-only)	The name for this application
 * <br>resource_uri (read-only)	The URI to get more information about this item
 * </p>
 */
public class Application extends BaseObject {
	private int created;
	
	/**
	 * @param jsonObject
	 */
	public Application(JSONObject jsonObject) {
		// TODO Auto-generated constructor stub
		super(jsonObject);
		created = BaseObject.getIntField("created", jsonObject);
	}

	public int getCreated() {
		return created;
	}

	public void setCreated(int created) {
		this.created = created;
	}
	
}
