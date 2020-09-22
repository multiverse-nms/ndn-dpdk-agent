package nms.restclient.service.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class CredentialsProvider {
	private String username;
	private String password;
	
	public CredentialsProvider(String filename) {
		loadCredentials(filename);		
	}

	public void loadCredentials(String filename) {
		Properties prop = new Properties();
		InputStream propertiesInputStream = getClass().getClassLoader().getResourceAsStream(filename);
		try {
			prop.load(propertiesInputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.setUsername(prop.getProperty("username"));
		this.setPassword(prop.getProperty("password"));
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}

}
