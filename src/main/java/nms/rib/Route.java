package nms.rib2fib;

import net.named_data.jndn.Name;

public class Route {

	private Name prefix;
	private int faceId;
	private int cost;
	private int origin;
	boolean captureFlag = false;
	boolean childInheritFlag = true;

	public Route(Name prefix, int faceId, int origin) {
		this.prefix = prefix;
		this.faceId = faceId;
		this.origin = origin;
		this.captureFlag = false;
		this.childInheritFlag = true;
	}

	public Name getPrefix() {
		return prefix;
	}

	public void setPrefix(Name prefix) {
		this.prefix = prefix;
	}

	public int getFaceId() {
		return faceId;
	}

	public void setFaceId(int faceId) {
		this.faceId = faceId;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getOrigin() {
		return origin;
	}

	public void setOrigin(int origin) {
		this.origin = origin;
	}

	public void setChildInheritFlag(boolean value) {
		this.childInheritFlag = value;
	}
	
	public boolean hasChildInheritFlag() {
		return this.childInheritFlag;
	}
	
	public void setCaptureFlag(boolean value) {
		this.captureFlag = value;
	}
	
	public boolean hasCaptureFlag() {
		return this.captureFlag;
	}

	public Nexthop toNexthop() {
		return new Nexthop(this.prefix, this.faceId, this.cost);
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
		Route route = (Route) o;
		// field comparison
		return faceId == route.getFaceId() && origin == route.getOrigin();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(" { faceId: ").append(this.faceId).append(", cost: ").append(this.cost).append(" }");
		return sb.toString();
	}
}
