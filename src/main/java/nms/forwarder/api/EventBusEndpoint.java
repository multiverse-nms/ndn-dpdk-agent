package nms.forwarder.api;

public enum EventBusEndpoint {
	
	GET_VERSION("Version.Version"),
	GET_FACE("Face.Get"),
	LIST_PORTS("EthFace.ListPorts"),
	LIST_FACES("Face.List"),
	CREATE_FACE("Face.Create"),
	DESTROY_FACE("Face.Destroy"),
	ANNOUNCE_PREFIX("Prefix.Annouce"),
	ADD_ROUTE("Route.Add"),
	REMOVE_ROUTE("Route.Remove"),
	LIST_FIB("Fib.List"),
	On_Client_Disconnect("Client.Disconnect"),
	WITHDRAW_PREFIX("Prefix.Withdraw"),
	// can only be used by RIB verticle
	INSERT_FIB("Fib.Insert"),
	ERASE_FIB("Fib.Erase");
	;
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
