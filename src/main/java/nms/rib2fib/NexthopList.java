package nms.rib2fib;

import java.util.ArrayList;
import java.util.List;

import net.named_data.jndn.Name;

public class NexthopList {

	private List<Nexthop> nexthops;

	public NexthopList() {
		this.nexthops = new ArrayList<>();
	}

	public List<Nexthop> getNexthops() {
		return nexthops;
	}

	public void addnexthop(Name name, int faceId, int cost) {
		this.nexthops.add(new Nexthop(name, faceId, cost));
	}
	
	public void removeNexthop(Name name, int faceId, int cost) {
		this.nexthops.remove(new Nexthop(name, faceId, cost));
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
		return list.getNexthops().equals(this.nexthops);
	}

}
