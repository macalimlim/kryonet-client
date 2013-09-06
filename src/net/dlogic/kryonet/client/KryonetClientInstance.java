package net.dlogic.kryonet.client;

import java.io.IOException;

public class KryonetClientInstance {
	private static KryonetClient client;
	public static void initialize(int writeBufferSize, int objectBufferSize, int timeout, String host, int tcpPort, int udpPort) throws IOException, KryonetClientInstanceException {
		if (client != null) {
			throw new KryonetClientInstanceException(KryonetClientInstanceException.ALREADY_INITIALIZED);
		}
		client = new KryonetClient(writeBufferSize, objectBufferSize, timeout, host, tcpPort, udpPort);
	}
	public static KryonetClient getInstance() throws KryonetClientInstanceException {
		if (client == null) {
			throw new KryonetClientInstanceException(KryonetClientInstanceException.NOT_INITIALIZED);
		}
		return client;
	}
}
