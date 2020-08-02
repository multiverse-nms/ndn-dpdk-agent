package nms.rib.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;

import io.vertx.core.json.JsonObject;
import net.named_data.jndn.Name;
import nms.forwarder.api.EventBusEndpoint;
import nms.rib.NexthopList;

public class FibInsert implements FibCommand {

	private Name name;
	private NexthopList nexthops;

	public FibInsert(Name name, NexthopList nexthops) {
		this.name = name;
		this.nexthops = nexthops;
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub

	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT ");
		sb.append("name: ").append(name);
		sb.append(", nexthops:").append(nexthops);
		return sb.toString();
	}

	@Override
	public JsonObject toEventBusFormat() {
		JsonObject json = new JsonObject();
		json.put("method", "Fib.Insert");
		json.put("Name", name.toUri());
		json.put("Nexthops", nexthops.toJsonArray());
		return json;
	}

	@Override
	public JsonObject toJsonRpcRequest() {
		String method = EventBusEndpoint.INSERT_FIB.getName();
		String id = UUID.randomUUID().toString();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Name", name.toString());
		// problem when library tries to serialize prefix in nexthop
		ArrayList<Integer> nexthopsList = new ArrayList<>();
		nexthops.getList().forEach(nh -> {
			nexthopsList.add(nh.getFaceId());
		});
		params.put("Nexthops", nexthopsList);
		params.put("StrategyId", 1);
		JSONRPC2Request reqOut = new JSONRPC2Request(method, params, id);
		String jsonString = reqOut.toString();
		return new JsonObject(jsonString);
	}
	
	@Override
	public boolean hasNexthops() {
		return nexthops.size() != 0;
	}
	

}
