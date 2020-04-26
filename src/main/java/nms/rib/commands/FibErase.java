package nms.rib2fib.commands;

import net.named_data.jndn.Name;

public class FibErase implements FibCommand {

	private Name name;

	public FibErase(Name name) {
		this.name = name;
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public String toString() {
		return "ERASE " + name.toUri();
	}


}
