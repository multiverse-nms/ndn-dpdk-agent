package nms.rib2fib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.named_data.jndn.Name;
import net.named_data.jndn.Name.Component;
import nms.rib2fib.commands.FibCommand;
import nms.rib2fib.commands.FibErase;
import nms.rib2fib.commands.FibInsert;

public class FibNode {

	private Component component;
	private FibNode parent;
	private Map<Component, FibNode> children;
//	private Map<Integer, Integer> nexthops;
	private NexthopList nexthops;

	public FibNode() {
		this.component = null;
		this.parent = null;
		this.children = new HashMap<>();
		this.nexthops = new NexthopList();
	}

	public FibNode(Component comp, FibNode parent) {
		if (parent == null) {
			// throw error?
		}
		this.component = comp;
		this.parent = parent;
		this.children = new HashMap<>();
		this.nexthops = new NexthopList();
	}

	public Name getName() {
		if (parent == null) {
			return new Name();
		}
		return this.parent.getName().append(this.getComponent());
	}

	public Component getComponent() {
		if (this.parent == null) {
			// throw error?
		}
		return this.component;
	}

	public FibNode findOrInsertChild(Component comp) {
		FibNode entry = this.children.get(comp);
		if (entry == null) {
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

	public NexthopList getNexthops() {
		return this.nexthops;
	}

	public List<Nexthop> getNexthopsList() {
		return this.nexthops.getList();
	}

	public void addNexthop(int faceId, int cost) {
		this.nexthops.addnexthop(getName(), faceId, cost);
		Collections.sort(this.nexthops.getList());
	}

	public boolean hasNexthops() {
		return this.nexthops.size() > 0;
	}

	public void diff(Name name, FibNode prev, List<FibCommand> commands) {
		if (prev == null || !prev.getNexthops().equals(this.getNexthops())) {
			commands.add(new FibInsert(name, this.nexthops));
		}

		Map<Component, FibNode> prevChildren = new HashMap<>();
		if (prev != null)
			prevChildren = prev.children;

		List<Component> prevChildrenComponents = new ArrayList<>(prevChildren.keySet());

		for (Map.Entry<Component, FibNode> entry : this.children.entrySet()) {
			Component comp = entry.getKey();
			FibNode child = entry.getValue();
			child.diff(this.getName().append(comp), prevChildren.getOrDefault(comp, null), commands);
			prevChildrenComponents.remove(comp);
		}
		
		for (Component comp: prevChildrenComponents) {
			commands.add(new FibErase(name.append(comp)));
		}

	}
	

	@Override
	public boolean equals(Object o) {
		// self check
		if (this == o)
			return true;
		// null check
		if (o == null)
			return false;
		// type check and cast
		if (getClass() != o.getClass())
			return false;

		FibNode node = (FibNode) o;

		return node.getName().equals(this.getName()) && node.getNexthops().equals(this.getNexthops());
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("prefix: ").append(this.getName().toString());
		sb.append(", nexthops: [");
		nexthops.getList().forEach((nexthop) -> {
			sb.append(" { faceId: ").append(nexthop.getFaceId());
			sb.append(", cost: ").append(nexthop.getCost()).append(" }");
		});
		sb.append(" ]").append("\n");
		return sb.toString();
	}

}
