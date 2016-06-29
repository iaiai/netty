package com.xiaozhi.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by iaiai(QQ:176291935) on 16/6/16.
 */
public final class NettyService {

    /**
     * 启动netty
     */
    public final static void startNetty() throws Exception{
        NettyConfigProperty property = new NettyConfigProperty();

        //配置服务端线程组NIO线程
        EventLoopGroup bossGroup   = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(property.getPort())
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,property.getTimeout()) //设置连接的超时时间
                    .option(ChannelOption.SO_BACKLOG, 1024) //连接数
                    .option(ChannelOption.SO_RCVBUF, property.getSoRcvbuf())  //套接字接收的缓存大小
                    .option(ChannelOption.SO_SNDBUF, property.getSoSndbuf())  //套接字发送的缓存大小
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)    //5.x中默认是开启
                    .option(ChannelOption.TCP_NODELAY, true)  //不延迟，消息立即发送,激活或禁止TCP_NODELAY套接字选项,它决定是否使用Nagle算法,如果是时延敏感型的应用,建议关闭Nagle算法.
                    .option(ChannelOption.SO_KEEPALIVE, true) //长连接
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)    //5.x中默认是开启
                    .childHandler(new NettyChannelInitializer(property));

            //绑定端口,同步等待成功
            ChannelFuture f = b.bind().sync();

            //后面用不用考虑关闭再定,暂时先这样

            //等待服务端监听端口关闭
            f.channel().closeFuture().sync();
        }finally {
            //优雅退出,释放线程池资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args){
        try {
            startNetty();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
