package nms.forwarder.model.pit;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.vertx.core.json.JsonObject;

public class DpGlobal {

	@JsonProperty("NInputs")
	private Integer nInputs;
	@JsonProperty("NFwds")
	private Integer nFwds;

	@JsonProperty("NInputs")
	public Integer getNInputs() {
		return nInputs;
	}

	@JsonProperty("NInputs")
	public void setNInputs(Integer nInputs) {
		this.nInputs = nInputs;
	}

	@JsonProperty("NFwds")
	public Integer getNFwds() {
		return nFwds;
	}

	@JsonProperty("NFwds")
	public void setNFwds(Integer nFwds) {
		this.nFwds = nFwds;
	}

	public JsonObject toJsonObject() {
		return new JsonObject().put("nInputs", nInputs).put("nFwds", nFwds);
	}

	public String toString() {
		return this.toJsonObject().encodePrettily();
	}

}
