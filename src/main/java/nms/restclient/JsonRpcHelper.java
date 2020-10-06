package nms.restclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;

import io.vertx.core.json.JsonObject;

public class JsonRpcHelper {
	
	public JsonRpcHelper() {
		
	}
	
	public List<JsonObject> createMutipleFacesCommands(List<Face> faces) {	
		List<JsonObject> batch = new ArrayList<>();	
		faces.forEach(face -> {
			batch.add(makeNewFaceCommand(face));
		});
		return batch;
	}
	
	public List<JsonObject> createMutipleRoutesCommands(List<Route> routes) {	
		List<JsonObject> batch = new ArrayList<>();	
		routes.forEach(route -> {
			batch.add(makeNewRouteCommand(route));
		});
		return batch;
	}
	
	public JsonObject makeNewFaceCommand(Face face) {
		String method = "Face.Create";
		String id = UUID.randomUUID().toString();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("local", face.getLocal());
		params.put("port", face.getPort());
		params.put("remote", face.getRemote());
		params.put("scheme", face.getScheme());
		JSONRPC2Request reqOut = new JSONRPC2Request(method, params, id);
		String jsonString = reqOut.toString();
		return new JsonObject(jsonString);
	}
	
	public JsonObject makeNewRouteCommand(Route route) {
		String method = "Route.Add";
		String id = UUID.randomUUID().toString();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("prefix", route.getPrefix());
		params.put("faceId", route.getFaceId());
		params.put("origin", route.getOrigin());
		params.put("cost", route.getCost());
		JSONRPC2Request reqOut = new JSONRPC2Request(method, params, id);
		String jsonString = reqOut.toString();
		return new JsonObject(jsonString);
	}

}
