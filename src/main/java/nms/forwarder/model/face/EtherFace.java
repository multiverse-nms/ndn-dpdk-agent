package nms.forwarder.model.face;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.vertx.core.json.JsonObject;

public class EtherFace extends Face {

	@JsonProperty("Locator")
	private EtherLocator locator;

	public EtherLocator getLocator() {
		return locator;
	}

	public void setLocator(EtherLocator locator) {
		this.locator = locator;
	}

	@Override
	public JsonObject toJsonObject() {
		JsonObject json = new JsonObject().put("scheme", locator.getScheme()).put("port", locator.getPort())
				.put("local", locator.getLocal()).put("remote", locator.getRemote());
		return new JsonObject().put("id", getId()).put("Locator", json);
	}

}
