package nms.restclient;

public enum Status {
	UP("UP"),
	DOWN("DOWN");
	
	private String status;
	
	private Status(String status) {
		this.status = status;
	}
	
	public String getStatus( ) {
		return this.status;
	}


}
