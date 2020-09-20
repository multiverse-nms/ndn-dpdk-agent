package nms.restclient;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import io.vertx.core.json.JsonObject;

public class FaultNotification implements Notification {

	private String id;
	private String timestamp;
	private int code;
	private String message;
	
	
	public FaultNotification() {
		this.id = UUID.randomUUID().toString();
		this.timestamp = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
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
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
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
		return NotificationType.FAULT;
	}

	@Override
	public JsonObject toJsonObject() {
		return new JsonObject()
				.put("id", id)
				.put("timestamp", timestamp)
				.put("code", code)
				.put("message", message);
	}

}
