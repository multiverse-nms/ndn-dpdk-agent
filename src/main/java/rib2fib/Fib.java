package rib2fib;

import net.named_data.jndn.Name;

public class Fib {
	
	private FibEntry root;
	
	public Fib() {
		this.root = new FibEntry();
	}
	
	public FibEntry getRoot() {
		return this.root;
	}
	
	
	public FibEntry findOrInsertEntry(Name name) {
		FibEntry entry = this.root;
		for (int i =0; i<name.size(); i++ ) {
			entry = entry.findOrInsertChild(name.get(i));
		}
		return entry;
	}
	
	
	public Nexthop findOrInsertNexthop(Nexthop nexthop) {
		return this.findOrInsertEntry(nexthop.getPrefix()).findOrInsertNexthop(nexthop);
	}
	
	
	public void eraseFibEntry() {
		
	}

}
