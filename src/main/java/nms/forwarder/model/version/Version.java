package nms.forwarder.model.version;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.vertx.core.json.JsonObject;

public class Version {
	@JsonProperty("Commit")
	String commit;

	public String getCommit() {
		return commit;
	}


	public JsonObject toJsonObject() {
		return new JsonObject().put("commit", commit);
	}

	public String toString() {
		return this.toJsonObject().encodePrettily();
	}
}
