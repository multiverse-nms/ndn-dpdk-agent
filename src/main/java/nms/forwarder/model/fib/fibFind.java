package nms.forwarder.model.fib;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.vertx.core.json.JsonObject;

public class fibFind {
	@JsonProperty("HasEntry")
	private Boolean hasEntry;
	@JsonProperty("Name")
	private String name;
	@JsonProperty("Nexthops")
	private List<Integer> nexthops = null;
	@JsonProperty("StrategyId")
	private Integer strategyId;

	public JsonObject toJsonObject() {
		return new JsonObject().put("hasEntry", hasEntry).put("name", name).put("Nexthops", nexthops).put("strategyId",
				strategyId);
	}

	public String toString() {
		return this.toJsonObject().encodePrettily();
	}

}