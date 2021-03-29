package nms.restclient.service.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import nms.restclient.EntryPoint;
import nms.restclient.Notification;
import nms.restclient.service.NotificationService;

public class NotificationServiceImpl implements NotificationService {

	private WebClient webClient;
	private EntryPoint entryPoint;
	private String token;
	private static String NOTIFICATION_STATUS_ENDPOINT = "/notification/status";
	private static String NOTIFICATION_EVENT_ENDPOINT = "/notification/event";
	private static String NOTIFICATION_FAULT_ENDPOINT = "/notification/fault";
	
	private static long STATUS_PERIOD = 60000;
	private CredentialsProvider credsProvider;
	
	public NotificationServiceImpl(WebClient webClient, EntryPoint entryPoint) {
              this.webClient = webClient;
              this.entryPoint = entryPoint;
              this.credsProvider = new CredentialsProvider("data.properties");
      		  TokenProvider.getFreshToken(credsProvider.getUsername(), credsProvider.getPassword()).onComplete(ar -> {
      			if (ar.succeeded()) {
      				this.token = ar.result();
      			}
      		});
	}

	@Override
	public Future<Void> sendNotification(Notification notification) {
		String EndPoint ="";
		Promise<Void> promise = Promise.promise();
		HttpRequest<Buffer> request = webClient.put(entryPoint.getPort(), entryPoint.getHost(), EndPoint);
		switch (notification.getType()) {
		case STATUS:
			authorize(request);
			EndPoint = NOTIFICATION_STATUS_ENDPOINT;
			request
			.sendJsonObject(notification.toJsonObject(),ar -> {
				if(ar.succeeded()) {
					HttpResponse<Buffer> response = ar.result();
					if(response.statusCode() == 200) {
						promise.complete();
					} else {
					  if (response.statusCode() == 401) {
						  refreshTokenAndRetry(request).onComplete(ar1 -> {
								if (ar1.succeeded()) {
									promise.complete();
								}
							});
					  }
					  if(response.statusCode() == 400) {
						  promise.fail("Incorrect Status object.");
					  }
					}
			   } else {
					promise.fail("Failed to send the status notification");
				}
			  });
			
			break;
		case EVENT:
			EndPoint = NOTIFICATION_EVENT_ENDPOINT;
			authorize(request);
			request
			.sendJsonObject(notification.toJsonObject(),ar -> {
				if(ar.succeeded()) {
					HttpResponse<Buffer> response = ar.result();
					if(response.statusCode() == 200) {
						promise.complete();
					} else {
					  if (response.statusCode() == 401) {
						  refreshTokenAndRetry(request).onComplete(ar1 -> {
								if (ar1.succeeded()) {
									promise.complete();
								}
							});
					  }
					  if(response.statusCode() == 400) {
						  promise.fail("Incorrect Event object.");
					  }
					}
			   } else {
					promise.fail(" Failed to send the Event notification.");
				}
			  });
			break;
		case FAULT:
			authorize(request);
			EndPoint = NOTIFICATION_FAULT_ENDPOINT;
			request
			.sendJsonObject(notification.toJsonObject(),ar -> {
				if(ar.succeeded()) {
					HttpResponse<Buffer> response = ar.result();
					if(response.statusCode() == 200) {
						promise.complete();
					} else {
					  if (response.statusCode() == 401) {
						  refreshTokenAndRetry(request).onComplete(ar1 -> {
								if (ar1.succeeded()) {
									promise.complete();
								}
							});
					  }
					  if(response.statusCode() == 400) {
						  promise.fail("Incorrect Fault object.");
					  }
					}
			   } else {
					promise.fail("Failed to send the Fault notification");
				}
			});
			break;
		}

		
		return promise.future();
	}
	

	public void sendNotificationPeriodically(Notification notification, Vertx vertx,  Handler<AsyncResult<Void>> handler) {
		// TODO Auto-generated method stub
		vertx.setPeriodic(STATUS_PERIOD, id -> this.sendNotification(notification).onComplete(handler));
	}

	private void authorize(HttpRequest<Buffer> request) {
		// if token has expiration info, we can test and check if it's expired then get
		// a new token;
		request.bearerTokenAuthentication(token);
	}
   
	private Future<Notification> refreshTokenAndRetry(HttpRequest<Buffer> request) {
		Promise<Notification> promise = Promise.promise();
		TokenProvider.getFreshToken(credsProvider.getUsername(), credsProvider.getPassword()).onComplete(ar -> {
			if (ar.succeeded()) {
				request.send(ar1 -> {
					if (ar1.succeeded()) {
						promise.complete();
					} else {
						promise.fail("something went wrong");
					}
				});
			} else {
				promise.fail("can't get a new token");
			}
		});
		
		return promise.future();
	}
}
