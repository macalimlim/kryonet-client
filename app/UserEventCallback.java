import com.esotericsoftware.minlog.Log;

import net.dlogic.kryonet.client.event.callback.IUserEventCallback;
import net.dlogic.kryonet.common.entity.User;


public class UserEventCallback implements IUserEventCallback {

	@Override
	public void onLoginFailure(String errorMessage) {
		Log.info("UserEventCallback.onLoginFailure()");
	}

	@Override
	public void onLogout() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLoginSuccess(User myself) {
		Log.info("UserEventCallback.onLoginSuccess()");
	}

}
