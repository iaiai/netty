package com.xiaozhi.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.UUID;

/**
 * Created by iaiai(QQ:176291935) on 16/6/16.
 */
public final class NettyConnectionProperty {

    /**
     * 保存所有的客户端连接
     * allChannels.writeAndFlush(str);  可群发小伙伴们
     */
    private final static ChannelGroup allChannels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 每一个连接在添加的时候生成一个唯一的id,这个是程序中生成的
     */
    private final static String ATTRIBUTE_SESSION_ID = "_NETTY_SESSION_ID";

    /**
     * 获取客户端连接数据
     * @return
     */
    public final static int getConnectionSize(){
        return allChannels.size();
    }

    /**
     * 添加一个客户端连接
     * @param channel
     * @return
     */
    public final static synchronized boolean addConnection(Channel channel){
        setConnectionAttribute(channel,ATTRIBUTE_SESSION_ID, UUID.randomUUID().toString());
        return allChannels.add(channel);
    }

    /**
     * 删除一个客户端连接
     * @param channelId
     * @return
     */
    public final static synchronized boolean removeConnection(ChannelId channelId){
        return allChannels.remove(getConnection(channelId));
    }

    /**
     * 删除一个客户端连接
     * @param channel
     * @return
     */
    public final static synchronized boolean removeConnection(Channel channel){
        return allChannels.remove(channel);
    }

    /**
     * 获取一个连接
     * @param channelId
     * @return
     */
    public final static Channel getConnection(ChannelId channelId){
        return allChannels.find(channelId);
    }

    /**
     * 给其中一个连接设置属性
     * @param channelId
     * @param key
     * @param value
     */
    public final static void setConnectionAttribute(ChannelId channelId, String key, Object value){
        getConnection(channelId).attr(AttributeKey.<Object>valueOf(key)).set(value);
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
     * @param channelId
     * @param key
     * @return
     */
    public final static Object getConnectionAttribute(ChannelId channelId, String key){
        return getConnectionAttribute(getConnection(channelId),key);
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

    /**
     * 获取sessionId
     * @param channel
     * @return
     */
    public final static String getConnectionSessionId(Channel channel){
        return channel.attr(AttributeKey.<Object>valueOf(ATTRIBUTE_SESSION_ID)).get().toString();
    }

    /**
     * 获取sessionId
     * @param channelId
     * @return
     */
    public final static String getConnectionSessionId(ChannelId channelId){
        return getConnectionSessionId(getConnection(channelId));
    }

}
