package com.pucit.mainpack;

import java.net.URL;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.opengl.CCGLSurfaceView;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.WindowManager;


public class XtremeRacing extends Activity {

	protected CCGLSurfaceView _glSurfaceView;
	int stage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		_glSurfaceView = new CCGLSurfaceView(this);				
		setContentView(_glSurfaceView);

		stage = this.getIntent().getExtras().getInt("Stage");
		
		new PlaySounds().execute();
		
		//playSound();
	}

	boolean flag = true;
	
	public void playSound(){
		
		MediaPlayer mp = MediaPlayer.create(XtremeRacing.this, R.raw.bg);
		mp.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
			}

		});
		 
		////mp.setLooping(true);
		
		while (flag){
			mp.start();
		}
		
		
	}

	@Override
	public void onStart()
	{
		super.onStart();

		CCDirector.sharedDirector().attachInView(_glSurfaceView);

		//CCDirector.sharedDirector().setDeviceOrientation(CCDirector.kCCDeviceOrientationLandscapeLeft);

		CCDirector.sharedDirector().setDisplayFPS(true);

		CCDirector.sharedDirector().setAnimationInterval(1.0f / 60.0f);

		CCScene scene = null;
		
		if ( stage == 1 )
			scene = GameLayer.scene();
		else if ( stage == 2 )
			scene = GameLayer2.scene();
		else
			scene = GameLayer3.scene();
		
		CCDirector.sharedDirector().runWithScene(scene);

	}

	@Override
	public void onPause()
	{
		super.onPause();

		flag = false;
		
		CCDirector.sharedDirector().pause();
	}

	@Override
	public void onResume()
	{
		super.onResume();
		
		flag = true;

		CCDirector.sharedDirector().resume();
	}

	@Override
	public void onStop()
	{
		super.onStop();

		CCDirector.sharedDirector().end();
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.activity_xtreme_racing, menu);
		return true;
	}

	class PlaySounds extends AsyncTask<Void, Void, Void> {
	     
	     protected void onProgressUpdate(Integer... progress) {
	     }

	     protected void onPostExecute(Long result) {
	     }

		@Override
		protected Void doInBackground(Void... arg0) {
			
			playSound();
			
			return null;
		}
	 }
	
}
