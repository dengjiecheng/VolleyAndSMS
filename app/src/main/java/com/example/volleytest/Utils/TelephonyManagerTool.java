package com.example.volleytest.Utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.BatteryManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

/**
 * 获取手机的一些基本信息，比如生产商家、固件版本、手机型号、手机号码、屏幕分辨率等
 * */
public class TelephonyManagerTool {

	private static final String TAG = "TelephonyManagerTool";

	/**
	 * 生产商家
	 * 
	 * @return
	 */
	public static String getManufacturer() {
		return android.os.Build.MANUFACTURER;
	}

	/**
	 * 获得固件版本
	 * 
	 * @return
	 */
	public static String getRelease() {
		return android.os.Build.VERSION.RELEASE;
	}

	/**
	 * 获取设备的系统版本号
	 */
	public static int getDeviceSDK() {
		int sdk = android.os.Build.VERSION.SDK_INT;
		return sdk;
	}

	/**
	 * 获取手机系统版本
	 */
	public static int getAndroidOSVersion() {
		int osVersion;
		try {
			osVersion = Integer.parseInt(android.os.Build.VERSION.SDK);
		} catch (NumberFormatException e) {
			osVersion = 0;
		}
		return osVersion;
	}

	/**
	 * 获取设备的型号
	 */
	public static String getDeviceName() {
		String model = android.os.Build.MODEL;
		return model;
	}

	/**
	 * 获得手机型号
	 * 
	 * @return
	 */
	public static String getModel() {
		return android.os.Build.MODEL;
	}

	/**
	 * 获得手机品牌
	 * 
	 * @return
	 */
	public static String getBrand() {
		return android.os.Build.BRAND;
	}

	/**
	 * 获取手机运营商
	 */
	public static String getSimOperatorName(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);

		return tm.getSimOperatorName();
	}

	/**
	 * 得到本机手机号码,未安装SIM卡或者SIM卡中未写入手机号，都会获取不到
	 * 
	 * @return
	 */
	public static String getPhoneNumber(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String number = tm.getLine1Number();
		return number;
	}

	/**
	 * 是否是电话号码
	 * 
	 * @param phonenumber
	 * @return
	 */
//	public static boolean isPhoneNumber(String phonenumber) {
//		if (phonenumber == null) {
//			return false;
//		}
//		String regExp = "^[1]([3][0-9]{1}|59|58|88|89)[0-9]{8}$";
//		Pattern pa = Pattern.compile(regExp);
//		Matcher ma = pa.matcher(phonenumber);
//		return ma.matches();
//	}

	/**
	 * 打电话
	 * 
	 * @param phone
	 * @param context
	 */
	public static void doPhone(Context context, String phone) {
		Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
				+ phone));
		context.startActivity(phoneIntent);
	}

	/**
	 * 发短信
	 * 
	 * @param phone
	 * @param content
	 */
	public static void doSMS(Context context, String phone, String content) {
		Uri uri = null;
		if (!TextUtils.isEmpty(phone))
			uri = Uri.parse("smsto:" + phone);
		Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
		intent.putExtra("sms_body", content);
		context.startActivity(intent);
	}

	/**
	 * 调用浏览器打开
	 *
	 * @param url
	 */
	public static void openWeb(Context context, String url) {
		Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);

	}

	/**
	 * 获取应用程序的IMEI号
	 */
	public static String getIMEI(Context context) {
		if (context == null) {
			Log.e(TAG, "getIMEI  context为空");
		}
		TelephonyManager telecomManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = telecomManager.getDeviceId();
		return imei;
	}
}
