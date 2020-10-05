package nms.forwarder.verticle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.github.arteam.simplejsonrpc.client.JsonRpcClient;
import com.github.arteam.simplejsonrpc.client.builder.RequestBuilder;
import com.github.arteam.simplejsonrpc.client.exception.JsonRpcException;
import com.github.arteam.simplejsonrpc.core.domain.ErrorMessage;
import com.thetransactioncompany.jsonrpc2.JSONRPC2ParseException;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import nms.forwarder.api.EventBusEndpoint;
import nms.forwarder.model.face.Face;
import nms.forwarder.model.face.FaceData;
import nms.forwarder.model.fib.FibEntry;
import nms.forwarder.model.version.Version;
import nms.forwarder.model.port.PortInfo;
import nms.forwarder.rpc.RpcTransport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ForwarderVerticle extends AbstractVerticle {

	private static Logger LOG = LoggerFactory.getLogger(ForwarderVerticle.class.getName());
	private static String EVENTBUS_ADDRESS = "fw-verticle.eventbus";

	@Override
	public void start(Promise<Void> promise) {
		LOG.info("starting verticle");
		// setup EventBus
		this.consumeEventBus(EVENTBUS_ADDRESS, promise);
	}

	private void consumeEventBus(String address, Promise<Void> promise) {
		vertx.eventBus().consumer(address, (message) -> {
			JSONRPC2Request req = null;
			String method = "";
			String body = message.body().toString();

			try {
				req = JSONRPC2Request.parse(body);
			} catch (JSONRPC2ParseException e) {
				LOG.error("error while parsing the json rpc request {}", e.getMessage());
				return;
			}

			method = req.getMethod();
			LOG.debug("METHOD={}" + method);
			
			if (method.equals(EventBusEndpoint.GET_VERSION.getName())) {
				this.getVersioFuture_JsonRPC(req).onComplete(ar -> {
					if (ar.succeeded()) {
						message.reply(ar.result().toJsonObject());
					} else {
						message.reply(new JsonObject().put("status", "error").put("message", ar.cause().getMessage()));
					}
				});
			}

			if (method.equals(EventBusEndpoint.CREATE_FACE.getName())) {
				this.createFaceFuture_JsonRPC(req).onComplete(ar -> {
					if (ar.succeeded()) {
						message.reply(ar.result().toJsonObject());
					} else {
						message.reply(new JsonObject().put("status", "error").put("message", ar.cause().getMessage()));
					}
				});

			}

			if (method.equals(EventBusEndpoint.DESTROY_FACE.getName())) {
				this.destroyFaceFuture_JsonRPC(req).onComplete(ar -> {
					if (ar.succeeded()) {
						message.reply(new JsonObject().put("status", "success").put("message", "face with Id " + ar.result() + " was destroyed"));
					} else {
						message.reply(new JsonObject().put("status", "error").put("message", ar.cause().getMessage()));
					}
				});

			}
			
			if (method.equals(EventBusEndpoint.LIST_PORTS.getName())) {
				this.getPortsFuture_JsonRPC(req).onComplete(ar -> {
					if (ar.succeeded()) {
						List<PortInfo> portsList = ar.result();
						JsonArray portsJson = new JsonArray();
						portsList.forEach(port -> {
							portsJson.add(port.toJsonObject());
						});
						JsonObject result = new JsonObject().put("ports", portsJson);
						LOG.debug("PORTS={}", result.encodePrettily());
						message.reply(result);
					} else {
						message.reply(new JsonObject().put("status", "error").put("message", ar.cause().getMessage()));
					}
				});
			}

			if (method.equals(EventBusEndpoint.LIST_FACES.getName())) {
				this.getFacesFuture_JsonRPC(req).onComplete(ar -> {
					if (ar.succeeded()) {
						List<Face> facesList = ar.result();
						JsonArray facesJson = new JsonArray();
						facesList.forEach(face -> {
							facesJson.add(face.toJsonObject());
						});
						JsonObject result = new JsonObject().put("faces", facesJson);
						LOG.debug("FACES={}", result.encodePrettily());
						message.reply(result);
					} else {
						message.reply(new JsonObject().put("status", "error").put("message", ar.cause().getMessage()));
					}
				});
			}
			
			if (method.equals(EventBusEndpoint.LIST_FIB.getName())) {
				this.getFibFuture_JsonRPC(req).onComplete(ar -> {
					if (ar.succeeded()) {
						List<String> fibList = ar.result();
						JsonArray fibJson = new JsonArray();
						fibList.forEach(entry -> {
							fibJson.add(entry);
						});
						JsonObject result = new JsonObject().put("fib", fibJson);
						LOG.debug("FIB={}", result.encodePrettily());
						message.reply(result);
					} else {
						message.reply(new JsonObject().put("status", "error").put("message", ar.cause().getMessage()));
					}
				});
			}

			if (method.equals(EventBusEndpoint.GET_FACE.getName())) {
				this.getFaceFuture_JsonRPC(req).onComplete(ar -> {
					if (ar.succeeded()) {
						LOG.debug("FACE={}", ar.result().toJsonObject().encodePrettily());
						message.reply(ar.result().toJsonObject());
					} else {
						message.reply(new JsonObject().put("status", "error").put("message", ar.cause().getMessage()));
					}

				});
			}

			if (method.equals(EventBusEndpoint.INSERT_FIB.getName())) {
				this.insertFibFuture_JsonRPC(req).onComplete(ar -> {
					if (ar.succeeded()) {
						message.reply(new JsonObject().put("status", "success").put("message", "FIB was updated"));
					} else {
						message.reply(new JsonObject().put("status", "error").put("message", ar.cause().getMessage()));
					}

				});
			}

			if (method.equals(EventBusEndpoint.ERASE_FIB.getName())) {
				this.eraseFibFuture_JsonRPC(req).onComplete(ar -> {
					if (ar.succeeded()) {
						message.reply(new JsonObject().put("status", "success").put("message", "FIB was updated"));
					} else {
						message.reply(new JsonObject().put("status", "error").put("message", ar.cause().getMessage()));
					}

				});
			}
		});

		promise.complete();
	}

	private Future<List<PortInfo>> getPortsFuture_JsonRPC(JSONRPC2Request req) {
		RpcTransport tp = new RpcTransport();
		JsonRpcClient client = new JsonRpcClient(tp);
		Promise<List<PortInfo>> promise = Promise.promise();
		RequestBuilder<Object> reqBuilder = client.createRequest().id(req.getID().toString()).param("_", 0)
				.method(req.getMethod());
		List<PortInfo> ports = null;
		try {
			ports = reqBuilder.returnAsList(PortInfo.class).execute();
			promise.complete(ports);
		} catch (JsonRpcException e) {
			e.printStackTrace();
			ErrorMessage error = e.getErrorMessage();

			if (error.getCode() == -32602) {
				LOG.error("invalid parameter: {} ", error.getMessage());
			}
			promise.fail(error.getMessage());
		} catch (IllegalStateException e) {
			LOG.error(e.getMessage());
		}
		return promise.future();
	}

	private Future<List<String>> getFibFuture_JsonRPC(JSONRPC2Request req) {
		RpcTransport tp = new RpcTransport();
		JsonRpcClient client = new JsonRpcClient(tp);
		Promise<List<String>> promise = Promise.promise();
		RequestBuilder<Object> reqBuilder = client.createRequest().id(req.getID().toString()).param("_", 0)
				.method(req.getMethod());
		List<String> fib = null;
		try {
			fib = reqBuilder.returnAsList(String.class).execute();
			promise.complete(fib);
		} catch (JsonRpcException e) {
			e.printStackTrace();
			ErrorMessage error = e.getErrorMessage();

			if (error.getCode() == -32602) {
				LOG.error("invalid parameter: {}", error.getMessage());
			}
			promise.fail(error.getMessage());
		} catch (IllegalStateException e) {
			LOG.error(e.getMessage());
		}
		return promise.future();
	}

	private Future<FibEntry> insertFibFuture_JsonRPC(JSONRPC2Request req) {
		RpcTransport tp = new RpcTransport();
		JsonRpcClient client = new JsonRpcClient(tp);
		Promise<FibEntry> promise = Promise.promise();
		Map<String, Object> params = req.getNamedParams();
		List<Integer> nexthops = (ArrayList) params.get("Nexthops");
		String name = (String) params.get("Name");
		if (nexthops.size() != 0) {
			try {
				client.createRequest().method(req.getMethod()).id(req.getID().toString()).param("Name", name)
						.param("Nexthops", nexthops).param("StrategyId", 1).returnAs(FibEntry.class).execute();
				
			} catch (JsonRpcException e) {
				e.printStackTrace();
				ErrorMessage error = e.getErrorMessage();

				if (error.getCode() == -32602) {
					LOG.error("invalid parameter: " + error.getMessage());
				}
				promise.fail(error.getMessage());
			}
		}
		promise.complete();
		return promise.future();
	}

	private Future<String> eraseFibFuture_JsonRPC(JSONRPC2Request req) {
		RpcTransport tp = new RpcTransport();
		JsonRpcClient client = new JsonRpcClient(tp);
		Promise<String> promise = Promise.promise();

		Map<String, Object> params = req.getNamedParams();
		String name = (String) params.get("Name");
		LOG.info("attempt to erase fib entry prefix " + name);
		try {
			client.createRequest().method(req.getMethod()).id(req.getID().toString()).param("Name", name).returnAs(Void.class).execute();
			promise.complete(name);
		} catch (JsonRpcException e) {
			e.printStackTrace();
			ErrorMessage error = e.getErrorMessage();

			if (error.getCode() == -32602) {
				LOG.error("invalid parameter: " + error.getMessage());
			}
			promise.fail(error.getMessage());
		}
		return promise.future();
	}

	private Future<Integer> destroyFaceFuture_JsonRPC(JSONRPC2Request req) {
		RpcTransport tp = new RpcTransport();
		JsonRpcClient client = new JsonRpcClient(tp);
		Promise<Integer> promise = Promise.promise();

		Map<String, Object> params = req.getNamedParams();
		Number faceNumber = (Number) params.get("Id");
		int faceId = faceNumber.intValue();
		LOG.info("attempt to destroy face with ID " + faceId);
		try {
			client.createRequest().method(req.getMethod()).id(req.getID().toString()).param("Id", faceId).execute();
			promise.complete(faceId);
		} catch (JsonRpcException e) {
			e.printStackTrace();
			ErrorMessage error = e.getErrorMessage();

			if (error.getCode() == -32602) {
				LOG.error("invalid parameter: " + error.getMessage());
			}
			promise.fail(error.getMessage());
		}
		return promise.future();
	}

	private Future<Version> getVersioFuture_JsonRPC(JSONRPC2Request req) {
		RpcTransport tp = new RpcTransport();
		JsonRpcClient client = new JsonRpcClient(tp);
		Promise<Version> promise = Promise.promise();
		Version version;
		try {
			version = client.createRequest().id(req.getID().toString()).method(req.getMethod()).param("_", 0)
					.returnAs(Version.class).execute();

			promise.complete(version);
		} catch (JsonRpcException e) {
			ErrorMessage error = e.getErrorMessage();
			if (error.getCode() == -32602) {
				LOG.error("invalid parameter: " + error.getMessage());
			}
			promise.fail(error.getMessage());
		}

		return promise.future();

	}


	private Future<Face> createFaceFuture_JsonRPC(JSONRPC2Request req) {
		RpcTransport tp = new RpcTransport();
		JsonRpcClient client = new JsonRpcClient(tp);

		Promise<Face> promise = Promise.promise();
		RequestBuilder<Object> reqBuilder = client.createRequest().id(req.getID().toString()).method(req.getMethod());
		Map<String, Object> params = req.getNamedParams();
//		String local = (String) params.get("Local");
		String port = (String) params.get("Port");
		String remote = (String) params.get("Remote");
		String scheme = (String) params.get("Scheme");
		Face face = null;
		try {
			face = reqBuilder.param("Remote", remote).param("Scheme", scheme).param("Port", port).returnAs(Face.class)
					.execute();
			promise.complete(face);
		} catch (JsonRpcException e) {
			e.printStackTrace();
			ErrorMessage error = e.getErrorMessage();

			if (error.getCode() == -32602) {
				LOG.error("invalid parameter: " + error.getMessage());
			}
			promise.fail(error.getMessage());
		}
		return promise.future();
	}


	private Future<List<Face>> getFacesFuture_JsonRPC(JSONRPC2Request req) {
		RpcTransport tp = new RpcTransport();
		JsonRpcClient client = new JsonRpcClient(tp);
		Promise<List<Face>> promise = Promise.promise();
		RequestBuilder<Object> reqBuilder = client.createRequest().id(req.getID().toString()).param("_", 0)
				.method(req.getMethod());
		List<Face> faces = null;
		try {
			faces = reqBuilder.returnAsList(Face.class).execute();
			promise.complete(faces);
		} catch (JsonRpcException e) {
			e.printStackTrace();
			ErrorMessage error = e.getErrorMessage();

			if (error.getCode() == -32602) {
				LOG.error("invalid parameter: {}", error.getMessage());
			}
			promise.fail(error.getMessage());
		} catch (IllegalStateException e) {
			LOG.error(e.getMessage());
		}
		return promise.future();
	}

	private Future<FaceData> getFaceFuture_JsonRPC(JSONRPC2Request req) {
		RpcTransport tp = new RpcTransport();
		JsonRpcClient client = new JsonRpcClient(tp);
		Promise<FaceData> promise = Promise.promise();

		Map<String, Object> params = req.getNamedParams();
		Number faceNumber = (Number) params.get("Id");
		int faceId = faceNumber.intValue();
		LOG.info("retrieving info of face with ID={}", faceId);
		FaceData face = null;
		try {
			face = client.createRequest().method(req.getMethod()).id(req.getID().toString()).param("Id", faceId)
					.returnAs(FaceData.class).execute();
			promise.complete(face);
		} catch (JsonRpcException e) {
			LOG.error(e.getMessage());
			promise.fail(e.getMessage());
		} catch (IllegalStateException e) {
			LOG.error(e.getMessage());
			promise.fail(e.getMessage());
		}
		return promise.future();
	}

	@Override
	public void stop() {
		LOG.info("stopping verticle");
	}

	public static String getEventBusAddress() {
		return EVENTBUS_ADDRESS;
	}

}