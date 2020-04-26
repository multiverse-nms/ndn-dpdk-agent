package nms.forwarder.model.fib;


public class Fib {
	String name;
	String nexthops;
	String cost;

	public Fib(String name, String nexthops, String cost) {
		this.name = name;
		this.nexthops = nexthops;
		this.cost = cost;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNexthops() {
		return nexthops;
	}

	public void setNexthops(String nexthops) {
		this.nexthops = nexthops;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}
}
