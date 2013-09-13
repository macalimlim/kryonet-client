package net.dlogic.kryonet.client;

import java.io.IOException;

import com.esotericsoftware.minlog.Log;

public class KryonetClientInstance {
	private static KryonetClient client;
	public static void initialize(int writeBufferSize, int objectBufferSize) throws IOException, KryonetClientException {
		Log.info("KryonetClientInstance.initialize(" + writeBufferSize + ", " + objectBufferSize + ")");
		if (client != null) {
			throw new KryonetClientException(KryonetClientException.ALREADY_INITIALIZED);
		}
		client = new KryonetClient(writeBufferSize, objectBufferSize);
	}
	public static KryonetClient getInstance() throws KryonetClientException {
		Log.info("KryonetClientInstance.getInstance()");
		if (client == null) {
			throw new KryonetClientException(KryonetClientException.NOT_INITIALIZED);
		}
		return client;
	}
}
