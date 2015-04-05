package com.appify4u.lostphone;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.appify4u.lostphone.core.AppLock;
import com.appify4u.lostphone.core.AppLockActivity;
import com.appify4u.lostphone.core.LockManager;

public class MainActivity extends ActionBarActivity implements View.OnClickListener{

	Button a,btOnOff,btChange;
	private ServiceConnection mConnection;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_main);
		a = (Button) findViewById(R.id.button1);
		btOnOff = (Button) findViewById(R.id.bt_on_off);
		btOnOff.setOnClickListener(this);

		btChange = (Button) findViewById(R.id.bt_change);
		btChange.setText(R.string.change_passcode);
		btChange.setOnClickListener(this);

		updateUI();
		a.setOnClickListener(this);
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

	    if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
	    	final AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
	               .setCancelable(false)
	               .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	                   public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
	                	   startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
	                   }
	               })
	               .setNegativeButton("No", new DialogInterface.OnClickListener() {
	                   public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
	                        dialog.cancel();
	                   }
	               });
	        final AlertDialog alert = builder.create();
	        alert.show();

	    }
	    
	    mConnection = new ServiceConnection() {
	        // Called when the connection with the service is established
	        public void onServiceConnected(ComponentName className, IBinder service) {
	        }

	        // Called when the connection with the service disconnects unexpectedly
	        public void onServiceDisconnected(ComponentName className) {
	        }
	    };

		
		Thread timer = new Thread(){
			public void run(){
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally{
					Intent a = new Intent(getApplicationContext(),SmsService.class);
					startService(a);
					bindService(new Intent(getApplicationContext(), SmsService.class), mConnection, Context.BIND_AUTO_CREATE);
				}
			}
		};
		timer.start();
		
		}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch(id)
		{
		case R.id.about :
			Intent i = new Intent("com.appify4u.lostphone.ABOUT");
			startActivity(i);
			break;
		case R.id.settings :
			Intent j = new Intent("com.appify4u.lostphone.SETTINGSACTIVITY");
			startActivity(j);
		case R.id.help :
			Intent k = new Intent("com.appify4u.lostphone.HELPACTIVITY");
			startActivity(k);
			
			
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unbindService(mConnection);
		super.onDestroy();
	}

	
	public static class SettingsFragment extends PreferenceFragment {
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);


	        // Load the preferences from an XML resource
	        addPreferencesFromResource(R.xml.prefs);
	    }
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if(view.equals(a)){
			Intent j = new Intent("com.appify4u.lostphone.SETTINGSACTIVITY");
			startActivity(j);
		}
		
		if (view.equals(btOnOff)) {
			int type = LockManager.getInstance().getAppLock().isPasscodeSet() ? AppLock.DISABLE_PASSLOCK
					: AppLock.ENABLE_PASSLOCK;
			Intent intent = new Intent(this, AppLockActivity.class);
			intent.putExtra(AppLock.TYPE, type);
			startActivityForResult(intent, type);
		} else if (view.equals(btChange)) {
			Intent intent = new Intent(this, AppLockActivity.class);
			intent.putExtra(AppLock.TYPE, AppLock.CHANGE_PASSWORD);
			intent.putExtra(AppLock.MESSAGE,
					getString(R.string.enter_old_passcode));
			startActivityForResult(intent, AppLock.CHANGE_PASSWORD);
		}
		
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case AppLock.DISABLE_PASSLOCK:
			break;
		case AppLock.ENABLE_PASSLOCK:
		case AppLock.CHANGE_PASSWORD:
			if (resultCode == RESULT_OK) {
				Toast.makeText(this, getString(R.string.setup_passcode),
						Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
		updateUI();
	}

	private void updateUI() {
		if (LockManager.getInstance().getAppLock().isPasscodeSet()) {
			btOnOff.setText(R.string.disable_passcode);
			btChange.setEnabled(true);
		} else {
			btOnOff.setText(R.string.enable_passcode);
			btChange.setEnabled(false);
		}
	}


	}

