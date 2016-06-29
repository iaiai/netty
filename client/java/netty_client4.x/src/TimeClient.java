import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.*;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.AttributeKey;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by iaiai(QQ:176291935) on 16/6/14.
 */
public class TimeClient {

    public void connect(int port,String host, EventLoopGroup g) throws Exception{
        //配置客户端nio线程组
//        EventLoopGroup group = new NioEventLoopGroup();

        try{
            Bootstrap b = new Bootstrap();
            b.group(g)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,5000) //设置连接的超时时间
                    .option(ChannelOption.TCP_NODELAY, true)  //不延迟，消息立即发送,激活或禁止TCP_NODELAY套接字选项,它决定是否使用Nagle算法,如果是时延敏感型的应用,建议关闭Nagle算法.
                    .option(ChannelOption.SO_KEEPALIVE, true) //长连接
                    .remoteAddress(host, port)  //设置ip端口
                    .handler(new ChildChannelHandler());

            b.connect();

//            //发起异步连接操作
//            ChannelFuture f = b.connect(host,port).sync();
//
//            //等待客户端链路关闭
//            f.channel().closeFuture().sync();
        }finally {
            //优雅退出,释放nio线程组
            //g.shutdownGracefully();
        }
    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel>{
        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            ChannelPipeline pipeline = socketChannel.pipeline();    //拦截器,其中任何ChannelHandler都可以中断当前的流程

            //固定长度解码器
            pipeline.addLast("framerEncoder",new LengthFieldPrepender(4));
            pipeline.addLast("framerDecoder",new LengthFieldBasedFrameDecoder(65535, 0, 4, 0, 4));

            pipeline.addLast("decoder", new StringDecoder());
            pipeline.addLast("encoder", new StringEncoder());
            pipeline.addLast("readTimeoutHandler", new ReadTimeoutHandler(10));  //设置超时时间
            pipeline.addLast("idleStateHandler", new IdleStateHandler(0,5,0));
            pipeline.addLast("heartbeat",new HeartbeatHandler());
            pipeline.addLast("handler",new TimeClientHandler());
        }
    }

    public class HeartbeatHandler extends SimpleChannelInboundHandler<String>{
        long startTime = -1;

        /**
         * 发送心跳包
         * @param ctx
         * @param evt
         * @throws Exception
         */
        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if (IdleStateEvent.class.isAssignableFrom(evt.getClass())) {
                IdleStateEvent event = (IdleStateEvent) evt;
                if (event.state() == IdleState.WRITER_IDLE) {
                    ctx.writeAndFlush(Unpooled.copiedBuffer("1".getBytes()));
                }
            }
        }

        @Override
        public void channelInactive(final ChannelHandlerContext ctx) {
            println("断开连接: " + ctx.channel().remoteAddress());
        }

        @Override
        public void channelUnregistered(final ChannelHandlerContext ctx) throws Exception {
            println("关闭连接，睡眠5秒后继续");

            final EventLoop loop = ctx.channel().eventLoop();
            loop.schedule(new Runnable() {
                @Override
                public void run() {
                    println("连接到: 192.168.1.234:7777");
                    try {
                        connect(7777,"192.168.1.234",loop);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 5, TimeUnit.SECONDS);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            ctx.close();
            println("关闭连接...");
        }

        @Override
        public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
            println("1服务器发来的数据:"+msg);

            //往下传递
            ctx.fireChannelRead(msg);
        }

        void println(String msg) {
            if (startTime < 0) {
                System.err.format("[SERVER IS DOWN] %s%n", msg);
            } else {
                System.err.format("[UPTIME: %5ds] %s%n", (System.currentTimeMillis() - startTime) / 1000, msg);
            }
        }
    }

    public class TimeClientHandler extends SimpleChannelInboundHandler<String>{
        private String req;

        long startTime = -1;

        public TimeClientHandler(){
            req = "{\"username\":\"张三\",\"age\":55,\"realname\":\"李四\",\"obj\":{\"username\":\"张三\",\"age\":55,\"realname\":\"李四\",\"obj\":{\"username\":\"张三\",\"age\":55,\"realname\":\"李四\",\"obj\":{\"username\":\"张三\",\"age\":55,\"realname\":\"李四\",\"obj\":{\"username\":\"张三\",\"age\":55,\"realname\":\"李四\",\"obj\":{\"username\":\"张三\",\"age\":55,\"realname\":\"李四\",\"obj\":{\"username\":\"张三\",\"age\":55,\"realname\":\"李四\",\"obj\":{\"username\":\"张三\",\"age\":55,\"realname\":\"李四\",\"obj\":{\"username\":\"张三\",\"age\":55,\"realname\":\"李四\",\"obj\":{\"username\":\"张三\",\"age\":55,\"realname\":\"李四\",\"obj\":{\"username\":\"张三\",\"age\":55,\"realname\":\"李四\",\"obj\":{\"username\":\"张三\",\"age\":55,\"realname\":\"李四\",\"obj\":{\"username\":\"张三\",\"age\":55,\"realname\":\"李四\",\"obj\":{\"username\":\"张三\",\"age\":55,\"realname\":\"李四\",\"obj\":{\"username\":\"张三\",\"age\":55,\"realname\":\"李四\",\"obj\":{\"username\":\"张三\",\"age\":55,\"realname\":\"李四\",\"obj\":{\"username\":\"张三\",\"age\":55,\"realname\":\"李四\",\"obj\":{\"username\":\"张三\",\"age\":55,\"realname\":\"李四\",\"obj\":{\"username\":\"张三\",\"age\":55,\"realname\":\"李四\",\"obj\":{\"username\":\"张三\",\"age\":55,\"realname\":\"李四\",\"obj\":{\"username\":\"张三\",\"age\":55,\"realname\":\"李四\",\"obj\":}}}}}}}}}}}}}}}}}}}}}";
        }

        void println(String msg) {
            if (startTime < 0) {
                System.err.format("[SERVER IS DOWN] %s%n", msg);
            } else {
                System.err.format("[UPTIME: %5ds] %s%n", (System.currentTimeMillis() - startTime) / 1000, msg);
            }
        }

        /**
         * 覆盖 channelActive 方法 在channel被启用的时候触发 (在建立连接的时候)
         **/
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            if (startTime < 0) {
                startTime = System.currentTimeMillis();
            }
            println(req);
            ctx.writeAndFlush(Unpooled.copiedBuffer(req.getBytes())).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    println("异常:"+future.cause());
                    if(!future.isSuccess()) {
                        println("异常:"+future.cause());
                    }
                }
            });

            //存储session的一些信息,比如登录信息
            ctx.channel().attr(AttributeKey.valueOf("session")).set(UUID.randomUUID());
        }

        @Override
        public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
            println("服务器发来的数据:"+msg+"____session:"+ctx.channel().attr(AttributeKey.valueOf("session")).get()+"____channelId:"+ctx.channel().id());
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            ctx.close();
            println("关闭连接...");
        }
    }

    public static void main(String[] args) throws Exception{
        int port = 7777;
        new TimeClient().connect(port,"192.168.1.234",new NioEventLoopGroup());
    }

}