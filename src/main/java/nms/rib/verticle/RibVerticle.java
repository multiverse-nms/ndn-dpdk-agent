package nms.rib.verticle;

import java.util.ArrayList;
import java.util.List;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import net.named_data.jndn.Name;
import nms.rib.Fib;
import nms.rib.Rib;
import nms.rib.RibAction;
import nms.rib.Route;
import nms.rib.commands.FibCommand;
import nms.rib.service.RibHandler;
import nms.rib.service.RibService;
import nms.rib.service.RibServiceImpl;

public class RibVerticle extends AbstractVerticle {

	private static final Logger LOG = LoggerFactory.getLogger(RibVerticle.class);
	private static String RIB_EVENTBUS_ADDRESS = "rib-verticle.eventbus";
	private static String FORWARDER_EVENTBUS_ADDRESS = "rib-verticle.eventbus";
	private RibService ribService;
	private Fib currentFib;

	@Override
	public void start(Promise<Void> startFuture) {
		LOG.info("starting " + this.getClass().getName());
		this.ribService = new RibServiceImpl();
		LOG.info("instantiated RIB Service");

		currentFib = new Fib(); // the agent starts with an empty fib
		// setup EventBus
		this.consumeEventBus(RIB_EVENTBUS_ADDRESS);

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
						sendFibCommands(rib);
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
						sendFibCommands(rib);
					}
				});
			}
		});

	}

	private void sendFibCommands(Rib rib) {
		Fib fib = rib.toFib();
		// get the fib commands that need to be sent to the forward verticle
		List<FibCommand> fibCommands = fib.compare(currentFib);
		List<Future> allFutures = new ArrayList<>();
		fibCommands.forEach(command -> {
			allFutures.add(sendCommandFuture(command));
		});
		CompositeFuture.all(allFutures).onComplete(ar -> {
			if (ar.succeeded()) {
				LOG.debug("all commands were executed successfully");
			} else {
				LOG.debug("something went bad");
			}
		});
	}

	private Future<Void> sendCommandFuture(FibCommand command) {
		Promise<Void> promise = Promise.promise();
		vertx.eventBus().request(FORWARDER_EVENTBUS_ADDRESS, command.toEventBusFormat(), ar -> {
			if (ar.succeeded()) {
				promise.complete();
			} else {
				promise.fail(ar.cause());
			}
		});
		return promise.future();
	}

	@Override
	public void stop() {
		LOG.info("stopping " + this.getClass().getName());
	}

}
