package nms.restclient;

import io.vertx.core.Future;

public interface RestClient {
	
	public Future<Configuration> getCandidateConfiguration(String token);
	
	public Future<Configuration> getRunningConfiguration(String token);
	
	public Future<Void> sendNotification(Notification notification, String token);
	
	public Future<String> basicAuthentication(String user, String password);

}
