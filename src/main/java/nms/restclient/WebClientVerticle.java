package nms.restclient;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class WebClientVerticle extends AbstractVerticle {
	
	private RestClient restClient;
	
	private static final Logger LOG = LoggerFactory.getLogger(WebClientVerticle.class);
	private static String WEBCLIENT_EVENTBUS_ADDRESS = "webclient-verticle.eventbus";
	private static long CONFIG_PERIOD = 60000; // delay the delay in milliseconds, after which the timer will fire
	private static long STATUS_PERIOD = 60000;
	
	@Override
	public void start(Promise<Void> promise) {
		LOG.info("starting " + this.getClass().getName());
		EntryPoint localhostEntryPoint = new EntryPoint(9001, "localhost");
		this.restClient = new RestClientImpl(vertx, localhostEntryPoint);
		// send status periodically 
		this.pollConfiguration(new Handler<AsyncResult<Configuration>>() {
			@Override
			public void handle(AsyncResult<Configuration> ar) {
				if (ar.succeeded()) {
					// TODO: apply configuration to forwarder (or do diff?)
				}
				
			}
		});		
	}
	
	
	private void sendStatus(StatusNotification status, Handler<AsyncResult<Void>> handler) {
		vertx.setPeriodic(STATUS_PERIOD, id -> this.restClient.sendNotification(status).onComplete(handler));
	}

	
	private Future<String> login(String user, String password) {
		return this.restClient.basicAuthentication(user, password);
	}
	
	
	private void pollConfiguration(Handler<AsyncResult<Configuration>> handler) {
		vertx.setPeriodic(CONFIG_PERIOD, id -> this.restClient.getCandidateConfiguration().onComplete(handler));
	}

	@Override
	public void stop() {
		LOG.info("stopping " + this.getClass().getName());
	}
}
