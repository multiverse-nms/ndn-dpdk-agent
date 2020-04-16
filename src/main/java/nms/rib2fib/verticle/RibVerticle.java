package nms.rib2fib.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class RibVerticle extends AbstractVerticle {

	private static final Logger LOG = LoggerFactory.getLogger(RibVerticle.class);
	
	@Override
	public void start(Promise<Void> startFuture) {
		LOG.info("starting " + this.getClass().getName());
		
	}
	
	@Override
	public void stop() {
		LOG.info("stopping " + this.getClass().getName());
	}

}
