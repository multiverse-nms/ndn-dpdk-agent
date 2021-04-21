package nms.forwarder.verticle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import nms.VerticleAdress;
import nms.forwarder.api.EventBusEndpoint;
import nms.forwarder.model.face.EtherFace;
import nms.forwarder.model.face.Face;
import nms.forwarder.model.face.FaceData;
import nms.forwarder.model.face.FaceStoreRecord;
import nms.forwarder.model.face.MemifFace;
import nms.forwarder.model.fib.FibEntry;
import nms.forwarder.model.version.Version;
import nms.forwarder.model.port.PortInfo;
import nms.forwarder.rpc.RpcTransport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ForwarderVerticle extends AbstractVerticle {

	private static Logger LOG = LoggerFactory.getLogger(ForwarderVerticle.class.getName());
	static Map<String, FaceStoreRecord> faceStore = new HashMap<>();
	static String ClientId;

	@Override
	public void start(Promise<Void> promise) {
		LOG.info("starting forwarder verticle");
		// setup EventBus
		this.consumeEventBus(VerticleAdress.forwarder_verticle.getAdress(), promise);
	}

	private void consumeEventBus(String address, Promise<Void> promise) {

		vertx.eventBus().consumer(address, (message) -> {
			JSONRPC2Request req = null;
			String method = "";
			String body = message.body().toString();
			JsonObject json = new JsonObject(body);
			ClientId = json.getString("ClientId");

			try {

				req = JSONRPC2Request.parse(body);
				System.out.println("req:" + req);
			} catch (JSONRPC2ParseException e) {
				LOG.error("error while parsing the json rpc request {}", e.getMessage());
				return;
			}

			method = req.getMethod();
			LOG.debug("METHOD={}", method);

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
						LOG.debug("Face={}", ar.result());
						FaceStoreRecord record = faceStore.get(ClientId);
						int Id = ar.result().getInteger("id");
						boolean scope = ar.result().getBoolean("facescope");
						// record = {faces, scope}
						if (record != null) {
							record.addFace(Id);
							record.setFaceScope(scope);
							// add test to check if record was updated
						} else {
							FaceStoreRecord newRecord = new FaceStoreRecord();
							newRecord.addFace(Id);
							newRecord.setFaceScope(scope);
							faceStore.put(ClientId, newRecord);
						}
						message.reply(ar.result());
					} else {
						message.reply(new JsonObject().put("status", "error").put("message", ar.cause().getMessage()));
					}
				});

			}

			if (method.equals(EventBusEndpoint.On_Client_Disconnect.getName())) {
				this.OnClientDisconnect(req);
			}

			if (method.equals(EventBusEndpoint.DESTROY_FACE.getName())) {
				this.destroyFaceFuture_JsonRPC(req).onComplete(ar -> {
					if (ar.succeeded()) {
						LOG.debug("Face={}", ar.result());
						message.reply(new JsonObject().put("status", "success").put("message",
								"face with Id " + ar.result() + " was destroyed"));
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
		@SuppressWarnings("unchecked")
		List<Integer> nexthops = (ArrayList<Integer>) params.get("Nexthops");
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
			client.createRequest().method(req.getMethod()).id(req.getID().toString()).param("Name", name).execute();
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
		LOG.debug("faceNumber", faceNumber);
		int faceId = faceNumber.intValue();
		LOG.debug("faceId", faceId);
		LOG.info("attempt to destroy face with ID " + faceId);
		try {
			client.createRequest().method(req.getMethod()).id(req.getID().toString()).param("id", faceId).execute();
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

	private Future<JsonObject> createFaceFuture_JsonRPC(JSONRPC2Request req) {

		RpcTransport tp = new RpcTransport();
		JsonRpcClient client = new JsonRpcClient(tp);
		Promise<JsonObject> promise = Promise.promise();
		RequestBuilder<Object> reqBuilder = client.createRequest().id(req.getID().toString()).method(req.getMethod());
		Map<String, Object> params = req.getNamedParams();
		Number crtlIdNumber = (Number) params.get("ctrlId");
		int ctrlId = 0;

		if (crtlIdNumber != null)
			ctrlId = crtlIdNumber.intValue();
		String scheme = (String) params.get("scheme");
		LOG.debug("scheme={}", scheme);

		switch (scheme) {
		case "ether":

			LOG.debug("ctrlId={}", ctrlId);
			String local = (String) params.get("local");
			LOG.debug("local={}", local);
			String remote = (String) params.get("remote");
			LOG.debug("remote={}", remote);

			try {

				EtherFace face = reqBuilder.param("local", local).param("remote", remote).param("scheme", scheme)
						.returnAs(EtherFace.class).execute();

				promise.complete(face.toJsonObject().put("ctrlId", ctrlId));

			} catch (JsonRpcException e) {
				e.printStackTrace();
				ErrorMessage error = e.getErrorMessage();

				if (error.getCode() == -32602) {
					LOG.error("invalid parameter: " + error.getMessage());
				}
				promise.fail(error.getMessage());
			}
			break;

		case "memif":
			Number idNumber = (Number) params.get("id");
			Integer id = idNumber.intValue();
			LOG.debug("id={}", id);

			String socketName = (String) params.get("socketName");
			LOG.debug("socketName={}", socketName);
			boolean facescope = (boolean) params.get("facescope");
			LOG.debug("facescope={}", facescope);

			try {

				MemifFace face = reqBuilder.param("id", id).param("socketName", socketName).param("scheme", scheme)
						.returnAs(MemifFace.class).execute();

				promise.complete(face.toJsonObject().put("facescope", facescope));

			} catch (JsonRpcException e) {
				e.printStackTrace();
				ErrorMessage error = e.getErrorMessage();

				if (error.getCode() == -32602) {
					LOG.error("invalid parameter: " + error.getMessage());
				}
				promise.fail(error.getMessage());
			}

			break;
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

	private void OnClientDisconnect(JSONRPC2Request req) {
		Map<String, Object> params = req.getNamedParams();
		String clientId = (String) params.get("clientId");

		if (faceStore.containsKey(clientId)) {
			FaceStoreRecord record = faceStore.get(ClientId);

			List<Integer> FaceIds = record.getFaces();
			boolean faceScope = record.getScope();

			FaceIds.forEach(faceId -> {
				if (faceScope == true) {
					String method = "Face.Destroy";
					String id = UUID.randomUUID().toString();
					params.put("Id", faceId);
					JSONRPC2Request reqOut = new JSONRPC2Request(method, params, id);
					String jsonString = reqOut.toString();
					JsonObject json = new JsonObject(jsonString);
					try {
						JSONRPC2Request request = JSONRPC2Request.parse(json.toString());
						this.destroyFaceFuture_JsonRPC(request).onComplete(ar -> {
							if (ar.succeeded()) {
								LOG.debug("FaceId={}", ar.result());

							}

							else {
								LOG.debug("Something went wrong");
							}

						});

					} catch (JSONRPC2ParseException e) {

						e.printStackTrace();
					}
				} else {
					LOG.debug("Face can't be destroyed");
				}
				record.removeFace(faceId);
			});
		}
	}

	@Override
	public void stop() {
		LOG.info("stopping verticle");
	}

	public static String getEventBusAddress() {
		return VerticleAdress.forwarder_verticle.getAdress();
	}

}