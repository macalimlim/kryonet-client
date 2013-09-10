package net.dlogic.kryonet.client.event.callback;

public interface IRoomEventCallback {
	public void onJoinRoomFailure(String errorMessage);
	public void onJoinRoomSuccess();
}
