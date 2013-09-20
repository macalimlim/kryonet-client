package net.dlogic.kryonet.client;

import net.dlogic.kryonet.client.event.callback.IConnectionEventCallback;
import net.dlogic.kryonet.client.event.callback.IErrorEventCallback;
import net.dlogic.kryonet.client.event.callback.IPersonMessageEventCallback;
import net.dlogic.kryonet.client.event.callback.IRoomEventCallback;
import net.dlogic.kryonet.client.event.callback.IUserEventCallback;
import net.dlogic.kryonet.common.entity.Myself;
import net.dlogic.kryonet.common.entity.User;
import net.dlogic.kryonet.common.manager.MyselfInstance;
import net.dlogic.kryonet.common.manager.RoomManager;
import net.dlogic.kryonet.common.manager.RoomManagerException;
import net.dlogic.kryonet.common.manager.RoomManagerInstance;
import net.dlogic.kryonet.common.manager.UserManager;
import net.dlogic.kryonet.common.manager.UserManagerInstance;
import net.dlogic.kryonet.common.response.ErrorResponse;
import net.dlogic.kryonet.common.response.GetRoomsResponse;
import net.dlogic.kryonet.common.response.JoinRoomFailureResponse;
import net.dlogic.kryonet.common.response.JoinRoomSuccessResponse;
import net.dlogic.kryonet.common.response.LeaveRoomResponse;
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
	private UserManager userManager = UserManagerInstance.manager;
	private RoomManager roomManager = RoomManagerInstance.manager;
	private IErrorEventCallback errorEventCallback;
	private IConnectionEventCallback connectionEventCallback;
	private IRoomEventCallback roomEventCallback;
	private IUserEventCallback userEventCallback;
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
		userEventCallback = callback;
	}
	public void setPersonMessageEventCallback(IPersonMessageEventCallback callback) {
		personMessageEventCallback = callback;
	}
	public void connected(Connection connection) {
		Log.debug("KryonetClientListener.connected()");
		myself.id = connection.getID();
		myself.isAdmin = false;
		myself.isItMe = true;
		myself.isPlayer = false;
		roomManager.map.clear();
		userManager.map.put(myself.id, myself);
		connectionEventCallback.onConnected();
		Log.debug(userManager.map.toString());
	}
	public void disconnected(Connection connection) {
		myself.username = null;
		myself.isAdmin = false;
		myself.isItMe = false;
		myself.isPlayer = false;
		roomManager.map.clear();
		userManager.map.clear();
		connectionEventCallback.onDisconnected();
	}
	public void received(Connection connection, Object object) {
		//Log.info("KryonetClientListener.received()");
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
				response.userJoined.isItMe = (myself.id == response.userJoined.id) ? true : false;
				Log.debug(userManager.map.toString());
				if (!userManager.map.containsKey(response.userJoined.id)) {
					userManager.map.put(response.userJoined.id, response.userJoined);
				}
				if (!roomManager.map.containsKey(response.roomJoined.name)) {
					roomManager.map.put(response.roomJoined.name, response.roomJoined);
				}
				User userJoined = userManager.map.get(response.userJoined.id);
				roomManager.addUserToRoom(userJoined, response.roomJoined.name);
				roomEventCallback.onJoinRoomSuccess(response.userJoined, response.roomJoined);
			} catch (RoomManagerException e) {
				roomEventCallback.onJoinRoomFailure(e.getMessage());
			}
		} else if (object instanceof LeaveRoomResponse) {
			try {
				LeaveRoomResponse response = (LeaveRoomResponse)object;
				response.userLeft.isItMe = (myself.id == response.userLeft.id) ? true : false;
				roomManager.removeUserFromRoom(response.userLeft, response.roomLeft.name);
				if (!roomManager.isUserJoinedInAnyRoom(response.userLeft)) {
					userManager.map.remove(response.userLeft.id);
				}
				roomEventCallback.onLeaveRoom(response.userLeft, response.roomLeft);
			} catch (RoomManagerException e) {
				errorEventCallback.onError(e.getMessage());
			}
		} else if (object instanceof LoginFailureResponse) {
			LoginFailureResponse response = (LoginFailureResponse)object;
			userEventCallback.onLoginFailure(response.errorMessage);
		} else if (object instanceof LoginSuccessResponse) {
			LoginSuccessResponse response = (LoginSuccessResponse)object;
			myself.username = response.myself.username;
			myself.isAdmin = response.myself.isAdmin;
			myself.isItMe = response.myself.isItMe;
			myself.isPlayer = response.myself.isPlayer;
			roomManager.map.clear();
			userManager.map.clear();
			userEventCallback.onLoginSuccess(response.myself);
		} else if (object instanceof LogoutResponse) {
			myself.username = null;
			myself.isAdmin = false;
			myself.isItMe = false;
			myself.isPlayer = false;
			roomManager.map.clear();
			userManager.map.clear();
			userEventCallback.onLogout();
		} else if (object instanceof PrivateMessageResponse) {
			PrivateMessageResponse response = (PrivateMessageResponse)object;
			personMessageEventCallback.onPrivateMessage(response.sender, response.message);
		} else if (object instanceof PublicMessageResponse) {
			PublicMessageResponse response = (PublicMessageResponse)object;
			personMessageEventCallback.onPublicMessage(response.sender, response.message);
		}
	}
	public void idle(Connection connection) {
		connectionEventCallback.onIdle();
	}
}
