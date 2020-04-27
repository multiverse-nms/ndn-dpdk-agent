package nms.forwarder.api;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
/**
 * @author hjg1
 *
 */
public class EventBusClient extends AbstractVerticle {

	private static final Logger LOG = LoggerFactory.getLogger(EventBusClient.class);
	
	@Override
	public void start(Future<Void> fut) {
		vertx.setPeriodic(5000, id -> {
			vertx.eventBus().send("fw-verticle.eventbus", new JsonObject().put("action", "insert_fib"));
		});

	}

}