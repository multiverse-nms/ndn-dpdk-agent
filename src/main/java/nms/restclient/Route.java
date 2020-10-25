package nms.restclient;

import java.nio.ByteBuffer;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Objects;

import io.vertx.core.json.JsonObject;
import net.named_data.jndn.ComponentType;
import net.named_data.jndn.Name;
import net.named_data.jndn.encoding.EncodingException;
import net.named_data.jndn.encoding.tlv.Tlv;
import net.named_data.jndn.encoding.tlv.TlvDecoder;
import net.named_data.jndn.util.Blob;

public class Route {

	private static final Logger LOG = LoggerFactory.getLogger(Route.class);

	private String prefix;
	private int faceId;
	private int faceFwdId = 0;
	private int origin;
	private int cost;
	private boolean childInherit = true;
	private boolean capture = false;

	public Route(String prefix, int faceId, int origin, int cost, boolean childInherit, boolean capture) {
		this.setPrefix(prefix);
		this.setFaceId(faceId);
		this.setOrigin(origin);
		this.setCost(cost);
		this.setChildInherit(childInherit);
		this.setCapture(capture);
	}
	
	public Route(String prefix, int faceId, int origin, int cost) {
		this.setPrefix(prefix);
		this.setFaceId(faceId);
		this.setOrigin(origin);
		this.setCost(cost);
		this.setChildInherit(true);
		this.setCapture(false);
	}

	// TODO: crash higher up when prefix is invalid
	public Route(JsonObject json) {
		String decodedPrefix = decodePrefix(json.getString("prefix"));
		this.prefix = decodedPrefix;
		this.faceId = json.getInteger("faceId");
		this.origin = json.getInteger("origin");
		this.cost = json.getInteger("cost");
	}

	private String decodePrefix(String base64Encoded) {
		LOG.debug("decodePrefix({})", base64Encoded);
		byte[] base64Decoded = Base64.getDecoder().decode(base64Encoded);
		ByteBuffer buffer = ByteBuffer.wrap(base64Decoded);
		TlvDecoder tlvDecoder = new TlvDecoder(buffer);
		Name name = new Name();
		try {
			while (tlvDecoder.getOffset() < base64Decoded.length) {
				name.append(decodeNameComponent(tlvDecoder, true));
			}
		} catch (Exception e) {
			LOG.error(e.toString());
		}
		LOG.debug("decodedPrefix = {}", name.toUri());
		return name.toUri();
	}

	private Name.Component decodeNameComponent(TlvDecoder decoder, boolean copy) throws EncodingException {
		int savePosition = decoder.getOffset();
		int type = decoder.readVarNumber();
		// Restore the position.
		decoder.seek(savePosition);

		Blob value = new Blob(decoder.readBlobTlv(type), copy);
		if (type == Tlv.ImplicitSha256DigestComponent)
			return Name.Component.fromImplicitSha256Digest(value);
		else if (type == Tlv.ParametersSha256DigestComponent)
			return Name.Component.fromParametersSha256Digest(value);
		else if (type == Tlv.NameComponent)
			return new Name.Component(value);
		else
			// Unrecognized type code.
			return new Name.Component(value, ComponentType.OTHER_CODE, type);
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

	public int getFaceFwdlId() {
		return faceFwdId;
	}

	public void setFaceFwdId(int faceFwdId) {
		this.faceFwdId = faceFwdId;
	}

	public JsonObject toJsonObject() {
		JsonObject json = new JsonObject();
		json.put("prefix", prefix);
		json.put("faceId", faceId);
		json.put("faceFwdId", faceFwdId);
		json.put("origin", origin);
		json.put("cost", cost);
		return json;
	}
	
	
	@Override
	public String toString() {
		return this.toJsonObject().toString();
	}

	@Override
	public boolean equals(Object o) {
//		// self check
//		if (this == o)
//			return true;
//		// null check
//		if (o == null)
//			return false;
//		// type check and cast
//		if (getClass() != o.getClass())
//			return false;
//		Route route = (Route) o;
		// field comparison=
		return Objects.equals(faceId, ((Route) o).faceId) && Objects.equals(prefix, ((Route) o).prefix) && Objects.equals(origin, ((Route) o).origin)  ;
	}

	public static void main(String[] args) {
		JsonObject json = new JsonObject()
				.put("prefix", "CARuaXN0CANnb3Y=")
				.put("faceId", 3000)
				.put("cost" , 0)
				.put("origin" , 1);
		
		Route route = new Route(json);
		String value = route.decodePrefix("CARuaXN0CANnb3Y=");
		System.out.print(value);
	}
}
