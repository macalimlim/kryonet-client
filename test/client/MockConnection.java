package client;

import com.esotericsoftware.kryonet.Connection;

public class MockConnection extends Connection {
	public int getID () {
		return 1;
	}
}
