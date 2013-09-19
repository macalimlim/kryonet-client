import net.dlogic.kryonet.client.event.callback.IRoomEventCallback;
import net.dlogic.kryonet.common.entity.Room;
import net.dlogic.kryonet.common.entity.User;

import com.esotericsoftware.minlog.Log;

public class RoomEventCallback implements IRoomEventCallback {

	@Override
	public void onJoinRoomFailure(String errorMessage) {
		Log.info(errorMessage);
	}

	@Override
	public void onJoinRoomSuccess(User joinedUser, Room joinedRoom) {
		Log.info(joinedUser.username + "=>" + joinedRoom.name);
	}

	@Override
	public void onGetRooms(Room[] rooms) {
		for (Room room : rooms) {
			Log.info(room.name);
		}
	}

	@Override
	public void onLeaveRoom(User userLeft, Room roomLeft) {
		// TODO Auto-generated method stub
		
	}

}
