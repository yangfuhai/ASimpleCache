package com.yangfuhai.asimplecachedemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SaveJsonActivity extends Activity {

	private Button btn_save_string;
	private Button btn_save_json;
	private Button btn_save_bitmap;
	private Button btn_save_object;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		btn_save_string = (Button) findViewById(R.id.btn_save_string);
		btn_save_json = (Button) findViewById(R.id.btn_save_json);
		btn_save_bitmap = (Button) findViewById(R.id.btn_save_bitmap);
		btn_save_object = (Button) findViewById(R.id.btn_save_object);
		
		btn_save_string.setOnClickListener(mListner);
		btn_save_json.setOnClickListener(mListner);
		btn_save_bitmap.setOnClickListener(mListner);
		btn_save_object.setOnClickListener(mListner);
	}
	
	
	
	private OnClickListener mListner = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(v == btn_save_string){
				
			}else if(v == btn_save_json){
				
			}else if(v == btn_save_bitmap){
				
			}else if(v == btn_save_object){
				
			}
		}
	};


}
