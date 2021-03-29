package nms.restclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private Comparable<Face> typeFace = new Comparable<Face>();
	private Comparable<Route> typeRoute = new Comparable<Route>();

	private Configuration running;

	private Map<Integer, Face> faces;

	public ConfigurationHandler(Vertx vertx) {
		this.vertx = vertx;
		this.jsonRpcHelper = new JsonRpcHelper();
		this.running = new Configuration();
		this.faces = new HashMap<>();
	}

	public Future<Void> send(List<JsonObject> commands) {
		Promise<Void> promise = Promise.promise();

		List<JsonObject> pendingAddRouteCommands = new ArrayList<>();
		List<JsonObject> pendingCreateFaceCommands = new ArrayList<>();
		List<JsonObject> pendingDestroyFaceCommands = new ArrayList<>();
		List<JsonObject> pendingRemoveRouteCommands = new ArrayList<>();

		commands.forEach(command -> {
			if (command.getString("method").equals("Face.Create")) {
				pendingCreateFaceCommands.add(command);
			}
			if (command.getString("method").equals("Face.Destroy")) {
				LOG.info("NEW pendingDestroyFaceCommands {}", command);
				pendingDestroyFaceCommands.add(command);
			}

			if (command.getString("method").equals("Route.Add")) {
				pendingAddRouteCommands.add(command);
			}

			if (command.getString("method").equals("Route.Remove")) {
				pendingRemoveRouteCommands.add(command);
			}
		});

		Future<Void> updatingProcess = removeRoutes(pendingRemoveRouteCommands)
				.compose(v -> destroyFaces(pendingDestroyFaceCommands))
				.compose(v -> createFaces(pendingCreateFaceCommands))
				.compose(v-> addRoutes(pendingAddRouteCommands));

		updatingProcess.onComplete(ar -> {
			if (ar.succeeded()) {
				LOG.info("updated forwarder with new configuration");
			} else {
				LOG.info("something went wrong");
			}
		});

		promise.complete();
		return promise.future();
	}

	private int getFaceId(JsonObject json) {
		int faceId = 0;
		for (Face face : this.running.getFaces()) {
			if (face.getCtrlId() == json.getJsonObject("params").getInteger("faceId")) {
				faceId = face.getFwdId();
				LOG.debug("faceId = {}", faceId);
			}
		}
		return faceId;
	}

	private Future<Void> removeRoutes(List<JsonObject> commands) {
		Promise<Void> promise = Promise.promise();
		List<Future>  routeRemovalFutures = new ArrayList<>();
		commands.forEach(command -> {
			routeRemovalFutures.add(removeRoute(command));
		});
		CompositeFuture.all(routeRemovalFutures).onComplete(ar -> {
			if (ar.succeeded()) {
				LOG.info("all routes were removed successfully");
				promise.complete();
			}
			else {
				promise.fail("could not remove all routes: " + ar.cause());
			}
		});
		return promise.future();
	}

	private Future<Void> destroyFaces(List<JsonObject> commands) {
		Promise<Void> promise = Promise.promise();
		List<Future> faceRemovalFutures = new ArrayList<>();
		commands.forEach(command -> {
			faceRemovalFutures.add(destroyFace(command));
		});
		CompositeFuture.all(faceRemovalFutures).onComplete(ar -> {
			if (ar.succeeded()) {
				LOG.info("all faces were destroyed successfully");
				promise.complete();
			}
			else {
				promise.fail("could not destroy all faces: " + ar.cause());
			}
		});
		return promise.future();
	}

	private Future<Void> createFaces(List<JsonObject> commands) {
		Promise<Void> promise = Promise.promise();
		List<Future> faceCreationFutures = new ArrayList<>();
		commands.forEach(command -> {
			if (command.getString("method").equals("Face.Create")) {
				faceCreationFutures.add(createFace(command));
			}
		});
		CompositeFuture.all(faceCreationFutures).onComplete(ar -> {
			if (ar.succeeded()) {
				LOG.info("all faces were created successfully");
				promise.complete();
			}
			else {
				promise.fail("could not create all faces: " + ar.cause());
			}
		});
		return promise.future();
	}

	private Future<Void> addRoutes(List<JsonObject> commands) {
		Promise<Void> promise = Promise.promise();
		List<Future> routeCreationFutures = new ArrayList<>();
		commands.forEach(routeCmd -> {
			routeCreationFutures.add(addRoute(routeCmd));
		});
		CompositeFuture.all(routeCreationFutures).onComplete(ar -> {
			if (ar.succeeded()) {
				LOG.info("all routes were added successfully");
				promise.complete();
			}
			else {
				promise.fail("could not add all routes: " + ar.cause());
			}
		});
		return promise.future();
	}

	

	private Future<Face> createFace(JsonObject msg) {
		Promise<Face> promise = Promise.promise();
		vertx.eventBus().request(VerticleAdress.forwarder_verticle.getAdress(), msg, ar -> {
			if (ar.succeeded()) {
				JsonObject faceObj = (JsonObject) ar.result().body();
				Face newFace = addFaceToLocalConfig(faceObj);
				promise.complete(newFace);
			} else {
				promise.fail(ar.cause());
			}
		});
		return promise.future();
	}

	private Face addFaceToLocalConfig(JsonObject faceObj) {
		String local = faceObj.getString("local");
		String remote = faceObj.getString("remote");
		String port = faceObj.getString("port");
		String scheme = faceObj.getString("scheme");
		int faceFwdId = faceObj.getInteger("faceId");
		int faceCtrlId = faceObj.getInteger("ctrlId");
		Face newFace = new Face(faceCtrlId, local, port, remote, scheme);
		newFace.setFwdId(faceFwdId);
		LOG.debug("adding face {} to local config", faceObj);
		this.running.addFaceIDsMapping(faceCtrlId, faceFwdId);
		this.running.addFace(newFace);
		return newFace;
	}

	private Future<Void> destroyFace(JsonObject msg) {
		Promise<Void> promise = Promise.promise();
		vertx.eventBus().request(VerticleAdress.forwarder_verticle.getAdress(), msg, ar -> {
			if (ar.succeeded()) {
				LOG.info("received reply: " + ar.result().body());
				int faceId = removeFaceFromLocalConfig(msg);
				LOG.info("removed face with ID {} ", faceId);
				promise.complete();
			} else {
				promise.fail(ar.cause());
			}
		});
		return promise.future();
	}

	private int removeFaceFromLocalConfig(JsonObject msg) {
		JsonObject faceObj = msg.getJsonObject("params");
		int faceId = faceObj.getInteger("fwdId");
		this.running.removeFace(faceId);
		return faceId;
	}

	private Future<Route> addRoute(JsonObject msg) {
		Promise<Route> promise = Promise.promise();
		updateFaceId(msg);
		vertx.eventBus().request(VerticleAdress.rib_verticle.getAdress(), msg, ar -> {
			if (ar.succeeded()) {
				System.out.println("received reply: " + ar.result().body());
				Route newRoute = addRouteToLocalConfig(msg);
				promise.complete(newRoute);
			} else {
				promise.fail(ar.cause());
			}
		});
		return promise.future();
	}

	private void updateFaceId(JsonObject msg) {
		LOG.debug("before {}", msg);
		int faceFwdId = getFaceId(msg);
//		msg.getJsonObject("params").remove("faceId");
		msg.getJsonObject("params").put("faceFwdId", faceFwdId);
		LOG.debug("after {}", msg);
	}

	private Route addRouteToLocalConfig(JsonObject json) {
		JsonObject routeObj = json.getJsonObject("params");
		String prefix = routeObj.getString("prefix");
		int origin = routeObj.getInteger("origin");
		int cost = routeObj.getInteger("cost");
		int faceCtrlId = routeObj.getInteger("faceId");
		int faceFwdId = routeObj.getInteger("faceFwdId"); 
		Route newRoute = new Route(prefix, faceCtrlId, origin, cost);
		newRoute.setFaceFwdId(faceFwdId);
		LOG.debug("adding route {} to local config", newRoute);
		this.running.addRoute(newRoute);
		return newRoute;
	}

	private Future<Void> removeRoute(JsonObject msg) {
		Promise<Void> promise = Promise.promise();
		LOG.info("REMOVE ROUTE: " + msg);
		vertx.eventBus().request(VerticleAdress.rib_verticle.getAdress(), msg, ar -> {
			if (ar.succeeded()) {
				LOG.info("received reply: " + ar.result().body());
				removeRouteFromLocalConfig(msg);
				promise.complete();
			} else {
				promise.fail(ar.cause());
			}
		});
		return promise.future();
	}
	
	private void removeRouteFromLocalConfig(JsonObject msg) {
		String prefix = msg.getJsonObject("params").getString("prefix");
		int faceId = msg.getJsonObject("params").getInteger("faceId");
		int origin = msg.getJsonObject("params").getInteger("origin");
		this.running.removeRoute(new Route(prefix, faceId, origin, 0));
	}

	public Future<List<JsonObject>> compare(Configuration candidate) {

		LOG.debug("[RUNNING]   Faces {}", running.getFacesMap());
		LOG.debug("[CANDIDATE] Faces {}", candidate.getFacesMap());

		Promise<List<JsonObject>> promise = Promise.promise();
		List<JsonObject> json = new ArrayList<>();

		typeFace.getDifferenceOfLists(running.getFaces(), candidate.getFaces()).forEach(face -> {
			json.add(jsonRpcHelper.makeNewFaceCommand(face));
		});

		typeFace.getUnionOfLists(running.getFaces(), candidate.getFaces()).forEach(face -> {
			if (!(typeFace.contains(candidate.getFaces(), face))) {
				json.add(jsonRpcHelper.makeDestroyFaceCommand(face));
			}
		});

		LOG.debug("[RUNNING]   Routes() {}", running.getRoutes());
		LOG.debug("[CANDIDATE] Routes() {}", candidate.getRoutes());

		typeRoute.getDifferenceOfLists(running.getRoutes(), candidate.getRoutes()).forEach(route -> {
			json.add(jsonRpcHelper.makeNewRouteCommand(route));
		});

		typeRoute.getUnionOfLists(running.getRoutes(), candidate.getRoutes()).forEach(route -> {
			if (!(typeRoute.contains(candidate.getRoutes(), route))) {
				json.add(jsonRpcHelper.makeDestroyRouteCommand(route));
			}
		});
		promise.complete(json);
		return promise.future();
	}

	

	private Configuration getRunningConfig() {
		return this.running;
	}
}
