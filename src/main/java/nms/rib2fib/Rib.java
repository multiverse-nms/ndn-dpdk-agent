package nms.rib2fib;

import java.util.ArrayList;
import java.util.Arrays;
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
		RibNode node = this.root;
		for (int i = 0; i < name.size(); i++) {
			node = node.findOrInsertChild(name.get(i));
		}
		return node;
	}

	private Route findOrInsertRoute(Route route) {
		RibNode node = this.findOrInsertEntry(route.getPrefix());
		Route rt = node.findOrInsertRoute(route);
		return rt;
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
				FibNode fibNode = fib.findOrInsertEntry(ribNode.getName());
				Map<Integer, Integer> nexthops = collectNexthops(ribNode);
				nexthops.forEach((id, cost) -> {
					fibNode.addNexthop(id, cost);
				});
			}
		});
		return fib;
	}

	private int getLowestCost(int cost1, int cost2) {
		return Math.min(cost1, cost2);
	}
	

	private Map<Integer, Integer> collectNexthops(RibNode ribNode) {
		Map<Integer, Integer> routes = new HashMap<>();
		List<RibNode> ancestors = ribNode.getAncestors();
		boolean isSelf = true;
		for (RibNode node: ancestors) {
			Map<Integer, Integer> routesAtRibNode = new HashMap<>();
			for (Route route: node.getRoutes().values()) {
				if(isSelf || route.hasChildInheritFlag()) {
					int faceId = route.getFaceId();
					int cost = route.getCost();
					int minCost = getLowestCost(routesAtRibNode.getOrDefault(faceId, Integer.MAX_VALUE), cost);
					routesAtRibNode.put(faceId, minCost);
				}
			}
			for (Map.Entry<Integer, Integer> entry: routesAtRibNode.entrySet()) {
				routes.putIfAbsent(entry.getKey(), entry.getValue());
			}
			if (ribNode.hasCapture()) 
				 break;
			isSelf = false;
		}
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
				if (ribNode.hasRoutes()) {
					sb.append(ribNode).append("\n");
				}
			}
		});
		return sb.toString();
	}

	public void insertRoutes(List<Route> routes) {
		routes.forEach(route -> {
			this.findOrInsertRoute(route);
		});
	}

	public static void main(String[] args) {
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

		System.out.println("RIB:\n" + rib);
		System.out.println("FIB:\n" + rib.toFib());
	}
}
