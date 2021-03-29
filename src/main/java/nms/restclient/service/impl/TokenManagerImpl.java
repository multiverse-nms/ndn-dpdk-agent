package nms.restclient.service.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import nms.restclient.AuthToken;
import nms.restclient.EntryPoint;


public class TokenManagerImpl {

	private static WebClient webClient;
	private static EntryPoint entryPoint;
	private static Login creds;
	private static String token;
	
	private static String LOGIN_ENDPOINT = "/login/agent";
//	
	public TokenManagerImpl(WebClient webClient, EntryPoint entryPoint) {
		TokenManagerImpl.webClient = webClient;
		TokenManagerImpl.entryPoint = entryPoint;
		loadCredentials("data.properties");
	}
//	@Override
//	public Future<AuthToken> getNewToken() {
//		Promise<AuthToken> promise = Promise.promise();
//		webClient
//		.post(entryPoint.getPort(), entryPoint.getHost(), LOGIN_ENDPOINT)
//		.as(BodyCodec.jsonObject())
//		.sendJsonObject(new JsonObject()
//				.put("username", this.creds.getUsername())
//				.put("password", this.creds.getPassword()), ar -> {
//					if (ar.succeeded()) {
//						HttpResponse<JsonObject> response = ar.result();
//						token = response.body().getString("token");
//						promise.complete(new AuthToken(token));
//					} else {
//						promise.fail("unable to login");
//					}
//				});
//		return promise.future();
//	}
//	
	public void loadCredentials(String filename) {
		Properties prop = new Properties();

		try {
			prop.load(new FileInputStream(filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		TokenManagerImpl.creds = new Login(prop.getProperty("username"), prop.getProperty("password"));
	}

	public static Future<AuthToken> getNewToken() {
		Promise<AuthToken> promise = Promise.promise();
		webClient.post(entryPoint.getPort(), entryPoint.getHost(), LOGIN_ENDPOINT).as(BodyCodec.jsonObject())
				.sendJsonObject(new JsonObject().put("username", creds.getUsername()).put("password",
						creds.getPassword()), ar -> {
							if (ar.succeeded()) {
								HttpResponse<JsonObject> response = ar.result();
								token = response.body().getString("token");
								promise.complete(new AuthToken(token));
							} else {
								promise.fail("unable to login");
							}
						});
		return promise.future();
	}

}
