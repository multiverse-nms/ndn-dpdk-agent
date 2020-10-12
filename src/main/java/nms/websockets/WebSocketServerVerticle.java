package nms.websockets;

import java.util.HashMap;
import java.util.Map;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.json.JsonObject;
import nms.VerticleAdress;
import nms.forwarder.api.EventBusEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebSocketServerVerticle extends AbstractVerticle {

	private static int PORT = 9001;
	private static final Logger LOG = LoggerFactory.getLogger(WebSocketServerVerticle.class);
	private static String forwarderVerticleAddress = VerticleAdress.forwarder_verticle.getAdress();
	private static String ribVerticleAddress = VerticleAdress.rib_verticle.getAdress();
	private Map<String, String> verticlesMap = new HashMap<>();
	
	public void start(Promise<Void> promise) {
		initVerticlesMap();
		startServer(vertx, promise);
		LOG.info("websocket server started and listening on port " + config().getInteger("ws.port", PORT));
	}


	private void startServer(Vertx vertx, Promise<Void> promise) {
		HttpServer server = vertx.createHttpServer();
		LOG.info("starting websocket server");
		server.webSocketHandler(new Handler<ServerWebSocket>() {
			@Override
			public void handle(ServerWebSocket socket) {
				socket.textMessageHandler(msg -> {
					JsonObject json = new JsonObject(msg);
					LOG.debug("received request from client:\n{}", json.encodePrettily());
					String method = json.getString("method");
					String correspondingVerticle = verticlesMap.get(method);
					LOG.info("forwaring request to verticle {} ", correspondingVerticle);
					sendToVerticle(correspondingVerticle, json).onComplete(ar -> {
						if (ar.succeeded()) {
							if (ar.result() != null) {
								LOG.info("response from from " + correspondingVerticle + ": " + ar.result().encodePrettily());
								socket.writeTextMessage(ar.result().toString());
							}
						} else {
							ar.cause().printStackTrace();
						}
					});
				});
			}
		}).listen(config().getInteger("ws.port", PORT));
		promise.complete();
	}

	private Future<JsonObject> sendToVerticle(String address, JsonObject json) {
		Promise<JsonObject> promise = Promise.promise();
		vertx.eventBus().request(address, json, ar -> {
			if (ar.succeeded()) {
				promise.complete((JsonObject) ar.result().body());
			} else {
				promise.fail(ar.cause());
			}
		});
		return promise.future();
	}

	public void stop(Promise<Void> startFuture) {
	}
	
	private void initVerticlesMap() {
		verticlesMap.put(EventBusEndpoint.LIST_FACES.getName(), forwarderVerticleAddress);
		verticlesMap.put(EventBusEndpoint.GET_FACE.getName(), forwarderVerticleAddress);
		verticlesMap.put(EventBusEndpoint.CREATE_FACE.getName(), forwarderVerticleAddress);
		verticlesMap.put(EventBusEndpoint.DESTROY_FACE.getName(), forwarderVerticleAddress);
		verticlesMap.put(EventBusEndpoint.LIST_PORTS.getName(), forwarderVerticleAddress);
		verticlesMap.put(EventBusEndpoint.LIST_FIB.getName(), forwarderVerticleAddress);
		verticlesMap.put(EventBusEndpoint.ADD_ROUTE.getName(), ribVerticleAddress);
		verticlesMap.put(EventBusEndpoint.REMOVE_ROUTE.getName(), ribVerticleAddress);	
	}

}
