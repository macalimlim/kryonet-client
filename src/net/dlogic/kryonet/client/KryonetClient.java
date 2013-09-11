package net.dlogic.kryonet.client;

import java.io.IOException;

import net.dlogic.kryonet.common.request.LoginRequest;
import net.dlogic.kryonet.common.utility.KryonetUtility;

import com.esotericsoftware.kryonet.Client;

public class KryonetClient {
	private static Client client;
	public KryonetClient(int writeBufferSize, int objectBufferSize, int timeout, String host, int tcpPort, int udpPort) throws IOException {
		client = new Client(writeBufferSize, objectBufferSize);
		KryonetUtility.registerClasses(client);
		client.start();
		client.connect(timeout, host, tcpPort, udpPort);
		client.addListener(new KryonetClientListener());
	}
	public void sendLoginRequest(String username, String password) {
		LoginRequest request = new LoginRequest();
		request.username = username;
		request.password = password;
		client.sendTCP(request);
	}
}
