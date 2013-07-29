package com.yangfuhai.asimplecachedemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	private Button btn_save_string;
	private Button btn_save_json;
	private Button btn_save_bitmap;
	private Button btn_save_object;
	private Button btn_about;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		btn_save_string = (Button) findViewById(R.id.btn_save_string);
		btn_save_json = (Button) findViewById(R.id.btn_save_json);
		btn_save_bitmap = (Button) findViewById(R.id.btn_save_bitmap);
		btn_save_object = (Button) findViewById(R.id.btn_save_object);
		btn_about = (Button) findViewById(R.id.btn_about);
		
		btn_save_string.setOnClickListener(mListner);
		btn_save_json.setOnClickListener(mListner);
		btn_save_bitmap.setOnClickListener(mListner);
		btn_save_object.setOnClickListener(mListner);
		btn_about.setOnClickListener(mListner);
	}
	
	
	
	private OnClickListener mListner = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(v == btn_save_string){
				Intent intent = new Intent(MainActivity.this, SaveStringActivity.class);
				startActivity(intent);
			}else if(v == btn_save_json){
				Intent intent = new Intent(MainActivity.this, SaveJsonActivity.class);
				startActivity(intent);
			}else if(v == btn_save_bitmap){
				Intent intent = new Intent(MainActivity.this, SaveBitmapActivity.class);
				startActivity(intent);
			}else if(v == btn_save_object){
				Intent intent = new Intent(MainActivity.this, SaveObjectActivity.class);
				startActivity(intent);
			}else if(v == btn_about){
				Intent intent = new Intent(MainActivity.this, AboutActivity.class);
				startActivity(intent);
			}
		}
	};


}
