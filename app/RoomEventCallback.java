import java.util.Iterator;
import java.util.List;

import com.esotericsoftware.minlog.Log;

import net.dlogic.kryonet.client.event.callback.IRoomEventCallback;
import net.dlogic.kryonet.common.entity.Room;
import net.dlogic.kryonet.common.entity.User;


public class RoomEventCallback implements IRoomEventCallback {

	@Override
	public void onJoinRoomFailure(String errorMessage) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onJoinRoomSuccess(User joinedUser, Room joinedRoom) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetRooms(List<Room> roomList) {
		Iterator<Room> it = roomList.iterator();
		while (it.hasNext()) {
			Room room = it.next();
			Log.info(room.name);
		}
	}

}
