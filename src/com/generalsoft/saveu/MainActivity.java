package com.generalsoft.saveu;

import com.example.saveu.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	public static final String TAG = Config.TAG;
	Context mContext;
	Button stopBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_main);

		stopBtn = (Button) findViewById(R.id.stopBtn);

		stopBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				stopService(new Intent(MainActivity.this, RingtoneService.class));
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	
}
