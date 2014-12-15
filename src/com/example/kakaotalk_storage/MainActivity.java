package com.example.kakaotalk_storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

	TextView talkNumber;
	ListView listView;

	ArrayAdapter<Spanned> fileList;
	String name;
	String[] chatPath = new String[100];
	int index = 0;
	int talkCount = 0;
	int select;
	Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		setView();
		setFileList();
		setListViewOnClickListener();
		setTalkNumber();
	}

	void setView() {
		talkNumber = (TextView)findViewById(R.id.talkNumber);
		listView = (ListView)findViewById(R.id.listView);
		fileList = new ArrayAdapter<Spanned>(this, R.layout.file_list);
	}

	void setTalkNumber() {
		SpannableStringBuilder sp = new SpannableStringBuilder("" + talkCount + "개의 대화가 있습니다.");
		sp.setSpan(new ForegroundColorSpan(Color.RED), 0, calTalkNumber(talkCount), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		talkNumber.append(sp);
	}

	int calTalkNumber(int num) {
		if(num / 10 == 0) {
			return 3;
		} else if(num / 100 == 0) {
			return 4;
		} else if(num / 1000 == 0) {
			return 5;
		} else {
			return 0;
		}
	}

	void setFileList() {
		String path = Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/KakaoTalk/Chats";

		File files = new File(path);
		if(files.listFiles().length > 0)
		{
			for(File file : files.listFiles())
			{
				openFolder(path + "/" + file.getName());
				if(name != null) {
					fileList.add(Html.fromHtml("<FONT color=" +"#00a2d5"+">" + name + "</FONT>" + " 님과의 대화"));
					talkCount++;
				}
			}
		}

		listView.setAdapter(fileList);

	}

	void openFolder(String path) {

		File files = new File(path);

		if(files.listFiles().length > 0)
		{
			for(File file : files.listFiles())
			{
				if(file.getName().toLowerCase().endsWith(".txt")) {
					openFile(path + "/" + file.getName());
					chatPath[index] = path + "/" + file.getName();
					index++;
				}
			}
		}
	}

	void openFile(String path) {

		FileInputStream fis;
		BufferedReader buff;

		try {
			fis = new FileInputStream(path);
			buff = new BufferedReader(new InputStreamReader(fis));
			readName(buff);
		}
		catch(IOException e) {
			e.printStackTrace();
		}


	}

	void readName(BufferedReader buff) {
		String temp;

		try {
			temp = buff.readLine();
			name = temp.split(" ")[0];
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	void setListViewOnClickListener() {


		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {


				Intent talk = new Intent(getApplicationContext(), Talk.class);
				talk.putExtra("PATH", chatPath[arg2]);
				startActivity(talk);

			}
		});
	}
}
