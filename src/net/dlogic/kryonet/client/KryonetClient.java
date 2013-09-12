package net.dlogic.kryonet.client;

import java.io.IOException;

import net.dlogic.kryonet.common.entity.Room;
import net.dlogic.kryonet.common.request.JoinRoomRequest;
import net.dlogic.kryonet.common.request.LoginRequest;
import net.dlogic.kryonet.common.request.LogoutRequest;
import net.dlogic.kryonet.common.utility.KryonetUtility;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Listener;

public class KryonetClient {
	private Client client;
	private KryonetClientListener listener;
	public KryonetClient(int writeBufferSize, int objectBufferSize) {
		client = new Client(writeBufferSize, objectBufferSize);
		KryonetUtility.registerClasses(client);
		client.addListener(listener = new KryonetClientListener());
	}
	public void start(int timeout, String host, int tcpPort, int udpPort) throws IOException, KryonetClientException {
		if (!client.isConnected()) {
			client.start();
			client.connect(timeout, host, tcpPort, udpPort);
		} else {
			throw new KryonetClientException("client already started");
		}
	}
	public KryonetClientListener getKryonetClientListener() {
		return listener;
	}
	public void addListener(Listener listener) {
		client.addListener(listener);
	}
	public void registerClass(Class type) {
		client.getKryo().register(type);
	}
	public void sendJoinRoomRequest(Room roomToJoin, String password) {
		JoinRoomRequest request = new JoinRoomRequest();
		request.roomToJoin = roomToJoin;
		request.password = password;
		client.sendTCP(request);
	}
	public void sendLoginRequest(String username, String password) {
		LoginRequest request = new LoginRequest();
		request.username = username;
		request.password = password;
		client.sendTCP(request);
	}
	public void sendLogoutRequest() {
		LogoutRequest request = new LogoutRequest();
		client.sendTCP(request);
	}
}
