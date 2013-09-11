package net.dlogic.kryonet.client.event.callback;

import net.dlogic.kryonet.common.entity.User;

public interface ILoginOrLogoutEventCallback {
	public void onLoginFailure(String errorMessage);
	public void onLoginSuccess(User myself);
	public void onLogout();
}
