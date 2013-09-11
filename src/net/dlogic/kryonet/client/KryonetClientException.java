package net.dlogic.kryonet.client;

public class KryonetClientException extends Exception {
	private static final long serialVersionUID = -4999045643489800015L;
	public static final String ALREADY_INITIALIZED = "client already initialized";
	public static final String NOT_INITIALIZED = "client not initialized";
	public KryonetClientException(String message) {
		super(message);
	}
}
