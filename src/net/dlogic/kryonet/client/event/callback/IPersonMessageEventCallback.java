package net.dlogic.kryonet.client.event.callback;

import net.dlogic.kryonet.common.entity.User;

public interface IPersonMessageEventCallback {
	public void onPrivateMessage(User sender, String message);
	public void onPublicMessage(User sender, String message);
}
