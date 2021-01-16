package nms.restclient;

import static com.xebialabs.restito.builder.stub.StubHttp.whenHttp;
import static com.xebialabs.restito.semantics.Action.resourceContent;
import static com.xebialabs.restito.semantics.Action.status;
import static com.xebialabs.restito.semantics.Condition.get;
import static com.xebialabs.restito.semantics.Condition.put;
import static com.xebialabs.restito.semantics.Condition.withHeader;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.awt.List;
import java.io.FileReader;
import java.io.InputStream;

import org.glassfish.grizzly.http.util.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.xebialabs.restito.server.StubServer;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxTestContext;
import nms.restclient.service.impl.ConfigurationServiceImpl;

public class ConfigurationTest {
	
	private StubServer server;
	private WebClient webClient;
	
          
	@BeforeEach
	public void start() {
		server = new StubServer(8889).run();
	}

	@AfterEach
	public void stop() {
		server.stop();
	}

	//@Test
	public void testRetrieveConfiguration(Vertx vertx, VertxTestContext testContext) throws Exception {
		whenHttp(server).match(get("/configuration/candidate-config"), withHeader("Authentication")).then(resourceContent("exampleConfiguration.json"));

		EntryPoint entryPoint = new EntryPoint(server.getPort(), "localhost");
		new ConfigurationServiceImpl(webClient, entryPoint).getConfiguration().onComplete(ar -> {
			if (ar.failed()) {
				testContext.failNow(ar.cause());
			} else  {
				
				Configuration candidateConfig = ar.result();
				testContext.verify(() -> assertThat(candidateConfig.getFaces().size(), is(1)));
				testContext.completeNow();
			}
		});
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
	public void  testCompareConfig (Vertx vertx,  VertxTestContext testContext) {		
		RestClientImpl restClientImpl = new RestClientImpl(vertx, new JsonObject());
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
		
		restClientImpl.compareConfiguration(running, candidate).forEach(action -> {
			System.out.println(action.encodePrettily());
		});	
	}	
}
