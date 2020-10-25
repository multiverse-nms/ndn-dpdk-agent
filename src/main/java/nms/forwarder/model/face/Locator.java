package nms.forwarder.model.face;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.vertx.core.json.JsonObject;

public class Locator {

	@JsonProperty("scheme")
	private String scheme;
	@JsonProperty("port")
	private String port;
	@JsonProperty("local")
	private String local;
	@JsonProperty("remote")
	private String remote;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("scheme")
	public String getScheme() {
		return scheme;
	}

	public JsonObject toJsonObject() {
		return new JsonObject().put("scheme", scheme).put("port", port).put("local", local).put("remote", remote);
	}

	@JsonProperty("port")
	public String getPort() {
		return port;
	}

	@JsonProperty("local")
	public String getLocal() {
		return local;
	}

	@JsonProperty("remote")
	public String getRemote() {
		return remote;
	}

}