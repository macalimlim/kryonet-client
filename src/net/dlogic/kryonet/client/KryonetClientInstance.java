package net.dlogic.kryonet.client;

import java.io.IOException;

import net.dlogic.kryonet.common.constant.ErrorMessage;

import com.esotericsoftware.minlog.Log;

public class KryonetClientInstance {
	private static KryonetClient client;
	public static void initialize(int writeBufferSize, int objectBufferSize) throws IOException, KryonetClientException {
		Log.debug("KryonetClientInstance.initialize(" + writeBufferSize + ", " + objectBufferSize + ")");
		if (client != null) {
			throw new KryonetClientException(ErrorMessage.CLIENT_ALREADY_INITIALIZED);
		}
		client = new KryonetClient(writeBufferSize, objectBufferSize);
	}
	public static KryonetClient getInstance() throws KryonetClientException {
		Log.debug("KryonetClientInstance.getInstance()");
		if (client == null) {
			throw new KryonetClientException(ErrorMessage.CLIENT_NOT_INITIALIZED);
		}
		return client;
	}
}
