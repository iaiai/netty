package org.iaiai.netty.util;

import android.app.ActivityManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * <br/>
 * Title: PhoneUtil.java<br/>
 * E-Mail: 176291935@qq.com<br/>
 * QQ: 176291935<br/>
 * Http: iaiai.iteye.com<br/>
 * Create time: 2013-1-23 下午5:24:47<br/>
 * <br/>
 * 
 * @author 丸子
 * @version 0.0.1
 */
public class PhoneUtil {

	private PhoneUtil(){
		/** cannot be instantiated **/
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	/**
	 * 获取手机号，有可能取不到
	 * 
	 * @return
	 */
	public final static String getPhone(Context context) {
		String phoneNumber = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getLine1Number();
		if (phoneNumber != null && phoneNumber.equals("0")) {
			phoneNumber = null;
		}
		return phoneNumber;
	}

	/**
	 * 获取imei
	 * 
	 * @return
	 */
	public final static String getImei(Context context) {
		return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
	}

	/**
	 * 获取imsi
	 * 
	 * @return
	 */
	public final static String getImsi(Context context) {
		return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();
	}
	
	/**
	 * 获取系统版本
	 * @return
	 */
	public final static String getOs(){
		return "Android "+Build.VERSION.RELEASE;
	}
	
	/**
	 * 获取手机型号
	 * @return
	 */
	public final static String getModel(){
		return Build.MODEL;
	}


	/**
	 * 判断是否开启网络
	 *
	 * @return true有网络，false无网络
	 */
	public final static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 手机号正则验证
	 * @param phoneNumber
	 * @return
     */
	public static boolean checkPhoneNumber(String phoneNumber){
		Pattern pattern=Pattern.compile("^1[0-9]{10}$");
		Matcher matcher=pattern.matcher(phoneNumber);
		return matcher.matches();
	}

	//判断email格式是否正确
	public  static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);

		return m.matches();
	}


	/**
	 * 用来判断服务是否运行.
	 * @param mContext
	 * @param className 判断的服务名字
	 * @return true 在运行 false 不在运行
	 */
	public static boolean isServiceRunning(Context mContext,String className) {
		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager)
				mContext.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList
				= activityManager.getRunningServices(30);
		if (!(serviceList.size()>0)) {
			return false;
		}
		for (int i=0; i<serviceList.size(); i++) {
			if (serviceList.get(i).service.getClassName().equals(className) == true) {
				isRunning = true;
				break;
			}
		}
		return isRunning;
	}
}
