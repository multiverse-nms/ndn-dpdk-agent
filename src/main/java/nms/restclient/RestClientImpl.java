package nms.restclient;

import com.google.gson.Gson;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

public class RestClientImpl implements RestClient {

	private EntryPoint entryPoint;
	private WebClient webClient;
	
	private static String CANDIDATE_CONFIG_ENDPOINT = "/configuration/candidate-config";
	private static String RUNNING_CONFIG_ENDPOINT = "/configuration/running-config";
	private static String NOTIFICATION_STATUS_ENDPOINT = "/notification/status";
	private static String NOTIFICATION_EVENT_ENDPOINT = "/notification/event";
	private static String NOTIFICATION_FAULT_ENDPOINT = "/notification/fault";
	
	public RestClientImpl(Vertx vertx, final EntryPoint entryPoint) {
		this.webClient = WebClient.create(vertx);
		this.entryPoint = entryPoint;
	}
	
	public RestClientImpl(EntryPoint entryPoint) {
		this.entryPoint = entryPoint;
	}
	
	
	@Override
	public Future<Configuration> getCandidateConfiguration() {
		Promise<Configuration> promise = Promise.promise();
		webClient.get(entryPoint.getPort(), entryPoint.getHost(), CANDIDATE_CONFIG_ENDPOINT).send(ar -> {
			if (ar.succeeded()) {
				HttpResponse<Buffer> response = ar.result();
				Configuration config = new Gson().fromJson(response.bodyAsString(), Configuration.class);
				promise.complete(config);
			} else {
				promise.fail("could not retrieve candidate configuration");
			}
		});
		return promise.future();
	}

	@Override
	public Future<Configuration> getRunningConfiguration() {
		Promise<Configuration> promise = Promise.promise();
		webClient.get(entryPoint.getPort(), entryPoint.getHost(), RUNNING_CONFIG_ENDPOINT).send(ar -> {
			if (ar.succeeded()) {
				HttpResponse<Buffer> response = ar.result();
				Configuration config = new Gson().fromJson(response.bodyAsString(), Configuration.class);
				promise.complete(config);
			} else {
				promise.fail("could not retrieve running configuration");
			}
		});
		return promise.future();
	}

	@Override
	public Future<Void> sendNotification(Notification notification) {
		Promise<Void> promise = Promise.promise();
		// TODO: add logic to send notification here
		return promise.future();
	}

}
