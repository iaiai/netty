package org.iaiai.netty;

import java.util.UUID;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

/**
 * Created by iaiai(QQ:176291935) on 16/6/17.
 */
public final class NettyConnectionProperty {

    /**
     * 每一个连接在添加的时候生成一个唯一的id,这个是程序中生成的
     */
    private final static String ATTRIBUTE_SESSION_ID = "_NETTY_SESSION_ID";

    /**
     * 创建session
     * @param channel
     * @return
     */
    public final static synchronized void createSession(Channel channel){
        setConnectionAttribute(channel,ATTRIBUTE_SESSION_ID,UUID.randomUUID().toString());
    }

    /**
     * 获取session
     * @param channel
     * @return
     */
    public final static String getSession(Channel channel){
        return (String)getConnectionAttribute(channel,ATTRIBUTE_SESSION_ID);
    }

    /**
     * 给其中一个连接设置属性
     * @param channel
     * @param key
     * @param value
     */
    public final static void setConnectionAttribute(Channel channel, String key, Object value){
        channel.attr(AttributeKey.<Object>valueOf(key)).set(value);
    }

    /**
     * 获取属性
     * @param channel
     * @param key
     * @return
     */
    public final static Object getConnectionAttribute(Channel channel, String key){
        return channel.attr(AttributeKey.<Object>valueOf(key)).get();
    }

}
