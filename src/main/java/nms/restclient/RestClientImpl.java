package nms.restclient;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.net.PemTrustOptions;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;

public class RestClientImpl implements RestClient {

	private WebClient webClient;
	private static int PORT = 8787;
	private static String HOST = "mnms.controller";
	private static final Logger LOG = LoggerFactory.getLogger(RestClientImpl.class);
	private String token = "";
	private  Comparable<Face> typeFace = new Comparable<Face>();
	private  Comparable<Route> typeRoute = new Comparable<Route>();

	public RestClientImpl(Vertx vertx) {
		WebClientOptions options = new WebClientOptions();
		options.setSsl(true);
		options.setPemTrustOptions(new PemTrustOptions().addCertPath("mnms-rootCA.crt.pem"));
		this.webClient = WebClient.create(vertx, options);
		
	}

	@Override
	public Future<String> login(String user, String password) {
		Promise<String> promise = Promise.promise();
		RequestBuilder rb = new RequestBuilder(this.webClient, this.token).setPort(PORT).setHost(HOST);
		rb.makeAgentLoginRequest().sendJsonObject(new JsonObject().put("username", user).put("password", password),
				ar -> {
					if (ar.succeeded()) {
						HttpResponse<Buffer> response = ar.result();
						if (response.statusCode() == 200) {
							JsonObject body = response.bodyAsJsonObject();
							String token = body.getString("token");
							promise.complete(token);
						} else {
							if (response.statusCode() == 401) {
								LOG.info("error: 401 - unauthorized");
								promise.fail("error: 401 - unauthorized");
							}
						}
					} else {
						promise.fail("error: unable to login, " + ar.cause());
					}
				});
		return promise.future();
	}

	@Override
	public Future<Configuration> getConfiguration() {
		Promise<Configuration> promise = Promise.promise();
		RequestBuilder rb = new RequestBuilder(this.webClient, this.token).setPort(PORT).setHost(HOST);
		rb.makeGetConfigurationRequest().send(ar -> {
					if (ar.succeeded()) {
						HttpResponse<Buffer> response = ar.result();
						if (response.statusCode() == 200) {
							Configuration config = new Configuration(response.bodyAsJsonObject());
//							Configuration config = new Gson().fromJson(response.bodyAsString(), Configuration.class);
							promise.complete(config);
						} else {
							if (response.statusCode() == 401) {
								LOG.info("error: 401 - unauthorized");
								promise.fail("error: 401 - unauthorized");
							}
						}
					} else {
						promise.fail("error: unable to login, " + ar.cause());
					}
				});
		return promise.future();
	}


	@Override
	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public Future<Void> sendStatus() {
		Promise<Void> promise = Promise.promise();
		StatusNotification status = new StatusNotification(Status.UP);
		LOG.info("sending status: " + status.toJsonObject());
		String apiEndpoint = "/api/notification/status/" + status.getId();
		HttpRequest<Buffer> request = this.webClient.put(PORT, HOST, apiEndpoint)
				.putHeader("Authorization", "Bearer " + this.token);
		request.sendJsonObject(status.toJsonObject(), ar-> {
			if (ar.succeeded()) {
				promise.complete();
			} else {
				promise.fail(ar.cause());
			}
		});
		return promise.future();
	}
	
	@Override
	public List<JsonObject> compareConfiguration(Configuration prev, Configuration current) {
		
		 List <JsonObject> json = new ArrayList<>();
		 
			typeFace.getDifferenceOfLists(prev.getFaces(), current.getFaces())
			.forEach(face -> {
		    	json.add(JsonRpcHelper.makeNewFaceCommand(face));
		    });
			
			typeFace.getUnionOfLists(prev.getFaces(), current.getFaces())
			.forEach(face -> {
		        if (!(typeFace.contains(current.getFaces(), face))) {
		        	json.add(JsonRpcHelper.makeDestroyFaceCommand(face));
		        }
		    });
			
			
			typeRoute.getDifferenceOfLists(prev.getRoutes(), current.getRoutes())
			.forEach(face -> {
			    	json.add(JsonRpcHelper.makeNewRouteCommand(face));
			    });
				
			typeRoute.getUnionOfLists(prev.getRoutes(), current.getRoutes())
			.forEach(face -> {
			        if (!(typeRoute.contains(current.getRoutes(), face))) {
			        	json.add(JsonRpcHelper.makeDestroyFaceCommand(face));
			        }
			 });
			return json;
	}

	@Override
	public void setRootCA(String path) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPort(Integer port) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHost(String host) {
		// TODO Auto-generated method stub
		
	}

	
	

}
