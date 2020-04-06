package nms.rib2fib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.named_data.jndn.Name;
import net.named_data.jndn.Name.Component;

public class RibNode {

	private Component component;
	private RibNode parent;
	private Map<Component, RibNode> children;
	private Map<RouteKey, Route> routes;
	private int nCaptures = 0;

	public RibNode() {
		this.parent = null;
		this.children = new HashMap<>();
		this.routes = new HashMap<>();
	}

	public RibNode(Component comp, RibNode parent) {
		if (parent == null) {
			throw new IllegalArgumentException("parent is null");
		}
		this.component = comp;
		this.parent = parent;
		this.children = new HashMap<>();
		this.routes = new HashMap<>();
	}

	public Component getComponent() {
		if (this.parent == null) {
			throw new NullPointerException("parent is null");
		}
		return this.component;
	}

	public Name getName() {
		if (parent == null) {
			return new Name();
		}
		// this has access to the parent
		// the parent has access to its children
		// one of the children is this
		// then this can retrieve its own component
		return this.parent.getName().append(this.getComponent());
	}

	public RibNode findOrInsertChild(Component comp) {
		RibNode child = this.children.get(comp);
		if (child == null) {
			child = new RibNode(comp, this);
			this.children.put(comp, child);
		}
		return child;
	}

	public Route findOrInsertRoute(Route route) {
		RouteKey routeKey = new RouteKey(route.getFaceId(), route.getOrigin());
		Route found = this.routes.get(routeKey);
		if (found == null) {
			found = route;
			if (route.hasCaptureFlag()) {
				this.nCaptures++;
			}
			this.routes.put(routeKey, found);
		}
		return found;
	}
	
	
	public List<RibNode> getAncestors() {
		List<RibNode> ancestors = new ArrayList<>();
		ancestors.add(this);
		RibNode parent = this.parent;
		while(parent != null) {
			ancestors.add(parent);
			parent = parent.getParent();
		}
		return ancestors;
	}

	public void eraseRoute(Route route) {
		RouteKey routeKey = new RouteKey(route.getFaceId(), route.getOrigin());
		Route found = this.routes.get(routeKey);
		if (found != null) {
			if (found.hasCaptureFlag()) {
				this.nCaptures--;
			}
			this.routes.remove(routeKey);
		}
	}

	public void setComponent(Component component) {
		this.component = component;
	}

	public RibNode getParent() {
		return this.parent;
	}

	public void setParent(RibNode parent) {
		this.parent = parent;
	}

	public Map<Component, RibNode> getChildren() {
		return children;
	}

	public void setChildren(Map<Component, RibNode> children) {
		this.children = children;
	}

	public Map<RouteKey, Route> getRoutes() {
		return routes;
	}

	public void setRoutes(Map<RouteKey, Route> mRoutes) {
		this.routes = mRoutes;
	}

	public boolean hasChildren() {
		return this.children.isEmpty() == false;
	}

	public boolean hasCapture() {
		return this.nCaptures > 0;
	}

	public boolean hasRoutes() {
		return this.routes.size() > 0;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		List<Route> routesList = new ArrayList<Route>(this.routes.values());
		sb.append("prefix: ").append(this.getName().toString());
		sb.append(", routes: [");
		routesList.forEach(route -> {
			sb.append(route);
		});
		sb.append(" ]");
		return sb.toString();
	}
}
