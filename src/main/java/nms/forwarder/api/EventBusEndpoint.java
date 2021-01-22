package nms.forwarder.api;

public enum EventBusEndpoint {
	
	GET_VERSION("Version.Version"),
	GET_FACE("Face.Get"),
	LIST_PORTS("EthFace.ListPorts"),
	LIST_FACES("Face.List"),
	CREATE_FACE("Face.Create"),
	DESTROY_FACE("Face.Destroy"),
	ADD_ROUTE("Route.Add"),
	REMOVE_ROUTE("Route.Remove"),
	LIST_FIB("Fib.List"),
	APP_FACE_CREATE("Face.Create"),
	
	INSERT_FIB("Fib.Insert"),
	ERASE_FIB("Fib.Erase");
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
