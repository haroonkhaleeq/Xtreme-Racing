package com.pucit.mainpack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.pucit.mainpack.R;

public class StageScreen extends Activity implements OnClickListener {

	Button stage1, stage2, stage3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stages);
		
		stage1 = (Button) findViewById(R.id.button1);
		stage2 = (Button) findViewById(R.id.button3);
		stage3 = (Button) findViewById(R.id.button2);
		
		stage1.setOnClickListener(this);
		stage2.setOnClickListener(this);
		stage3.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		
		if (v.equals(stage1)){
			Toast.makeText(this, "Stage 1", Toast.LENGTH_LONG).show();
			
			Intent i = new Intent(StageScreen.this, XtremeRacing.class);
			i.putExtra("Stage", 1);
			startActivity(i);
			
		}else if (v.equals(stage2)){
			Toast.makeText(this, "Stage 2", Toast.LENGTH_LONG).show();
			
			Intent i = new Intent(StageScreen.this, XtremeRacing.class);
			i.putExtra("Stage", 2);
			startActivity(i);
			
		}else if (v.equals(stage3)){
			Toast.makeText(this, "Stage 3", Toast.LENGTH_LONG).show();
			
			Intent i = new Intent(StageScreen.this, XtremeRacing.class);
			i.putExtra("Stage", 3);
			startActivity(i);
			
		}
		
	}

	
}
