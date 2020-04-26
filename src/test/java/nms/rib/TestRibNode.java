package nms.rib2fib;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import net.named_data.jndn.Name;
import net.named_data.jndn.Name.Component;

class TestRibNode {

	@Test
	void testFindOrInsertChild() {
		Name prefix = new Name("/a/b/c");
		Component compA = prefix.get(0);
		Component compB = prefix.get(1);
		Component compC = prefix.get(2);

		RibNode root = new RibNode();
		RibNode nodeA = root.findOrInsertChild(compA);
		RibNode nodeB = nodeA.findOrInsertChild(compB);
		RibNode nodeC = nodeB.findOrInsertChild(compC);

		nodeC.findOrInsertRoute(new Route(prefix, 1000, 1)); // should be inserted here
		assertEquals(1, nodeC.getRoutes().size(), "node should not be null");
		RibNode foundC = nodeB.findOrInsertChild(compC);
		assertEquals(false, foundC == null, "node should not be null");
	}

	@Test
	void testGetParent() {
		RibNode root = new RibNode();
		RibNode nodeA = root.findOrInsertChild(new Component("a"));
		RibNode nodeB = nodeA.findOrInsertChild(new Component("b"));
		RibNode nodeC = nodeB.findOrInsertChild(new Component("c"));

		assertEquals(nodeA, nodeB.getParent(), "should be the same node");
		assertEquals(nodeB, nodeC.getParent(), "should be the same node");
	}

	@Test
	void testCapture() {
		RibNode node = new RibNode();
		assertEquals(false, node.hasCapture(), "should not have capture");

		Route route1 = new Route(new Name("/a/b/c"), 1000, 1);
		node.findOrInsertRoute(route1);
		assertEquals(false, node.hasCapture(), "should not have capture");

		Route route2 = new Route(new Name("/a/b/c"), 1001, 0);
		route2.setCaptureFlag(true);
		node.findOrInsertRoute(route2);
		assertEquals(true, node.hasCapture(), "should have capture");
	}

	@Test
	void testAncestors() {
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

		RibNode node = rib.findOrInsertEntry(new Name("/a/b/c"));
		List<Name> expected = new ArrayList<>(
				Arrays.asList(new Name("/a/b/c"), new Name("/a/b"), new Name("/a"), new Name("/")));
		List<Name> actual = new ArrayList<>();
		List<RibNode> ancestors = node.getAncestors();

		ancestors.forEach(ancestor -> {
			actual.add(ancestor.getName());
		});

		assertEquals(expected, actual);

	}

}