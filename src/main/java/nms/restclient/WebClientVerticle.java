package nms.restclient;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;

public class WebClientVerticle extends AbstractVerticle {
  
	private static final Logger LOG = LoggerFactory.getLogger(WebClientVerticle.class);

	private RestClient restClient;
	
	private static long DEFAULT_CONFIG_PERIOD = 30000; // in milliseconds
	private static long DEFAULT_STATUS_PERIOD = 60000;

	private Long statusTaskId = (long) 0;
	private Long configTaskId = (long) 0;

	private ConfigurationHandler configHandler;

	@Override
	public void start(Promise<Void> promise) throws Exception {
		LOG.info("starting " + this.getClass().getName() + " verticle");
		
		System.out.println("configuration"+config());
		//LOG.info("config: " + config().getJsonObject("controller").encodePrettily());
		
		this.restClient = getRestClient();
		this.configHandler = new ConfigurationHandler(vertx);
		
		JsonObject creds = config().getJsonObject("login");
		this.login(creds.getString("username"), creds.getString("password")).onComplete(ar -> {
			if (ar.succeeded()) {
				LOG.info("agent logged in.");
				String token = ar.result();
				
				this.restClient.setToken(token);

				this.setStatusTask();
				this.setConfigTask();
				
				promise.complete();
			} else {
				LOG.info("failed to login: " + ar.cause());
				promise.fail(ar.cause());
			}
		});
		}
//	}

	private RestClient getRestClient() {
		//System.out.println(""+config().getJsonObject("controller"));
		return new RestClientImpl(vertx, config());
	}

	private Future<String> login(String username, String password) {
		return this.restClient.login(username, password);
	}

	private void setStatusTask() {
		long statusPeriod = config()
				//config().getJsonObject("controller")
				.getLong("status.period", DEFAULT_STATUS_PERIOD);
		
		statusTaskId = vertx.setPeriodic(statusPeriod, id -> {
			this.restClient.sendStatus().onComplete(ar -> {
				if (ar.succeeded()) {
					LOG.info("status sent to controller");
				} else {
					LOG.info("failed to send status");
				}
			});
		});
	}

	private void setConfigTask() {
		long configPeriod = config()
				//getJsonObject("controller")
				.getLong("config.period", DEFAULT_CONFIG_PERIOD);
		
		configTaskId = vertx.setPeriodic(configPeriod, id -> {
			this.restClient.getConfiguration().onComplete(ar -> {
				if (ar.succeeded()) {
					LOG.info("configuration retrieved");
					
					// TODO: method
					Configuration candidate = ar.result();
					LOG.info(candidate.toJsonObject().encodePrettily());
					configHandler.compare(candidate)
							.onComplete(new Handler<AsyncResult<List<JsonObject>>>() {
								@Override
								public void handle(AsyncResult<List<JsonObject>> ar) {
									if (ar.succeeded()) {
										LOG.debug("COMMANDS = {}", ar.result());
										configHandler.send(ar.result());
									}
								}
							});
				} else {
					LOG.info("failed to retrive configuration: " + ar.result());
				}
			});
		});
	}	

	@Override
	public void stop() throws Exception {
		LOG.info("stopping " + this.getClass().getName());
		vertx.cancelTimer(statusTaskId);
		vertx.cancelTimer(configTaskId);
	}
}
