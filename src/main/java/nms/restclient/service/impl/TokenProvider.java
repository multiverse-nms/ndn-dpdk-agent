package nms.restclient.service.impl;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import nms.restclient.EntryPoint;

public class TokenProvider {

	private static WebClient webClient;
	private static EntryPoint entryPoint;
	private static String LOGIN_ENDPOINT = "/login/agent";

	private static String token;
	
	public TokenProvider(WebClient webClient, EntryPoint entryPoint) {
		TokenProvider.webClient = webClient;
		TokenProvider.entryPoint = entryPoint;
	}

	public static Future<String> getFreshToken(String username, String password) {
		Promise<String> promise = Promise.promise();
		webClient.post(entryPoint.getPort(), entryPoint.getHost(), LOGIN_ENDPOINT).as(BodyCodec.jsonObject())
				.sendJsonObject(new JsonObject().put("username", username).put("password", username), ar -> {
					if (ar.succeeded()) {
						HttpResponse<JsonObject> response = ar.result();
						token = response.body().getString("token");
						promise.complete(token);
					} else {
						promise.fail("unable to login");
					}
				});
		return promise.future();
	}
	
	
	public static String getToken() {
		return TokenProvider.token;
	}

}
