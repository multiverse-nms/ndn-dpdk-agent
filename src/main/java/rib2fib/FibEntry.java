package rib2fib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.named_data.jndn.Name.Component;

public class FibEntry {

	private Component component;
	private FibEntry parent;
	private Map<Component, FibEntry> children;
	private List<Nexthop> nexthops;
	
	
	public FibEntry() {
		this.component = null;
		this.parent = null;
		this.children = new HashMap<>();
		this.nexthops = new ArrayList<>();
	}
	
	public FibEntry(Component comp, FibEntry parent) {
		this.component = comp;
		this.parent = parent;
		this.children = new HashMap<>();
		this.nexthops = new ArrayList<>();
	}
	
	
	public FibEntry findOrInsertChild(Component comp) {
		FibEntry entry = this.children.get(comp);
		if(entry == null) {
			entry = new FibEntry(comp, this);
			this.children.put(comp, entry);
		}
		return entry;
	}

	public Nexthop findOrInsertNexthop(Nexthop nexthop) {
		for (Nexthop nh: nexthops) {
			if (nh.equals(nexthop)) {
				return nh;
			}
			this.nexthops.add(nexthop);
		}
		return nexthop;
	}

	public Component getComponent() {
		return component;
	}


	public void setComponent(Component component) {
		this.component = component;
	}


	public FibEntry getParent() {
		return parent;
	}


	public void setParent(FibEntry parent) {
		this.parent = parent;
	}


	public Map<Component, FibEntry> getChildren() {
		return children;
	}


	public void setChildren(Map<Component, FibEntry> children) {
		this.children = children;
	}


	public List<Nexthop> getNexthops() {
		return nexthops;
	}


	public void setNexthops(List<Nexthop> nexthops) {
		this.nexthops = nexthops;
	}
	
	
	
	
}
