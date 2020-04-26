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
@JsonPropertyOrder({ "Count", "Min", "Max", "Mean", "Stdev" })
public class Latency {

	@JsonProperty("Count")
	private Integer count;
	@JsonProperty("Min")
	private Integer min;
	@JsonProperty("Max")
	private Integer max;
	@JsonProperty("Mean")
	private Double mean;
	@JsonProperty("Stdev")
	private Double stdev;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("Count")
	public Integer getCount() {
		return count;
	}

	@JsonProperty("Count")
	public void setCount(Integer count) {
		this.count = count;
	}

	@JsonProperty("Min")
	public Integer getMin() {
		return min;
	}

	@JsonProperty("Min")
	public void setMin(Integer min) {
		this.min = min;
	}

	@JsonProperty("Max")
	public Integer getMax() {
		return max;
	}

	@JsonProperty("Max")
	public void setMax(Integer max) {
		this.max = max;
	}

	@JsonProperty("Mean")
	public Double getMean() {
		return mean;
	}

	@JsonProperty("Mean")
	public void setMean(Double mean) {
		this.mean = mean;
	}

	@JsonProperty("Stdev")
	public Double getStdev() {
		return stdev;
	}

	@JsonProperty("Stdev")
	public void setStdev(Double stdev) {
		this.stdev = stdev;
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

