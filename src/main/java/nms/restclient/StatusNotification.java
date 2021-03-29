package nms.restclient;

import java.sql.Timestamp;

import java.util.UUID;

import io.vertx.core.json.JsonObject;

public class StatusNotification implements Notification {

	private String id;
	private String timestamp;
	private Status status;

	public StatusNotification(Status status) {
		this.id = newId();
		this.timestamp = now();
		this.status = status;
	}

	private String newId() {
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	private String now() {
		return new Timestamp(System.currentTimeMillis()).toInstant().toString();
	}
	
	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return this.id;
	}

	@Override
	public String getTimestamp() {
		// TODO Auto-generated method stub
		return this.timestamp;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Status status) {
		this.status = status;
	}

	@Override
	public NotificationType getType() {
		// TODO Auto-generated method stub
		return NotificationType.STATUS;
	}

	@Override
	public JsonObject toJsonObject() {
		return new JsonObject().put("timestamp", timestamp).put("status", status.getStatus());
	}

}