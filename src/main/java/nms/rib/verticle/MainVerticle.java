package nms.rib2fib.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class MainVerticle extends AbstractVerticle {
	private static final Logger LOG = LoggerFactory.getLogger(MainVerticle.class);
	
	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new RibVerticle(), ar -> {
			if (ar.succeeded()) {
				LOG.info("verticle deployed successfully!");
			}
		});
	}
	
	@Override
	public void start(Promise<Void> startFuture) {
		LOG.info("starting " + this.getClass().getName());
	}
	
	@Override
	public void stop() {
		LOG.info("stopping " + this.getClass().getName());
	}

}
