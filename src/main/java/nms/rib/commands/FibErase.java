package nms.rib.commands;

import io.vertx.core.json.JsonObject;
import net.named_data.jndn.Name;

public class FibErase implements FibCommand {

	private Name name;

	public FibErase(Name name) {
		this.name = name;
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
		json.put("name", name.toUri());
		return json;
	}


}
