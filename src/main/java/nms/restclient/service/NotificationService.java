package nms.restclient.service;

import io.vertx.core.Future;
import nms.restclient.Notification;

public interface NotificationService {

	public Future<Void> sendNotification(Notification notification);
	public void refreshToken(String token);
}
