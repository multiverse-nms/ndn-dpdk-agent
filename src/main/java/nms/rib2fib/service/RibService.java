package nms.rib2fib.service;

import net.named_data.jndn.Name;
import nms.rib2fib.Route;

public interface RibService {

	public void addRoute(Name name, int face, int cost);

	public void removeRoute(Name name, int face, int origin);

	public void addRoute(Route route, RibHandler handler);

	public void removeRoute(Route route, RibHandler handler);

}
