package net.dlogic.kryonet.client;

import net.dlogic.kryonet.client.event.callback.IConnectionEventCallback;
import net.dlogic.kryonet.client.event.callback.ILoginOrLogoutEventCallback;
import net.dlogic.kryonet.client.event.callback.IPersonMessageEventCallback;
import net.dlogic.kryonet.client.event.callback.IRoomEventCallback;
import net.dlogic.kryonet.common.response.JoinRoomFailureResponse;
import net.dlogic.kryonet.common.response.JoinRoomSuccessResponse;
import net.dlogic.kryonet.common.response.LoginFailureResponse;
import net.dlogic.kryonet.common.response.LoginSuccessResponse;
import net.dlogic.kryonet.common.response.LogoutResponse;
import net.dlogic.kryonet.common.response.PrivateMessageResponse;
import net.dlogic.kryonet.common.response.PublicMessageResponse;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

public class KryonetClientListener extends Listener {
	private IConnectionEventCallback connectionEventCallback;
	private IRoomEventCallback roomEventCallback;
	private ILoginOrLogoutEventCallback loginOrLogoutEventCallback;
	private IPersonMessageEventCallback personMessageEventCallback;
	public KryonetClientListener() {
		Log.info("KryonetClientListener()");
	}
	public void setConnectionEventCallback(IConnectionEventCallback callback) {
		connectionEventCallback = callback;
	}
	public void setRoomEventCallback(IRoomEventCallback callback) {
		roomEventCallback = callback;
	}
	public void setLoginOrLogoutEventCallback(ILoginOrLogoutEventCallback callback) {
		loginOrLogoutEventCallback = callback;
	}
	public void setPersonMessageEventCallback(IPersonMessageEventCallback callback) {
		personMessageEventCallback = callback;
	}
	public void connected(Connection connection) {
		connectionEventCallback.onConnected();
	}
	public void disconnected(Connection connection) {
		connectionEventCallback.onDisconnected();
	}
	public void received(Connection connection, Object object) {
		Log.info("KryonetClientListener.received()");
		if (object instanceof JoinRoomFailureResponse) {
			JoinRoomFailureResponse response = (JoinRoomFailureResponse)object;
			roomEventCallback.onJoinRoomFailure(response.errorMessage);
		} else if (object instanceof JoinRoomSuccessResponse) {
			JoinRoomSuccessResponse response = (JoinRoomSuccessResponse)object;
			roomEventCallback.onJoinRoomSuccess(response.joinedUser, response.joinedRoom);
		} else if (object instanceof LoginFailureResponse) {
			LoginFailureResponse response = (LoginFailureResponse)object;
			loginOrLogoutEventCallback.onLoginFailure(response.errorMessage);
		} else if (object instanceof LoginSuccessResponse) {
			LoginSuccessResponse response = (LoginSuccessResponse)object;
			loginOrLogoutEventCallback.onLoginSuccess(response.myself);
		} else if (object instanceof LogoutResponse) {
			loginOrLogoutEventCallback.onLogout();
		} else if (object instanceof PrivateMessageResponse) {
			PrivateMessageResponse response = (PrivateMessageResponse)object;
			personMessageEventCallback.onPrivateMessage(response.sender, response.message);
		} else if (object instanceof PublicMessageResponse) {
			PublicMessageResponse response = (PublicMessageResponse)object;
			personMessageEventCallback.onPublicMessage(response.sender, response.message);
		}
	}
	public void idle(Connection connection) {
		super.idle(connection);
	}
}
