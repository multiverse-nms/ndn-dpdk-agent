package nms.restclient;

public class EntryPoint {
	
	private int port;
	private String host;
	
	public EntryPoint(int port, String host) {
		this.setPort(port);
		this.setHost(host);
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}
}