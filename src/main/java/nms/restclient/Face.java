package nms.restclient;

import java.util.Objects;

import io.vertx.core.json.JsonObject;

public class Face{

	private int id;
	private String port;
	private String local;
	private String remote;
	private String scheme;
	private int vlan;

	public Face(int id, String local, String port, String remote, String scheme) {
		this(id, local, port, remote, scheme, 1);
	}

	public Face(int id, String local, String port, String remote, String scheme, int vlan) {
		this.setId(id);
		this.setLocal(local);
		this.setPort(port);
		this.setRemote(remote);
		this.setScheme(scheme);
		this.setVlan(vlan);
	}

	public Face(JsonObject json) {
		this.id = json.getInteger("id");
		this.local = json.getString("local");
		this.remote = json.getString("remote");
		this.scheme = json.getString("scheme");
		// port and vlan don't appear in api spec
//		this.port = json.getString("port");
//		this.vlan = json.getInteger("vlan");
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public Object getPort() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(String port) {
		this.port = port;
	}
	 
	public boolean equals(Object obj) {
		return Objects.equals(id, ((Face) obj).id);
	}
}