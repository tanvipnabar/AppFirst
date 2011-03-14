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
import java.util.List;

/**
 * <p>
 * Mapping between Alert object in Java Class and JSON object from AppFirst public API. 
 * </p>
 * @author Bin Liu
 * <p>
 * Each Alert object should have following field.
 * <br> id (read-only)	Unique id for the alert in our system
 * <br> active	Boolean indicating whether the alert is active
 * <br> name (read-only)	The name for this alert
 * <br> type (read-only)	The type of this alert
 * <br> target (read-only)	The id of the target of this alert
 * <br> target_uri (read-only)	The URI of the target of this alert
 * <br> trigger (read-only)	The trigger of this alert
 * <br> last_triggered (read-only)	The last time this alert was triggered
 * <br> in_incident (read-only)	Boolean indicating whether the alert is currently in an incident
 * <br> subscribers	The REST API subscribers that will be called when this alert triggers or 
 * resolved. When an alert is triggered or resolved, we will call this API with a GET (default) or 
 * POST request and url encoded data like:
 * alert_id=<ALERT_ID>&alert_history_id=<ID>&status=<TRIGGERED or RESOLVED>.
 * So if you provide a subscriber of {"url":"http://example.com/"}, when an alert triggers we 
 * would make a GET request to something like:
 * http://example.com/?alert_id=5&alert_history_id=65&status=TRIGGERED
 * <br> resource_uri (read-only)	The URI to get more information about this item
 * </p>
 */
public class Alert extends BaseObject{
	
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getTarget() {
		return target;
	}
	public void setTarget(int target) {
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
	public boolean isIn_incident() {
		return in_incident;
	}
	public void setIn_incident(boolean inIncident) {
		in_incident = inIncident;
	}
	public List<Subscriber> getSubscribers() {
		return subscribers;
	}
	public void setSubscribers(List<Subscriber> subscribers) {
		this.subscribers = subscribers;
	}
	private boolean active;
	private String type;
	private int target;
	private URI target_uri;
	private String trigger;
	private int last_triggered;
	private boolean in_incident;
	private List<Subscriber> subscribers;
}
