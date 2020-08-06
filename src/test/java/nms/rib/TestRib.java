package nms.rib;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import net.named_data.jndn.Name;
import nms.rib.Fib;
import nms.rib.FibNode;
import nms.rib.FibNodeHandler;
import nms.rib.Rib;
import nms.rib.RibNode;
import nms.rib.RibNodeHandler;
import nms.rib.Route;

class TestRib {

	@Test
	void testTraverse() {
		Rib rib = new Rib();
		Route route1 = new Route(new Name("/a/b/c"), 1000, 1);
		rib.addRoute(route1);
		Route route2 = new Route(new Name("/a/b/d"), 1002, 0);
		rib.addRoute(route2);
		Route route3 = new Route(new Name("/b/c"), 1003, 0);
		rib.addRoute(route3);
		
		List<Name> names = new ArrayList<>();
		rib.traverse(rib.getRoot(), new RibNodeHandler() {

			@Override
			public void handle(RibNode node) {
				names.add(node.getName());	
			}
			
		});
		
		
		List<Name> expected = new ArrayList<>();
		expected.add(new Name("/"));
		expected.add(new Name("/a"));
		expected.add(new Name("/a/b"));
		expected.add(new Name("/a/b/c"));
		expected.add(new Name("/a/b/d"));
		expected.add(new Name("/b"));
		expected.add(new Name("/b/c"));
		assertEquals(expected, names, "both arrays must have the same elements");
	}
	
	
	@Test
	void testRemove() {
		Rib rib = new Rib();
		Route route1 = new Route(new Name("/a/b/c"), 1000, 1);
		rib.addRoute(route1);
		Route route2 = new Route(new Name("/a/b/d"), 1002, 0);
		rib.addRoute(route2);
		Route route3 = new Route(new Name("/b/c"), 1003, 0);
		rib.addRoute(route3);
		
		Route routeToDelete1 = new Route(new Name("/a/b/d"), 1002, 0);
		rib.removeRoute(routeToDelete1);
		
		System.out.println(rib);
	}
	
	
	
	@Test
	void testRemoveCase2() {
		Rib rib = new Rib();
		Route route1 = new Route(new Name("/a/b"), 1001, 10);
		rib.addRoute(route1);
		Route route2 = new Route(new Name("/a/b"), 1002, 20);
		rib.addRoute(route2);
		Route route3 = new Route(new Name("/a/b"), 1003, 15);
		rib.addRoute(route3);
		
		Route routeToDelete1 = new Route(new Name("/a/b"), 1003, 15);
		rib.removeRoute(routeToDelete1);
		Route routeToDelete2 = new Route(new Name("/a/b"), 1002, 20);
		rib.removeRoute(routeToDelete2);
		Route routeToDelete3 = new Route(new Name("/a/b"), 1001, 10);
		rib.removeRoute(routeToDelete3);
		
		System.out.println("RIB:\n" + rib);
	}
	
	
	
	@Test
	void testRibTiFib1() {
		Rib rib = new Rib();

		Route routeA1 = new Route(new Name("/a"), 1, 0);
		routeA1.setCost(10);
		routeA1.setChildInheritFlag(false);

		Route routeA2 = new Route(new Name("/a"), 1, 1);
		routeA2.setCost(20);

		Route routeA3 = new Route(new Name("/a"), 2, 0);
		routeA3.setCost(30);
		routeA3.setChildInheritFlag(false);

		Route routeAB1 = new Route(new Name("/a/b"), 1, 0);
		routeAB1.setCost(40);

		Route routeAB2 = new Route(new Name("/a/b"), 2, 0);
		routeAB2.setCost(50);

		Route routeABC = new Route(new Name("/a/b/c"), 3, 0);
		routeABC.setCost(60);

		Route routeABCD = new Route(new Name("/a/b/c/d"), 4, 0);
		routeABCD.setCost(70);
		routeABCD.setCaptureFlag(true);

		List<Route> routes = new ArrayList<>();
		routes.addAll(Arrays.asList(routeA1, routeA2, routeA3, routeAB1, routeAB2, routeABC, routeABCD));

		rib.insertRoutes(routes);
		
		Fib fib = rib.toFib();
		
		Name name = new Name("/a/b/c/d");
		Map<Integer, Integer> expectedNexthops = new HashMap<>();
		expectedNexthops.put(4, 70);
		fib.traverse(fib.getRoot(), new FibNodeHandler() {
			@Override
			public void handle(FibNode node) {
				if (node.getName().equals(name)) {
					assertEquals(expectedNexthops, node.getNexthops().getMap());
				}
			}
		});
	}
	
	@Test
	void testRibTiFib2() {
		Rib rib = new Rib();

		Route route1 = new Route(new Name("/"), 1, 0);
		route1.setCost(10);

		Route route2 = new Route(new Name("/"), 2, 1);
		route2.setCost(20);
		route2.setChildInheritFlag(false);

		Route routeA = new Route(new Name("/a"), 3, 0);
		routeA.setCost(30);
		

		Route routeABC = new Route(new Name("/a/b/c"), 4, 0);
		routeABC.setCost(40);

		Route routeD1 = new Route(new Name("/d"), 5, 0);
		routeD1.setCost(50);
		routeD1.setCaptureFlag(true);

		Route routeD2 = new Route(new Name("/d"), 6, 0);
		routeD2.setCost(60);

		List<Route> routes = new ArrayList<>();
		routes.addAll(Arrays.asList(route1, route2, routeA, routeABC, routeD1, routeD2));

		rib.insertRoutes(routes);
		
		Fib fib = rib.toFib();
		
		Name nameABC = new Name("/a/b/c");
		Map<Integer, Integer> expectedNexthopsABC = new HashMap<>();
		expectedNexthopsABC.put(1, 10);
		expectedNexthopsABC.put(3, 30);
		expectedNexthopsABC.put(4, 40);
		fib.traverse(fib.getRoot(), new FibNodeHandler() {
			@Override
			public void handle(FibNode node) {
				if (node.getName().equals(nameABC)) {
					assertEquals(expectedNexthopsABC, node.getNexthops().getMap());
				}
			}	
		});
				
		Name nameD = new Name("/d");
		Map<Integer, Integer> expectedNexthopsD = new HashMap<>();
		expectedNexthopsD.put(5, 50);
		expectedNexthopsD.put(6, 60);
		fib.traverse(fib.getRoot(), new FibNodeHandler() {
			@Override
			public void handle(FibNode node) {
				if (node.getName().equals(nameD)) {
					assertEquals(expectedNexthopsD, node.getNexthops().getMap());
				}
			}
		});			
	}

}
