package net.dlogic.kryonet.client;

public class KryonetClientInstanceException extends Exception {
	private static final long serialVersionUID = 3323206532088320089L;
	public static final String ALREADY_INITIALIZED = "client already initialized";
	public static final String NOT_INITIALIZED = "client not initialized";
	public KryonetClientInstanceException(String message) {
		super(message);
	}
}
