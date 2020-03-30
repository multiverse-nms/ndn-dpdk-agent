package nms.rib2fib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.named_data.jndn.Name;

public class Rib {

	private RibNode root;

	public Rib() {
		this.root = new RibNode();
	}

	public Rib(RibNode root) {
		this.root = root;
	}

	public RibNode findOrInsertEntry(Name name) {
		RibNode entry = this.root;
		for (int i = 0; i < name.size(); i++) {
			entry = entry.findOrInsertChild(name.get(i));
		}
		return entry;
	}

	private Route findOrInsertRoute(Route route) {
		return this.findOrInsertEntry(route.getPrefix()).findOrInsertRoute(route);
	}

	public Rib addRoute(Route route) {
		Rib newRib = new Rib(this.root);
		this.findOrInsertRoute(route);
		return newRib;
	}

	private void eraseRoute(Route route) {
		RibNode entry = this.findOrInsertEntry(route.getPrefix());
		entry.eraseRoute(route);
		while (entry != null && entry.getRoutes().size() == 0 && entry.getChildren().size() == 0) {
			if (entry.getParent() == null) { // <- if this is root
				return;
			}
			entry.getParent().getChildren().remove(entry.getComponent());
			entry = entry.getParent();
		}

	}

	public Rib removeRoute(Route route) {
		Rib newRib = new Rib(this.root);
		this.eraseRoute(route);
		return newRib;
	}

	public Fib toFib() {
		Fib fib = new Fib();
		this.traverse(this.root, new RibNodeHandler() {
			@Override
			public void handle(RibNode ribNode) {
//				FibNode fibNode = fib.findOrInsertEntry(ribNode.getName());
				fib.findOrInsertEntry(ribNode.getName());
				Map<Integer, Integer> routes = collectRoutes(ribNode);
				routes.forEach((id, cost) -> {
					System.out.println("id=" + id + ", cost=" + cost);
				});
			}

		});
		return fib;
	}

	private Map<Integer, Integer> collectRoutes(RibNode ribNode) {
		RibNode parentNode = ribNode.getParent();
		Map<Integer, Integer> routes = new HashMap<>();
		System.out.println("ribNode parent " + parentNode);
		if (parentNode == null) {
			for (Map.Entry<RouteKey, Route> mapEntry : ribNode.getRoutes().entrySet()) {
				Route route = mapEntry.getValue();
				routes.put(route.getFaceId(), route.getCost());
			}
		} else {
			
			Map<Integer, Integer> routesAtRibEntry = new HashMap<>();
			for (Map.Entry<RouteKey, Route> mapEntry : parentNode.getRoutes().entrySet()) {
				if (mapEntry.getValue().hasChildInheritFlag()) {
					routesAtRibEntry.put(mapEntry.getKey().getFaceId(), mapEntry.getValue().getCost());
				}
			}
			for (Map.Entry<Integer, Integer> mapEntry : routesAtRibEntry.entrySet()) {
				routes.put(mapEntry.getKey(), mapEntry.getValue());
			}
		}
		
//		if (ribNode.hasCapture()) {
//			//
//		} 

		return routes;
	}

	public void traverse(RibNode node, RibNodeHandler handler) {
		handler.handle(node);
		node.getChildren().forEach((comp, child) -> {
			traverse(child, handler);
		});
	}

	public RibNode getRoot() {
		return this.root;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		traverse(this.root, new RibNodeHandler() {
			@Override
			public void handle(RibNode ribNode) {
				sb.append(ribNode).append("\n");
			}

		});
		return sb.toString();
	}

	public static void main(String[] args) {
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
			public void handle(RibNode entry) {
				names.add(entry.getName());
			}

		});

		System.out.println("RIB:\n" + rib);
		System.out.println("FIB:\n" + rib.toFib());

	}
}
