package net.dlogic.kryonet.client;

import net.dlogic.kryonet.client.event.callback.IConnectionEventCallback;
import net.dlogic.kryonet.client.event.callback.IErrorEventCallback;
import net.dlogic.kryonet.client.event.callback.IPersonMessageEventCallback;
import net.dlogic.kryonet.client.event.callback.IRoomEventCallback;
import net.dlogic.kryonet.client.event.callback.IUserEventCallback;
import net.dlogic.kryonet.common.entity.Myself;
import net.dlogic.kryonet.common.manager.MyselfInstance;
import net.dlogic.kryonet.common.manager.RoomManager;
import net.dlogic.kryonet.common.manager.RoomManagerException;
import net.dlogic.kryonet.common.manager.UserManager;
import net.dlogic.kryonet.common.response.ErrorResponse;
import net.dlogic.kryonet.common.response.GetRoomsResponse;
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
	private Myself myself = MyselfInstance.myself;
	private IErrorEventCallback errorEventCallback;
	private IConnectionEventCallback connectionEventCallback;
	private IRoomEventCallback roomEventCallback;
	private IUserEventCallback loginOrLogoutEventCallback;
	private IPersonMessageEventCallback personMessageEventCallback;
	public KryonetClientListener() {
		Log.info("KryonetClientListener()");
	}
	public void setErrorEventCallback(IErrorEventCallback callback) {
		errorEventCallback = callback;
	}
	public void setConnectionEventCallback(IConnectionEventCallback callback) {
		connectionEventCallback = callback;
	}
	public void setRoomEventCallback(IRoomEventCallback callback) {
		roomEventCallback = callback;
	}
	public void setLoginOrLogoutEventCallback(IUserEventCallback callback) {
		loginOrLogoutEventCallback = callback;
	}
	public void setPersonMessageEventCallback(IPersonMessageEventCallback callback) {
		personMessageEventCallback = callback;
	}
	public void connected(Connection connection) {
		myself.id = connection.getID();
		myself.isAdmin = false;
		myself.isItMe = true;
		myself.isPlayer = false;
		myself.roomManager.map.clear();
		connectionEventCallback.onConnected();
	}
	public void disconnected(Connection connection) {
		myself.username = null;
		myself.isAdmin = false;
		myself.isItMe = false;
		myself.isPlayer = false;
		myself.roomManager.map.clear();
		connectionEventCallback.onDisconnected();
	}
	public void received(Connection connection, Object object) {
		Log.info("KryonetClientListener.received()");
		if (object instanceof ErrorResponse) {
			ErrorResponse response = (ErrorResponse)object;
			errorEventCallback.onError(response.errorMessage);
		} else if (object instanceof GetRoomsResponse) {
			GetRoomsResponse response = (GetRoomsResponse)object;
			roomEventCallback.onGetRooms(response.rooms);
		} else if (object instanceof JoinRoomFailureResponse) {
			JoinRoomFailureResponse response = (JoinRoomFailureResponse)object;
			roomEventCallback.onJoinRoomFailure(response.errorMessage);
		} else if (object instanceof JoinRoomSuccessResponse) {
			try {
				JoinRoomSuccessResponse response = (JoinRoomSuccessResponse)object;
				response.joinedUser.isItMe = (myself.id == response.joinedUser.id) ? true : false;
				UserManager userManager = myself.userManager;
				RoomManager roomManager = myself.roomManager;
				if (!roomManager.map.containsKey(response.joinedRoom.name)) {
					roomManager.map.put(response.joinedRoom.name, response.joinedRoom);
				}
				userManager.map.put(response.joinedUser.id, response.joinedUser);
				roomManager.addUserToRoom(response.joinedUser, response.joinedRoom.name);
				roomEventCallback.onJoinRoomSuccess(response.joinedUser, response.joinedRoom);
			} catch (RoomManagerException e) {
				roomEventCallback.onJoinRoomFailure(e.getMessage());
			}
		} else if (object instanceof LoginFailureResponse) {
			LoginFailureResponse response = (LoginFailureResponse)object;
			loginOrLogoutEventCallback.onLoginFailure(response.errorMessage);
		} else if (object instanceof LoginSuccessResponse) {
			LoginSuccessResponse response = (LoginSuccessResponse)object;
			myself.username = response.myself.username;
			myself.isAdmin = response.myself.isAdmin;
			myself.isItMe = response.myself.isItMe;
			myself.isPlayer = response.myself.isPlayer;
			loginOrLogoutEventCallback.onLoginSuccess(response.myself);
		} else if (object instanceof LogoutResponse) {
			myself.username = null;
			myself.isAdmin = false;
			myself.isItMe = false;
			myself.isPlayer = false;
			myself.roomManager.map.clear();
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
