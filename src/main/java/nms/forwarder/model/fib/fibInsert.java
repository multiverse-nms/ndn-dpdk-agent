package nms.forwarder.model.fib;

import com.fasterxml.jackson.annotation.JsonProperty;

public class fibInsert {
	@JsonProperty
	boolean IsNew;

	public String toString() {
		return "isNew: " + IsNew;
	}
}