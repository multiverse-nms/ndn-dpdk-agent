package nms.restclient;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import nms.VerticleAdress;

public class ConfigurationHandler {

	private static final Logger LOG = LoggerFactory.getLogger(ConfigurationHandler.class);
	private Vertx vertx;
	private JsonRpcHelper jsonRpcHelper;
	
	public ConfigurationHandler(Vertx vertx) {
		this.vertx = vertx;
		this.jsonRpcHelper = new JsonRpcHelper();
	}

	public Future<Void> send(Configuration config) {
		Promise<Void> promise = Promise.promise();
		ArrayList<Face> faces = config.getFaces();
		ArrayList<Route> routes = config.getRoutes();
		sendFaces(faces).compose(v -> sendRoutes(routes)).onComplete(ar -> {
			if (ar.succeeded()) {
				promise.complete();
			}
		});
		return promise.future();
	}

	private Future<Void> sendFaces(ArrayList<Face> faces) {
		Promise<Void> promise = Promise.promise();
		List<Future> futures = new ArrayList<>();
		faces.forEach(face -> {
			JsonObject json = jsonRpcHelper.makeNewFaceCommand(face);
			futures.add(sendOnEventbus(VerticleAdress.forwarder_verticle.getAdress(), json));
		});
		CompositeFuture.all(futures).onComplete(res -> {
	        if (res.succeeded())    {
	            promise.complete();
	        } else  {
	            LOG.error("failed to create all faces, reason: {} ", res.cause());
	            promise.fail(res.cause());
	        }
	    });		
		return promise.future();
	}

	private Future<Void> sendRoutes(ArrayList<Route> routes) {
		Promise<Void> promise = Promise.promise();
		List<Future> futures = new ArrayList<>();
		routes.forEach(route -> {
			JsonObject json = jsonRpcHelper.makeNewRouteCommand(route);
			futures.add(sendOnEventbus(VerticleAdress.rib_verticle.getAdress(), json));
		});
		CompositeFuture.all(futures).onComplete(res -> {
	        if (res.succeeded())    {
	            promise.complete();
	        } else  {
	            LOG.error("failed to create all routes, reason: {} ", res.cause());
	            promise.fail(res.cause());
	        }
	    });		
		return promise.future();
	}

	public Future<Configuration> compare(Configuration running, Configuration candidate) {

		Promise<Configuration> promise = Promise.promise();
		Configuration config = new Configuration();

		ArrayList<Face> candidateFaces = new ArrayList<>(candidate.getFaces());
		ArrayList<Face> prevFaces = new ArrayList<>(running.getFaces());

		if (candidateFaces.equals(prevFaces)) {
			prevFaces.retainAll(candidateFaces);
		} else {

			prevFaces.retainAll(candidateFaces);
			candidateFaces.removeAll(prevFaces);

			candidateFaces.forEach(s -> {
				prevFaces.add(s);
			});
		}

		ArrayList<Route> candidateRoutes = new ArrayList<>(candidate.getRoutes());
		ArrayList<Route> prevRoutes = new ArrayList<>(running.getRoutes());

		if (candidateRoutes.equals(prevRoutes)) {
			prevRoutes.retainAll(candidateRoutes);
		} else {

			prevRoutes.retainAll(candidateRoutes);
			candidateRoutes.removeAll(prevRoutes);

			candidateRoutes.forEach(s -> {
				prevRoutes.add(s);
			});
		}

		config.setFaces(prevFaces);
		config.setRoutes(prevRoutes);

		promise.complete(config);

		return promise.future();

	}

	private Future<Void> sendOnEventbus(String address, JsonObject msg) {
		Promise<Void> promise = Promise.promise();
		vertx.eventBus().request(address, msg, ar -> {
			if (ar.succeeded()) {
				System.out.println("received reply: " + ar.result().body());
				promise.complete();
			} else {
				promise.fail(ar.cause());
			}
		});	
		return promise.future();
	}

}
