package nms.restclient.service;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import nms.restclient.Notification;

public interface NotificationService {

	public Future<Void> sendNotification(Notification notification);
	public Future<Void> sendNotificationPeriodically(Notification notification, Vertx vertx);
}
