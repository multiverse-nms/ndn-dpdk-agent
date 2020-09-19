package nms.restclient.service.impl;

import com.google.gson.Gson;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import nms.restclient.AuthToken;
import nms.restclient.Configuration;
import nms.restclient.EntryPoint;
import nms.restclient.service.ConfigurationService;

public class ConfigurationServiceImpl implements ConfigurationService {

	private WebClient webClient;
	private EntryPoint entryPoint;
	private Configuration runningConfiguration;
	private AuthToken authToken;

	private static String CANDIDATE_CONFIG_ENDPOINT = "/configuration/candidate-config";
	private static String RUNNING_CONFIG_ENDPOINT = "/configuration/running-config";

	public ConfigurationServiceImpl(WebClient webClient, EntryPoint entryPoint, AuthToken token) {
		this.webClient = webClient;
		this.entryPoint = entryPoint;
		this.authToken = token;
	}

	@Override
	public Future<Configuration> getCandidateConfiguration() {
		Promise<Configuration> promise = Promise.promise();
		webClient.get(entryPoint.getPort(), entryPoint.getHost(), CANDIDATE_CONFIG_ENDPOINT)
				.bearerTokenAuthentication(this.authToken.getToken()).send(ar -> {
					if (ar.succeeded()) {
						HttpResponse<Buffer> response = ar.result();
						if (response.statusCode() == 200
								&& response.getHeader("content-type").equals("application/json")) {
							Configuration config = new Gson().fromJson(response.bodyAsString(), Configuration.class);
							// TODO: update running config
							promise.complete(config);
						} else {
							if (response.statusCode() == 401) {
								// TODO: refresh token
							}
						}
					} else {
						promise.fail("could not retrieve candidate configuration");
					}
				});
		return promise.future();
	}

	@Override
	public Future<Configuration> getRunningConfiguration() {
		Promise<Configuration> promise = Promise.promise();
		webClient.get(entryPoint.getPort(), entryPoint.getHost(), RUNNING_CONFIG_ENDPOINT)
				.bearerTokenAuthentication(this.authToken.getToken()).send(ar -> {
					if (ar.succeeded()) {
						HttpResponse<Buffer> response = ar.result();
						if (response.statusCode() == 200
								&& response.getHeader("content-type").equals("application/json")) {
							Configuration config = new Gson().fromJson(response.bodyAsString(), Configuration.class);
							promise.complete(config);
						} else {
							if (response.statusCode() == 401) {
								
							}
						}
					} else {
						promise.fail("could not retrieve running configuration");
					}
				});
		return promise.future();
	}

	@Override
	public Future<AuthToken> refreshToken() {
		// TODO Auto-generated method stub
		return null;
	}

}
