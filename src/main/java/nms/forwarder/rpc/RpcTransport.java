package nms.forwarder.rpc;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import com.github.arteam.simplejsonrpc.client.Transport;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class RpcTransport implements Transport {

	// private static int PORT = 6345;
	// private static String HOST = "10.0.31.99";
	// private static String LOCALHOST = "127.0.0.1";
	
	private String host = "";
	private int port = 0;

	Logger logger = LoggerFactory.getLogger(RpcTransport.class.getName());
	
	public RpcTransport(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public String pass(String request) throws IOException {
		logger.debug("request=" + request);
		String response = "";
		try {
			// create client socket, connect to server
			Socket clientSocket = new Socket(host, port);
			// create input stream attached to socket
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			// create output stream attached to socket
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			// send request to server
			outToServer.writeBytes(request);
			// read line from server
			String str = null;
			if ((str = inFromServer.readLine()) != null) {
				response += str;
			}
			clientSocket.close();
		} catch (Exception ex) {
			logger.error("rpc error: " + ex.getMessage());
		}
		logger.debug("response=" + response);
		return response;
	}
}