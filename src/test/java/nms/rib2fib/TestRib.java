package nms.rib2fib;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import net.named_data.jndn.Name;

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
				System.out.println("Parent " + node.getParent());
				System.out.println("Node " + node);
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

}
