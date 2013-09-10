package net.dlogic.kryonet.client;

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

public class KryonetClientListener extends Listener {
	private IRoomEventCallback roomEventCallback;
	public void setRoomEventCallback(IRoomEventCallback callback) {
		roomEventCallback = callback;
	} 
	public void connected(Connection connection) {
		// TODO Auto-generated method stub
		super.connected(connection);
	}
	public void disconnected(Connection connection) {
		// TODO Auto-generated method stub
		super.disconnected(connection);
	}
	public void received(Connection connection, Object object) {
		if (object instanceof JoinRoomFailureResponse) {
			JoinRoomFailureResponse response = (JoinRoomFailureResponse)object;
			roomEventCallback.onJoinRoomFailure(response.errorMessage);
		} else if (object instanceof JoinRoomSuccessResponse) {
			JoinRoomSuccessResponse response = (JoinRoomSuccessResponse)object;
			roomEventCallback.onJoinRoomSuccess();
		} else if (object instanceof LoginFailureResponse) {
			
		} else if (object instanceof LoginSuccessResponse) {
			
		} else if (object instanceof LogoutResponse) {
			
		} else if (object instanceof PrivateMessageResponse) {
			
		} else if (object instanceof PublicMessageResponse) {
			
		}
	}
	public void idle(Connection connection) {
		super.idle(connection);
	}
}
