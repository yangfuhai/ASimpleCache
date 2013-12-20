package com.yangfuhai.asimplecachedemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * 
 * @ClassName: MainActivity
 * @Description: 主界面
 * @Author Yoson Hao
 * @WebSite www.haoyuexing.cn
 * @Email haoyuexing@gmail.com
 * @Date 2013-8-8 下午2:08:47
 * 
 */
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void string(View v) {
		startActivity(new Intent().setClass(this, SaveStringActivity.class));
	}

	public void jsonobject(View v) {
		startActivity(new Intent().setClass(this, SaveJsonObjectActivity.class));
	}

	public void jsonarray(View v) {
		startActivity(new Intent().setClass(this, SaveJsonArrayActivity.class));
	}

	public void bitmap(View v) {
		startActivity(new Intent().setClass(this, SaveBitmapActivity.class));
	}

    public void media(View v) {
        startActivity(new Intent().setClass(this, SaveMediaActivity.class));
    }

	public void drawable(View v) {
		startActivity(new Intent().setClass(this, SaveDrawableActivity.class));
	}

	public void object(View v) {
		startActivity(new Intent().setClass(this, SaveObjectActivity.class));
	}

	public void about(View v) {
		startActivity(new Intent().setClass(this, AboutActivity.class));
	}

}
