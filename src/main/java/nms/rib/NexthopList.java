package nms.rib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.vertx.core.json.JsonArray;

import net.named_data.jndn.Name;

public class NexthopList {

	private List<Nexthop> nexthops;
	private Map<Integer, Integer> nexthopsMap;

	public NexthopList() {
		this.nexthops = new ArrayList<>();
		this.nexthopsMap = new HashMap<>();
	}

	public List<Nexthop> getList() {
		return nexthops;
	}
	
	public Map<Integer, Integer> getMap() {
		return nexthopsMap;
	}

	public void addnexthop(Name name, int faceId, int cost) {
		this.nexthops.add(new Nexthop(name, faceId, cost));
		this.nexthopsMap.put(faceId, cost);
	}

	public void removeNexthop(Name name, int faceId, int cost) {
		this.nexthops.remove(new Nexthop(name, faceId, cost));
		this.nexthopsMap.remove(faceId);
	}

	public int size() {
		return this.nexthops.size();
	}

	@Override
	public boolean equals(Object o) {
		// self check
		if (this == o)
			return true;
		// null check
		if (o == null)
			return false;
		// type check and cast
		if (getClass() != o.getClass())
			return false;
		NexthopList list = (NexthopList) o;
		// field comparison
		return list.getList().equals(this.nexthops);
	}

	@Override
	public String toString() {
		return this.nexthops.toString();
	}
	
	public JsonArray toJsonArray() {
		JsonArray array = new JsonArray();
		nexthops.forEach(nexthop -> {
			array.add(nexthop.toJsonObject());
		});
		return array;
	}
	
}
