package net.dlogic.kryonet.client;

import java.io.IOException;

import net.dlogic.kryonet.common.entity.Room;
import net.dlogic.kryonet.common.entity.User;
import net.dlogic.kryonet.common.request.GetRoomsRequest;
import net.dlogic.kryonet.common.request.JoinRoomRequest;
import net.dlogic.kryonet.common.request.LeaveRoomRequest;
import net.dlogic.kryonet.common.request.LoginRequest;
import net.dlogic.kryonet.common.request.LogoutRequest;
import net.dlogic.kryonet.common.request.PrivateMessageRequest;
import net.dlogic.kryonet.common.request.PublicMessageRequest;
import net.dlogic.kryonet.common.utility.KryonetUtility;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.minlog.Log;

public class KryonetClient {
	public Client endpoint;
	public final KryonetClientListener listener = new KryonetClientListener();
	public KryonetClient(int writeBufferSize, int objectBufferSize) {
		Log.info("KryonetClient(" + writeBufferSize + ", " + objectBufferSize + ")");
		endpoint = new Client(writeBufferSize, objectBufferSize);
		KryonetUtility.registerClasses(endpoint);
		endpoint.addListener(listener);
	}
	public void start(final int timeout, String host, int tcpPort, int udpPort) throws IOException, KryonetClientException {
		Log.info("KryonetClient.start(" + timeout + ", " + host + ", " + tcpPort + ", " + udpPort + ")");
		if (!endpoint.isConnected()) {
			new Thread(endpoint).start();
			endpoint.connect(timeout, host, tcpPort, udpPort);
		} else {
			throw new KryonetClientException("client already started");
		}
	}
	public void sendGetRoomsRequest(String search) {
		GetRoomsRequest request = new GetRoomsRequest();
		request.search = search;
		endpoint.sendTCP(request);
	}
	public void sendJoinRoomRequest(Room roomToJoin, String password) {
		JoinRoomRequest request = new JoinRoomRequest();
		request.targetRoomId = roomToJoin.id;
		request.password = password;
		endpoint.sendTCP(request);
	}
	public void sendLeaveRoomRequest(Room roomToLeave) {
		LeaveRoomRequest request = new LeaveRoomRequest();
		request.targetRoomId = roomToLeave.id;
		endpoint.sendTCP(request);
	}
	public void sendLoginRequest(String username, String password) {
		Log.info("KryonetClient.sendLoginRequest(" + username + ", " + password + ")");
		LoginRequest request = new LoginRequest();
		request.username = username;
		request.password = password;
		endpoint.sendTCP(request);
	}
	public void sendLogoutRequest() {
		LogoutRequest request = new LogoutRequest();
		endpoint.sendTCP(request);
	}
	public void sendPrivateMessageRequest(User targetUser, String message) {
		PrivateMessageRequest request = new PrivateMessageRequest();
		request.targetUserId = targetUser.id;
		request.message = message;
		endpoint.sendTCP(request);
	}
	public void sendPublicMessageRequest(Room targetRoom, String message) {
		PublicMessageRequest request = new PublicMessageRequest();
		request.targetRoomId = targetRoom.id;
		request.message = message;
		endpoint.sendTCP(request);
	}
}
