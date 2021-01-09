package nms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import nms.forwarder.verticle.ForwarderVerticle;
import nms.restclient.WebClientVerticle;
import nms.rib.verticle.RibVerticle;
import nms.websockets.WebSocketServerVerticle;


public class MainVerticle extends AbstractVerticle {

	private static final Logger LOG = LoggerFactory.getLogger(MainVerticle.class);
	// private static String DEFAULT_CONFIG_FILE = "/data/config.json";
	// private static String DEFAULT_HOSTS_FILE = "hosts";

	public static void main(String[] args) {
		/* Buffer buffer = null;
		try {
			buffer = resolveHosts(DEFAULT_HOSTS_FILE);
		} catch (IOException e) {
			LOG.error("unable to resolve hosts {}", e.getMessage());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		VertxOptions vertxOptions = new VertxOptions();
		if (buffer.length() != 0) {
			vertxOptions.setAddressResolverOptions(new AddressResolverOptions().setHostsValue(buffer));
		} */
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new MainVerticle());
	}

	@Override
	public void start(Promise<Void> promise) throws Exception {
		/* getConfigRetriever(DEFAULT_CONFIG_FILE).getConfig(ar -> {
			if (ar.failed()) {
				LOG.error("failed to retrieve the verticles configuration");
				promise.fail(ar.cause());
			} else {
				JsonObject config = ar.result();
				deployAllVerticles(config).onComplete(ar1 -> {
					if (ar1.succeeded()) {
						LOG.info("all verticles were deployed successfully");
						promise.complete();
					} else {
						LOG.error("error occured while deploying verticles");
						promise.fail(ar1.cause());
					}
				});
			}
		}); */
		
		deployAllVerticles().onComplete(ar -> {
			if (ar.succeeded()) {
				LOG.info("all verticles were deployed successfully");
				promise.complete();
			} else {
				LOG.error("error occured while deploying verticles");
				promise.fail(ar.cause());
			}
		});
	}

	private Future<Void> deployAllVerticles() {
		DeploymentOptions verticleConfig = new DeploymentOptions().setConfig(config());
		return this.deployVerticle(ForwarderVerticle.class.getName(), verticleConfig)
				.compose(v -> deployVerticle(RibVerticle.class.getName(), verticleConfig))
				.compose(v -> deployVerticle(WebSocketServerVerticle.class.getName(), verticleConfig))
				.compose(v -> deployVerticle(WebClientVerticle.class.getName(), verticleConfig));
	}

	/* private static Buffer resolveHosts(String filename) throws IOException, URISyntaxException {
		URL res = MainVerticle.class.getClassLoader().getResource(filename);
		LOG.info("file path = {}", res);
		File file = Paths.get(res.toURI()).toFile();
		String absolutePath = file.getAbsolutePath();
		DataInputStream reader = new DataInputStream(new FileInputStream(absolutePath));
		int nBytesToRead = reader.available();
		byte[] bytes = new byte[nBytesToRead];
		if (nBytesToRead > 0) {
			bytes = new byte[nBytesToRead];
			reader.read(bytes);
			reader.close();
		}
		LOG.info("bytes = {}", bytes);
		return Buffer.buffer(bytes);
	} */

	/* private ConfigRetriever getConfigRetriever(String filename) {
		ConfigStoreOptions fileStore = new ConfigStoreOptions().setType("file")
				.setConfig(new JsonObject().put("path", filename));
		ConfigRetrieverOptions configRetrieverOptions = new ConfigRetrieverOptions().addStore(fileStore);
		ConfigRetriever retriever = ConfigRetriever.create(vertx, configRetrieverOptions);
		return retriever;
	} */

	private Future<Void> deployVerticle(String verticle, DeploymentOptions options) {
		Promise<Void> promise = Promise.promise();
		if (options.getInstances() == 0) {
			promise.complete();
		} else {
			vertx.deployVerticle(verticle, options, handleDeployment(verticle, promise));
		}
		return promise.future();
	}

	private <T> Handler<AsyncResult<T>> handleDeployment(String verticle, Promise<Void> promise) {
		return ar -> {
			if (ar.succeeded()) {
				LOG.info("deployed verticle {} with ID {}", verticle, (String) ar.result());
				promise.complete();
			} else {
				ar.cause().printStackTrace();
				promise.fail(ar.cause());
			}
		};
	}
}
