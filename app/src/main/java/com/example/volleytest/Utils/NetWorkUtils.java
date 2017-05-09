package com.example.volleytest.Utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * 网络工具类
 * 
 */
public class NetWorkUtils {

	private static String LOG_TAG = "NetWorkHelper";

	public static Uri uri = Uri.parse("content://telephony/carriers");

	/**
	 * 判断是否有网络连接
	 */
	// public static boolean isNetworkAvailable(Context context) {
	// try {
	// return isWifiDataEnable(context)||isMobileDataEnable(context);
	// } catch (Exception e) {
	// // TODO: handle exception
	// e.printStackTrace();
	// return false;
	// }
	// }
	/**
	 * 判断网络连接是否打开,包括移动数据连接
	 * 
	 * @param context
	 *            上下文
	 * @return 是否联网
	 */
	public static boolean isNetworkAvailable(Context context) {
		boolean netstate = false;
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {

			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == State.CONNECTED) {
						netstate = true;
						break;
					}
				}
			}
		}
		return netstate;
	}

	/**
	 * 判断网络是否为漫游
	 */
	public static boolean isNetworkRoaming(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			Log.w(LOG_TAG, "couldn't get connectivity manager");
		} else {
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info != null
					&& info.getType() == ConnectivityManager.TYPE_MOBILE) {
				TelephonyManager tm = (TelephonyManager) context
						.getSystemService(Context.TELEPHONY_SERVICE);
				if (tm != null && tm.isNetworkRoaming()) {
					Log.d(LOG_TAG, "network is roaming");
					return true;
				} else {
					Log.d(LOG_TAG, "network is not roaming");
				}
			} else {
				Log.d(LOG_TAG, "not using mobile network");
			}
		}
		return false;
	}

	/**
	 * 判断MOBILE网络是否可用
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static boolean isMobileDataEnable(Context context) throws Exception {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		boolean isMobileDataEnable = false;

		isMobileDataEnable = connectivityManager.getNetworkInfo(
				ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();

		return isMobileDataEnable;
	}

	/**
	 * 判断wifi 是否可用
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static boolean isWifiDataEnable(Context context) throws Exception {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		boolean isWifiDataEnable = false;
		isWifiDataEnable = connectivityManager.getNetworkInfo(
				ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
		return isWifiDataEnable;
	}

	// /**
	// * 设置Mobile网络开关
	// *
	// * @param context
	// * @param enabled
	// * @throws Exception
	// */
	// public static void setMobileDataEnabled(Context context, boolean enabled)
	// throws Exception {
	// APNManager apnManager=APNManager.getInstance(context);
	// List<APN> list = apnManager.getAPNList();
	// if (enabled) {
	// for (APN apn : list) {
	// ContentValues cv = new ContentValues();
	// cv.put("apn", apnManager.matchAPN(apn.apn));
	// cv.put("type", apnManager.matchAPN(apn.type));
	// context.getContentResolver().update(uri, cv, "_id=?",
	// new String[] { apn.apnId });
	// }
	// } else {
	// for (APN apn : list) {
	// ContentValues cv = new ContentValues();
	// cv.put("apn", apnManager.matchAPN(apn.apn) + "mdev");
	// cv.put("type", apnManager.matchAPN(apn.type) + "mdev");
	// context.getContentResolver().update(uri, cv, "_id=?",
	// new String[] { apn.apnId });
	// }
	// }
	// }

	// public static boolean isNetWorkAvailble(Context context) throws
	// Exception{
	// return isWifiDataEnable(context)||isMobileDataEnable(context);
	// }

	/**
	 * GPS是否打开
	 * 
	 * @param context
	 *            上下文
	 * @return Gps是否可用
	 */
	public static boolean isGpsEnabled(Context context) {
		LocationManager lm = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

	/**
	 * 只是判断WIFI
	 * 
	 * @param context
	 *            上下文
	 * @return 是否打开Wifi
	 */
	public static boolean isWiFi(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
        return wifi == State.CONNECTED || wifi == State.CONNECTING;

    }

	/**
	 * 检测当前打开的网络类型是否3G
	 * 
	 * @param context
	 *            上下文
	 * @return 是否是3G上网
	 */
	public static boolean is3G(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE;
    }

	/**
	 * 检测当前开打的网络类型是否4G
	 * 
	 * @param context
	 *            上下文
	 * @return 是否是4G上网
	 */
	public static boolean is4G(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null && activeNetInfo.isConnectedOrConnecting()) {
			if (activeNetInfo.getType() == TelephonyManager.NETWORK_TYPE_LTE) {
				return true;
			}
		}
		return false;
	}

	/**
	 * IP地址校验
	 * 
	 * @param ip
	 *            待校验是否是IP地址的字符串
	 * @return 是否是IP地址
	 */
	public static boolean isIP(String ip) {
		Pattern pattern = Pattern
				.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
		Matcher matcher = pattern.matcher(ip);
		return matcher.matches();
	}

	/**
	 * IP转化成int数字
	 * 
	 * @param addr
	 *            IP地址
	 * @return Integer
	 */
	public static int ipToInt(String addr) {
		String[] addrArray = addr.split("\\.");
		int num = 0;
		for (int i = 0; i < addrArray.length; i++) {
			int power = 3 - i;
			num += ((Integer.parseInt(addrArray[i]) % 256 * Math
					.pow(256, power)));
		}
		return num;
	}

	/**
	 * 枚举网络状态 NET_NO：没有网络 NET_2G:2g网络 NET_3G：3g网络 NET_4G：4g网络 NET_WIFI：wifi
	 * NET_UNKNOWN：未知网络
	 */
	public enum NetState {
		NET_NO, NET_2G, NET_3G, NET_4G, NET_WIFI, NET_UNKNOWN
	}

    /**
	 * 判断当前是否网络连接
	 * 
	 * @param context
	 *            上下文
	 * @return 状态码
	 */
	public NetState isConnected(Context context) {
		NetState stateCode = NetState.NET_NO;
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni != null && ni.isConnectedOrConnecting()) {
			switch (ni.getType()) {
			case ConnectivityManager.TYPE_WIFI:
				stateCode = NetState.NET_WIFI;
				break;
			case ConnectivityManager.TYPE_MOBILE:
				switch (ni.getSubtype()) {
				case TelephonyManager.NETWORK_TYPE_GPRS: // 联通2g
				case TelephonyManager.NETWORK_TYPE_CDMA: // 电信2g
				case TelephonyManager.NETWORK_TYPE_EDGE: // 移动2g
				case TelephonyManager.NETWORK_TYPE_1xRTT:
				case TelephonyManager.NETWORK_TYPE_IDEN:
					stateCode = NetState.NET_2G;
					break;
				case TelephonyManager.NETWORK_TYPE_EVDO_A: // 电信3g
				case TelephonyManager.NETWORK_TYPE_UMTS:
				case TelephonyManager.NETWORK_TYPE_EVDO_0:
				case TelephonyManager.NETWORK_TYPE_HSDPA:
				case TelephonyManager.NETWORK_TYPE_HSUPA:
				case TelephonyManager.NETWORK_TYPE_HSPA:
				case TelephonyManager.NETWORK_TYPE_EVDO_B:
				case TelephonyManager.NETWORK_TYPE_EHRPD:
				case TelephonyManager.NETWORK_TYPE_HSPAP:
					stateCode = NetState.NET_3G;
					break;
				case TelephonyManager.NETWORK_TYPE_LTE:
					stateCode = NetState.NET_4G;
					break;
				default:
					stateCode = NetState.NET_UNKNOWN;
				}
				break;
			default:
				stateCode = NetState.NET_UNKNOWN;
			}

		}
		return stateCode;
	}

	/**
	 * 获取URL中参数 并返回Map
	 * 
	 * @param url
	 * @return
	 */
	public static Map<String, String> getUrlParams(String url) {
		Map<String, String> map = null;

		if (url != null && url.indexOf("&") > -1 && url.indexOf("=") > -1) {
			map = new HashMap<String, String>();

			String[] arrTemp = url.split("&");
			for (String str : arrTemp) {
				String[] qs = str.split("=");
				map.put(qs[0], qs[1]);
			}
		}

		return map;
	}

	public static String getWiFiIP(Context context) {
		WifiManager wifimanage = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);// 获取WifiManager
		WifiInfo wifiinfo = wifimanage.getConnectionInfo();
		int i = wifiinfo.getIpAddress();
		String IP = (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "."
				+ ((i >> 16) & 0xFF) + "." + ((i >> 24) & 0xFF);
		return IP;
	}

	public static void openWifi(Context context) {
		WifiManager wifimanage = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		// 获取WifiManager    
		// 检查wifi是否开启
		if (!wifimanage.isWifiEnabled()) {
			wifimanage.setWifiEnabled(true);
		}
	}

	public static boolean checkWifiStatic(Context context) {
		WifiManager wifimanage = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);// 获取WifiManager    
		// 检查wifi是否开启
		// ConnectivityManager mCm = (ConnectivityManager)
		// getSystemService(Context.CONNECTIVITY_SERVICE);
		// NetworkInfo networkInfo = mCm
		// .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		// return networkInfo.isConnected();
		return wifimanage.isWifiEnabled();
	}
	
	/**
	 * 得到网络类型，0是未知或未连上网络，1为WIFI，2为2g，3为3g，4为4g
	 * 
	 * @return
	 */
	public static int getNetType(Context context) {
		ConnectivityManager connectMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		int type = 0;
		NetworkInfo info = connectMgr.getActiveNetworkInfo();
		if (info == null || !info.isConnected()) {
			return type;
		}

		switch (info.getType()) {
		case ConnectivityManager.TYPE_WIFI:
			type = 1;
			break;
		case ConnectivityManager.TYPE_MOBILE:
			type = getNetworkClass(info.getSubtype());
			break;

		default:
			type = 0;
			break;
		}

		return type;
	}

	/**
	 * 判断数据连接的类型
	 * 
	 * @param networkType
	 * @return
	 */
	public static int getNetworkClass(int networkType) {
		switch (networkType) {
		case TelephonyManager.NETWORK_TYPE_GPRS:
		case TelephonyManager.NETWORK_TYPE_EDGE:
		case TelephonyManager.NETWORK_TYPE_CDMA:
		case TelephonyManager.NETWORK_TYPE_1xRTT:
		case TelephonyManager.NETWORK_TYPE_IDEN:
		case TelephonyManager.NETWORK_TYPE_UNKNOWN:

			return 2;
		case TelephonyManager.NETWORK_TYPE_UMTS:
		case TelephonyManager.NETWORK_TYPE_EVDO_0:
		case TelephonyManager.NETWORK_TYPE_EVDO_A:
		case TelephonyManager.NETWORK_TYPE_HSDPA:
		case TelephonyManager.NETWORK_TYPE_HSUPA:
		case TelephonyManager.NETWORK_TYPE_HSPA:
		case TelephonyManager.NETWORK_TYPE_EVDO_B:
		case TelephonyManager.NETWORK_TYPE_EHRPD:
			return 3;
		case TelephonyManager.NETWORK_TYPE_LTE:
			return 4;
		default:
			return 0;
		}
	}
}
