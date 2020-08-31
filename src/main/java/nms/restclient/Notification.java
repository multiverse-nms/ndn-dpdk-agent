package nms.restclient;

import io.vertx.core.json.JsonObject;

public interface Notification {
	public String getId();
	public String getTimestamp();
	public NotificationType getType();
	
	public JsonObject toJsonObject();

}
