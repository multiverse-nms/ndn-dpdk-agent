package nms.forwarder.api;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

/**
 * @author hjg1
 *
 */
public class EventBusClient extends AbstractVerticle {

	@Override
	public void start(Future<Void> fut) {
		vertx.setPeriodic(5000, id -> {
			vertx.eventBus().send("fw.service.eb", new JsonObject().put("action", "get_faces"));
		});

	}

}