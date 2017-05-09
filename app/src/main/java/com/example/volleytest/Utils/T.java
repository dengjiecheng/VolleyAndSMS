package com.example.volleytest.Utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast统一管理类
 * 
 */
public class T {

	private T() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	private static boolean debugShow = true;
	private static boolean isShow = true;

	public static final int notShowInterval = 2 * 1000;

	private static LastTip mLastToastTip = null;

	/***
	 * 检查是否是上次显示的内容，时间间隔是否为 notShowInterval，true
	 * 为上次显示的内容而且时间间隔为notShowInterval内；
	 *
	 * @param message
	 * @return
	 */
	public static boolean isLastToastMsg(String message) {
		if (StringUtils.isEmpty(message)) {
			return false;
		}
		if (mLastToastTip != null) {
			String lastcontent = mLastToastTip.getMessage();
			long lastTtime = mLastToastTip.getLastToastTime();
			if (StringUtils.isNotEmpty(lastcontent)
					&& lastcontent.equalsIgnoreCase(message)
					&& (System.currentTimeMillis() - lastTtime) <= notShowInterval) {
				return true;
			}
		}
		if (mLastToastTip == null) {
			mLastToastTip = new LastTip(message, System.currentTimeMillis());
		} else {
			mLastToastTip.setMessage(message);
			mLastToastTip.setLastToastTime(System.currentTimeMillis());
		}
		return false;
	}

	/**
	 * 短时间显示Toast
	 * 
	 * @param context
	 * @param message
	 */
	public static void showDebugInfo(Context context, CharSequence message) {
		if (debugShow && AppUtils.isApkDebugable(context)) {
			Toast.makeText(context.getApplicationContext(), message,
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 短时间显示Toast
	 * 
	 * @param context
	 * @param message
	 */
	public static void showShort(Context context, CharSequence message) {
		if (isShow) {
			if (!isLastToastMsg(message.toString())) {
				Toast.makeText(context.getApplicationContext(), message,
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	/**
	 * 短时间显示Toast
	 * 
	 * @param context
	 */
	public static void showShort(Context context, int resId) {
		if (isShow)
			if (!isLastToastMsg(context.getString(resId))) {
				Toast.makeText(context.getApplicationContext(), resId,
						Toast.LENGTH_SHORT).show();
			}
	}

	/**
	 * 短时间显示Toast
	 * 
	 * @param context
	 * @param resId
	 */
	public static void showShortRes(Context context, int resId) {
		if (isShow)
			if (!isLastToastMsg(context.getString(resId))) {
				Toast.makeText(context.getApplicationContext(),
						context.getResources().getString(resId),
						Toast.LENGTH_SHORT).show();
			}
	}

	/**
	 * 长时间显示Toast
	 * 
	 * @param context
	 * @param message
	 */
	public static void showLong(Context context, CharSequence message) {
		if (isShow)
			if (!isLastToastMsg(message.toString())) {
				Toast.makeText(context.getApplicationContext(), message,
						Toast.LENGTH_LONG).show();
			}
	}

	/**
	 * 长时间显示Toast
	 * 
	 * @param context
	 * @param message
	 */
	public static void showLong(Context context, int resId) {
		if (isShow)
			if (!isLastToastMsg(context.getString(resId))) {
				Toast.makeText(context.getApplicationContext(), resId,
						Toast.LENGTH_LONG).show();
			}
	}

	/**
	 * 长时间显示Toast
	 * 
	 * @param context
	 * @param resId
	 */
	public static void showLongRes(Context context, int resId) {
		if (isShow)
			if (!isLastToastMsg(context.getString(resId))) {
				Toast.makeText(context.getApplicationContext(),
						context.getResources().getString(resId),
						Toast.LENGTH_LONG).show();
			}
	}

	/**
	 * 自定义显示Toast时间
	 * 
	 * @param context
	 * @param message
	 * @param duration
	 */
	public static void show(Context context, CharSequence message, int duration) {
		if (isShow)
			if (!isLastToastMsg(message.toString())) {
				Toast.makeText(context.getApplicationContext(), message,
						duration).show();
			}
	}

	/**
	 * 自定义显示Toast时间
	 * 
	 * @param context
	 * @param message
	 * @param duration
	 */
	public static void show(Context context, int resId, int duration) {
		if (isShow)
			if (!isLastToastMsg(context.getString(resId))) {
				Toast.makeText(context.getApplicationContext(), resId, duration)
						.show();
			}
	}

	/**
	 * 自定义显示Toast时间
	 * 
	 * @param context
	 * @param resId
	 * @param duration
	 */
	public static void showRes(Context context, int resId, int duration) {
		if (isShow)
			if (!isLastToastMsg(context.getString(resId))) {
				Toast.makeText(context.getApplicationContext(),
						context.getResources().getString(resId), duration)
						.show();
			}
	}

	public static class LastTip {
		private String message;
		private long lastToastTime;

		public LastTip(String message, long lastToastTime) {
			this.message = message;
			this.lastToastTime = lastToastTime;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public long getLastToastTime() {
			return lastToastTime;
		}

		public void setLastToastTime(long lastToastTime) {
			this.lastToastTime = lastToastTime;
		}
	}
}
