package nms.forwarder.verticle;

import java.util.Arrays;
import java.util.List;

import com.github.arteam.simplejsonrpc.client.JsonRpcClient;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import nms.forwarder.api.EventBusEndpoint;
import nms.forwarder.api.RestEndpoint;
import nms.forwarder.model.face.Face;
import nms.forwarder.model.face.FaceData;
import nms.forwarder.model.fib.fibErase;
import nms.forwarder.model.fib.fibFind;
import nms.forwarder.model.fib.fibInsert;
import nms.forwarder.model.version.Version;
import nms.forwarder.rpc.RpcTransport;

public class ForwarderVerticle extends AbstractVerticle {

	Logger logger = LoggerFactory.getLogger(ForwarderVerticle.class.getName());
	private static int PORT = 8080;
	private static String EVENTBUS_ADDRESS = "fw-verticle.eventbus";

	@Override
	public void start(Promise<Void> promise) {

		// setup EventBus
		this.consumeEventBus(EVENTBUS_ADDRESS);

		// setup HTTP server
		Router router = Router.router(vertx);
		this.registerHandlers(router);
		this.startHttpServer(router, promise);

	}

	private void consumeEventBus(String address) {
		vertx.eventBus().consumer(address, (message) -> {
			JsonObject body = (JsonObject) message.body();
			String action = body.getString("action");

			if (action == EventBusEndpoint.GET_VERSION.getName()) {
				logger.debug("[eventbus] action = " + EventBusEndpoint.GET_VERSION.getName());
				Version version = this.getVersion_JsonRPC();

				// send response back to requester through EventBus
				message.replyAndRequest(version.toJsonObject(), null);
			}

			if (action == EventBusEndpoint.GET_FACES.getName()) {
				logger.debug("[eventbus] action = " + EventBusEndpoint.GET_FACES.getName());
				JsonArray array = this.getFaces_JsonRPC();
				logger.debug("[eventbus] received " + array.size() + " faces");
				message.replyAndRequest(array, null);
			}

			if (action == EventBusEndpoint.GET_FACE.getName()) {
				logger.debug("[eventbus] action = " + EventBusEndpoint.GET_FACE.getName());
				int id = body.getInteger("id");
				FaceData face = this.getFace_JsonRPC(id);
				System.out.println("Face = " + face.toString());
			}

			if (action == EventBusEndpoint.ADD_FACE.getName()) {
				logger.debug("[eventbus] action = " + EventBusEndpoint.ADD_FACE.getName());
			}

			if (action == EventBusEndpoint.GET_FIB.getName()) {
				logger.debug("[eventbus] action = " + EventBusEndpoint.GET_FIB.getName());
				JsonArray fibAarray = this.getFibs_JsonRPC();
				System.out.println("Face = " + fibAarray.toString());
			}

			if (action == EventBusEndpoint.GET_FIB_ENTRY.getName()) {
				logger.debug("[eventbus] action = " + EventBusEndpoint.GET_FIB_ENTRY.getName());
				String name = body.getString("name");
				fibFind fib = this.getFib_JsonRPC(name);
				System.out.println("Face = " + fib.toString());
			}

			if (action == EventBusEndpoint.REGISTER_PREFIX.getName()) {
				logger.debug("[eventbus] action = " + EventBusEndpoint.REGISTER_PREFIX.getName());
				String name = body.getString("name");

				Object[] nexthopsJson = body.getJsonArray("nexthops").getList().toArray();
				Integer[] nexthops = Arrays.copyOf(nexthopsJson, nexthopsJson.length, Integer[].class);

				this.insertFib_JsonRPC(name, nexthops);
				System.out.println("Inserted " + name);
			}

			if (action == EventBusEndpoint.UNREGISTER_PREFIX.getName()) {
				logger.debug("[eventbus] action = " + EventBusEndpoint.UNREGISTER_PREFIX.getName());
				String name = body.getString("name");
				fibErase fib = this.eraseFib_JsonRPC(name);
				System.out.println("Erased " + fib.toString());
			}
		});

	}

	private void startHttpServer(Router router, Promise<Void> promise) {
		vertx.createHttpServer().requestHandler(router).listen(PORT, result -> {
			if (result.succeeded()) {
				logger.info("HTTP server listening on port 8080");
				promise.complete();
			} else {
				System.out.println(result.cause());
				promise.fail(result.cause());
			}
		});
	}

	private void registerHandlers(Router router) {
		router.route().handler(BodyHandler.create());
		router.get(RestEndpoint.GET_VERSION.getEndpoint()).handler(this::getVersion);
		router.get(RestEndpoint.GET_FACES.getEndpoint()).handler(this::getFaces);
		router.get(RestEndpoint.GET_FACE.getEndpoint()).handler(this::getFace);
		router.post(RestEndpoint.ADD_FACE.getEndpoint()).handler(this::addFace);
		router.get("/api/fibs").handler(this::getFibs);
		router.get("/api/fibs/:name").handler(this::getFib);
		router.get("/api/fibs/:name").handler(this::eraseFib);
		router.post("/api/fibs").handler(this::insertFib);
	}

	private void getVersion(RoutingContext context) {
		HttpServerResponse response = context.response();
		Version version = this.getVersion_JsonRPC();

		response.putHeader("content-type", "application/json")
				.end(new JsonObject().put("version", version.toJsonObject()).encodePrettily());
	}

