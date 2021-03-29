package nms.forwarder.model.face;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MemifLocator {

	@JsonProperty("id")
	private Integer id;

	@JsonProperty("socketName")
	private String socketName;

	@JsonProperty("scheme")
	private String scheme;

	@JsonIgnore
	private Integer dataroom;

	@JsonIgnore
	private Integer ringCapacity;

	public Integer getDataroom() {
		return dataroom;
	}

	public void setDataroom(Integer dataroom) {
		this.dataroom = dataroom;
	}

	public Integer getRingCapacity() {
		return ringCapacity;
	}

	public void setRingCapacity(Integer ringCapacity) {
		this.ringCapacity = ringCapacity;
	}

	public Integer getId() {
		return id;
	}

	public String getScheme() {
		return scheme;
	}

	public String getsocketName() {
		return socketName;
	}

}
