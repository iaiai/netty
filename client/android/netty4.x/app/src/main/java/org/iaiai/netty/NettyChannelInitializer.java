package org.iaiai.netty;

import org.iaiai.netty.handler.NettyConfigHandler;
import org.iaiai.netty.handler.NettyDataHandler;
import org.iaiai.netty.handler.NettyHeartbeatHandler;
import org.iaiai.netty.handler.NettyLoginHandler;

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
 * Created by iaiai(QQ:176291935) on 16/6/17.
 */
public final class NettyChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected final void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();    //拦截器,其中任何ChannelHandler都可以中断当前的流程

        //固定长度解码器
        pipeline.addLast("framerEncoder",new LengthFieldPrepender(4));
        pipeline.addLast("framerDecoder",new LengthFieldBasedFrameDecoder(65535, 0, 4, 0, 4));

        pipeline.addLast("decoder",            new StringDecoder());
        pipeline.addLast("encoder",            new StringEncoder());
        pipeline.addLast("readTimeoutHandler", new ReadTimeoutHandler(NettyConfigProperty.getNettyReadTimeout()));  //设置超时时间
        pipeline.addLast("idleStateHandler",   new IdleStateHandler(0,NettyConfigProperty.getNettyIdleWriter(),0));
        pipeline.addLast("configHandler",      new NettyConfigHandler());
        pipeline.addLast("heartbeatHandler",   new NettyHeartbeatHandler());
        pipeline.addLast("loginHandler",       new NettyLoginHandler());
        pipeline.addLast("dataHandler",        new NettyDataHandler());
    }

}
