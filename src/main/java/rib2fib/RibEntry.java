package rib2fib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.named_data.jndn.Name.Component;

public class RibEntry {

	private Component component;
	private RibEntry parent;
	private Map<Component, RibEntry> children;
	private List<Route> routes;

	public RibEntry() {
		this.component = null;
		this.parent = null;
		this.children = new HashMap<>();
		this.routes = new ArrayList<>();
	}

	public RibEntry(Component comp, RibEntry parent) {
		this.component = comp;
		this.parent = parent;
		this.children = new HashMap<>();
		this.routes = new ArrayList<>();
	}

	public List<Route> getRoutes() {
		return this.routes;
	}

	public RibEntry findOrInsertChild(Component comp) {
		RibEntry entry = this.children.get(comp);
		if(entry == null) {
			entry = new RibEntry(comp, this);
			this.children.put(comp, entry);
		}
		return entry;
	}

	public Route findOrInsertRoute(Route route) {
		for (Route rt : this.routes) {
			if (rt.equals(route)) {
				return rt;
			}
			this.routes.add(route);
		}
		return route;
	}

	public void eraseRoute(Route route) {
		for (Route rt : this.routes) {
			if (rt.equals(route)) {
				this.routes.remove(rt);
			}
		}
	}

	public Component getComponent() {
		return component;
	}

	public void setComponent(Component component) {
		this.component = component;
	}

	public RibEntry getParent() {
		return parent;
	}

	public void setParent(RibEntry parent) {
		this.parent = parent;
	}

	public Map<Component, RibEntry> getChildren() {
		return children;
	}

	public void setChildren(Map<Component, RibEntry> children) {
		this.children = children;
	}

	public void setRoutes(List<Route> routes) {
		this.routes = routes;
	}
	
	
	public boolean hasChildren() {
		return this.children.isEmpty() == false;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("routes: [");
		routes.forEach(route -> {
			sb.append(route);
		});
		sb.append("]");
		return sb.toString();
	}

}
