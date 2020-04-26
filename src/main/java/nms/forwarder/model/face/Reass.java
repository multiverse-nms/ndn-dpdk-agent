package nms.forwarder.model.face;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "Accepted", "OutOfOrder", "Delivered", "Incomplete" })
public class Reass {

	@JsonProperty("Accepted")
	private Integer accepted;
	@JsonProperty("OutOfOrder")
	private Integer outOfOrder;
	@JsonProperty("Delivered")
	private Integer delivered;
	@JsonProperty("Incomplete")
	private Integer incomplete;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("Accepted")
	public Integer getAccepted() {
		return accepted;
	}

	@JsonProperty("Accepted")
	public void setAccepted(Integer accepted) {
		this.accepted = accepted;
	}

	@JsonProperty("OutOfOrder")
	public Integer getOutOfOrder() {
		return outOfOrder;
	}

	@JsonProperty("OutOfOrder")
	public void setOutOfOrder(Integer outOfOrder) {
		this.outOfOrder = outOfOrder;
	}

	@JsonProperty("Delivered")
	public Integer getDelivered() {
		return delivered;
	}

	@JsonProperty("Delivered")
	public void setDelivered(Integer delivered) {
		this.delivered = delivered;
	}

	@JsonProperty("Incomplete")
	public Integer getIncomplete() {
		return incomplete;
	}

	@JsonProperty("Incomplete")
	public void setIncomplete(Integer incomplete) {
		this.incomplete = incomplete;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}