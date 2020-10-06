package nms;

public enum VerticleAdress {
	
	main_verticle("main.evetbus"),
	forwarder_verticle("fwd.eventbus"),
	rib_verticle("rib.eventbus"),
	websockets_verticle("websockets.eventbus"),
	webclient_verticle("webclient.eventbus");
	;
	
	private String adress;

	VerticleAdress(String adress) {
		this.adress = adress;
	}

	public String getAdress() {
		// TODO Auto-generated method stub
		return adress;
	}
}
