package nms.rib;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

import net.named_data.jndn.Name;
import nms.rib.Route;
import nms.rib.RouteKey;

class TestRouteKey {

	@Test
	void test() {
		HashMap<RouteKey, Route> routes = new HashMap<>();
		
		Route route = new Route(new Name("/a/b/c"), 1000, 1);
		
		routes.put(new RouteKey(route.getFaceId(), route.getOrigin()), route);
			
		Route found  = routes.get(new RouteKey(1000, 1));
		Route notFound  = routes.get(new RouteKey(2000, 1));
		
		assertEquals(found, route, "same routes");
		assertEquals(notFound, null, "no route");
	}

}
