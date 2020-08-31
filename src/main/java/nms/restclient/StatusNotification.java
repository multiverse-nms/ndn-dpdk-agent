package nms.restclient;

import io.vertx.core.json.JsonObject;

public class StatusNotification implements Notification {

	private String id;
	private String timestamp;
	private Status status;

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
		return new JsonObject()
				.put("id", id)
				.put("timestamp", timestamp)
				.put("status", status.getStatus());
	}

}