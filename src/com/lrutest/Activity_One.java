package com.lrutest;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * http://blog.csdn.net/guolin_blog/article/details/9316683<br/> 根据输入的宽高进行缩放图片
 * 
 * @author admin
 * 
 */
public class Activity_One extends Activity {

	private ImageView img;
	private EditText tv_width, tv_height;
	private Button btn_confirm;
	private int width, height;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_one);
		// 应用最大可用内存
		int maxMemory = (int) (Runtime.getRuntime().maxMemory() / (1024 * 1024));
		img = (ImageView) findViewById(R.id.img_cover);
		tv_width = (EditText) findViewById(R.id.tv_width);
		tv_height = (EditText) findViewById(R.id.tv_height);
		btn_confirm = (Button) findViewById(R.id.btn_confirm);
		btn_confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				width = Integer.valueOf(tv_width.getText().toString());
				height = Integer.valueOf(tv_height.getText().toString());
				img.setImageBitmap(decodeSampledBitmapFromResource(getResources(), R.drawable.myimage, width, height));
			}
		});
		// 获取应用可以使用的最大内存
		Log.i("TAG", "Max memory is " + maxMemory + "MB");
		// 下面的代码非常简单地将任意一张图片压缩成100*100的缩略图，并在ImageView上展示。
	}

	/**
	 * 先解析一次获取图片宽高和inSampleSize的值,再解析一次对图片进行压缩
	 * 
	 * @param res
	 * @param resId
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
		// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
		// options.inJustDecodeBounds设置为true时系统并不会为bitmap分配内存,decodeResource返回的结果为null
		// 但是可以获得图片的height,width,mimeType
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);
		// 调用上面定义的方法计算inSampleSize值
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
		// 使用获取到的inSampleSize值再次解析图片
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}

	/**
	 * 根据传入期望的宽度和高度计算inSampleSize的值
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// 源图片的高度和宽度
		final int height = options.outHeight;
		final int width = options.outWidth;
		Log.i("TAG", "图片的宽高->宽:" + width + " 高:" + height);
		Log.i("TAG", "需求的宽高->宽:" + reqWidth + " 高:" + reqHeight);
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			// 计算出实际宽高和目标宽高的比率
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			// 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
			// 一定都会大于等于目标的宽和高。
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		Log.i("TAG", "压缩比率:" + inSampleSize);
		return inSampleSize;
	}

}
