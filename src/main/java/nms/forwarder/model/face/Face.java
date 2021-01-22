package nms.forwarder.model.face;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.vertx.core.json.JsonObject;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Face {

	Integer id;

	@JsonProperty("Locator")
	Object locator;

	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("Id")
	public Integer getId() {
		return id;
	}

	@JsonProperty("Id")
	public void setId(Integer id) {
		this.id = id;
	}

	public JsonObject toJsonObject() {
		return new JsonObject().put("faceId", id).put("Locator", locator);
	}

	public String toString() {
		return this.toJsonObject().encodePrettily();
	}
}
