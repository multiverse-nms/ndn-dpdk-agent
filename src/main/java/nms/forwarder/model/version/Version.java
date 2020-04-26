package nms.forwarder.model.version;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.vertx.core.json.JsonObject;

public class Version {
	@JsonProperty("Commit")
	String commit;
	@JsonProperty("BuildTime")
	String buildTime;

	public String getCommit() {
		return commit;
	}

	public String getBuildTime() {
		return buildTime;
	}

	public JsonObject toJsonObject() {
		return new JsonObject().put("commit", commit).put("buildTime", buildTime);
	}

	public String toString() {
		return this.toJsonObject().encodePrettily();
	}
}
