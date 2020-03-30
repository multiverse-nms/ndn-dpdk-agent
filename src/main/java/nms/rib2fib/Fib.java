package nms.rib2fib;

import net.named_data.jndn.Name;
import net.named_data.jndn.Name.Component;

public class Fib {
	
	private FibNode root;
	
	public Fib() {
		this.root = new FibNode();
	}
	
	
	public FibNode getRoot() {
		return this.root;
	}
	
	public void setRoot(FibNode root) {
		this.root = root;
	}
	
	
	public FibNode findOrInsertEntry(Name name) {
		FibNode entry = this.root;
		for (int i =0; i<name.size(); i++ ) {
			entry = entry.findOrInsertChild(name.get(i));
		}
		return entry;
	}
	
	
//	public Nexthop findOrInsertNexthop(Nexthop nexthop) {
//		return this.findOrInsertEntry(nexthop.getPrefix()).findOrInsertNexthop(nexthop);
//	}
	
	
	public void eraseFibNode() {
		
	}
	
	public void print(FibNode entry, String appender) {
		Component comp = entry.getComponent();
		if (comp == null) {
			comp = new Component("");
		}
		System.out.println(appender + comp.toEscapedString());
		entry.getChildren().forEach((c, child) -> print(child, appender + appender));
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		traverse(this.root, new FibNodeHandler() {
			@Override
			public void handle(FibNode fibNode) {
				sb.append(fibNode);
			}
		});
		return sb.toString();	
	}


	private void traverse(FibNode node, FibNodeHandler handler) {
		handler.handle(node);
		node.getChildren().forEach((comp, child) -> {
			traverse(child, handler);
		});
	}
}
