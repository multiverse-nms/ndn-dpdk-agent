package nms.rib2fib;

import java.util.ArrayList;
import java.util.List;

import net.named_data.jndn.Name;
import net.named_data.jndn.Name.Component;
import nms.rib2fib.commands.FibCommand;

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

	public List<FibCommand> compare(Fib fib) {
		List<FibCommand> commands = new ArrayList<>();
		this.root.diff(new Name(""), fib.getRoot() , commands);
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
	
	
	public static void main(String[] args) {
		FibNode root = new FibNode();
		Fib fib = new Fib();
		fib.setRoot(root);
		FibNode nodeA = new FibNode(new Component("a"), root);
		FibNode nodeAB = new FibNode(new Component("b"), nodeA);
		FibNode nodeAC = new FibNode(new Component("c"), nodeA);
		FibNode nodeD = new FibNode(new Component("d"), root);
	
		System.out.println(fib);
	}
}
