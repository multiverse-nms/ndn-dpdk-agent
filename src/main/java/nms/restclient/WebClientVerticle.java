package nms.restclient;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class WebClientVerticle extends AbstractVerticle {

	private RestClient restClient;

	private static final Logger LOG = LoggerFactory.getLogger(WebClientVerticle.class);
	private static String WEBCLIENT_EVENTBUS_ADDRESS = "webclient-verticle.eventbus";
	private static String FORWARDER_EVENTBUS_ADDRESS = "fw-verticle.eventbus";
	private static String RIB_EVENTBUS_ADDRESS = "rib-verticle.eventbus";
	private static long CONFIG_PERIOD = 60000; // delay the delay in milliseconds, after which the timer will fire
	private static long STATUS_PERIOD = 60000;
	boolean isNewConfig = true;
	
	private String token;

	@Override
	public void start(Promise<Void> promise) {

		LOG.info("starting " + this.getClass().getName());
		EntryPoint localhostEntryPoint = new EntryPoint(9001, "localhost");
		this.restClient = new RestClientImpl(vertx, localhostEntryPoint);
		
		Properties prop = new Properties();

		try {
			prop.load(new FileInputStream("data.properties"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String username = prop.getProperty("username");
		String password = prop.getProperty("password");
		
		
		// login
		this.login(username, password).onComplete(ar -> {
			if (ar.succeeded()) {
				token = ar.result();
				// send status periodically 
				this.sendStatus(new StatusNotification(Status.UP), new Handler<AsyncResult<Void>>() {
					@Override
					public void handle(AsyncResult<Void> ar) {
						if (ar.succeeded()) {
							
						} else {
							
						}

					}
				});
				
				
				// retrieve config periodically
				this.pollConfiguration(new Handler<AsyncResult<Configuration>>() {
					@Override
					public void handle(AsyncResult<Configuration> ar) {
						if (ar.succeeded()) {
							if (isNewConfig) {
								// apply to forwarder
								applyConfiguration(ar.result());
							} else {
								compareConfiguration(null, ar.result());
							}
							isNewConfig = false;
						}

					}
				});
				
			} else {
				LOG.error("could not authenticate this agent");
			}
		});
		
		
	}

	private void sendStatus(StatusNotification status, Handler<AsyncResult<Void>> handler) {
		vertx.setPeriodic(STATUS_PERIOD, id -> this.restClient.sendNotification(status,token).onComplete(handler));
	}

	private Future<String> login(String user, String password) {
		return this.restClient.basicAuthentication(user, password);
	}

	private void pollConfiguration(Handler<AsyncResult<Configuration>> handler) {
		vertx.setPeriodic(CONFIG_PERIOD, id -> this.restClient.getCandidateConfiguration(token).onComplete(handler));
	}

	private void applyConfiguration(Configuration config) {
		config.getFaces().forEach(face -> {
			createNewFace(face);
		});

		config.getRoutes().forEach(route -> {
			createNewRoute(route);
		});

	}

	private Future<JsonObject> createNewFace(Face face) {
		Promise<JsonObject> promise = Promise.promise();
		vertx.eventBus().request(FORWARDER_EVENTBUS_ADDRESS, JsonRpcHelper.makeNewFaceCommand(face), ar -> {
			if (ar.succeeded()) {
				promise.complete((JsonObject) ar.result().body());
			} else {
				promise.fail(ar.cause());
			}
		});
		return promise.future();
	}

	private Future<JsonObject> createNewRoute(Route route) {
		Promise<JsonObject> promise = Promise.promise();
		vertx.eventBus().request(RIB_EVENTBUS_ADDRESS, JsonRpcHelper.makeNewRouteCommand(route), ar -> {
			if (ar.succeeded()) {
				promise.complete((JsonObject) ar.result().body());
			} else {
				promise.fail(ar.cause());
			}
		});
		return promise.future();
	}

	private void compareConfiguration(Configuration prev, Configuration current) {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop() {
		LOG.info("stopping " + this.getClass().getName());
	}
}
