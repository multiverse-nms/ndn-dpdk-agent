package nms.rib2fib.service;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import net.named_data.jndn.Name;
import nms.rib2fib.Rib;
import nms.rib2fib.Route;

public class RibServiceImpl implements RibService {
	private static final Logger LOG = LoggerFactory.getLogger(RibServiceImpl.class);
	private Rib rib;
	
	public RibServiceImpl() {
		rib = new Rib();
		LOG.info("new empty RIB instantiated");
	}
	
	
	@Override
	public void addRoute(Name name, int face, int origin) {
		rib.addRoute(new Route(name, face, origin));
		
	}

	@Override
	public void removeRoute(Name name, int face, int origin) {
		rib.removeRoute(new Route(name, face, origin));
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

}
