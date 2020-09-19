package nms.restclient.service;

import io.vertx.core.Future;
import nms.restclient.AuthToken;

public interface TokenManager {
	
	public Future<AuthToken> getNewToken();

}
