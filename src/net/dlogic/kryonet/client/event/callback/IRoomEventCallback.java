package net.dlogic.kryonet.client.event.callback;

import java.util.List;

import net.dlogic.kryonet.common.entity.Room;
import net.dlogic.kryonet.common.entity.User;

public interface IRoomEventCallback {
	public void onGetRooms(List<Room> roomList);
	public void onJoinRoomFailure(String errorMessage);
	public void onJoinRoomSuccess(User joinedUser, Room joinedRoom);
}
