package nms.forwarder.api;

public enum RestEndpoint {
	
	GET_VERSION("/api/version"),
	GET_FACE("/api/faces/:id"),
	GET_FACES("/api/faces"),
	ADD_FACE("/api/faces"),
	EDIT_FACE("edit_face"),
	GET_FIB("/api/fib"),
	ADD_PREFIX("/api/fib/:name"),
	DELETE_PREFIX("/api/fib/:name");
	
	
	private String endpoint;
	
	
	RestEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
	
	public String getEndpoint() {
		return endpoint;
	}

}
