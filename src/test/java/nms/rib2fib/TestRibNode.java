package nms.rib2fib;
import static org.junit.jupiter.api.Assertions.*;

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
		RibNode entryA = root.findOrInsertChild(compA);
		RibNode entryB = entryA.findOrInsertChild(compB);
		RibNode entryC = entryB.findOrInsertChild(compC);
		
		
		Route route1 = entryC.findOrInsertRoute(new Route(prefix, 1000, 1)); // should be inserted here
		assertEquals(1, entryC.getRoutes().size(), "entry should not be null");
		RibNode foundC = entryB.findOrInsertChild(compC);
		assertEquals(false, foundC == null, "entry should not be null");
	}

	
	@Test
	void testGetParent() {
		RibNode root = new RibNode();	
		RibNode nodeA = root.findOrInsertChild(new Component("a"));
		RibNode nodeB = nodeA.findOrInsertChild(new Component("b"));
		RibNode nodeC = nodeB.findOrInsertChild(new Component("c"));
		
		
		assertEquals(nodeB, nodeC.getParent(), "should be the same node");
	}
}

