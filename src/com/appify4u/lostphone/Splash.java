package com.appify4u.lostphone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;


public class Splash extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.splash);
		ImageView logo = (ImageView) findViewById(R.id.imageView1);
		logo.setVisibility(View.INVISIBLE);
		Animation fadeIn = new AlphaAnimation(0,1);
		fadeIn.setInterpolator(new DecelerateInterpolator());
		fadeIn.setDuration(5000);
		logo.setAnimation(fadeIn);
		
		Thread timer = new Thread(){
			public void run(){
				try{
					sleep(5000);
				} catch(InterruptedException e){
					e.printStackTrace();
				} finally {
					Intent intent = new Intent("com.appify4u.lostphone.MAINACTIVITY");
					startActivity(intent);
				}
			}
		};
		timer.start();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
}
