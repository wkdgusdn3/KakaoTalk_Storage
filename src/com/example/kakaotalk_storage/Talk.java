package com.example.kakaotalk_storage;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Talk extends ActionBarActivity {

	private FileInputStream fis;
	private BufferedReader buff;
	private String totalTalk = "";
	private String arr[];
	private String myTalk = "";
	private String yourTalk = "";
	private int typeTalk = 0;	// 0->내대화, 1->상대대화
	private String path;
	private Handler handler;
	private String talk_tmp;
	private int talkType_tmp;

	private LinearLayout linearLayout;
	private TextView talk;
	private ImageView loading;

	TextView textView_tmp;
	LinearLayout.LayoutParams p_tmp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.talk);

		handler = new Handler();

		setView();

		loading.setVisibility(View.VISIBLE);

		getPath();
		openFile();		

		Thread read_Thread = new Thread() {
			public void run() {
				readText();		
			}
		};

		read_Thread.start();
	}

	void setView() {
		talk = new TextView(this);
		linearLayout = (LinearLayout)findViewById(R.id.linearLayout);
		loading = (ImageView)findViewById(R.id.loading);
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

	void readText() {

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

		handler.post(new Runnable() {
			public void run() {
				loading.setVisibility(View.INVISIBLE);	
			}
		});
	}

	void splitTalk(String talkLine) {
		String parseTalk[];
		String parseTalk2[];

		parseTalk = talkLine.split(", ");	// 날짜 parsing
		if(parseTalk[0].equals("")) {

		} else if(parseTalk.length == 1) {
			if(parseTalk[0].length() <= 1) {
				addTalk(parseTalk[0], typeTalk);
			}
			else if(parseTalk[0].substring(0, 2).equals("20")) {
				addTalk(parseTalk[0], 2);	
			} else {
				addTalk(parseTalk[0], typeTalk);
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

					addTalk(parseTalk2[1], 0);

					typeTalk = 0;
				} else {

					if(parseTalk2.length == 1) {
						addTalk(parseTalk2[0], 2);
						typeTalk = 2;

					} else {
						addTalk(parseTalk2[1], 1);
						typeTalk = 1;
					}
				}
			} else {
				addTalk(talkLine, typeTalk);
			}
		}
	}

	void addTalk(String talk, int talkType) {
		talk_tmp = talk;
		talkType_tmp = talkType;
		Log.v("wkdgusdn3", "☆" + talk_tmp);

		TextView textView = new TextView(getApplicationContext());
		textView.setText(talk_tmp);
		LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		if(talkType_tmp == 0) {				// 내 대화
			textView.setGravity(Gravity.END);
			textView.setTextColor(Color.parseColor("#5CD1E5"));
		} else if(talkType_tmp == 1){		// 상대방 대화
			textView.setTextColor(Color.parseColor("#F361A6"));
		} else {
			textView.setGravity(Gravity.CENTER);
			textView.setTextColor(Color.parseColor("#ffffff"));
			textView.setBackgroundColor(Color.parseColor("#8C8C8C"));
		}
		
		textView_tmp = textView;
		p_tmp = p;
		
		handler.post(new Runnable() {
			public void run() {
				Log.v("wkdgusdn3", talk_tmp);
				linearLayout.addView(textView_tmp, p_tmp);		
			}
		});
	}
}