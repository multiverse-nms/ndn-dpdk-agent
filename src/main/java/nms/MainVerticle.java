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

	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new MainVerticle());
	}

	@Override
	public void start(Promise<Void> promise) throws Exception {
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
