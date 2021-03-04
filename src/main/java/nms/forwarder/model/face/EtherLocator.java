package nms.forwarder.model.face;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EtherLocator {

	@JsonProperty("scheme")
	private String scheme;

	@JsonProperty("port")
	private String port;

	@JsonProperty("local")
	private String local;

	@JsonProperty("remote")
	private String remote;

	
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public void setRemote(String remote) {
		this.remote = remote;
	}

	
	@JsonGetter("scheme")
	public String getScheme() {
		return scheme;
	}

	@JsonGetter("port")
	public String getPort() {
		return port;
	}

	@JsonGetter("local")
	public String getLocal() {
		return local;
	}

	@JsonGetter("remote")
	public String getRemote() {
		return remote;
	}
	
}
