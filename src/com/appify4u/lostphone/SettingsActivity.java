package com.appify4u.lostphone;

import com.appify4u.lostphone.core.BaseActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class SettingsActivity extends BaseActivity {

	SharedPreferences prefs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getFragmentManager().beginTransaction()
        .replace(android.R.id.content, new MainActivity.SettingsFragment())
        .commit();
		prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		prefs.registerOnSharedPreferenceChangeListener(
				  new SharedPreferences.OnSharedPreferenceChangeListener() {
				  public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
				    // listener implementation
					  if(key.equals("smstext")){
						  Intent a = new Intent(getBaseContext(),SmsService.class);
							stopService(a);
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							startService(a);
					  }
				  }
				});
	}

}
