package nms.rib2fib.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import net.named_data.jndn.Name;
import nms.rib2fib.Rib;
import nms.rib2fib.RibAction;
import nms.rib2fib.Route;
import nms.rib2fib.service.RibHandler;
import nms.rib2fib.service.RibService;
import nms.rib2fib.service.RibServiceImpl;

public class RibVerticle extends AbstractVerticle {

	private static final Logger LOG = LoggerFactory.getLogger(RibVerticle.class);
	private static String EVENTBUS_ADDRESS = "rib-verticle.eventbus";
	private RibService ribService;

	@Override
	public void start(Promise<Void> startFuture) {
		LOG.info("starting " + this.getClass().getName());
		this.ribService = new RibServiceImpl();
		LOG.info("instantiated RIB Service");

		// setup EventBus
		this.consumeEventBus(EVENTBUS_ADDRESS);

	}

	private void consumeEventBus(String address) {
		vertx.eventBus().consumer(address, (message) -> {
			JsonObject body = (JsonObject) message.body();
			String action = body.getString("action");

			LOG.debug("[eventbus] action = " + action);

			if (action == RibAction.ADD_ROUTE.getName()) {
				Name name = new Name(body.getString("name"));
				int faceId = body.getInteger("faceId");
				int origin = body.getInteger("origin");
				ribService.addRoute(new Route(name, faceId, origin), new RibHandler() {

					@Override
					public void handleRib(Rib rib) {
						// TODO Auto-generated method stub
						
					}
					
				});
			}

			if (action == RibAction.REMOVE_ROUTE.getName()) {
				Name name = new Name(body.getString("name"));
				int faceId = body.getInteger("faceId");
				int origin = body.getInteger("origin");
				ribService.removeRoute(new Route(name, faceId, origin), new RibHandler() {

					@Override
					public void handleRib(Rib rib) {
						// TODO Auto-generated method stub
						
					}
					
				});
			}
		});

	}

	@Override
	public void stop() {
		LOG.info("stopping " + this.getClass().getName());
	}

}
