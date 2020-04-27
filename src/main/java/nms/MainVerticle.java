package nms;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import nms.forwarder.verticle.ForwarderVerticle;
import nms.rib.verticle.RibVerticle;
import nms.forwarder.api.EventBusClient;

public class MainVerticle extends AbstractVerticle {

	private static final Logger LOG = LoggerFactory.getLogger(MainVerticle.class);
	private Map<String, String> verticlesDeployed = new HashMap<>();

	private JsonObject configuration;

	public static void main(String[] args) {

		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new MainVerticle());
	}

	public void start(Promise<Void> startFuture) {
		Promise<Void> promise = Promise.promise();
		this.retrieveConfiguration()
		.compose(v -> deployForwarderVerticle())
		.compose(v -> deployRibVerticle())
		.compose(v -> deployClientVerticle())
		.compose(v -> promise.future())
				.onComplete(ar -> {
					if (ar.succeeded()) {
						LOG.info("all verticles were deployed successfully!");
						startFuture.complete();
					} else {
						LOG.error("error occured while deploying verticles");
						this.shutdown();
						startFuture.fail(ar.cause());
					}
				});

	}

	private Future<Void> retrieveConfiguration() {
		Promise<Void> promise = Promise.promise();
		ConfigRetrieverOptions options = new ConfigRetrieverOptions();
		this.addConfigStoreOptions(options);
		ConfigRetriever configRetriever = ConfigRetriever.create(vertx, options);
		configRetriever.getConfig(ar -> {
			if (ar.succeeded()) {
				this.configuration = ar.result();
				LOG.debug("retrieved configuration: {}", this.configuration.encodePrettily());
				promise.complete();
			} else {
				LOG.error("unable to retrieve configuration");
				promise.fail(ar.cause());
			}
		});
		return promise.future();
	}

	private void addConfigStoreOptions(ConfigRetrieverOptions options) {
		options.addStore(new ConfigStoreOptions().setType("json").setConfig(vertx.getOrCreateContext().config()));

	}

	private Future<Void> deployForwarderVerticle() {
		return this.deployVerticle(ForwarderVerticle.class,
				this.configuration.getJsonObject("forwarder", new JsonObject()));
	}

	private Future<Void> deployRibVerticle() {
		return this.deployVerticle(RibVerticle.class, this.configuration.getJsonObject("rib", new JsonObject()));
	}

	private Future<Void> deployClientVerticle() {
		return this.deployVerticle(EventBusClient.class,
				this.configuration.getJsonObject("client", new JsonObject()));
	}

	// method to deploy a single verticle
	@SuppressWarnings("rawtypes")
	private Future<Void> deployVerticle(Class classe, JsonObject config) {
		Promise<Void> promise = Promise.promise();
		DeploymentOptions options = new DeploymentOptions().setConfig(config);

		vertx.deployVerticle(classe.getName(), options, ar -> {
			if (ar.succeeded()) {
				LOG.info("deployed verticle {} with ID {}", classe.getName(), ar.result());
				verticlesDeployed.put(classe.getSimpleName(), ar.result());
				promise.complete();
			} else {
				promise.fail(ar.cause());
			}
		});

		return promise.future();
	}

	// method to undeploy verticles
	@SuppressWarnings("rawtypes")
	private void shutdown() {
		LOG.info("shutting down");

		List<Future> futures = new LinkedList<>();

		this.verticlesDeployed.forEach((name, ID) -> {
			if (ID != null && vertx.deploymentIDs().contains(ID)) {
				LOG.info("undeploying verticle {} with ID {}", name, ID);
				Promise<Void> promise = Promise.promise();
				vertx.undeploy(ID);
				futures.add(promise.future());
			}
		});

		CompositeFuture.all(futures).onComplete(ar -> {
			if (ar.succeeded()) {
				LOG.info("all verticles were undeployed successfully");
			} else {
				LOG.error("failed to undeploy some verticles", ar.cause());
			}
		});
	}

	@Override
	public void stop() throws Exception {
		LOG.info("shutting down main verticle");
		this.shutdown();
		super.stop();
	}
}
