package nms.restclient;

import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.json.JsonObject;
import net.named_data.jndn.Name;

public class Route {

	private static final Logger LOG = LoggerFactory.getLogger(Route.class);
	
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
		LOG.debug("decodedPrefix", json.getString("prefix"));
		String decodedPrefix =  decodePrefix(json.getString("prefix"));
		LOG.debug("decodedPrefix", decodedPrefix);
		this.prefix = decodedPrefix;
		this.faceId = json.getInteger("faceId");
		this.origin = json.getInteger("origin");
		this.cost = json.getInteger("cost");
	}

	private String decodePrefix(String prefix) {
		byte[] decodedBytes = Base64.getUrlDecoder().decode(prefix);
		String decodedPrefix = new String(decodedBytes);
		return decodedPrefix;
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

	public JsonObject toJsonObject() {
		JsonObject json = new JsonObject();
		json.put("prefix", prefix);
		json.put("faceId", faceId);
		json.put("origin", origin);
		json.put("cost", cost);
		return json;
	}
}
