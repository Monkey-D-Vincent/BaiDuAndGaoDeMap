package com.xianzhi.map.gaode.util.util;

import android.content.res.Resources;
import android.graphics.Point;
import android.util.DisplayMetrics;

/**
 * @author liMing
 * @Demo class DisplayUtil
 * @Description TODO
 * @date 2019-11-07 15:33
 */
public class DisplayUtil {

	/**
	 * 屏幕宽度，横向像素点个数
	 */
	private int mScreenWidth;
	/**
	 * 屏幕高度，纵向像素点个数
	 */
	private int mScreenHeight;

	/**
	 * 屏幕分辨率
	 */
	private Point mScreenResolution;

	/**
	 * 屏幕密度,dots-per-inch
	 */
	private int mDensityDpi;
	/**
	 * 缩放系数
	 */
	private float mScaleFactor;
	private float mFontScaleFactor;
	private float mXdpi;
	private float mYdpi;
	private static DisplayUtil mDisplayUtil;

	private DisplayUtil() {
		DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();
		mScreenResolution = new Point(dm.widthPixels, dm.heightPixels);
		mScreenWidth = dm.widthPixels;
		mScreenHeight = dm.heightPixels;
		mDensityDpi = dm.densityDpi;
		mXdpi = dm.xdpi;
		mYdpi = dm.ydpi;
		mScaleFactor = dm.density;
		mFontScaleFactor = dm.scaledDensity;
	}

	private static void getInstance() {
		if (mDisplayUtil == null) {
			synchronized (DisplayUtil.class) {
				if (mDisplayUtil == null) {
					mDisplayUtil = new DisplayUtil();
				}
			}
		}
	}

	public static int dp2px(float dpValue) {
		if (mDisplayUtil == null) {
			getInstance();
		}
		return (int) (mDisplayUtil.mScaleFactor * dpValue + 0.5f);
	}
}
