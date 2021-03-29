package nms.rib;

import java.util.ArrayList;
import java.util.List;

import net.named_data.jndn.Name;
import nms.rib.commands.FibCommand;

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
		for (int i = 0; i < name.size(); i++) {
			entry = entry.findOrInsertChild(name.get(i));
		}
		return entry;
	}

	public void eraseFibNode() {

	}

	
	
	public List<FibCommand> compare(Fib oldFib) {
		List<FibCommand> commands = new ArrayList<>();
		this.root.diff(new Name(""), oldFib.getRoot() , commands);
		return commands;	
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		traverse(this.root, new FibNodeHandler() {
			@Override
			public void handle(FibNode fibNode) {
				if (fibNode.hasNexthops()) {
					sb.append(fibNode);
				}
			}
		});
		return sb.toString();
	}

	public void traverse(FibNode node, FibNodeHandler handler) {
		handler.handle(node);
		node.getChildren().forEach((comp, child) -> {
			traverse(child, handler);
		});
	}
	
	
	
}
