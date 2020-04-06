package nms.rib2fib;

import java.util.HashMap;
import java.util.Map;

import net.named_data.jndn.Name;
import net.named_data.jndn.Name.Component;

public class FibNode {

	private Component component;
	private FibNode parent;
	private Map<Component, FibNode> children;
	private Map<Integer, Integer> nexthops;
	
	
	public FibNode() {
		this.component = null;
		this.parent = null;
		this.children = new HashMap<>();
		this.nexthops = new HashMap<>();
	}
	
	public FibNode(Component comp, FibNode parent) {
		if (parent == null) {
			// throw error?
		}
		this.component = comp;
		this.parent = parent;
		this.children = new HashMap<>();
		this.nexthops = new HashMap<>();
	}

	public Name getName() {
		if (parent == null) {
			return new Name();
		}
		return this.parent.getName().append(this.getComponent());
	}
	
	public Component getComponent() {
		if(this.parent == null ) {
			// throw error?
		}
		return this.component;
	}
	
	
	
	public FibNode findOrInsertChild(Component comp) {
		FibNode entry = this.children.get(comp);
		if(entry == null) {
			entry = new FibNode(comp, this);
			this.children.put(comp, entry);
		}
		return entry;
	}

	public FibNode addChild(Component comp) {
		FibNode node = new FibNode(comp, this);
		this.children.put(comp, node);
		return node;
	}

	public void setComponent(Component component) {
		this.component = component;
	}


	public FibNode getParent() {
		return parent;
	}
	
	public FibNode getRoot() {
		if (parent == null) {
			return this;
		}
		return this.parent.getRoot();
	}


	public void setParent(FibNode parent) {
		this.parent = parent;
	}


	public Map<Component, FibNode> getChildren() {
		return children;
	}


	public void setChildren(Map<Component, FibNode> children) {
		this.children = children;
	}


	public Map<Integer, Integer> getNexthops() {
		return nexthops;
	}


	public void setNexthops(Map<Integer, Integer> nexthops) {
		this.nexthops = nexthops;
	}
	
	
	public void addNexthop(int face, int cost) {
		this.nexthops.put(face, cost);
	}
	
	public boolean hasNexthops() {
		return this.nexthops.size() > 0;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("prefix: ").append(this.getName().toString());
		sb.append(", nexthops: [");
		nexthops.forEach((faceId, cost) -> {
			sb.append(" { faceId: ").append(faceId);
			sb.append(", cost: ").append(cost).append(" }");
		});
		sb.append(" ]").append("\n");
		return sb.toString();
	}
	
	
	
}
