package com.xiaozhi.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

/**
 * Created by iaiai(QQ:176291935) on 16/6/16.
 */
public final class NettyChannelInitializer extends ChannelInitializer<SocketChannel> {

    /**
     * 配置信息
     */
    private NettyConfigProperty property;

    public NettyChannelInitializer(NettyConfigProperty property){
        this.property = property;
    }

    @Override
    protected final void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();    //拦截器,其中任何ChannelHandler都可以中断当前的流程

        //固定长度解码器
        pipeline.addLast("framerEncoder",      new LengthFieldPrepender(4));
        pipeline.addLast("framerDecoder",      new LengthFieldBasedFrameDecoder(65535, 0, 4, 0, 4));

        pipeline.addLast("decoder",            new StringDecoder());
        pipeline.addLast("encoder",            new StringEncoder());
        pipeline.addLast("readTimeoutHandler", new ReadTimeoutHandler(property.getReadTimeout()));
        pipeline.addLast("idleStateHandler",   new IdleStateHandler(0,property.getIdleWriter(),0));
        pipeline.addLast("heartbeat",          new NettyHeartbeatHandler());
        pipeline.addLast("login",              new NettyLoginHandler());
        pipeline.addLast("handler",            new NettyDataHandler());
    }

}
