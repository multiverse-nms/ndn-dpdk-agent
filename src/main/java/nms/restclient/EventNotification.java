package nms.restclient;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import io.vertx.core.json.JsonObject;

public class EventNotification implements Notification {
	private String id;
	private String timestamp;
	private int code;
	private Severity severity;
	private String message;
	
	public EventNotification() {
		this.id = UUID.randomUUID().toString();
		this.timestamp = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
		
	}
	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTimestamp() {
		// TODO Auto-generated method stub
		return null;
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
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(int code) {
		this.code = code;
	}

	/**
	 * @return the severity
	 */
	public Severity getSeverity() {
		return severity;
	}

	/**
	 * @param severity the severity to set
	 */
	public void setSeverity(Severity severity) {
		this.severity = severity;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public NotificationType getType() {
		// TODO Auto-generated method stub
		return NotificationType.EVENT;
	}

	@Override
	public JsonObject toJsonObject() {	
		return new JsonObject()
				.put("id", id)
				.put("timestamp", timestamp)
				.put("code", code)
				.put("severity", severity.getSeverity())
				.put("message", message);
	}

}
