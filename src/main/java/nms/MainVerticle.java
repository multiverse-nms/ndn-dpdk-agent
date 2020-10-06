package nms;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.dns.AddressResolverOptions;
import io.vertx.core.json.JsonObject;
import nms.forwarder.verticle.ForwarderVerticle;
import nms.restclient.WebClientVerticle;
import nms.rib.verticle.RibVerticle;
import nms.websockets.WebSocketServerVerticle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MainVerticle extends AbstractVerticle {

	private static final Logger LOG = LoggerFactory.getLogger(MainVerticle.class);
	private static String DEFAULT_CONFIG_FILE = "verticles.json";
	private static String DEFAULT_HOSTS_FILE = "hosts";

	public static void main(String[] args) {
		Buffer buffer = null;
		try {
			buffer = resolveHosts(DEFAULT_HOSTS_FILE);
		} catch (IOException e) {
			LOG.error("unable to resolve hosts {}", e.getMessage());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		Vertx vertx = Vertx.vertx(new VertxOptions()
				.setAddressResolverOptions(new AddressResolverOptions().setHostsValue(buffer)));
		vertx.deployVerticle(new MainVerticle());
	}

	@Override
	public void start(Promise<Void> promise) throws Exception {
		getConfigRetriever(DEFAULT_CONFIG_FILE).getConfig(ar -> {
			if (ar.failed()) {
				LOG.error("failed to retrieve the verticles configuration");
			} else {
				JsonObject configServerVerticle = ar.result().getJsonObject("main.verticle");
				deployAllVerticles(configServerVerticle, promise).onComplete(ar1 -> {
					if (ar1.succeeded()) {
						LOG.info("all verticles were deployed successfully");
						promise.complete();
					} else {
						LOG.error("error occured while deploying verticles");
						promise.fail(ar.cause());
					}
				});
			}
		});
	}

	private Future<Void> deployAllVerticles(JsonObject config, Promise<Void> promise) {
		DeploymentOptions ribVerticleOptions = new DeploymentOptions().setConfig(config().getJsonObject("rib.verticle"));
		DeploymentOptions fwVerticleOptions = new DeploymentOptions().setConfig(config().getJsonObject("forwarder.verticle"));
		DeploymentOptions wsVerticleOptions = new DeploymentOptions().setConfig(config().getJsonObject("websockets.verticle"));
		DeploymentOptions webClientVerticleOptions = new DeploymentOptions().setConfig(config().getJsonObject("webclient.verticle"));

		return this.deployVerticle(ForwarderVerticle.class.getName(), fwVerticleOptions)
				.compose(v -> deployVerticle(RibVerticle.class.getName(), ribVerticleOptions))
				.compose(v -> deployVerticle(WebSocketServerVerticle.class.getName(), wsVerticleOptions))
				.compose(v -> deployVerticle(WebClientVerticle.class.getName(), webClientVerticleOptions));
	}

	private static Buffer resolveHosts(String filename) throws IOException, URISyntaxException {
		URL res = MainVerticle.class.getClassLoader().getResource(filename);
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
		return Buffer.buffer(bytes);
	}

	private ConfigRetriever getConfigRetriever(String filename) {
		ConfigStoreOptions fileStore = new ConfigStoreOptions().setType("file")
				.setConfig(new JsonObject().put("path", filename));
		ConfigRetrieverOptions configRetrieverOptions = new ConfigRetrieverOptions().addStore(fileStore);
		ConfigRetriever retriever = ConfigRetriever.create(vertx, configRetrieverOptions);
		return retriever;
	}

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
