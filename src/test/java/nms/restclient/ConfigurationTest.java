package nms.restclient;

import static com.xebialabs.restito.builder.stub.StubHttp.whenHttp;
import static com.xebialabs.restito.semantics.Action.resourceContent;
import static com.xebialabs.restito.semantics.Action.status;
import static com.xebialabs.restito.semantics.Action.stringContent;
import static com.xebialabs.restito.semantics.Condition.get;
import static com.xebialabs.restito.semantics.Condition.put;
import static com.xebialabs.restito.semantics.Condition.withHeader;
import static com.xebialabs.restito.semantics.Condition.withPostBodyContaining;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static com.xebialabs.restito.semantics.Condition.startsWithUri;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.concurrent.TimeUnit;

import static com.xebialabs.restito.semantics.Condition.parameter;
import static com.xebialabs.restito.semantics.Condition.post;

import org.glassfish.grizzly.http.util.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.xebialabs.restito.server.StubServer;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.Checkpoint;
import io.vertx.junit5.Timeout;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import nms.restclient.service.impl.ConfigurationServiceImpl;
import nms.restclient.service.impl.TokenProvider;

@ExtendWith(VertxExtension.class)
public class ConfigurationTest {

	private StubServer server;
	private WebClient webClient;
	private String Token = "qwertyuiopasdfghjklzxcvbnm123456";

	
	@BeforeEach
	public void start(Vertx vertx) {
		 server = new StubServer(8889);
		 server.secured();
		 server.run();
		 webClient = WebClient.create(vertx);
	       
	}

	@AfterEach
	public void finish(VertxTestContext testContext, Vertx vertx) {
		System.out.println("after");
		vertx.close(testContext.succeeding(response -> {
			testContext.completeNow();
		}));
		server.stop();
	}
	
	

	//@Test
	//@Timeout(value = 100000, timeUnit = TimeUnit.SECONDS)
	public void testRetrieveConfiguration(Vertx vertx, VertxTestContext testContext) throws Exception {
		whenHttp(server)
		.match(get("/api/configuration/candidate-config"), withHeader("Authentication"))
		.then( resourceContent("exampleConfiguration.json"));

		
		
		JsonObject config = new JsonObject().put("http.host", "localhost").put("http.port", server.getPort());
		
		new RestClientImpl(vertx, config).getConfiguration().onComplete(testContext.succeeding(buffer -> {
			testContext.verify(() -> {
				System.out.println("buffer\n="+buffer);
				assertThat(buffer.getFaces().size(), is(1));
				testContext.completeNow();

			});
			testContext.failed();
		}));

	}

	//@Test
	public void testSendStatus(Vertx vertx, VertxTestContext testContext) throws Exception {
		whenHttp(server)
				.match(startsWithUri("/api/notification/status"))
				.then(status(HttpStatus.CREATED_201),stringContent("Status successfully processed"));
		JsonObject config = new JsonObject().put("http.host", "localhost").put("http.port", server.getPort());
		new RestClientImpl(vertx, config).sendStatus().onComplete(testContext.succeeding(buffer -> {
			testContext.verify(() -> {
				testContext.completeNow();
			});
			testContext.failed();
        }));

	}
	//@Test
		public void testSendIncorretStatus(Vertx vertx, VertxTestContext testContext) throws Exception {
			whenHttp(server)
					.match(startsWithUri("/api/notification/status"))
					.then(status(HttpStatus.BAD_REQUEST_400),stringContent("Incorrect Status object"));
			JsonObject config = new JsonObject().put("http.host", "localhost").put("http.port", server.getPort());
			new RestClientImpl(vertx, config).sendStatus().onFailure(handler ->{
				testContext.failed();
				testContext.completeNow();
			});

		}

	//@Test
	public void testSendRunningConfiguration(Vertx vertx, VertxTestContext testContext) throws Exception {

		Configuration runningConfiguration = new Configuration();

		whenHttp(server).match(put("/api/configuration/running-config"), withHeader("Authentication"))
				.then(status(HttpStatus.OK_200));
		
		EntryPoint entryPoint = new EntryPoint(server.getPort(), "localhost");
		new ConfigurationServiceImpl(webClient, entryPoint).sendRunningConfiguration(runningConfiguration)
		
		.onFailure(handler ->{
			testContext.failed();
		})
				
				  .onSuccess(buffer -> testContext.verify(() -> {
				  System.out.println("buffer:\n"+buffer);
				  assertThat(buffer.toString()).isEqualTo("Ok"); testContext.completeNow(); }))
		
		
		
				.onComplete(testContext.succeeding(handler ->{
					testContext.verify(() -> {
						System.out.println("handler:\n"+handler);
						//assertThat(handler.toString()).isEqualTo("Ok");
						testContext.completeNow();
					});
					testContext.failed();
					
				}));
					
	}

	//@Test
	public void testCompareConfig(Vertx vertx) {

		Configuration running = new Configuration();
		Configuration candidate = new Configuration();

		Face face = new Face(111, "a1.a1.a1.a1", "b1.b1.b1.b1", "eth0", "scheme", 12);
		Face face1 = new Face(112, "a1.a1.a1.a1", "b1.b1.b1.b1", "eth0", "scheme", 12);
		Face face2 = new Face(115, "a1.a1.a1.a1", "b1.b1.b1.b1", "eth0", "scheme", 12);
		Face face3 = new Face(112, "a1.a1.a1.a1", "b1.b1.b1.b1", "eth0", "scheme", 12);

		running.addFace(face);
		running.addFace(face1);

		candidate.addFace(face2);
		candidate.addFace(face3);

		Route route = new Route("/a/b/c", 1101, 0, 10, true, false);
		Route route1 = new Route("/a/b/c", 1101, 0, 10, true, false);

		running.addRoute(route);
		candidate.addRoute(route1);

		new RestClientImpl(vertx, new JsonObject()).compareConfiguration(running, candidate).forEach(action -> {
			System.out.println(action.encodePrettily());
		});
	}
	
	//@Test 
	public void testgetFreshToken(Vertx vertx, VertxTestContext testContext) throws IOException , GeneralSecurityException {
		
		String user = "test";
		String pass = "test@1234";
		
		whenHttp(server).match(post("/api/login/agent"), withPostBodyContaining("\"username\":\"test\",\"password\":\"test@1234\""))
		.then(resourceContent("token.json"));
				JsonObject config = new JsonObject()
				.put("http.host", "localhost")
				.put("http.port", server.getPort());
				
		new RestClientImpl(vertx, config).login(user, pass).onComplete(testContext.succeeding(buffer -> {
			testContext.verify(() -> {
				
				assertThat(buffer.toString(), is(Token));
				testContext.completeNow();
				
			});
			testContext.failed();
        }));
		
	}
}
