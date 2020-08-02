package nms;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import nms.forwarder.verticle.ForwarderVerticle;
import nms.rib.verticle.RibVerticle;
import nms.websockets.WebSocketServerVerticle;

public class Main extends AbstractVerticle {

	private static final Logger LOG = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new Main());
	}

	@Override
	public void start(Promise<Void> promise) throws Exception {
		deployAllVerticles(promise);
	}

	private void deployAllVerticles(Promise<Void> promise) {
		DeploymentOptions ribVerticleOptions = new DeploymentOptions().setConfig(config());
		DeploymentOptions fwVerticleOptions = new DeploymentOptions().setConfig(config());
		DeploymentOptions wsVerticleOptions = new DeploymentOptions().setConfig(config());

		this.deployVerticle(ForwarderVerticle.class.getName(), fwVerticleOptions)
				.compose(v -> deployVerticle(RibVerticle.class.getName(), ribVerticleOptions))
				.compose(v -> deployVerticle(WebSocketServerVerticle.class.getName(), wsVerticleOptions))
				.onComplete(ar -> {
					if (ar.succeeded()) {
						LOG.info("all verticles were deployed successfully!");
						promise.complete();
					} else {
						LOG.error("error occured while deploying verticles");
						promise.fail(ar.cause());
					}
				});
	}

	private Future<Void> deployVerticle(String verticle, DeploymentOptions options) {
		Promise<Void> promise = Promise.promise();
		if (options.getInstances() == 0) {
			promise.complete();
		} else {
			vertx.deployVerticle(verticle, options, reporter(verticle, promise));
		}
		return promise.future();
	}

	private <T> Handler<AsyncResult<T>> reporter(String verticle, Promise<Void> promise) {
		return ar -> {
			if (ar.succeeded()) {
				LOG.info("deployed verticle " + verticle + " with ID " + (String) ar.result());
				promise.complete();
			} else {
				ar.cause().printStackTrace();
				promise.fail(ar.cause());
			}
		};
	}

}
