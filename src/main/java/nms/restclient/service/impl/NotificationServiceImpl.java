package nms.restclient.service.impl;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.ext.web.client.WebClient;
import nms.restclient.AuthToken;
import nms.restclient.EntryPoint;
import nms.restclient.Notification;
import nms.restclient.service.NotificationService;

public class NotificationServiceImpl implements NotificationService {

	private WebClient webClient;
	private EntryPoint entryPoint;
	private AuthToken authToken;

	private static String NOTIFICATION_STATUS_ENDPOINT = "/notification/status";
	private static String NOTIFICATION_EVENT_ENDPOINT = "/notification/event";
	private static String NOTIFICATION_FAULT_ENDPOINT = "/notification/fault";
	
	
	public NotificationServiceImpl(WebClient webClient, EntryPoint entryPoint, AuthToken token) {

	}

	@Override
	public Future<Void> sendNotification(Notification notification) {
		Promise<Void> promise = Promise.promise();
		switch (notification.getType()) {
		case STATUS:
			webClient
			.put(entryPoint.getPort(), entryPoint.getHost(), NOTIFICATION_STATUS_ENDPOINT)
			.sendJsonObject(notification.toJsonObject(),ar -> {
				if(ar.succeeded()) {
					System.out.println("Status notification with status code" + ar.result().statusCode());
				}else {
					promise.fail(" Failed STATUS notification");
				}
			  });
			
			break;
		case EVENT:
			webClient
			.put(entryPoint.getPort(), entryPoint.getHost(), NOTIFICATION_EVENT_ENDPOINT)
			.sendJsonObject(notification.toJsonObject(),ar -> {
				if(ar.succeeded()) {
					System.out.println("Event notification with status code" + ar.result().statusCode());
				}else {
					promise.fail("Failed EVENT notification");
				}
			  });
			break;
		case FAULT:
			webClient
			.put(entryPoint.getPort(), entryPoint.getHost(), NOTIFICATION_FAULT_ENDPOINT)
			.sendJsonObject(notification.toJsonObject(),ar -> {
				if(ar.succeeded()) {
					System.out.println("Fault notification with status code" + ar.result().statusCode());
				}else {
					promise.fail("Failed FAULT notification");
				}
			  });
			break;
		}

		
		return promise.future();
	}

	
	@Override
	public void refreshToken(String token) {
		this.authToken = new AuthToken(token);
	}

}
