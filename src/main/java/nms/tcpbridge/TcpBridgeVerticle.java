package nms.tcpbridge;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.bridge.BridgeOptions;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.eventbus.bridge.tcp.TcpEventBusBridge;

public class TcpBridgeVerticle extends AbstractVerticle {
	private static final Logger LOG = LoggerFactory.getLogger(TcpBridgeVerticle.class);
	private static final String ADDRESS_BRIDGE = "eventbusbridge";
	private static final String ADDRESS_CLI = "clitool";
	private static final int PORT = 7001;

	@Override
	public void start(Promise<Void> startPromise) throws Exception {
		LOG.info("starting TCP bridge verticle");
		vertx.eventBus().consumer(ADDRESS_BRIDGE, message -> {
			LOG.info("received message " + message.body());
			// TODO: process messages
		});

		TcpEventBusBridge bridge = TcpEventBusBridge.create(vertx,
				new BridgeOptions().addInboundPermitted(new PermittedOptions().setAddress(ADDRESS_BRIDGE))
						.addOutboundPermitted(new PermittedOptions().setAddress(ADDRESS_CLI)));

		bridge.listen(PORT, res -> {
			if (res.succeeded()) {
				LOG.info("eventbus bridge listening on port " + PORT);
			} else {
				LOG.error("failed to start eventbus bridge, because " + res.cause());
			}
		});
	}
}