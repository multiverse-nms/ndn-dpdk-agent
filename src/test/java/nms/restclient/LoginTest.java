package nms.restclient;

import static com.xebialabs.restito.builder.stub.StubHttp.whenHttp;
import static com.xebialabs.restito.semantics.Action.resourceContent;

import static com.xebialabs.restito.semantics.Condition.post;
import static com.xebialabs.restito.semantics.Condition.withPostBodyContaining;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.xebialabs.restito.server.StubServer;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


@ExtendWith(VertxExtension.class)
public class LoginTest {

	private StubServer server;
	
	private String dummyToken = "qwertyuiopasdfghjklzxcvbnm123456";

	@BeforeEach
	public void start() {
		server = new StubServer(8889);
		server.secured();
		server.run();
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
	public void testLogin(Vertx vertx, VertxTestContext testContext) throws Exception {
		
		String user = "test";
		String pass = "test@1234";
		
		whenHttp(server).match(post("/api/login/agent"), withPostBodyContaining("\"username\":\"test\",\"password\":\"test@1234\""))
		.then(resourceContent("token.json"));
		
		

		JsonObject config = new JsonObject()
				.put("http.host", "localhost")
				.put("http.port", server.getPort());
		
		new RestClientImpl(vertx, config).login(user, pass).onComplete(testContext.succeeding(buffer -> {
			testContext.verify(() -> {
				
				assertThat(buffer.toString(), is(dummyToken));
				testContext.completeNow();
				
			});
			testContext.failed();
        }));
		
	}
	
	


	


}
