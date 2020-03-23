package rib2fib;

import net.named_data.jndn.Name;
import net.named_data.jndn.Name.Component;

public class Rib {

	private RibEntry root;

	public Rib() {
		this.root = new RibEntry();
	}

	public Rib(RibEntry root) {
		this.root = root;
	}

	public RibEntry findOrInsertEntry(Name name) {
		RibEntry entry = this.root;
		for (int i = 0; i < name.size(); i++) {
			entry = entry.findOrInsertChild(name.get(i));
		}
		return entry;
	}

	public Route findOrInsertRoute(Route route) {
		return this.findOrInsertEntry(route.getPrefix()).findOrInsertRoute(route);
	}

	public void eraseRoute(Route route) {
		RibEntry entry = this.findOrInsertEntry(route.getPrefix());
		entry.eraseRoute(route);
		while (entry != null && entry.getRoutes().size() == 0 && entry.getChildren().size() == 0) {
			if (entry.getParent() == null) { // <- if this is root
				return;
			}
			entry.getParent().getChildren().remove(entry.getComponent());
			entry = entry.getParent();
		}

	}
	
	
	public Fib toFib() {
		Fib fib = new Fib();
		
		
		
		return fib;	
	}

	public RibEntry getRoot() {
		return this.root;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		// TODO
		return sb.toString();
	}

	public void printTree(RibEntry entry, String appender) {
		Component comp = entry.getComponent();
		if(comp == null) {
			comp = new Component("/");
		}
		System.out.println(appender + comp.toEscapedString());
		entry.getChildren().forEach((c, child) -> printTree(child, appender + appender));
	}

	
	public static void main(String[] args) {
		Rib rib = new Rib();
//		System.out.println(rib.getRoot().hashCode());
		Route route1 = new Route(new Name("/a/b/c"), 1000, 1);
		Route inserted1 = rib.findOrInsertRoute(route1);
		System.out.println("Route 1: " + inserted1);
		Route route2 = new Route(new Name("/b/254"), 1002, 0);
		rib.findOrInsertRoute(route2);
		Route route3 = new Route(new Name("/a/b/c/d"), 1003, 0);
		rib.findOrInsertRoute(route3);
		Route route4 = new Route(new Name("/b/c/d"), 1002, 0);
		rib.findOrInsertRoute(route4);
		rib.printTree(rib.getRoot(),  " ");
		System.out.println(rib.findOrInsertEntry(new Name("/a/b/c")));
			
	}
}
