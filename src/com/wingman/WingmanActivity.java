package com.wingman;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.Bitmap.CompressFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;
import android.widget.ToggleButton;

public class WingmanActivity extends Activity implements SurfaceHolder.Callback,
		OnCheckedChangeListener {
	static final int FOTO_MODE = 0;
	private static final String TAG = "WingmanActivity";
	Camera mCamera;
	boolean mPreviewRunning = false;
	private Context mContext = this;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		Log.e(TAG, "onCreate");


		// Fullscreen
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.main);
		
		mSurfaceView = (SurfaceView) findViewById(R.id.surface_camera);
		mSurfaceView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mCamera.takePicture(null, mPictureCallback, mPictureCallback);
			}
		});
		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(this);
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		ToggleButton blitz = (ToggleButton) findViewById(R.id.toggleButton1);
		blitz.setOnCheckedChangeListener(this);
		
		Context context = getApplicationContext();
		String text = "Welcome to wingman! Please click the screen to start recognizing people!"; 
		Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
		toast.show();
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
		public void onPictureTaken(byte[] imageData, Camera c) {
			
			if (imageData != null) {
				Intent mIntent = new Intent();
				StoreByteImage(mContext, imageData, 100, "LatestWingman");

				// Run face recognition activity
				Intent myIntent = new Intent(getBaseContext(), FaceRecActivity.class);
				startActivity(myIntent);
				
				// Face rec done, go back to cam preview
				mCamera.startPreview();
				setResult(FOTO_MODE, mIntent);
				//finish();
			}
		}
	};
	
	protected void onResume() {
		Log.e(TAG, "onResume");
		super.onResume();
	}

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	protected void onStop() {
		Log.e(TAG, "onStop");
		super.onStop();
	}

	public void surfaceCreated(SurfaceHolder holder) {
		Log.e(TAG, "surfaceCreated");
		mCamera = Camera.open();

	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		Log.e(TAG, "surfaceChanged");

		// XXX stopPreview() will crash if preview is not running
		if (mPreviewRunning) {
			mCamera.stopPreview();
		}

		Camera.Parameters p = mCamera.getParameters();
		p.setPreviewSize(w, h);
		mCamera.setParameters(p);
		try {
			mCamera.setPreviewDisplay(holder);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mCamera.startPreview();
		mPreviewRunning = true;
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.e(TAG, "surfaceDestroyed");
		mCamera.stopPreview();
		mPreviewRunning = false;
		mCamera.release();
	}

	private SurfaceView mSurfaceView;
	private SurfaceHolder mSurfaceHolder;

	
	
	public static boolean StoreByteImage(Context mContext, byte[] imageData,
			int quality, String expName) {

        File sdImageMainDirectory = new File("/sdcard/wingman");
        sdImageMainDirectory.mkdirs();
		FileOutputStream fileOutputStream = null;
		try {

			BitmapFactory.Options options=new BitmapFactory.Options();
			options.inSampleSize = 5;
			
			Bitmap myImage = BitmapFactory.decodeByteArray(imageData, 0,
					imageData.length,options);

			
			fileOutputStream = new FileOutputStream(
					sdImageMainDirectory.toString() +"/" + expName + ".jpg");
							
  
			BufferedOutputStream bos = new BufferedOutputStream(
					fileOutputStream);

			myImage.compress(CompressFormat.JPEG, quality, bos);

			bos.flush();
			bos.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		if (mCamera == null)
	    {
	        return;
	    }
	    Camera.Parameters params = mCamera.getParameters();
	    String value;
	    boolean isOn;
	    isOn = arg0.isChecked();
		if (isOn) // we are being ask to turn it on
	    {
	        value = Camera.Parameters.FLASH_MODE_TORCH;
	    }
	    else  // we are being asked to turn it off
	    {
	        value =  Camera.Parameters.FLASH_MODE_OFF;
	    }

	    try{    
	        params.setFlashMode(value);
	        mCamera.setParameters(params);

	        String nowMode = mCamera.getParameters().getFlashMode();

	        if (isOn && nowMode.equals(Camera.Parameters.FLASH_MODE_TORCH))
	        {
	            return;
	        }
	        if (! isOn && nowMode.equals(Camera.Parameters.FLASH_MODE_AUTO))
	        {
	            return;
	        }
	        return;
	    }
	    catch (Exception ex)
	    {
	        Log.e(TAG, this.getClass().getSimpleName() +  " error setting flash mode to: "+ value + " " + ex.toString());
	    }
	}

}