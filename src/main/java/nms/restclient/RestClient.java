package nms.restclient;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;

public interface RestClient {

	public Future<Configuration> getConfiguration();


	public Future<String> login(String user, String password);

	public void setToken(String token);

	public Future<Void> sendStatus();

}
