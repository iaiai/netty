package org.iaiai.netty.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.iaiai.netty.util.ServiceUtil;

public class NetStatReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		ServiceUtil.startService(context);
	}

}
