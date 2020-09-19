package nms.restclient;

public class AuthToken {
	
	String token;
	
	public AuthToken(String token) {
		this.token = token;
	}
	
	public String getToken() {
		return this.token;
	}

	public boolean tokenExpired(String message) {
		// how to check if token has expired? does controller send a corresponding response
        return "token expired".equals(message);
    }
}
