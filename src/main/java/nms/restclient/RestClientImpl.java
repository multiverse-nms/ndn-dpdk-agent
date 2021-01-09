package nms.restclient;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;

public class RestClientImpl implements RestClient {

	private static String DEFAULT_HTTP_HOST = "localhost";
	private static int DEFAULT_HTTP_PORT = 8787;
	
	private String controllerHost = "";
	private int controllerPort = 0;
	private WebClient webClient;
	private String token = "";
	
	private  Comparable<Face> typeFace = new Comparable<Face>();
	private  Comparable<Route> typeRoute = new Comparable<Route>();
	
	private JsonRpcHelper jsonRpcHelper;
	
	private static final Logger LOG = LoggerFactory.getLogger(RestClientImpl.class);
	
	public RestClientImpl(Vertx vertx, JsonObject config) {	
		this.controllerHost = config.getString("http.host", DEFAULT_HTTP_HOST);
		this.controllerPort = config.getInteger("http.port", DEFAULT_HTTP_PORT);
		this.webClient = WebClient.create(vertx);
		this.jsonRpcHelper = new JsonRpcHelper();
	}

	@Override
	public Future<String> login(String username, String password) {
		Promise<String> promise = Promise.promise();
		
		RequestBuilder rb = new RequestBuilder(webClient, token)
				.setPort(controllerPort).setHost(controllerHost);
		JsonObject creds = new JsonObject().put("username", username).put("password", password);
		rb.makeAgentLoginRequest().sendJsonObject(creds, ar -> {
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
				promise.fail("failed to login: " + ar.cause());
			}
		});
		return promise.future();
	}

	@Override
	public Future<Configuration> getConfiguration() {
		Promise<Configuration> promise = Promise.promise();
		
		RequestBuilder rb = new RequestBuilder(webClient, token)
				.setPort(controllerPort).setHost(controllerHost);
		rb.makeGetConfigurationRequest().send(ar -> {
					if (ar.succeeded()) {
						HttpResponse<Buffer> response = ar.result();
						if (response.statusCode() == 200) {
							Configuration config = new Configuration(response.bodyAsJsonObject());
							LOG.debug("candidate config={}",config);
//							Configuration config = new Gson().fromJson(response.bodyAsString(), Configuration.class);
							promise.complete(config);
						} else {
							if (response.statusCode() == 401) {
								LOG.error("error: 401 - unauthorized");
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
		
		HttpRequest<Buffer> request = this.webClient
				.put(controllerPort, controllerHost, apiEndpoint)
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
		    	json.add(jsonRpcHelper.makeNewFaceCommand(face));
		    });
			
			typeFace.getUnionOfLists(prev.getFaces(), current.getFaces())
			.forEach(face -> {
		        if (!(typeFace.contains(current.getFaces(), face))) {
		        	json.add(jsonRpcHelper.makeDestroyFaceCommand(face));
		        }
		    });
			
			
			typeRoute.getDifferenceOfLists(prev.getRoutes(), current.getRoutes())
			.forEach(route -> {
			    	json.add(jsonRpcHelper.makeNewRouteCommand(route));
			    });
				
			typeRoute.getUnionOfLists(prev.getRoutes(), current.getRoutes())
			.forEach(route -> {
			        if (!(typeRoute.contains(current.getRoutes(), route))) {
			        	json.add(jsonRpcHelper.makeDestroyRouteCommand(route));
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
