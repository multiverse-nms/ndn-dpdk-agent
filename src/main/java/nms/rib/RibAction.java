package nms.rib2fib;

public enum RibAction {

	ADD_ROUTE("add_route"),
	REMOVE_ROUTE("rem_route");
	;
	
	private String name;

	RibAction(String name) {
		this.name = name;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}
}
