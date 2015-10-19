package com.lrutest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 主界面
 * 
 * @author admin
 * 
 */
public class MainActivity extends Activity implements OnClickListener {

	private Button btn_activity_one, btn_lru, btn_photo_wall;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btn_activity_one = (Button) findViewById(R.id.btn_activity_one);
		btn_lru = (Button) findViewById(R.id.btn_lru);
		btn_photo_wall = (Button) findViewById(R.id.btn_photo_wall);
		btn_lru.setOnClickListener(this);
		btn_activity_one.setOnClickListener(this);
		btn_photo_wall.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.btn_activity_one:// 简单的缩放
			intent.setClass(this, Activity_One.class);
			break;

		case R.id.btn_lru:// 使用Lru的单个加载
			intent.setClass(this, Activity_Lru.class);
			break;
		case R.id.btn_photo_wall:// 使用Lru的单个加载
			intent.setClass(this, Activity_Photo_Wall.class);
			break;
		default:
			break;
		}
		startActivity(intent);
	}

}
