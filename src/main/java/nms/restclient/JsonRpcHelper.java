package nms.restclient;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;

import io.vertx.core.json.JsonObject;

public class JsonRpcHelper {
	
	
	public static JsonObject makeNewFaceCommand(Face face) {
		String method = "Face.Create";
		String id = UUID.randomUUID().toString();
		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("Local", face.getLocal());
		params.put("Port", face.getPort());
		params.put("Remote", face.getRemote());
		params.put("Scheme", face.getScheme());
		JSONRPC2Request reqOut = new JSONRPC2Request(method, params, id);
		String jsonString = reqOut.toString();
		return new JsonObject(jsonString);
	}
	
	public static JsonObject makeNewRouteCommand(Route route) {
		String method = "Route.Add";
		String id = UUID.randomUUID().toString();
		Map<String, Object> params = new HashMap<String, Object>();
		
		JSONRPC2Request reqOut = new JSONRPC2Request(method, params, id);
		String jsonString = reqOut.toString();
		return new JsonObject(jsonString);
	}

	public static JsonObject makeDestroyFaceCommand(Face face) {
		// TODO Auto-generated method stub
		return null;
	}

	public static JsonObject makeDestroyFaceCommand(Route face) {
		// TODO Auto-generated method stub
		return null;
	}

}
