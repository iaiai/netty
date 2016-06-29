package org.iaiai.netty.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.iaiai.netty.util.ServiceUtil;

/**
 * 屏幕状态广播
 * 
 * @author iaiai(QQ:176291935)
 * 
 */
public class ScreenStatusReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		ServiceUtil.startService(context);
	}

}
