package nms.restclient;

import io.vertx.core.json.JsonObject;

public class Route {
	
	private String prefix;
	private int faceId;
	private int origin;
	private int cost;
	private boolean childInherit;
	private boolean capture;
	
	public Route(String prefix, int faceId, int origin, int cost, boolean childInherit, boolean capture) {
		this.setPrefix(prefix);
		this.setFaceId(faceId);
		this.setOrigin(origin);
		this.setCost(cost);
		this.setChildInherit(childInherit);
		this.setCapture(capture);
	}
	public Route(JsonObject json) {
		this.prefix = json.getString("prefix");
		this.faceId = json.getInteger("faceId");
		this.origin = json.getInteger("origin");
		this.cost = json.getInteger("cost");
	}
	/**
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}
	/**
	 * @param prefix the prefix to set
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	/**
	 * @return the faceId
	 */
	public int getFaceId() {
		return faceId;
	}
	/**
	 * @param faceId the faceId to set
	 */
	public void setFaceId(int faceId) {
		this.faceId = faceId;
	}
	/**
	 * @return the origin
	 */
	public int getOrigin() {
		return origin;
	}
	/**
	 * @param origin the origin to set
	 */
	public void setOrigin(int origin) {
		this.origin = origin;
	}
	/**
	 * @return the cost
	 */
	public int getCost() {
		return cost;
	}
	/**
	 * @param cost the cost to set
	 */
	public void setCost(int cost) {
		this.cost = cost;
	}
	/**
	 * @return the childInherit
	 */
	public boolean isChildInherit() {
		return childInherit;
	}
	/**
	 * @param childInherit the childInherit to set
	 */
	public void setChildInherit(boolean childInherit) {
		this.childInherit = childInherit;
	}
	/**
	 * @return the capture
	 */
	public boolean isCapture() {
		return capture;
	}
	/**
	 * @param capture the capture to set
	 */
	public void setCapture(boolean capture) {
		this.capture = capture;
	}	
	
}
