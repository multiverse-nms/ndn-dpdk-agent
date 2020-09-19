package nms.restclient.service;

import io.vertx.core.Future;
import nms.restclient.AuthToken;

public interface AuthenticationService {
		
	public Future<AuthToken> login();
	
	public Future<AuthToken> refreshToken();

}
