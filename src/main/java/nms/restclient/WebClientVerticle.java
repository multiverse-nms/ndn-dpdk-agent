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
import io.vertx.ext.web.client.WebClient;
import nms.restclient.service.AuthenticationService;
import nms.restclient.service.ConfigurationService;
import nms.restclient.service.NotificationService;
import nms.restclient.service.TokenManager;
import nms.restclient.service.impl.ConfigurationServiceImpl;
import nms.restclient.service.impl.Login;
import nms.restclient.service.impl.NotificationServiceImpl;
import nms.restclient.service.impl.TokenManagerImpl;

public class WebClientVerticle extends AbstractVerticle {

	private WebClient webClient;
	
	private TokenManager tokenManager;
	private ConfigurationService configService;
	private NotificationService notifService;

	private static final Logger LOG = LoggerFactory.getLogger(WebClientVerticle.class);
	private static String WEBCLIENT_EVENTBUS_ADDRESS = "webclient-verticle.eventbus";
	private static String FORWARDER_EVENTBUS_ADDRESS = "fw-verticle.eventbus";
	private static String RIB_EVENTBUS_ADDRESS = "rib-verticle.eventbus";
	private static long CONFIG_PERIOD = 60000; // delay the delay in milliseconds, after which the timer will fire
	private static long STATUS_PERIOD = 60000;
	boolean isNewConfig = true;
	
	private AuthToken authToken;

	@Override
	public void start(Promise<Void> promise) throws Exception {
		super.start(promise);
		LOG.info("starting " + this.getClass().getName());
		this.webClient = WebClient.create(vertx);
		EntryPoint localEntryPoint = new EntryPoint(9001, "localhost");
//		TokenManagerImpl(webClient, localEntryPoint);
		this.login().onComplete(ar -> {
			if (ar.succeeded()) {
				authToken = ar.result();
				this.notifService = new NotificationServiceImpl(webClient, localEntryPoint, authToken);
				this.configService = new ConfigurationServiceImpl(webClient, localEntryPoint, authToken);
				
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
							
						} else {
							
						}
					}
				});
			} else {
				LOG.error("could not authenticate this agent");
			}
		});
	}

	private void sendStatus(StatusNotification status, Handler<AsyncResult<Void>> handler) {
<<<<<<< HEAD
		vertx.setPeriodic(STATUS_PERIOD, id -> this.notifService.sendNotification(status).onComplete(handler));
=======
		vertx.setPeriodic(STATUS_PERIOD, id -> this.restClient.sendNotification(status,token).onComplete(handler));
>>>>>>> 9eaaa331549eceb1dda81782ce00a7e5e4372da8
	}

	private Future<AuthToken> login() {
		return this.tokenManager.getNewToken();
	}

	private void pollConfiguration(Handler<AsyncResult<Configuration>> handler) {
		vertx.setPeriodic(CONFIG_PERIOD, id -> this.configService.getCandidateConfiguration().onComplete(handler));
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
