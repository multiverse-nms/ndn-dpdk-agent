package nms.rib2fib;

public class RouteKey {

	private int faceId;
	private int origin;

	public RouteKey(int faceId, int origin) {
		this.faceId = faceId;
		this.origin = origin;
	}

	public int getFaceId() {
		return faceId;
	}

	public void setFaceId(int faceId) {
		this.faceId = faceId;
	}

	public int getOrigin() {
		return origin;
	}

	public void setOrigin(int origin) {
		this.origin = origin;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + faceId + origin;
		return result;
	}

	// Compare only account numbers
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RouteKey other = (RouteKey) obj;
		return faceId == other.faceId && origin == other.origin;
	}

}
