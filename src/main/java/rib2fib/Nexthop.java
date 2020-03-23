package rib2fib;

import net.named_data.jndn.Name;

public class Nexthop {
	
	private Name prefix;
	private int faceId;
	private int cost;
	
	
	public Nexthop(Name prefix, int faceId, int cost) {
		this.prefix = prefix;
		this.faceId = faceId;
		this.cost = cost;
	}


	public Name getPrefix() {
		return prefix;
	}


	public void setPrefix(Name prefix) {
		this.prefix = prefix;
	}


	public int getFaceId() {
		return faceId;
	}


	public void setFaceId(int faceId) {
		this.faceId = faceId;
	}


	public int getCost() {
		return cost;
	}


	public void setCost(int cost) {
		this.cost = cost;
	}
	
	

}
