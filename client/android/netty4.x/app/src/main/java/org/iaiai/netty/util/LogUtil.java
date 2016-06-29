package org.iaiai.netty.util;

import android.util.Log;

import org.iaiai.netty.NettyConnectionProperty;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.netty.channel.Channel;

/**
 * Created by iaiai(QQ:176291935) on 16/6/17.
 */
public final class LogUtil {

    //是否写入日志文件中
    private final static boolean isInfoFile = false;

    //是否写入日志文件中
    private final static boolean isErrorFile = false;

    //是否写入日志文件中
    private final static boolean isFile = false;

    //前缀
    private final static String PREFIX = "[xz_netty4.x]";

    private final static String getDateTime(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
    }

    /**
     * 日志输出
     * @param className
     * @param channel
     * @param identify
     * @param msg
     */
    public final static void i(String className,Channel channel, String identify, String msg){
        Log.i(PREFIX, String.format("%s - [info]------------------------",getDateTime()));
        Log.i(PREFIX, String.format("%s - [identify]:%s",getDateTime(),identify));
        if(channel!=null) {
            Log.i(PREFIX, String.format("%s - [ip]:%s",getDateTime(),channel.remoteAddress()));
            Log.i(PREFIX, String.format("%s - [channel id]:%s",getDateTime(),channel.id()));
            Log.i(PREFIX, String.format("%s - [session id]:%s",getDateTime(), NettyConnectionProperty.getSession(channel)));
        }

        if(msg!=null)Log.i(PREFIX, String.format("%s - [info]:%s",getDateTime(),msg));

        //是否写入到日志文件中
        if(isInfoFile){
            f(className,channel,identify,msg);
        }
    }

    /**
     * 日志输出
     * @param className
     * @param channel
     * @param identify
     * @param msg
     */
    public final static void e(String className,Channel channel, String identify, String msg){
        Log.e(PREFIX, String.format("%s - [info]------------------------", getDateTime()));
        Log.e(PREFIX, String.format("%s - [identify]:%s",getDateTime(),identify));
        if(channel!=null) {
            Log.e(PREFIX, String.format("%s - [ip]:%s",getDateTime(),channel.remoteAddress()));
            Log.e(PREFIX, String.format("%s - [channel id]:%s",getDateTime(),channel.id()));
            Log.e(PREFIX, String.format("%s - [session id]:%s",getDateTime(), NettyConnectionProperty.getSession(channel)));
        }

        if(msg!=null)Log.e(PREFIX, String.format("%s - [info]:%s",getDateTime(),msg));

        //是否写入到日志文件中
        if(isErrorFile){
            f(className,channel,identify,msg);
        }
    }

    /**
     * 直接输出到日志文件中
     * @param className
     * @param channel
     * @param identify
     * @param msg
     */
    public final static void f(String className,Channel channel, String identify, String msg){

    }

    /**
     * 调用i输出并且再调用f输出
     * @param className
     * @param channel
     * @param identify
     * @param msg
     */
    public final static void _if(String className,Channel channel, String identify, String msg){
        Log.i(PREFIX, String.format("%s - [info]------------------------", getDateTime()));
        Log.i(PREFIX, String.format("%s - [identify]:%s",getDateTime(),identify));
        if(channel!=null) {
            Log.i(PREFIX, String.format("%s - [ip]:%s",getDateTime(),channel.remoteAddress()));
            Log.i(PREFIX, String.format("%s - [channel id]:%s",getDateTime(),channel.id()));
            Log.i(PREFIX, String.format("%s - [session id]:%s",getDateTime(),NettyConnectionProperty.getSession(channel)));
        }

        if(msg!=null)Log.i(PREFIX, String.format("%s - [info]:%s",getDateTime(),msg));

        //写入到日志文件中
        f(className, channel, identify, msg);
    }

}
