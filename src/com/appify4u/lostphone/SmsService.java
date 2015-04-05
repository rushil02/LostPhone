package com.appify4u.lostphone;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;


public class SmsService extends Service implements LocationListener{
	 
	SharedPreferences prefs;
	String storedKey,msg;
	LocationManager locationManager;
	SmsManager sms;
    LocationListener locationListenerNetwork;
    Boolean RingerMode,FlashMode,VibrateMode;
    Vibrator v;

	private static final int ONE_MINUTE = 1000 * 60;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		storedKey = prefs.getString("smstext", "LOST");
		RingerMode = prefs.getBoolean("RingerMode", true);
		FlashMode = prefs.getBoolean("FlashMode", true);
		VibrateMode = prefs.getBoolean("VibrateMode", true);
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
		sms = SmsManager.getDefault();
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, ONE_MINUTE, 10, this);
		locationListenerNetwork = new LocationListener(){

			@Override
			public void onLocationChanged(Location location) {
				// TODO Auto-generated method stub
				msg = "https://maps.google.com/maps?q=loc:"+location.getLatitude()+","+location.getLongitude();
				Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}
			
		};
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		filter.addAction("YouWillNeverKillMe");
		registerReceiver(receiver, filter);
		Toast.makeText(getApplicationContext(), "Service Started", Toast.LENGTH_SHORT).show();
		return START_STICKY;
	}


	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
		}
		
	@Override
	  public void onDestroy() {
	    sendBroadcast(new Intent("YouWillNeverKillMe"));
	    unregisterReceiver(receiver);

	  }
	
	private final BroadcastReceiver receiver = new BroadcastReceiver() {
		

		
		@Override
		   public void onReceive(Context context, Intent intent) {
		      String action = intent.getAction();
		      if(action.equals("android.provider.Telephony.SMS_RECEIVED")){
		        //action for sms received
		          final Bundle bundle = intent.getExtras();
		          
		          try {
		               
		              if (bundle != null) {
		                   
		                  final Object[] pdusObj = (Object[]) bundle.get("pdus");
		                   
		                  for (int i = 0; i < pdusObj.length; i++) {
		                       
		                      SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
		                      String phoneNumber = currentMessage.getDisplayOriginatingAddress();
		                       
		                      String senderNum = phoneNumber;
		                      String message = currentMessage.getDisplayMessageBody();
		   
		                      Log.i("SmsReceiver", "senderNum: "+ senderNum + "; message: " + message); 
		                     
		   
		                     // Show Alert
		                      if(message.contentEquals(storedKey)){
		                    	  sendLoc(senderNum);
		                    	  if(RingerMode == true)
		                    		  ringersettings();
		                    	  if(FlashMode == true)
		                    		  Flash.run(500, 10);
		                    	  if(VibrateMode == true)
		                    		  for(int a=0;a<10;a++){
		                    			v.vibrate(500);
		                    			Thread.sleep(500);
		                    		  }
		                      }
		                  } // end for loop
		                } // bundle is null
		   
		          } catch (Exception e) {
		              Log.e("SmsReceiver", "Exception smsReceiver" +e);
		               
		          }

		      }
		      if(action.equals("YouWillNeverKillMe")){
		    	  startService(new Intent(getBaseContext(),SmsService.class));
		      }
		   }

		private void ringersettings() {
			// TODO Auto-generated method stub
			AudioManager audiomanage = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
			audiomanage.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
		}

		private void sendLoc(String senderNum) {
			// TODO Auto-generated method stub
			if(msg==null || msg.isEmpty()){
				msg = "Location was not traced. Sorry";
			}
			sms.sendTextMessage(senderNum, null, msg, null, null);
		}
		};

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

		msg = "https://maps.google.com/maps?q=loc:"+location.getLatitude()+","+location.getLongitude();
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}


	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
	}


	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		locationManager.removeUpdates(locationListenerNetwork);
	}


	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, ONE_MINUTE, 10, locationListenerNetwork);
	}
	
	

}
