package nms.restclient;

public enum NotificationType {
	STATUS("status"),
	EVENT("event"),
	FAULT("fault");
	
	private String type;
	
	private NotificationType(String type) {
		this.type = type;
	}
	
	public String toString( ) {
		return this.type;
	}
}
