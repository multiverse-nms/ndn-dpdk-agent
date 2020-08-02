package nms.rib.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;

import io.vertx.core.json.JsonObject;
import net.named_data.jndn.Name;
import nms.forwarder.api.EventBusEndpoint;
import nms.rib.NexthopList;

public class FibErase implements FibCommand {

	private Name name;
	private NexthopList nexthops;

	public FibErase(Name name, NexthopList nexthops) {
		this.name = name;
		this.nexthops = nexthops;
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public String toString() {
		return "ERASE " + name.toUri();
	}

	@Override
	public JsonObject toEventBusFormat() {
		JsonObject json = new JsonObject();
		json.put("method", "Fib.Erase");
		json.put("Name", name.toUri());
		return json;
	}

	@Override
	public JsonObject toJsonRpcRequest() {
		String method = EventBusEndpoint.ERASE_FIB.getName();
		String id = UUID.randomUUID().toString();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Name", name.toUri());
		JSONRPC2Request reqOut = new JSONRPC2Request(method, params, id);
		String jsonString = reqOut.toString();
		return new JsonObject(jsonString);
	}

	@Override
	public boolean hasNexthops() {
		return nexthops.size() != 0;
	}


}
