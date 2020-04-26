package nms.rib2fib.commands;

import net.named_data.jndn.Name;
import nms.rib2fib.NexthopList;

public class FibInsert implements FibCommand {

	private Name name;
	private NexthopList nexthops;

	public FibInsert(Name name, NexthopList nexthops) {
		this.name = name;
		this.nexthops = nexthops;
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub

	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT: ");
		sb.append("name: ").append(name);
		sb.append(", nexthops:").append(nexthops);
		return sb.toString();
	}

}
