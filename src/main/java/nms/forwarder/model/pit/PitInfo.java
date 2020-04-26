package nms.forwarder.model.pit;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.vertx.core.json.JsonObject;

public class PitInfo {

	@JsonProperty("NEntries")
	private Integer nEntries;
	@JsonProperty("NInsert")
	private Integer nInsert;
	@JsonProperty("NFound")
	private Integer nFound;
	@JsonProperty("NCsMatch")
	private Integer nCsMatch;
	@JsonProperty("NAllocErr")
	private Integer nAllocErr;
	@JsonProperty("NDataHit")
	private Integer nDataHit;
	@JsonProperty("NDataMiss")
	private Integer nDataMiss;
	@JsonProperty("NNackHit")
	private Integer nNackHit;
	@JsonProperty("NNackMiss")
	private Integer nNackMiss;
	@JsonProperty("NExpired")
	private Integer nExpired;

	public Integer getNEntries() {
		return nEntries;
	}

	@JsonProperty("NInsert")
	public Integer getNInsert() {
		return nInsert;
	}

	@JsonProperty("NFound")
	public Integer getNFound() {
		return nFound;
	}

	@JsonProperty("NFound")
	public void setNFound(Integer nFound) {
		this.nFound = nFound;
	}

	@JsonProperty("NCsMatch")
	public Integer getNCsMatch() {
		return nCsMatch;
	}

	@JsonProperty("NCsMatch")
	public void setNCsMatch(Integer nCsMatch) {
		this.nCsMatch = nCsMatch;
	}

	@JsonProperty("NAllocErr")
	public Integer getNAllocErr() {
		return nAllocErr;
	}

	@JsonProperty("NAllocErr")
	public void setNAllocErr(Integer nAllocErr) {
		this.nAllocErr = nAllocErr;
	}

	@JsonProperty("NDataHit")
	public Integer getNDataHit() {
		return nDataHit;
	}

	@JsonProperty("NDataHit")
	public void setNDataHit(Integer nDataHit) {
		this.nDataHit = nDataHit;
	}

	@JsonProperty("NDataMiss")
	public Integer getNDataMiss() {
		return nDataMiss;
	}

	@JsonProperty("NDataMiss")
	public void setNDataMiss(Integer nDataMiss) {
		this.nDataMiss = nDataMiss;
	}

	@JsonProperty("NNackHit")
	public Integer getNNackHit() {
		return nNackHit;
	}

	@JsonProperty("NNackHit")
	public void setNNackHit(Integer nNackHit) {
		this.nNackHit = nNackHit;
	}

	@JsonProperty("NNackMiss")
	public Integer getNNackMiss() {
		return nNackMiss;
	}

	@JsonProperty("NNackMiss")
	public void setNNackMiss(Integer nNackMiss) {
		this.nNackMiss = nNackMiss;
	}

	@JsonProperty("NExpired")
	public Integer getNExpired() {
		return nExpired;
	}

	@JsonProperty("NExpired")
	public void setNExpired(Integer nExpired) {
		this.nExpired = nExpired;
	}

	public JsonObject toJsonObject() {
		return new JsonObject().put("NEntries", nEntries).put("NInsert", nInsert);
	}

	public String toString() {
		return this.toJsonObject().encodePrettily();
	}

}

