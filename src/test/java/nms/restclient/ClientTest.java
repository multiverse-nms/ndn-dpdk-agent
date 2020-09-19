package nms.restclient;

import static com.xebialabs.restito.builder.stub.StubHttp.whenHttp;
import static com.xebialabs.restito.semantics.Action.resourceContent;
import static com.xebialabs.restito.semantics.Condition.get;
import static com.xebialabs.restito.semantics.Condition.post;
import static com.xebialabs.restito.semantics.Condition.basicAuth;
import static com.xebialabs.restito.semantics.Condition.withPostBodyContaining;
import static com.xebialabs.restito.semantics.Condition.withHeader;
import static org.junit.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.xebialabs.restito.server.StubServer;

import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;

@ExtendWith(VertxExtension.class)
public class ClientTest {

	private StubServer server;
	
	private String dummyToken = "qwertyuiopasdfghjklzxcvbnm123456";

	@BeforeEach
	public void start() {
		server = new StubServer(8889).run();
	}

	@AfterEach
	public void stop() {
		server.stop();
	}

	@Test
	public void testRetrieveConfiguration(Vertx vertx, VertxTestContext testContext) throws Exception {
		whenHttp(server).match(get("/configuration/candidate-config"), withHeader("Authentication")).then(resourceContent("exampleConfiguration.json"));

		EntryPoint entryPoint = new EntryPoint(server.getPort(), "localhost");
		new RestClientImpl(vertx, entryPoint).getCandidateConfiguration(dummyToken).onComplete(ar -> {
			if (ar.failed()) {
				testContext.failNow(ar.cause());
			} else {
				Configuration candidateConfig = ar.result();
				testContext.verify(() -> assertThat(candidateConfig.getFaces().size(), is(1)));
				testContext.completeNow();
			}
		});
	}
	
	
	@Test
	public void testLogin(Vertx vertx, VertxTestContext testContext) throws Exception {
		
		String user = "omar";
		String pass = "omar@1234";
		
		whenHttp(server).match(post("/login/agent"), withPostBodyContaining("\"username\":\"omar\",\"password\":\"omar@1234\"")).then(resourceContent("token.json"));
		
		EntryPoint entryPoint = new EntryPoint(server.getPort(), "localhost");
		new RestClientImpl(vertx, entryPoint).basicAuthentication(user, pass).onComplete(ar -> {
			if (ar.failed()) {
				testContext.failNow(ar.cause());
			} else {
				String token = ar.result();
				testContext.verify(() -> assertThat(token, is("qwertyuiopasdfghjklzxcvbnm123456")));
				testContext.completeNow();
			}
		});
	}

}
