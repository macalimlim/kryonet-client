package client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import net.dlogic.kryonet.client.KryonetClientListener;
import net.dlogic.kryonet.common.entity.Room;
import net.dlogic.kryonet.common.entity.User;
import net.dlogic.kryonet.common.manager.MyselfInstance;
import net.dlogic.kryonet.common.manager.RoomManagerInstance;
import net.dlogic.kryonet.common.manager.UserManagerInstance;
import net.dlogic.kryonet.common.response.JoinRoomSuccessResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class KryonetClientListenerTest {
	private KryonetClientListener listener;
	private MockConnection connection;
	@Before
	public void setUp() throws Exception {
		listener = new KryonetClientListener();
		listener.setConnectionEventCallback(new MockConnectionEventCallback());
		listener.setRoomEventCallback(new MockRoomEventCallback());
		RoomManagerInstance.manager.map.clear();
		UserManagerInstance.manager.map.clear();
		initializeMyself();
		connection = new MockConnection();
	}
	private void initializeMyself() {
		MyselfInstance.myself.id = 0;
		MyselfInstance.myself.username = null;
		MyselfInstance.myself.isAdmin = false;
		MyselfInstance.myself.isItMe = false;
		MyselfInstance.myself.isPlayer = false;
	}
	@After
	public void tearDown() throws Exception {
	}
	@Test
	public void testConnected() {
		listener.connected(connection);
		assertEquals(connection.getID(), MyselfInstance.myself.id);
		assertFalse(MyselfInstance.myself.isAdmin);
		assertTrue(MyselfInstance.myself.isItMe);
		assertFalse(MyselfInstance.myself.isPlayer);
		assertEquals(0, RoomManagerInstance.manager.map.size());
		assertEquals(1, UserManagerInstance.manager.map.size());
		assertTrue(UserManagerInstance.manager.map.containsKey(MyselfInstance.myself.id));
	}
	@Test
	public void testDisconnected() {
		testConnected();
		listener.disconnected(connection);
		assertEquals(0, MyselfInstance.myself.id);
		assertNull(MyselfInstance.myself.username);
		assertFalse(MyselfInstance.myself.isAdmin);
		assertFalse(MyselfInstance.myself.isItMe);
		assertFalse(MyselfInstance.myself.isPlayer);
		assertEquals(0, RoomManagerInstance.manager.map.size());
		assertEquals(0, UserManagerInstance.manager.map.size());
		assertFalse(UserManagerInstance.manager.map.containsKey(MyselfInstance.myself.id));
	}
	@Test
	public void testReceived_JoinRoomSuccessResponse_MyselfJoiningARoom() {
		testConnected();
		User user = new User();
		user.id = 1;
		user.username = "mike";
		Room room = new Room();
		room.name = "Test";
		JoinRoomSuccessResponse joinRoomSuccessResponse = new JoinRoomSuccessResponse();
		joinRoomSuccessResponse.roomJoined = room;
		joinRoomSuccessResponse.userJoined = user;
		// myself is in usermanager map
		UserManagerInstance.manager.map.put(user.id, user);
		assertEquals(0, RoomManagerInstance.manager.map.size());
		assertEquals(0, room.users.size());
		listener.received(connection, joinRoomSuccessResponse);
		assertEquals(1, UserManagerInstance.manager.map.size());
		assertEquals(1, RoomManagerInstance.manager.map.size());
		assertTrue(UserManagerInstance.manager.map.containsKey(user.id));
		assertTrue(RoomManagerInstance.manager.map.containsKey(room.name));
		Room roomJoined = RoomManagerInstance.manager.map.get(room.name);
		assertTrue(roomJoined.users.containsKey(user.id));
		assertEquals(1, roomJoined.users.size());
	}
	@Test
	public void testReceived_JoinRoomSuccessResponse_OtherUsersJoiningARoom() {
		testReceived_JoinRoomSuccessResponse_MyselfJoiningARoom();
		User other = new User();
		other.id = 2;
		other.username = "other";
		Room room = new Room();
		room.name = "Test";
		JoinRoomSuccessResponse joinRoomSuccessResponse = new JoinRoomSuccessResponse();
		joinRoomSuccessResponse.roomJoined = room;
		joinRoomSuccessResponse.userJoined = other;
		assertEquals(1, RoomManagerInstance.manager.map.get(room.name).users.size());
		assertEquals(1, UserManagerInstance.manager.map.size());
		assertEquals(1, RoomManagerInstance.manager.map.size());
		listener.received(connection, joinRoomSuccessResponse);
		assertEquals(2, UserManagerInstance.manager.map.size());
		assertEquals(1, RoomManagerInstance.manager.map.size());
		assertTrue(UserManagerInstance.manager.map.containsKey(other.id));
		Room roomJoined = RoomManagerInstance.manager.map.get(room.name);
		assertTrue(roomJoined.users.containsKey(other.id));
		assertEquals(2, roomJoined.users.size());
		//
		User other2 = new User();
		other2.id = 3;
		other2.username = "other2";
		joinRoomSuccessResponse.roomJoined = room;
		joinRoomSuccessResponse.userJoined = other2;
		listener.received(connection, joinRoomSuccessResponse);
		assertEquals(3, UserManagerInstance.manager.map.size());
		assertEquals(1, RoomManagerInstance.manager.map.size());
		assertTrue(UserManagerInstance.manager.map.containsKey(other2.id));
		assertTrue(roomJoined.users.containsKey(other2.id));
		assertEquals(3, roomJoined.users.size());
	}
}
