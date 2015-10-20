package com.lrutest.disklru;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewTreeObserver;
import android.widget.GridView;

import com.lrutest.Images;
import com.lrutest.R;

public class Activity_DiskLruCache extends Activity {

	/**
	 * 用于展示照片墙的GridView
	 */
	private GridView mPhotoWall;

	/**
	 * GridView的适配器
	 */
	private PhotoWallAdapter mAdapter;

	/**
	 * 每一张图片的宽度
	 */
	private int mImageThumbSize;
	/**
	 * 两张图片之间的间隙
	 */
	private int mImageThumbSpacing;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_disklru);

		mImageThumbSize = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_size);
		mImageThumbSpacing = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_spacing);

		mPhotoWall = (GridView) findViewById(R.id.photo_wall);
		mAdapter = new PhotoWallAdapter(this, 0, Images.imageUrls, mPhotoWall);
		mPhotoWall.setAdapter(mAdapter);

		mPhotoWall.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

			@SuppressWarnings("deprecation")
			@Override
			public void onGlobalLayout() {
				/**
				 * 照片墙的列数
				 */
				final int numColumns = (int) Math.floor(mPhotoWall.getWidth() / (mImageThumbSize + mImageThumbSpacing));
				if (numColumns > 0) {
					/**
					 * 每一列的宽度
					 */
					int columnWidth = (mPhotoWall.getWidth() / numColumns) - mImageThumbSpacing;
					// 长宽一样
					mAdapter.setItemHeight(columnWidth);
					mPhotoWall.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				}
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		mAdapter.fluchCache();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 退出程序时结束所有的下载任务
		mAdapter.cancelAllTasks();
	}
}
