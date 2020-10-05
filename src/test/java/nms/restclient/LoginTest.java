package nms.restclient;

import static com.xebialabs.restito.builder.stub.StubHttp.whenHttp;
import static com.xebialabs.restito.semantics.Action.resourceContent;

import static com.xebialabs.restito.semantics.Condition.post;
import static com.xebialabs.restito.semantics.Condition.withPostBodyContaining;
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
public class LoginTest {

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
	public void testLogin(Vertx vertx, VertxTestContext testContext) throws Exception {
		
		String user = "omar";
		String pass = "omar@1234";
		
		whenHttp(server).match(post("/login/agent"), withPostBodyContaining("\"username\":\"omar\",\"password\":\"omar@1234\"")).then(resourceContent("token.json"));
		
		EntryPoint entryPoint = new EntryPoint(server.getPort(), "localhost");
		new RestClientImpl(vertx).login(user, pass).onComplete(ar -> {
			if (ar.failed()) {
				testContext.failNow(ar.cause());
			} else {
				String token = ar.result();
				testContext.verify(() -> assertThat(token, is(dummyToken)));
				testContext.completeNow();
			}
		}); 
	}


	


}
