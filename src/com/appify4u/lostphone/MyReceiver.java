package com.appify4u.lostphone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver {   

    @Override
    public void onReceive(Context context, Intent intent) {

     Intent myIntent = new Intent(context, SmsService.class);
     context.startService(myIntent);
    }
}