package com.example.kakaotalk_storage;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class SplashActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		Log.v("wkdgusdn3", "splash activity");

		/*Thread thread = new Thread() {
			public void run() {
				while(true) {
					Log.v("wkdgusdn3", "while");
					if(Talk.loading) {
						Log.v("wkdgusdn3", "finish");
						finish();
						break;
					}
				}
			}
		};
		thread.start();*/


		/*Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				Bundle bundle = msg.getData();
				Log.v("wkdgusdn3", "get data");
				String text = bundle.getString("FINISH");
				Log.v("wkdgusdn3", "get string");

				if(text.equals("FINISH")) finish();
				Log.v("wkdgusdn3", "finish");
			}
		};*/
	}	
}
