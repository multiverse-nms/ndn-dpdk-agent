package nms.restclient;

import static com.xebialabs.restito.builder.stub.StubHttp.whenHttp;
import static com.xebialabs.restito.semantics.Action.resourceContent;
import static com.xebialabs.restito.semantics.Condition.get;
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
		whenHttp(server).match(get("/candidate-config")).then(resourceContent("exampleConfiguration.json"));

		EntryPoint entryPoint = new EntryPoint(server.getPort(), "localhost");
		new RestClientImpl(vertx, entryPoint).getCandidateConfiguration().onComplete(ar -> {
			if (ar.failed()) {
				testContext.failNow(ar.cause());
			} else {
				Configuration candidateConfig = ar.result();
				testContext.verify(() -> assertThat(candidateConfig.getFaces().size(), is(1)));
				testContext.completeNow();
			}
		});
	}

}
