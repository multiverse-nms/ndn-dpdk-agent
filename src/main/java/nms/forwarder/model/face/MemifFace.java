package nms.forwarder.model.face;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.vertx.core.json.JsonObject;

public class MemifFace extends Face {

	@JsonProperty("Locator")
	private MemifLocator locator;

	public MemifLocator getLocator() {
		return locator;
	}

	public void setLocator(MemifLocator locator) {
		this.locator = locator;
	}

	@Override
	public JsonObject toJsonObject() {
		JsonObject json = new JsonObject().put("id", locator.getId()).put("scheme", locator.getScheme())
				.put("socketName", locator.getsocketName());
		return new JsonObject().put("id", getId()).put("Locator", json);
	}

}
