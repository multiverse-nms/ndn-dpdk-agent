package nms.forwarder.model.face;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.vertx.core.json.JsonObject;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "Id", "IsDown", "Counters", "ExCounters", "Latency" })
public class FaceData {

	@JsonProperty("Id")
	private Integer id;
	@JsonProperty("IsDown")
	private Boolean isDown;
	@JsonProperty("Counters")
	private Counters counters;
	@JsonProperty("ExCounters")
	private ExCounters exCounters;
	@JsonProperty("Latency")
	private Latency latency;
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

	@JsonProperty("IsDown")
	public Boolean getIsDown() {
		return isDown;
	}

	@JsonProperty("IsDown")
	public void setIsDown(Boolean isDown) {
		this.isDown = isDown;
	}

	@JsonProperty("Counters")
	public Counters getCounters() {
		return counters;
	}

	@JsonProperty("Counters")
	public void setCounters(Counters counters) {
		this.counters = counters;
	}

	@JsonProperty("ExCounters")
	public ExCounters getExCounters() {
		return exCounters;
	}

	@JsonProperty("ExCounters")
	public void setExCounters(ExCounters exCounters) {
		this.exCounters = exCounters;
	}

	@JsonProperty("Latency")
	public Latency getLatency() {
		return latency;
	}

	@JsonProperty("Latency")
	public void setLatency(Latency latency) {
		this.latency = latency;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	public JsonObject toJsonObject() {
		return new JsonObject().put("counters",
				new JsonObject().put("rxFrames", counters.getRxFrames()).put("rxOctets", counters.getRxOctets())
						.put("rxInterests", counters.getRxInterests()).put("rxData", counters.getRxData())
						.put("rxNacks", counters.getRxNacks()).put("txFrames", counters.getTxFrames())
						.put("txOctets", counters.getTxOctets()).put("txInterests", counters.getTxInterests())
						.put("txData", counters.getTxData()).put("txNacks", counters.getTxNacks()));

	}

	public String toString() {
		return this.toJsonObject().encodePrettily();
	}

}