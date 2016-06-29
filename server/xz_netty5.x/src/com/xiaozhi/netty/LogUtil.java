package com.xiaozhi.netty;

import io.netty.channel.Channel;
import org.apache.logging.log4j.Logger;

/**
 * Created by iaiai(QQ:176291935) on 16/6/17.
 */
public final class LogUtil {

    public final static void error(Logger log, Channel channel, String identify, String msg){
        log.error("[error]---------------------------------");
        log.error(String.format("[ip]:%s",channel.remoteAddress()));
        log.error(String.format("[identify]:%s",identify));
        log.error(String.format("[channel id]:%s",channel.id()));
        log.error(String.format("[session id]:%s", NettyConnectionProperty.getConnectionSessionId(channel)));

        if(msg!=null)log.error(String.format("[info]:%s",msg));
    }

    public final static void info(Logger log, Channel channel, String identify, String msg){
        log.info("[info]---------------------------------");
        log.info(String.format("[ip]:%s",channel.remoteAddress()));
        log.info(String.format("[identify]:%s",identify));
        log.info(String.format("[channel id]:%s",channel.id()));
        log.info(String.format("[session id]:%s", NettyConnectionProperty.getConnectionSessionId(channel)));

        if(msg!=null)log.info(String.format("[info]:%s",msg));
    }

}
