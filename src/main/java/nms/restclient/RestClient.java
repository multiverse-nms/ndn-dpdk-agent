package nms.restclient;

<<<<<<< HEAD
import java.util.List;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
=======
import io.vertx.core.Future;
>>>>>>> 103f8d32cf7f7679c7390520fd9999ae1b55d7ba

public interface RestClient {

	public Future<Configuration> getConfiguration();

	public Future<String> login(String user, String password);

	public void setToken(String token);

	public Future<Void> sendStatus();

<<<<<<< HEAD
	public List<JsonObject> compareConfiguration(Configuration prev, Configuration current); 
=======
	public void setRootCA(String path);

	public void setPort(Integer port);

	public void setHost(String host);
>>>>>>> 103f8d32cf7f7679c7390520fd9999ae1b55d7ba


}
