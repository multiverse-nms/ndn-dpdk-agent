package nms.restclient;

import static com.xebialabs.restito.builder.stub.StubHttp.whenHttp;
import static com.xebialabs.restito.semantics.Action.status;
import static com.xebialabs.restito.semantics.Condition.put;
import static com.xebialabs.restito.semantics.Condition.withHeader;


import org.glassfish.grizzly.http.util.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.xebialabs.restito.server.StubServer;

import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import nms.restclient.service.impl.NotificationServiceImpl;

@ExtendWith(VertxExtension.class)
public class NotificationTest {
	
	private WebClient webClient;
	private StubServer server;
	
	
	@BeforeEach
	public void start(Vertx vertx) {
		server = new StubServer(8889).run();
		this.webClient = WebClient.create(vertx);
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
	public void testSendStatusNotification(Vertx vertx, VertxTestContext testContext) throws Exception {
		
	   	StatusNotification StatusNotification = new StatusNotification(Status.UP);
	   	
	   	
	    whenHttp(server).match(put("/api/notification/status"), withHeader("Authentication")).then(status(HttpStatus.OK_200));
	 
		 EntryPoint entryPoint = new EntryPoint(server.getPort(), "localhost");	
         new NotificationServiceImpl(webClient, entryPoint).sendNotification(StatusNotification).onComplete(ar -> {
        	 if(ar.failed()) {
        		 testContext.failNow(ar.cause());
        	 }
        	 else {
        		 testContext.completeNow();
        	 }
         });
		
	}
	
	//@Test
	public void testSendFaultNotification(Vertx vertx, VertxTestContext testContext) throws Exception {
		
		FaultNotification FaultNotification = new FaultNotification();
	   	FaultNotification.setCode(0);
	   	FaultNotification.setMessage("string");
	   
	    whenHttp(server).match(put("/api/notification/fault"), withHeader("Authentication")).then(status(HttpStatus.OK_200));
	 
		 EntryPoint entryPoint = new EntryPoint(server.getPort(), "localhost");	
         new NotificationServiceImpl(webClient, entryPoint).sendNotification(FaultNotification).onComplete(ar -> {
        	 if(ar.failed()) {
        		 testContext.failNow(ar.cause());
        	 }
        	 else {
        		 testContext.completeNow();
        	 }
         });
		
	}
	
	//@Test
	public void testSendEventNotification(Vertx vertx, VertxTestContext testContext) throws Exception {
		
		EventNotification EventNotification = new EventNotification();
		EventNotification.setCode(0);
		EventNotification.setMessage("string");
		EventNotification.setSeverity(Severity.LOW);
		EventNotification.setTimestamp("2020-09-16T15:06:40.892Z");
		
		
	    whenHttp(server).match(put("/api/notification/fault"), withHeader("Authentication")).then(status(HttpStatus.OK_200));
	 
		 EntryPoint entryPoint = new EntryPoint(server.getPort(), "localhost");	
         new NotificationServiceImpl(webClient, entryPoint).sendNotification(EventNotification).onComplete(ar -> {
        	 if(ar.failed()) {
        		 testContext.failNow(ar.cause());
        	 }
        	 else {
        		 testContext.completeNow();
        	 }
         });		
	}
}
