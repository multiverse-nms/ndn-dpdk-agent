package nms.forwarder.model.port;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.vertx.core.json.JsonObject;

public class PortInfo {

	private boolean active;
	private String implName;
	private String name;
	private String numaSocket;

	public PortInfo() {

	}

	@JsonProperty("Active")
	public boolean isActive() {
		return active;
	}

	@JsonProperty("Active")
	public void setActive(boolean active) {
		this.active = active;
	}

	@JsonProperty("ImplName")
	public String getImplName() {
		return implName;
	}

	@JsonProperty("ImplName")
	public void setImplName(String implName) {
		this.implName = implName;
	}

	@JsonProperty("Name")
	public String getName() {
		return name;
	}

	@JsonProperty("Name")
	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("NumaSocket")
	public String getNumaSocket() {
		return numaSocket;
	}

	@JsonProperty("NumaSocket")
	public void setNumaSocket(String numaSocket) {
		this.numaSocket = numaSocket;
	}

	public JsonObject toJsonObject() {
		return new JsonObject().put("active", active).put("implName", implName).put("name", name).put("numaSocket",
				numaSocket);
	}
	
	
	public String toString() {
		return this.toJsonObject().toString();
	}
}