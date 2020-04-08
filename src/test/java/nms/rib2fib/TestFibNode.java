package nms.rib2fib;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import net.named_data.jndn.Name.Component;

class TestFibNode {

	@Test
	void testEqualsSameNameSameNexthops() {
		FibNode root1 = new FibNode();
		FibNode root2 = new FibNode();

		// same names, same nexthops
		FibNode node1 = root1.findOrInsertChild(new Component("/a"));
		node1.addNexthop(1, 10);

		FibNode node2 = root2.findOrInsertChild(new Component("/a"));
		node2.addNexthop(1, 10);

		boolean expected = true;
		boolean actual = node1.equals(node2);
		assertEquals(expected, actual);

	}

	@Test
	void testEqualsSameNameDifferentNexthops() {
		FibNode root1 = new FibNode();
		FibNode root2 = new FibNode();

		// same names, different nexthops
		FibNode node1 = root1.findOrInsertChild(new Component("/a"));
		node1.addNexthop(1, 10);

		FibNode node2 = root2.findOrInsertChild(new Component("/a"));
		node2.addNexthop(2, 10);

		boolean expected = false;
		boolean actual = node1.equals(node2);
		assertEquals(expected, actual);

	}

	@Test
	void testEqualsDifferentNameSameNexthops() {
		FibNode root1 = new FibNode();
		FibNode root2 = new FibNode();

		// different names, same nexthops
		FibNode node1 = root1.findOrInsertChild(new Component("/b"));
		node1.addNexthop(1, 10);

		FibNode node2 = root2.findOrInsertChild(new Component("/a"));
		node2.addNexthop(1, 10);

		boolean expected = false;
		boolean actual = node1.equals(node2);
		assertEquals(expected, actual);

	}

	@Test
	void testEqualsDifferentNameDifferentNexthops() {
		FibNode root1 = new FibNode();
		FibNode root2 = new FibNode();

		// different names, different nexthops
		FibNode node1 = root1.findOrInsertChild(new Component("/b"));
		node1.addNexthop(1, 10);

		FibNode node2 = root2.findOrInsertChild(new Component("/a"));
		node2.addNexthop(2, 10);

		boolean expected = false;
		boolean actual = node1.equals(node2);
		assertEquals(expected, actual);

	}

//	@Test
//	void testEqualsSameNameSameNexthopsDifferentOrder() {
//		FibNode root1 = new FibNode();
//		FibNode root2 = new FibNode();
//
//		// same names, same nexthops
//		FibNode node1 = root1.findOrInsertChild(new Component("/a"));
//		node1.addNexthop(1, 10);
//		node1.addNexthop(2, 10);
//
//		FibNode node2 = root2.findOrInsertChild(new Component("/a"));
//		node2.addNexthop(2, 10);
//		node2.addNexthop(1, 10);
//
//		boolean expected = false;
//		boolean actual = node1.equals(node2);
//		assertEquals(expected, actual);
//
//	}
}