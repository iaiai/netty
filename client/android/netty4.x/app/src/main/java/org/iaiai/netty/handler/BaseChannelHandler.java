package org.iaiai.netty.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by iaiai(QQ:176291935) on 16/6/17.
 */
public class BaseChannelHandler<T> extends SimpleChannelInboundHandler<T> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, T msg) throws Exception {
        ctx.fireChannelRead(msg);
    }

    /**
     * 写入数据
     * @param channel
     * @param msg
     * @return
     */
    protected ChannelFuture writeAndFlush(Channel channel, String msg){
        return channel.writeAndFlush(Unpooled.copiedBuffer(msg.getBytes()));
    }

    /**
     * 写入数据
     * @param ctx
     * @param msg
     * @return
     */
    protected ChannelFuture writeAndFlush(ChannelHandlerContext ctx, String msg){
        return ctx.writeAndFlush(Unpooled.copiedBuffer(msg.getBytes()));
    }

}
