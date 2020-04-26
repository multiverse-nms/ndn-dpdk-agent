package nms.forwarder.model.face;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

public @JsonInclude(JsonInclude.Include.NON_NULL) @JsonPropertyOrder({ "Ipackets", "Opackets", "Ibytes", "Obytes",
		"Imissed", "Ierrors", "Oerrors", "Rx_nombuf", "Q_ipackets", "Q_opackets", "Q_ibytes", "Q_obytes",
		"Q_errors" }) class ExCounters {

	@JsonProperty("Ipackets")
	private Long ipackets;
	@JsonProperty("Opackets")
	private Long opackets;
	@JsonProperty("Ibytes")
	private Long ibytes;
	@JsonProperty("Obytes")
	private Long obytes;
	@JsonProperty("Imissed")
	private Integer imissed;
	@JsonProperty("Ierrors")
	private Integer ierrors;
	@JsonProperty("Oerrors")
	private Integer oerrors;
	@JsonProperty("Rx_nombuf")
	private Integer rxNombuf;
	@JsonProperty("Q_ipackets")
	private List<Long> qIpackets = null;
	@JsonProperty("Q_opackets")
	private List<Long> qOpackets = null;
	@JsonProperty("Q_ibytes")
	private List<Long> qIbytes = null;
	@JsonProperty("Q_obytes")
	private List<Long> qObytes = null;
	@JsonProperty("Q_errors")
	private List<Long> qErrors = null;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("Ipackets")
	public Long getIpackets() {
		return ipackets;
	}

	@JsonProperty("Ipackets")
	public void setIpackets(Long ipackets) {
		this.ipackets = ipackets;
	}

	@JsonProperty("Opackets")
	public Long getOpackets() {
		return opackets;
	}

	@JsonProperty("Opackets")
	public void setOpackets(Long opackets) {
		this.opackets = opackets;
	}

	@JsonProperty("Ibytes")
	public Long getIbytes() {
		return ibytes;
	}

	@JsonProperty("Ibytes")
	public void setIbytes(Long ibytes) {
		this.ibytes = ibytes;
	}

	@JsonProperty("Obytes")
	public Long getObytes() {
		return obytes;
	}

	@JsonProperty("Obytes")
	public void setObytes(Long obytes) {
		this.obytes = obytes;
	}

	@JsonProperty("Imissed")
	public Integer getImissed() {
		return imissed;
	}

	@JsonProperty("Imissed")
	public void setImissed(Integer imissed) {
		this.imissed = imissed;
	}

	@JsonProperty("Ierrors")
	public Integer getIerrors() {
		return ierrors;
	}

	@JsonProperty("Ierrors")
	public void setIerrors(Integer ierrors) {
		this.ierrors = ierrors;
	}

	@JsonProperty("Oerrors")
	public Integer getOerrors() {
		return oerrors;
	}

	@JsonProperty("Oerrors")
	public void setOerrors(Integer oerrors) {
		this.oerrors = oerrors;
	}

	@JsonProperty("Rx_nombuf")
	public Integer getRxNombuf() {
		return rxNombuf;
	}

	@JsonProperty("Rx_nombuf")
	public void setRxNombuf(Integer rxNombuf) {
		this.rxNombuf = rxNombuf;
	}

	@JsonProperty("Q_ipackets")
	public List<Long> getQIpackets() {
		return qIpackets;
	}

	@JsonProperty("Q_ipackets")
	public void setQIpackets(List<Long> qIpackets) {
		this.qIpackets = qIpackets;
	}

	@JsonProperty("Q_opackets")
	public List<Long> getQOpackets() {
		return qOpackets;
	}

	@JsonProperty("Q_opackets")
	public void setQOpackets(List<Long> qOpackets) {
		this.qOpackets = qOpackets;
	}

	@JsonProperty("Q_ibytes")
	public List<Long> getQIbytes() {
		return qIbytes;
	}

	@JsonProperty("Q_ibytes")
	public void setQIbytes(List<Long> qIbytes) {
		this.qIbytes = qIbytes;
	}

	@JsonProperty("Q_obytes")
	public List<Long> getQObytes() {
		return qObytes;
	}

	@JsonProperty("Q_obytes")
	public void setQObytes(List<Long> qObytes) {
		this.qObytes = qObytes;
	}

	@JsonProperty("Q_errors")
	public List<Long> getQErrors() {
		return qErrors;
	}

	@JsonProperty("Q_errors")
	public void setQErrors(List<Long> qErrors) {
		this.qErrors = qErrors;
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
