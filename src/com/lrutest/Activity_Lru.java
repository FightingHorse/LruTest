package com.lrutest;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

/**
 * LruTest http://blog.csdn.net/guolin_blog/article/details/9316683
 * 
 * @author admin
 * 
 */
public class Activity_Lru extends Activity implements OnClickListener {

	private LruCache<String, Bitmap> lruCache;
	private Button btn_confirm;
	private ImageView imgView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lru);
		btn_confirm = (Button) findViewById(R.id.btn_confirm);
		imgView = (ImageView) findViewById(R.id.img_cover);
		btn_confirm.setOnClickListener(this);
		// 获取到可用内存的最大值，使用内存超出这个值会引起OutOfMemory异常。
		// LruCache通过构造函数传入缓存值，以KB为单位。
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheSize = maxMemory / 8;

		lruCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				// 重写此方法来衡量每张图片的大小，默认返回图片数量。
				return bitmap.getByteCount() / 1024;
			}

		};
	}

	/**
	 * 添加一个图片到Lru队列
	 * 
	 * @param key
	 * @param bitmap
	 */
	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemCache(key) == null) {
			lruCache.put(key, bitmap);
		}
	}

	/**
	 * 从Lru队列中获取一个图片
	 * 
	 * @param key
	 * @return
	 */
	public Bitmap getBitmapFromMemCache(String key) {
		return lruCache.get(key);
	}

	public void loadBitmap(int resId, ImageView imageView) {
		final String imageKey = String.valueOf(resId);
		final Bitmap bitmap = getBitmapFromMemCache(imageKey);
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
		} else {
			imageView.setImageResource(R.drawable.myimage);
			BitmapWorkerTask task = new BitmapWorkerTask(imageView);
			task.execute(resId);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_confirm:
			loadBitmap(R.drawable.lru, imgView);
			break;

		default:
			break;
		}
	}

	/**
	 * 使用异步进行的加载图片
	 * 
	 * @author admin
	 * 
	 */
	class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
		ImageView imgview;

		public BitmapWorkerTask(ImageView imageView) {
			imgview = imageView;
		}

		// 在后台加载图片。
		@Override
		protected Bitmap doInBackground(Integer... params) {
			final Bitmap bitmap = Activity_One.decodeSampledBitmapFromResource(getResources(), params[0], 100, 100);
			addBitmapToMemoryCache(String.valueOf(params[0]), bitmap);
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			imgview.setImageBitmap(result);
		}
	}
}
