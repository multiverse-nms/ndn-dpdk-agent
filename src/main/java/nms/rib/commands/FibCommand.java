package nms.rib.commands;

import io.vertx.core.json.JsonObject;

public interface FibCommand {
	public void execute();
	
	public JsonObject toEventBusFormat();
	
	public JsonObject toJsonRpcRequest();
}
