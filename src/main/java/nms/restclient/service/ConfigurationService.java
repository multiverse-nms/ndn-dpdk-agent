package nms.restclient.service;

import io.vertx.core.Future;
import nms.restclient.AuthToken;
import nms.restclient.Configuration;

public interface ConfigurationService {
	
	public Future<Configuration> getCandidateConfiguration();
	
	public Future<Configuration> getRunningConfiguration();
	
	public Future<AuthToken> refreshToken();
	
}
