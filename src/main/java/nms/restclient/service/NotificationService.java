package nms.restclient.service;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import nms.restclient.Notification;

public interface NotificationService {

	public Future<Void> sendNotification(Notification notification);
	
	public void sendNotificationPeriodically(Notification notification, Vertx vertx, Handler<AsyncResult<Void>> handler);
}
