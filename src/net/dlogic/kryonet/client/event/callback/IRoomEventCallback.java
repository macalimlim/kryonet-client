package net.dlogic.kryonet.client.event.callback;

import net.dlogic.kryonet.common.entity.Room;
import net.dlogic.kryonet.common.entity.User;

public interface IRoomEventCallback {
	public void onGetRooms(Room[] rooms);
	public void onJoinRoomFailure(String errorMessage);
	public void onJoinRoomSuccess(User joinedUser, Room joinedRoom);
}
