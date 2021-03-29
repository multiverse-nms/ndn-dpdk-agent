package nms.rib;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import net.named_data.jndn.Name;
import net.named_data.jndn.Name.Component;
import nms.rib.Fib;
import nms.rib.FibNode;
import nms.rib.commands.FibCommand;

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
	
	
	
	@Test
	void testDiff() {
	
		Fib fib1 = new Fib();
		fib1.getRoot().addNexthop(0, 0);
		FibNode nodeA = fib1.findOrInsertEntry(new Name("/a"));
		nodeA.addNexthop(1, 10);
		nodeA.addNexthop(7, 5);
	
		FibNode nodeAB = fib1.findOrInsertEntry(new Name("/a/b"));
		nodeAB.addNexthop(2, 20);
		FibNode nodeAC = fib1.findOrInsertEntry(new Name("/a/c"));
		nodeAC.addNexthop(3, 30);
		FibNode nodeD = fib1.findOrInsertEntry(new Name("/d"));
		nodeD.addNexthop(4, 40);
				
		System.out.println(fib1);
		
		Fib fib2 = new Fib();
		fib2.getRoot().addNexthop(0, 0);
		FibNode nodeA2 = fib2.findOrInsertEntry(new Name("/a"));
		nodeA2.addNexthop(1, 11);
		nodeA2.addNexthop(7, 20);
		
		FibNode nodeAB2 = fib2.findOrInsertEntry(new Name("/a/b"));
		nodeAB2.addNexthop(2, 20);
		FibNode nodeAC2 = fib2.findOrInsertEntry(new Name("/a/c"));
		nodeAC2.addNexthop(3, 30);
		FibNode nodeD2 = fib2.findOrInsertEntry(new Name("/d"));
		nodeD2.addNexthop(4, 40);
		FibNode nodeDB = fib2.findOrInsertEntry(new Name("/d/b"));
		nodeDB.addNexthop(5, 50);
				
		System.out.println(fib2);
		List<FibCommand> commands = fib2.compare(fib1);
		System.out.println(commands);
		
		
		
		
	}

}