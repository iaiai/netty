package org.iaiai.netty.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.iaiai.netty.util.ServiceUtil;

/**
 * Created by iaiai(QQ:176291935) on 16/6/17.
 */
public class BootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ServiceUtil.startService(context);
    }

}
