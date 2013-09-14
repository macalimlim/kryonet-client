import java.io.IOException;

import net.dlogic.kryonet.client.KryonetClient;
import net.dlogic.kryonet.client.KryonetClientException;
import net.dlogic.kryonet.client.KryonetClientInstance;
import net.dlogic.kryonet.client.KryonetClientListener;

public class KryonetClientApplication {
	public static void main(String[] args) {
		try {
			int writeBufferSize = Integer.parseInt(args[0]);
			int objectBufferSize = Integer.parseInt(args[1]);
			int timeout = Integer.parseInt(args[2]);
			String host = args[3];
			int tcpPort = Integer.parseInt(args[4]);
			int udpPort = Integer.parseInt(args[5]);
			KryonetClientInstance.initialize(writeBufferSize, objectBufferSize);
			KryonetClient client = KryonetClientInstance.getInstance();
			KryonetClientListener listener = client.getKryonetClientListener();
			listener.setLoginOrLogoutEventCallback(new LoginOrLogoutEventCallback());
			listener.setPersonMessageEventCallback(new PersonMessageEventCallback());
			listener.setRoomEventCallback(new RoomEventCallback());
			client.start(timeout, host, tcpPort, udpPort);
			client.sendLoginRequest("mike", "mike");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (KryonetClientException e) {
			e.printStackTrace();
		}
	}
}
