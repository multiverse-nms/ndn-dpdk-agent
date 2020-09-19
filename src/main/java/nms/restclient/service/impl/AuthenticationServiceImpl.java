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
import nms.restclient.service.AuthenticationService;

public class AuthenticationServiceImpl implements AuthenticationService {

	private WebClient webClient;
	private EntryPoint entryPoint;
	private Login creds;
	
	private static String LOGIN_ENDPOINT = "/login/agent";
	
	public AuthenticationServiceImpl(WebClient webClient, EntryPoint entryPoint) {
		this.webClient = webClient;
		this.entryPoint = entryPoint;
		loadCredentials("data.properties");
	}
	
	@Override
	public Future<AuthToken> login() {
		Promise<AuthToken> promise = Promise.promise();
		webClient
		.post(entryPoint.getPort(), entryPoint.getHost(), LOGIN_ENDPOINT)
		.as(BodyCodec.jsonObject())
		.sendJsonObject(new JsonObject()
				.put("username", this.creds.getUsername())
				.put("password", this.creds.getPassword()), ar -> {
					if (ar.succeeded()) {
						HttpResponse<JsonObject> response = ar.result();
						String token = response.body().getString("token");
						promise.complete(new AuthToken(token));
					} else {
						promise.fail("unable to login");
					}
				});
		return promise.future();
	}
	
	public void loadCredentials(String filename) {
		Properties prop = new Properties();

		try {
			prop.load(new FileInputStream(filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.creds = new Login(prop.getProperty("username"), prop.getProperty("password"));
	}

	@Override
	public Future<AuthToken> refreshToken() {
		return this.login();
	}

}
