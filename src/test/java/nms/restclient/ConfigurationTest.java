package nms.restclient;

import static com.xebialabs.restito.builder.stub.StubHttp.whenHttp;
import static com.xebialabs.restito.semantics.Action.resourceContent;
import static com.xebialabs.restito.semantics.Action.status;
import static com.xebialabs.restito.semantics.Condition.get;
import static com.xebialabs.restito.semantics.Condition.put;
import static com.xebialabs.restito.semantics.Condition.withHeader;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static com.xebialabs.restito.semantics.Condition.parameter;


import org.glassfish.grizzly.http.util.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.xebialabs.restito.server.StubServer;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;

import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import nms.restclient.service.impl.ConfigurationServiceImpl;

@ExtendWith(VertxExtension.class)
public class ConfigurationTest {
	
	private StubServer server;
	private WebClient webClient;
	
	   
	@BeforeEach
	public void start() {
		server = new StubServer(8889).run();
		
	}

	
	@AfterEach
    public void finish(VertxTestContext testContext, Vertx vertx) {
		System.out.println("after");
        vertx.close(testContext.succeeding(response -> {
            testContext.completeNow();
        }));
        server.stop();
    }


	@Test
	//@Timeout(value = 100, timeUnit = TimeUnit.SECONDS)
	public void testRetrieveConfiguration(Vertx vertx, VertxTestContext testContext) throws Exception {
		whenHttp(server).match(get("/api/configuration/candidate-config"), withHeader("Authentication")).then(resourceContent("exampleConfiguration.json"));
		
		JsonObject config = new JsonObject()
				.put("host", "localhost")
				.put("port", server.getPort());
		new RestClientImpl(vertx, config).getConfiguration().onComplete(testContext.succeeding(buffer -> {
			testContext.verify(() -> {
								testContext.verify(() -> assertThat(buffer.getFaces().size(), is(1)));
				testContext.completeNow();
				
			});
			testContext.failed();
        }));

		
	}
	@Test 
	public void testSendStatus(Vertx vertx, VertxTestContext testContext) throws Exception {
		whenHttp(server).match(put("/api/notification/status/"),parameter("statusId", "UP") ,withHeader("Authentication")).then(status(HttpStatus.OK_200));
		JsonObject config = new JsonObject()
				.put("host", "localhost")
				.put("port", server.getPort());
		new RestClientImpl(vertx, config).sendStatus().onFailure(testContext::failNow)
           .onSuccess(buffer -> testContext.verify(() -> {
        	   assertThat(buffer.toString()).isEqualTo("Ok");
            testContext.completeNow();
          }));
		
	}
	
	@Test
	public void testSendRunningConfiguration(Vertx vertx, VertxTestContext testContext) throws Exception {
		
		  Configuration runningConfiguration = new Configuration();

		  whenHttp(server).match(put("/notification/status"), withHeader("Authentication")).then(status(HttpStatus.OK_200));
		 
		EntryPoint entryPoint = new EntryPoint(server.getPort(), "localhost");
		new ConfigurationServiceImpl(webClient, entryPoint).sendRunningConfiguration(runningConfiguration).onComplete(ar -> {
			if (ar.failed()) {
				testContext.failNow(ar.cause());
			} else  {
				testContext.completeNow();
			}
		});
	}
	
	@Test
	public void  testCompareConfig (Vertx vertx) {		
		
		Configuration running = new Configuration();
		Configuration candidate = new Configuration();

		Face face = new Face(111, "a1.a1.a1.a1", "b1.b1.b1.b1", "eth0","scheme", 12);
		Face face1 = new Face(112, "a1.a1.a1.a1", "b1.b1.b1.b1", "eth0","scheme", 12);
		Face face2 = new Face(115, "a1.a1.a1.a1", "b1.b1.b1.b1", "eth0","scheme", 12);
	    Face face3 = new Face(112, "a1.a1.a1.a1", "b1.b1.b1.b1", "eth0","scheme", 12);
	   
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
}