	private Version getVersion_JsonRPC() {
		RpcTransport tp = new RpcTransport();
		JsonRpcClient client = new JsonRpcClient(tp);
		Version version = client.createRequest().id("599851553").method("Version.Version").param("_", 0)
				.returnAs(Version.class).execute();
		System.out.println(version.toString());

		return version;
	}

	// Face.List
	private void getFaces(RoutingContext context) {
		HttpServerResponse response = context.response();
		JsonArray array = this.getFaces_JsonRPC();

		response.putHeader("content-type", "application/json")
				.end(new JsonObject().put("faces", array).encodePrettily());
	}

	private JsonArray getFaces_JsonRPC() {
		RpcTransport tp = new RpcTransport();
		JsonRpcClient client = new JsonRpcClient(tp);
		List<Face> faces = client.createRequest().id("1").method("Face.List").param("_", 0).returnAsList(Face.class)
				.execute();
		JsonArray array = new JsonArray(faces);

		return array;
	}

	// Face.Get
	private void getFace(RoutingContext context) {
		HttpServerResponse response = context.response();
		int id = Integer.valueOf(context.request().getParam("id"));
		FaceData face = this.getFace_JsonRPC(id);

		response.putHeader("content-type", "application/json").end(face.toString());
	}

	private FaceData getFace_JsonRPC(int id) {
		RpcTransport tp = new RpcTransport();
		JsonRpcClient client = new JsonRpcClient(tp);
		FaceData face = client.createRequest().id("2").method("Face.Get").param("Id", id).returnAs(FaceData.class)
				.execute();

		return face;
	}

	private void addFace(RoutingContext context) {
		JsonObject face = context.getBodyAsJson();
		String faceId = face.getString("faceId");

		HttpServerResponse response = context.response();
		this.addFace_JsonRPC();
		JsonObject payload = new JsonObject().put("message", "insert new face, faceId=" + faceId);
		response.putHeader("content-type", "application/json").end(payload.encodePrettily());
	}

	private void addFace_JsonRPC() {
		// TODO Auto-generated method stub

	}

	// Fib.List
	private void getFibs(RoutingContext context) {
		HttpServerResponse response = context.response();
		JsonArray fibs = this.getFibs_JsonRPC();

		response.putHeader("content-type", "application/json").end(new JsonObject().put("fibs", fibs).encodePrettily());
	}

	private JsonArray getFibs_JsonRPC() {
		RpcTransport tp = new RpcTransport();
		JsonRpcClient client = new JsonRpcClient(tp);

		List<String> fibs = client.createRequest().id("1").method("Fib.List").param("_", 0).returnAsList(String.class)
				.execute();

		System.out.println(fibs.toString());
		JsonArray array = new JsonArray(fibs);

		return array;
	}

	// Fib.Find
	private void getFib(RoutingContext context) {
		String name = "/" + context.request().getParam("name");
		HttpServerResponse response = context.response();
		fibFind fib = this.getFib_JsonRPC(name);

		response.putHeader("content-type", "application/json").end(fib.toString());
	}

	private fibFind getFib_JsonRPC(String name) {
		RpcTransport tp = new RpcTransport();
		JsonRpcClient client = new JsonRpcClient(tp);
		fibFind fib = client.createRequest().id("299851553").method("Fib.Find").param("Name", name)
				.returnAs(fibFind.class).execute();

		return fib;
	}

	// Fib.Insert
	private void insertFib(RoutingContext context) {
		JsonObject fib = context.getBodyAsJson();
		String name = fib.getString("name");
		Object[] nexthopsJson = fib.getJsonArray("nexthops").getList().toArray();
		Integer[] nexthops = Arrays.copyOf(nexthopsJson, nexthopsJson.length, Integer[].class);

		HttpServerResponse response = context.response();
		this.insertFib_JsonRPC(name, nexthops);
		JsonObject payload = new JsonObject().put("message", "Inserted new fib with name: " + name);

		response.putHeader("content-type", "application/json").end(payload.encodePrettily());
	}

	private fibInsert insertFib_JsonRPC(String name, Integer[] nexthops) {
		RpcTransport tp = new RpcTransport();
		JsonRpcClient client = new JsonRpcClient(tp);
		fibInsert ins = client.createRequest().id("599851553").method("Fib.Insert").param("Name", name)
				.param("Nexthops", nexthops).returnAs(fibInsert.class).execute();

		return ins;
	}

	// Fib.Erase
	private void eraseFib(RoutingContext context) {
		String name = context.request().getParam("name");
		HttpServerResponse response = context.response();
		this.eraseFib_JsonRPC(name);
		JsonObject payload = new JsonObject().put("message", "Erased fib with name: " + name);

		response.putHeader("content-type", "application/json").end(payload.encodePrettily());
	}

	private fibErase eraseFib_JsonRPC(String name) {
		RpcTransport tp = new RpcTransport();
		JsonRpcClient client = new JsonRpcClient(tp);
		fibErase er = client.createRequest().id("599851553").method("Fib.Erase").param("Name", name)
				.returnAs(fibErase.class).execute();

		return er;
	}

	@Override
	public void stop() {
//		List<Future> futures = new ArrayList<>();
		logger.info("stopping " + this.getClass().getName());
	}

}