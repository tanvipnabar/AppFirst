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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.appfirst.datatypes.FileData;

/**
 * <p>
 * Mapping between Alert object in Java Class and JSON object from AppFirst
 * public API.
 * </p>
 * 
 * @author Bin Liu
 *         <p>
 *         Each Alert object should have following field. <br>
 *         id (read-only) Unique id for the alert in our system <br>
 *         active Boolean indicating whether the alert is active <br>
 *         name (read-only) The name for this alert <br>
 *         type (read-only) The type of this alert <br>
 *         target (read-only) The id of the target of this alert <br>
 *         target_uri (read-only) The URI of the target of this alert <br>
 *         trigger (read-only) The trigger of this alert <br>
 *         last_triggered (read-only) The last time this alert was triggered <br>
 *         in_incident (read-only) Boolean indicating whether the alert is
 *         currently in an incident <br>
 *         subscribers The REST API subscribers that will be called when this
 *         alert triggers or resolved. When an alert is triggered or resolved,
 *         we will call this API with a GET (default) or POST request and url
 *         encoded data like:
 *         alert_id=<ALERT_ID>&alert_history_id=<ID>&status=<TRIGGERED or
 *         RESOLVED>. So if you provide a subscriber of
 *         {"url":"http://example.com/"}, when an alert triggers we would make a
 *         GET request to something like:
 *         http://example.com/?alert_id=5&alert_history_id=65&status=TRIGGERED <br>
 *         resource_uri (read-only) The URI to get more information about this
 *         item
 *         </p>
 */
public class Alert extends BaseObject {
	/**
	 * @param jsonObject
	 */
	public Alert(JSONObject jsonObject) {
		super(jsonObject);
		active = BaseObject.getBooleanField("active", jsonObject);
		in_incident = BaseObject.getBooleanField("in_incident", jsonObject);
		last_triggered = BaseObject.getIntField("last_triggered", jsonObject);
		target = BaseObject.getStringField("target", jsonObject);
		type = BaseObject.getStringField("type", jsonObject);
		trigger = BaseObject.getStringField("trigger", jsonObject);
		target_uri = BaseObject.getURIField("target_uri", jsonObject);
		setSubscribers(BaseObject.getJSONArrayField("subscribers", jsonObject));
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public URI getTarget_uri() {
		return target_uri;
	}

	public void setTarget_uri(URI targetUri) {
		target_uri = targetUri;
	}

	public String getTrigger() {
		return trigger;
	}

	public void setTrigger(String trigger) {
		this.trigger = trigger;
	}

	public int getLast_triggered() {
		return last_triggered;
	}

	public void setLast_triggered(int lastTriggered) {
		last_triggered = lastTriggered;
	}

	public Boolean getIn_incident() {
		return in_incident;
	}

	public void setIn_incident(Boolean inIncident) {
		in_incident = inIncident;
	}

	public List<Subscriber> getSubscribers() {
		return subscribers;
	}

	public void setSubscribers(List<Subscriber> subscribers) {
		this.subscribers = subscribers;
	}

	public void setSubscribers(JSONArray subscribers) {
		this.subscribers = new ArrayList<Subscriber>();
		for (int cnt = 0; cnt < subscribers.length(); cnt++) {
			try {
				this.subscribers.add(new Subscriber(subscribers
						.getJSONObject(cnt)));
			} catch (JSONException je) {
				je.printStackTrace();
			}
		}
		;
	}

	private Boolean active;
	private String type;
	private String target;
	private URI target_uri;
	private String trigger;
	private int last_triggered;
	private Boolean in_incident;
	private List<Subscriber> subscribers;
}
