package com.example.kakaotalk_storage;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Talk extends ActionBarActivity {

	private FileInputStream fis;
	private BufferedReader buff;
	private int typeTalk = 0;	// 0->내대화, 1->상대대화
	private String path;
	private Handler handler;
	private Animation loading_Anim;
	private boolean isLoading = false;

	private LinearLayout linearLayout;
	private ImageView loading;

	TextView textView_tmp;
	LinearLayout.LayoutParams p_tmp;

	TextView textView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.talk);

		handler = new Handler();

		setView();
		setAnimation();

		getPath();
		openFile();		
		
		isLoading = true;
		loading.startAnimation(loading_Anim);

		ReadText readText = new ReadText();
		readText.execute();

	}

	void setView() {
		linearLayout = (LinearLayout)findViewById(R.id.linearLayout);
		loading = (ImageView)findViewById(R.id.loading);
	}

	void setAnimation() {
		loading_Anim = AnimationUtils.loadAnimation(this, R.anim.loading_anim);

		loading_Anim.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			@Override
			public void onAnimationEnd(Animation animation) {
				if(isLoading) {
					loading.startAnimation(loading_Anim);
				}
			}
		});
	}

	void getPath() {

		Intent intent = getIntent();
		path = intent.getExtras().getString("PATH").toString();

	}

	void openFile() {

		try {
			fis = new FileInputStream(path);
			buff = new BufferedReader(new InputStreamReader(fis));
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}

	class ReadText extends AsyncTask<String, String, String> {
		protected void onPreExecute() {
		}

		protected String doInBackground(String ... values) {

			String temp;
			int i = 0;

			try {
				while((temp = buff.readLine()) != null) {
					if(i > 3) {
						splitTalk(temp);
					} else {
						i++;
					}

				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}

			return "";
		}

		void splitTalk(String talkLine) {
			String parseTalk[];
			String parseTalk2[];

			parseTalk = talkLine.split(", ");	// 날짜 parsing
			if(parseTalk[0].equals("")) {

			} else if(parseTalk.length == 1) {
				if(parseTalk[0].length() <= 1) {
					publishProgress(parseTalk[0], "" + typeTalk);
				}
				else if(parseTalk[0].substring(0, 2).equals("20")) {
					publishProgress(parseTalk[0], "" + 2);	
				} else {
					publishProgress(parseTalk[0], "" + typeTalk);
				}

			}
			else {
				if(parseTalk[0].substring(0, 2).equals("20"))
				{
					parseTalk2 = parseTalk[1].split(" : ");

					// typeTalk 0 -> 내 대화
					// typeTalk 1 -> 상대방 대화
					// typeTalk 2 -> 그 외 대화

					if(parseTalk2[0].equals("회원님")) {

						publishProgress(parseTalk2[1], ""+0);

						typeTalk = 0;
					} else {

						if(parseTalk2.length == 1) {
							publishProgress(parseTalk2[0], ""+2);
							typeTalk = 2;

						} else {
							publishProgress(parseTalk2[1], ""+1);
							typeTalk = 1;
						}
					}
				} else {
					publishProgress(talkLine, ""+typeTalk);
				}
			}
		}

		protected void onProgressUpdate(String ... values) {

			TextView textView = new TextView(getApplicationContext());
			textView.setText(values[0]);

			if(values[1].equals("0")) {				// 내 대화
				textView.setGravity(Gravity.END);
				textView.setTextColor(Color.parseColor("#5CD1E5"));
			} else if(values[1].equals("1")){		// 상대방 대화
				textView.setTextColor(Color.parseColor("#F361A6"));
			} else {
				textView.setGravity(Gravity.CENTER);
				textView.setTextColor(Color.parseColor("#ffffff"));
				textView.setBackgroundColor(Color.parseColor("#8C8C8C"));
			}

			LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);

			linearLayout.addView(textView, p);

		}

		protected void onPostExecute(String result) {
			Log.v("wkdgusdn3", "isLoading=false");
			isLoading = false;
		}
	}
}
