package com.yangfuhai.asimplecachedemo;

import org.afinal.simplecache.ACache;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SaveStringActivity extends Activity {

	private EditText text;
	private Button btn_save;
	private Button btn_read;
	private TextView textView;
	
	private ACache mCache ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_save_string);
		mCache = ACache.get(this);
		
		text = (EditText) findViewById(R.id.editText1);
		textView = (TextView) findViewById(R.id.textView1);
		btn_read = (Button) findViewById(R.id.btn_read);
		btn_save = (Button) findViewById(R.id.btn_save);
		
		btn_save.setOnClickListener(mListner);
		btn_read.setOnClickListener(mListner);
	}
	
	
	
	private OnClickListener mListner = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if(v == btn_save){
				mCache.put("test_key", text.getText().toString());
			}else if(v == btn_read){
				textView.setText(mCache.getAsString("test_key"));
			}
		}
	};


}
