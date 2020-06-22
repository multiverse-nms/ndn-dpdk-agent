package nms.forwarder.model.fib;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.vertx.core.json.JsonObject;

public class FibEntry {

	@JsonProperty("IsNew")
	private boolean isNew;

	
	@JsonProperty("IsNew")
	public boolean isNew() {
		return isNew == true;
	}
	
	public JsonObject toJsonObject() {
		return new JsonObject().put("IsNew", isNew);
	}

	public String toString() {
		return this.toJsonObject().encodePrettily();
	}

}