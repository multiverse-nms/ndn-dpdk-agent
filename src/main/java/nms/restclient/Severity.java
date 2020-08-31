package nms.restclient;

public enum Severity {
	
	LOW("Low"),
	MEDIUM("Medium"),
	HIGH("High");
	
	private String severity;
	
	private Severity(String lvl) {
		this.severity = lvl;
	}
	
	public String getSeverity( ) {
		return this.severity;
	}

}
