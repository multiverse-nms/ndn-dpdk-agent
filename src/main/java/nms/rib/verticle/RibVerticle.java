package nms.rib.verticle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import com.thetransactioncompany.jsonrpc2.JSONRPC2ParseException;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import net.named_data.jndn.Name;
import nms.VerticleAdress;
import nms.forwarder.api.EventBusEndpoint;
import nms.rib.Fib;
import nms.rib.Rib;
import nms.rib.Route;
import nms.rib.commands.FibCommand;
import nms.rib.service.RibService;
import nms.rib.service.RibServiceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RibVerticle extends AbstractVerticle {

	private static final Logger LOG = LoggerFactory.getLogger(RibVerticle.class);
	private RibService ribService;
	private Fib currentFib;

	@Override
	public void start(Promise<Void> promise) {
		LOG.info("starting verticle");
		this.ribService = new RibServiceImpl();
		LOG.info("instantiated the RIB service");
		currentFib = new Fib(); // the agent starts with an empty fib
		// setup EventBus
		this.consumeEventBus(VerticleAdress.rib_verticle.getAdress(), promise);

	}

	private void consumeEventBus(String address, Promise<Void> promise) {
		vertx.eventBus().consumer(address, (message) -> {
			String body = message.body().toString();
			JSONRPC2Request req;
			try {
				req = JSONRPC2Request.parse(body);

			} catch (JSONRPC2ParseException e) {
				LOG.error("error while parsing the json rpc request {}", e.getMessage());
				return;
			}

			String method = req.getMethod();
			LOG.debug("METHOD={}", method);

			Map<String, Object> params = req.getNamedParams();

			if (method.equals(EventBusEndpoint.ADD_ROUTE.getName())) {
				Name prefix = new Name((String) params.get("prefix"));
				Number faceNumber = (Number) params.get("faceFwdId");
				int faceId = faceNumber.intValue();
				Number originNumber = (Number) params.get("origin");
				int origin = originNumber.intValue();
				Number costNumber = (Number) params.get("cost");
				int cost = costNumber.intValue();
				LOG.debug("prefix={}, faceId={}, origin={}, cost={}", prefix.toUri(), faceId, origin, cost);

				Route route = new Route(prefix, faceId, origin);
				route.setCost(cost);
				ribService.addRoute(route).onComplete(ar -> {
					sendFibCommands(ar.result()).onComplete(ar1 -> {
						if (ar1.succeeded()) {
							message.reply(new JsonObject().put("status", "success").put("message", "FIB updated"));
						} else {
							message.reply(
									new JsonObject().put("status", "error").put("message", "something went wrong"));
						}
					});
				});
			}

			if (method.equals(EventBusEndpoint.REMOVE_ROUTE.getName())) {
				Name name = new Name((String) params.get("prefix"));
				Number faceNumber = (Number) params.get("faceId");
				int faceId = faceNumber.intValue();
				Number originNumber = (Number) params.get("origin");
				int origin = originNumber.intValue();

				ribService.removeRoute(new Route(name, faceId, origin)).onComplete(ar -> {
					sendFibCommands(ar.result()).onComplete(ar1 -> {
						if (ar1.succeeded()) {
							message.reply(new JsonObject().put("status", "success").put("message", "FIB updated"));
						} else {
							message.reply(
									new JsonObject().put("status", "error").put("message", "something went wrong"));
						}
					});
				});
			}
		});
		promise.complete();
	}

	private Future<Void> sendFibCommands(Rib rib) {
		Promise<Void> promise = Promise.promise();
		Fib fib = rib.toFib();
		// get the fib commands that need to be sent to the forward verticle
		List<FibCommand> fibCommands = fib.compare(currentFib);
		currentFib = fib;
		@SuppressWarnings("rawtypes")
		List<Future> allFutures = new ArrayList<>();
		fibCommands.forEach(command -> {
			if (command.hasNexthops()) {
				LOG.debug("COMMAND={}", command);
				allFutures.add(sendCommandFuture(command));
			}

		});
		CompositeFuture.all(allFutures).onComplete(ar -> {
			if (ar.succeeded()) {
				LOG.info("all commands were executed successfully");
				promise.complete();
			} else {
				LOG.error("something went bad");
				promise.fail(ar.cause());
			}
		});
		return promise.future();
	}

	private Future<Void> sendCommandFuture(FibCommand command) {
		Promise<Void> promise = Promise.promise();
		LOG.info("COMMAND={}", command);
		vertx.eventBus().request(VerticleAdress.forwarder_verticle.getAdress(), command.toJsonRpcRequest(), ar -> {
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
		LOG.info("stopping verticle");
	}

}
