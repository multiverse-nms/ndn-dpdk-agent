package nms.rib.service;


import io.vertx.core.Future;
import io.vertx.core.Promise;
import net.named_data.jndn.Name;
import nms.rib.Rib;
import nms.rib.Route;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RibServiceImpl implements RibService {
	private static final Logger LOG = LoggerFactory.getLogger(RibServiceImpl.class);
	private Rib rib;

	public RibServiceImpl() {
		rib = new Rib();
		LOG.info("new empty RIB instantiated");
	}

	@Override
	public void addRoute(Route route, RibHandler handler) {
		// TODO Auto-generated method stub
		Rib newRib = rib.addRoute(route);
		handler.handleRib(newRib);
	}

	@Override
	public void removeRoute(Route route, RibHandler handler) {
		// TODO Auto-generated method stub
		Rib newRib = rib.removeRoute(route);
		handler.handleRib(newRib);
	}

	@Override
	public Future<Rib> addRoute(Route route) {
		LOG.info("addRoute(" + route + ")");
		Promise<Rib> promise = Promise.promise();
		rib = rib.addRoute(route);
		LOG.info("new rib " + rib);
		promise.complete(rib);
		return promise.future();
	}

	@Override
	public Future<Rib> removeRoute(Route route) {
		LOG.info("removeRoute(" + route + ")");
		Promise<Rib> promise = Promise.promise();
		rib = rib.removeRoute(route);
		LOG.info("new rib " + rib);
		promise.complete(rib);
		return promise.future();
	}

}
