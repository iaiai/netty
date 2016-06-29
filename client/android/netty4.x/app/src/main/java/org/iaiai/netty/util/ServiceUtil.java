package org.iaiai.netty.util;

import android.content.Context;
import android.content.Intent;

import org.iaiai.netty.NettyService;

/**
 * Created by iaiai(QQ:176291935) on 16/6/20.
 */
public final class ServiceUtil {

    /**
     * 判断是否启动此service
     * @param context
     */
    public final static void startService(Context context){
        if(!PhoneUtil.isServiceRunning(context, NettyService.class.getName())){
            Intent intent = new Intent(context,NettyService.class);
            context.startService(intent);
        }
    }

}
