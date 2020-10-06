package nms.restclient;

import io.vertx.core.Future;

public interface RestClient {

	public Future<Configuration> getConfiguration();

	public Future<String> login(String user, String password);

	public void setToken(String token);

	public Future<Void> sendStatus();

	public void setRootCA(String path);

	public void setPort(Integer port);

	public void setHost(String host);

}
