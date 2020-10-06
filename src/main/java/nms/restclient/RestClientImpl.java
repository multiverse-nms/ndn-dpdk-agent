package nms.restclient;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.PemTrustOptions;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestClientImpl implements RestClient {

	private WebClient webClient;
	private int port = 8787;
	private String host = "mnms.controller";
	private static final Logger LOG = LoggerFactory.getLogger(RestClientImpl.class);
	private String token = "";
	private String rootCAPath = "mnms-rootCA.crt.pem";

	public RestClientImpl(Vertx vertx) {
		this.webClient = getWebClient(vertx);
	}

	private WebClient getWebClient(Vertx vertx) {
		WebClientOptions options = new WebClientOptions();
		options.setSsl(true);
		options.setPemTrustOptions(new PemTrustOptions().addCertPath(rootCAPath));
		return WebClient.create(vertx, options);
	}

	@Override
	public Future<String> login(String user, String password) {
		Promise<String> promise = Promise.promise();
		RequestBuilder rb = new RequestBuilder(this.webClient, this.token).setPort(port).setHost(host);
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
		RequestBuilder rb = new RequestBuilder(this.webClient, this.token).setPort(this.port).setHost(this.host);
		rb.makeGetConfigurationRequest().send(ar -> {
			if (ar.succeeded()) {
				HttpResponse<Buffer> response = ar.result();
				if (response.statusCode() == 200) {
					Configuration config = new Configuration(response.bodyAsJsonObject());
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
		HttpRequest<Buffer> request = this.webClient.put(this.port, this.host, apiEndpoint).putHeader("Authorization",
				"Bearer " + this.token);
		request.sendJsonObject(status.toJsonObject(), ar -> {
			if (ar.succeeded()) {
				promise.complete();
			} else {
				promise.fail(ar.cause());
			}
		});
		return promise.future();
	}
	
	@Override
	public  Future<Configuration> compare(Configuration running, Configuration current){
		
		Promise<Configuration> promise = Promise.promise();
        Configuration config = new Configuration ();

        ArrayList<Face> currentfaces = new ArrayList<>(current.getFaces());
        ArrayList<Face> prevfaces = new ArrayList<>(running.getFaces());
        
        if(currentfaces.equals(prevfaces)) {
        	prevfaces.retainAll(currentfaces);
        }
        else {
        	
        	prevfaces.retainAll(currentfaces);
        	currentfaces.removeAll(prevfaces);
        	
        	currentfaces.forEach(s ->{
        		prevfaces.add(s);
             });
        }
        
        ArrayList<Route> currentroutes = new ArrayList<>(current.getRoutes());
        ArrayList<Route> prevroutes = new ArrayList<>(running.getRoutes()); 
        
            
        if(currentroutes.equals(prevroutes)) {
        	prevroutes.retainAll(currentroutes);
        }
        else {
        	
        	prevroutes.retainAll(currentroutes);
        	currentroutes.removeAll(prevroutes);
        	
        	currentroutes.forEach(s ->{
        		prevroutes.add(s);
             });
        }
        
    	config.setFaces(prevfaces);
    	config.setRoutes(prevroutes);
    	
    	promise.complete(config);
    	
    	return promise.future();
       		
	  }

	@Override
	public void setRootCA(String path) {
		this.rootCAPath = path;
	}

	@Override
	public void setPort(Integer port) {
		this.port = port;
	}

	@Override
	public void setHost(String host) {
		this.host = host;
	}
	
	

}
