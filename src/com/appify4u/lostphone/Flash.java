package com.appify4u.lostphone;

import java.io.IOException;

import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.util.Log;

@SuppressWarnings("deprecation")
public class Flash {
	 	private static  Camera mCamera;
	    static Camera.Parameters mParams;
	    private static boolean on;
	    
	    public static void turnOn() {
	        if (mCamera != null) {
	        // Turn on LED
	        mParams = mCamera.getParameters();
	        mParams.setFlashMode(Parameters.FLASH_MODE_TORCH);
	        mCamera.setParameters(mParams);

	        on = true;
	    }
	    }

	    /** Turn the devices FlashLight off */
	    public static void turnOff() {
	        // Turn off flashlight
	        if (mCamera != null) {
	            mParams = mCamera.getParameters();
	            if (mParams.getFlashMode().equals(Parameters.FLASH_MODE_TORCH)) {
	                mParams.setFlashMode(Parameters.FLASH_MODE_OFF);
	                mCamera.setParameters(mParams);
	            }
	        }
	        on = false;
	    }

	    /** Toggle the flashlight on/off status */
	    public static void toggleFlashLight() {
	        if (!on) { // Off, turn it on
	            turnOn();
	        } else { // On, turn it off
	            turnOff();
	        }
	    }
	    
	    public static void run(int delay,int times) {
	        try {
	            // Switch on the cam for app's life
	            if (mCamera == null) {
	                // Turn on Cam
	                mCamera = Camera.open();
	                try {
	                    mCamera.setPreviewDisplay(null);
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	                mCamera.startPreview();
	            }

	            for (int i=0; i < times*2; i++) {
	                toggleFlashLight();
	                Thread.sleep(delay);
	            }

	            if (mCamera != null) {
	                mCamera.stopPreview();
	                mCamera.release();
	                mCamera = null;
	            }
	        } catch (Exception e){ 
	            e.printStackTrace(); 
	        }
	    }
}
