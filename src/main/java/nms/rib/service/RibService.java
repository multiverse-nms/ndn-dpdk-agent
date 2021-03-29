package nms.rib.service;

import io.vertx.core.Future;
import nms.rib.Rib;
import nms.rib.Route;

public interface RibService {

	public void addRoute(Route route, RibHandler handler);

	public void removeRoute(Route route, RibHandler handler);
	
	public Future<Rib> addRoute(Route route);

	public Future<Rib>  removeRoute(Route route);

}
