package nms.rib2fib;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import net.named_data.jndn.Name;

class TestNexthopList {

	@Test
	void testEquals() {
		
		NexthopList list1 = new NexthopList();
		list1.addnexthop(new Name("/a/b/c"), 1, 10);
		list1.addnexthop(new Name("/a/b/d"), 2, 20);
		list1.addnexthop(new Name("/a/b/e"), 3, 30);
		
		NexthopList list2 = new NexthopList();
		list2.addnexthop(new Name("/a/b/c"), 1, 10);
		list2.addnexthop(new Name("/a/b/d"), 2, 20);
		list2.addnexthop(new Name("/a/b/e"), 3, 30);
		
		System.out.println("list1:\n" + list1.getNexthops());
		System.out.println("list2:\n" + list2.getNexthops());
		
		// equal
		assertTrue(list1.equals(list2));
		
		list1.addnexthop(new Name("/a/b/f"), 4, 40);
		
		// not equal
		assertFalse(list1.equals(list2));
	}
	
	
	@Test
	void testRemoveNexthop() {
		NexthopList list1 = new NexthopList();
		list1.addnexthop(new Name("/a/b/c"), 1, 10);
		list1.addnexthop(new Name("/a/b/d"), 2, 20);
		list1.addnexthop(new Name("/a/b/e"), 3, 30);
		
		list1.removeNexthop(new Name("/a/b/e"), 3, 30);
		
		NexthopList list2 = new NexthopList();
		list2.addnexthop(new Name("/a/b/c"), 1, 10);
		list2.addnexthop(new Name("/a/b/d"), 2, 20);
		
		
		assertTrue(list1.equals(list2));
		
	}
		
		

}
