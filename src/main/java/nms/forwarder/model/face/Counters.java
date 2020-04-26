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
@JsonPropertyOrder({ "RxFrames", "RxOctets", "L2DecodeErrs", "Reass", "L3DecodeErrs", "RxInterests", "RxData",
		"RxNacks", "FragGood", "FragBad", "TxAllocErrs", "TxQueued", "TxDropped", "TxInterests", "TxData", "TxNacks",
		"TxFrames", "TxOctets" })
public class Counters {

	@JsonProperty("RxFrames")
	private Long rxFrames;
	@JsonProperty("RxOctets")
	private Long rxOctets;
	@JsonProperty("L2DecodeErrs")
	private Integer l2DecodeErrs;
	@JsonProperty("Reass")
	private Reass reass;
	@JsonProperty("L3DecodeErrs")
	private Integer l3DecodeErrs;
	@JsonProperty("RxInterests")
	private Long rxInterests;
	@JsonProperty("RxData")
	private Long rxData;
	@JsonProperty("RxNacks")
	private Long rxNacks;
	@JsonProperty("FragGood")
	private Long fragGood;
	@JsonProperty("FragBad")
	private Long fragBad;
	@JsonProperty("TxAllocErrs")
	private Long txAllocErrs;
	@JsonProperty("TxQueued")
	private Long txQueued;
	@JsonProperty("TxDropped")
	private Long txDropped;
	@JsonProperty("TxInterests")
	private Long txInterests;
	@JsonProperty("TxData")
	private Long txData;
	@JsonProperty("TxNacks")
	private Long txNacks;
	@JsonProperty("TxFrames")
	private Long txFrames;
	@JsonProperty("TxOctets")
	private Long txOctets;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("RxFrames")
	public Long getRxFrames() {
		return rxFrames;
	}

	@JsonProperty("RxFrames")
	public void setRxFrames(Long rxFrames) {
		this.rxFrames = rxFrames;
	}

	@JsonProperty("RxOctets")
	public Long getRxOctets() {
		return rxOctets;
	}

	@JsonProperty("RxOctets")
	public void setRxOctets(Long rxOctets) {
		this.rxOctets = rxOctets;
	}

	@JsonProperty("L2DecodeErrs")
	public Integer getL2DecodeErrs() {
		return l2DecodeErrs;
	}

	@JsonProperty("L2DecodeErrs")
	public void setL2DecodeErrs(Integer l2DecodeErrs) {
		this.l2DecodeErrs = l2DecodeErrs;
	}

	@JsonProperty("Reass")
	public Reass getReass() {
		return reass;
	}

	@JsonProperty("Reass")
	public void setReass(Reass reass) {
		this.reass = reass;
	}

	@JsonProperty("L3DecodeErrs")
	public Integer getL3DecodeErrs() {
		return l3DecodeErrs;
	}

	@JsonProperty("L3DecodeErrs")
	public void setL3DecodeErrs(Integer l3DecodeErrs) {
		this.l3DecodeErrs = l3DecodeErrs;
	}

	@JsonProperty("RxInterests")
	public Long getRxInterests() {
		return rxInterests;
	}

	@JsonProperty("RxInterests")
	public void setRxInterests(Long rxInterests) {
		this.rxInterests = rxInterests;
	}

	@JsonProperty("RxData")
	public Long getRxData() {
		return rxData;
	}

	@JsonProperty("RxData")
	public void setRxData(Long rxData) {
		this.rxData = rxData;
	}

	@JsonProperty("RxNacks")
	public Long getRxNacks() {
		return rxNacks;
	}

	@JsonProperty("RxNacks")
	public void setRxNacks(Long rxNacks) {
		this.rxNacks = rxNacks;
	}

	@JsonProperty("FragGood")
	public Long getFragGood() {
		return fragGood;
	}

	@JsonProperty("FragGood")
	public void setFragGood(Long fragGood) {
		this.fragGood = fragGood;
	}

	@JsonProperty("FragBad")
	public Long getFragBad() {
		return fragBad;
	}

	@JsonProperty("FragBad")
	public void setFragBad(Long fragBad) {
		this.fragBad = fragBad;
	}

	@JsonProperty("TxAllocErrs")
	public Long getTxAllocErrs() {
		return txAllocErrs;
	}

	@JsonProperty("TxAllocErrs")
	public void setTxAllocErrs(Long txAllocErrs) {
		this.txAllocErrs = txAllocErrs;
	}

	@JsonProperty("TxQueued")
	public Long getTxQueued() {
		return txQueued;
	}

	@JsonProperty("TxQueued")
	public void setTxQueued(Long txQueued) {
		this.txQueued = txQueued;
	}

	@JsonProperty("TxDropped")
	public Long getTxDropped() {
		return txDropped;
	}

	@JsonProperty("TxDropped")
	public void setTxDropped(Long txDropped) {
		this.txDropped = txDropped;
	}

	@JsonProperty("TxInterests")
	public Long getTxInterests() {
		return txInterests;
	}

	@JsonProperty("TxInterests")
	public void setTxInterests(Long txInterests) {
		this.txInterests = txInterests;
	}

	@JsonProperty("TxData")
	public Long getTxData() {
		return txData;
	}

	@JsonProperty("TxData")
	public void setTxData(Long txData) {
		this.txData = txData;
	}

	@JsonProperty("TxNacks")
	public Long getTxNacks() {
		return txNacks;
	}

	@JsonProperty("TxNacks")
	public void setTxNacks(Long txNacks) {
		this.txNacks = txNacks;
	}

	@JsonProperty("TxFrames")
	public Long getTxFrames() {
		return txFrames;
	}

	@JsonProperty("TxFrames")
	public void setTxFrames(Long txFrames) {
		this.txFrames = txFrames;
	}

	@JsonProperty("TxOctets")
	public Long getTxOctets() {
		return txOctets;
	}

	@JsonProperty("TxOctets")
	public void setTxOctets(Long txOctets) {
		this.txOctets = txOctets;
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

