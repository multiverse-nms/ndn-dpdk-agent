package nms.rib2fib;

import java.util.ArrayList;
import java.util.List;

import net.named_data.jndn.Name;

public class NexthopList {

	private List<Nexthop> nexthops;

	public NexthopList() {
		this.nexthops = new ArrayList<>();
	}

	public List<Nexthop> getList() {
		return nexthops;
	}

	public void addnexthop(Name name, int faceId, int cost) {
		this.nexthops.add(new Nexthop(name, faceId, cost));
	}

	public void removeNexthop(Name name, int faceId, int cost) {
		this.nexthops.remove(new Nexthop(name, faceId, cost));
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

}
