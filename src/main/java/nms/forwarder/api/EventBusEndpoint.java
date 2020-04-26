package nms.forwarder.api;

public enum EventBusEndpoint {
	
	GET_VERSION("get_version"),
	GET_FACE("get_face"),
	GET_FACES("get_faces"),
	ADD_FACE("add_face"),
	EDIT_FACE("edit_face"),
	GET_FIB("get_fib"),
	GET_FIB_ENTRY("get_fib"),
	INSERT_FIB_ENTRY("insert_fib"),
	EDIT_FIB_ENTRY("edit_fib"),
	REGISTER_PREFIX("reg_prefix"),
	UNREGISTER_PREFIX("unreg_prefix");
	;
	
	private String name;
	
	EventBusEndpoint(String name) {
		this.name= name;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

}
