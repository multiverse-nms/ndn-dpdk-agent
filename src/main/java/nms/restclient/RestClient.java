package nms.restclient;

import io.vertx.core.Future;

public interface RestClient {
	
	public Future<Configuration> getCandidateConfiguration();
	
	public Future<Configuration> getRunningConfiguration();
	
	public Future<Void> sendNotification(Notification notification);
	
	public Future<String> basicAuthentication(String user, String password);

}
