package nms.restclient;

import java.util.Objects;

import io.vertx.core.json.JsonObject;

public class Face {

	private int ctrlId = 0;
	private int fwdId = 0;
	private String port = "";
	private String local = "";
	private String remote = "";
	private String scheme = "";
	private int vlan = 1;



	public Face() {
	}

	
	public Face(int ctrlId, String local, String port, String remote, String scheme) {
		this(ctrlId, local, port, remote, scheme, 1);
	}

	public Face(int ctrlId, String local, String port, String remote, String scheme, int vlan) {
		this.setCtrlId(ctrlId);
		this.setLocal(local);
		this.setPort(port);
		this.setRemote(remote);
		this.setScheme(scheme);
		this.setVlan(vlan);
	}

	public Face(JsonObject json) {
		if (json.getInteger("id") != null)
			this.ctrlId = json.getInteger("id");
		if (json.getInteger("faceId") != null)
			this.fwdId = json.getInteger("faceId");
		if (json.getString("local") != null)
			this.local = json.getString("local");
		if (json.getString("remote") != null)
			this.remote = json.getString("remote");
		if (json.getString("scheme") != null)
			this.scheme = json.getString("scheme");
		// port and vlan don't appear in api spec
		if (json.getString("port") != null)
			this.port = json.getString("port");
//		this.vlan = json.getInteger("vlan");
	}
	/**
	 * @return the remote
	 */
	public String getRemote() {
		return remote;
	}

	/**
	 * @param remote the remote to set
	 */
	public void setRemote(String remote) {
		this.remote = remote;
	}

	/**
	 * @return the local
	 */
	public String getLocal() {
		return local;
	}

	/**
	 * @param local the local to set
	 */
	public void setLocal(String local) {
		this.local = local;
	}

	/**
	 * @return the scheme
	 */
	public String getScheme() {
		return scheme;
	}

	/**
	 * @param scheme the scheme to set
	 */
	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	/**
	 * @return the vlan
	 */
	public int getVlan() {
		return vlan;
	}

	/**
	 * @param vlan the vlan to set
	 */
	public void setVlan(int vlan) {
		this.vlan = vlan;
	}

	public String getPort() {
		// TODO Auto-generated method stub
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(String port) {
		this.port = port;
	}

	public int getCtrlId() {
		return ctrlId;
	}

	public void setCtrlId(int ctrlId) {
		this.ctrlId = ctrlId;
	}

	public int getFwdId() {
		return fwdId;
	}

	public void setFwdId(int fwdId) {
		this.fwdId = fwdId;
	}

	public JsonObject toJsonObject() {
		JsonObject json = new JsonObject();
		json.put("ctrlId", ctrlId);
		json.put("fwdId", fwdId);
		json.put("local", local);
		json.put("port", local);
		json.put("remote", remote);
		json.put("scheme", scheme);
		return json;
	}
	
	
	@Override
	public String toString() {		
		return this.toJsonObject().toString();
	}

	public boolean equals(Object obj) {
		return Objects.equals(ctrlId, ((Face) obj).ctrlId);
	}
}
