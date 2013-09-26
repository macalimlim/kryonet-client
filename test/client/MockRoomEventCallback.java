package client;

import net.dlogic.kryonet.client.event.callback.IRoomEventCallback;
import net.dlogic.kryonet.common.entity.Room;
import net.dlogic.kryonet.common.entity.User;

public class MockRoomEventCallback implements IRoomEventCallback {

	@Override
	public void onGetRooms(Room[] rooms) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onJoinRoomFailure(String errorMessage) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onJoinRoomSuccess(User userJoined, Room roomJoined) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLeaveRoom(User userLeft, Room roomLeft) {
		// TODO Auto-generated method stub

	}

}
