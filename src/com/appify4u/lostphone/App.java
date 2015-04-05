package com.appify4u.lostphone;

import com.appify4u.lostphone.core.LockManager;
import android.app.Application;

public class App extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		LockManager.getInstance().enableAppLock(this);
	}

}
