package com.pucit.mainpack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.pucit.mainpack.R;

public class MainMenuActivity extends Activity implements OnClickListener {

	Button play, option, exit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		play = (Button) findViewById(R.id.play);
		option = (Button) findViewById(R.id.option);
		exit = (Button) findViewById(R.id.exit);
		
		play.setOnClickListener(this);
		option.setOnClickListener(this);
		exit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		
		if (v.equals(play)){
			Intent i = new Intent(MainMenuActivity.this, StageScreen.class);
			startActivity(i);
		}else if (v.equals(option)){
			
		}else if (v.equals(exit)){
			finish();
		}
		
	}

	
}
