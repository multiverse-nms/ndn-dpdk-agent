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

	private static int PORT = 6345;
	private static String HOST = "10.0.31.18";

	Logger logger = LoggerFactory.getLogger(RpcTransport.class.getName());

	public String pass(String request) throws IOException {
		logger.debug("request=" + request);
		String response = "";
		try {
			// create client socket, connect to server
			Socket clientSocket = new Socket(HOST, PORT);
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
			return response;
		} catch (Exception ex) {
		}
		logger.debug("response=" + response);
		return response;
	}
}