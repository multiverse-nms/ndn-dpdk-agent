package nms.restclient;

import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;

public class RequestBuilder {

	private WebClient webClient;
	private int port = 8787;
	private String host = "mnms.controller";
	private String token = "";

	public RequestBuilder(WebClient webClient, String token) {
		this.webClient = webClient;
		this.token = token;
	}

	public RequestBuilder setPort(int port) {
		this.port = port;
		return this;
	}

	public RequestBuilder setHost(String host) {
		this.host = host;
		return this;
	}

	public RequestBuilder setToken(String token) {
		this.token = token;
		return this;
	}

	public HttpRequest<Buffer> makeAgentLoginRequest() {
		String apiEndpoint = "/api/login/agent";
		HttpRequest<Buffer> request = webClient.post(port, host, apiEndpoint);
		return request;
	}

	public HttpRequest<Buffer> makeStatusNotificationRequest(String statusId) {
		String apiEndpoint = "/api/notification/status/" + statusId;
		HttpRequest<Buffer> request = webClient.put(port, host, apiEndpoint)
				.putHeader("Authorization", "Bearer " + this.token);
		return request;
	}

	public HttpRequest<Buffer> makeGetConfigurationRequest() {
		String apiEndpoint = "/api/configuration/candidate-config";
		HttpRequest<Buffer> request = webClient.get(port, host, apiEndpoint)
				.putHeader("Authorization", "Bearer " + this.token);
		return request;
	}

}
