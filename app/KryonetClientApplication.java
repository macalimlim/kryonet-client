import java.io.IOException;

import net.dlogic.kryonet.client.KryonetClient;
import net.dlogic.kryonet.client.KryonetClientException;
import net.dlogic.kryonet.client.KryonetClientInstance;
import net.dlogic.kryonet.common.utility.KryonetUtility;

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
			client.listener.setConnectionEventCallback(new ConnectionEventCallback());
			client.listener.setLoginOrLogoutEventCallback(new UserEventCallback());
			client.listener.setPersonMessageEventCallback(new PersonMessageEventCallback());
			client.listener.setRoomEventCallback(new RoomEventCallback());
			client.listener.setErrorEventCallback(new ErrorEventCallback());
			KryonetUtility.registerClass(client.endpoint, CustomRequest.class);
			KryonetUtility.registerClass(client.endpoint, CustomResponse.class);
			client.endpoint.addListener(new CustomClientListener());
			client.start(timeout, host, tcpPort, udpPort);
			client.sendLoginRequest("mike", "mikex");
			client.sendLoginRequest("mike", "mike");
			client.sendGetRoomsRequest(null);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (KryonetClientException e) {
			e.printStackTrace();
		}
	}
}
