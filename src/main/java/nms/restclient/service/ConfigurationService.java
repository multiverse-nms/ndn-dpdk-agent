package nms.restclient.service;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import nms.restclient.Configuration;

public interface ConfigurationService {

	public Future<Configuration> getConfiguration();

	public void getConfigurationPeriodically(Vertx vertx, Handler<AsyncResult<Configuration>> handler);

	public Future<Void> sendRunningConfiguration(Configuration runningConfiguration);

}
