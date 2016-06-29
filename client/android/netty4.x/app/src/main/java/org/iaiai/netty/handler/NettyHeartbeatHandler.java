package org.iaiai.netty.handler;

import org.iaiai.netty.NettyApplication;
import org.iaiai.netty.NettyConnectionProperty;
import org.iaiai.netty.NettyService;
import org.iaiai.netty.util.LogUtil;

import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * Created by iaiai(QQ:176291935) on 16/6/17.
 */
public class NettyHeartbeatHandler extends BaseChannelHandler<String> {

    //客户端发送1代表心跳
    private final static String SEND_HEARTBEAT = "1";

    //服务器发送过来的0代表心跳
    private final static String RECEIVE_HEARTBEAT = "0";

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
                writeAndFlush(ctx, SEND_HEARTBEAT);
            }
        }
    }

    /**
     * 管道从活跃状态转到不活跃状态触发
     * @param ctx
     */
    @Override
    public void channelInactive(final ChannelHandlerContext ctx) {
        LogUtil._if(NettyHeartbeatHandler.class.getName(), ctx.channel(), "连接断开", "channelInactive");

        final EventLoop loop = ctx.channel().eventLoop();
        loop.schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    NettyApplication application = (NettyApplication) NettyConnectionProperty.getConnectionAttribute(ctx.channel(), NettyApplication.class.getName());

                    NettyService.startNetty(application, loop);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 5, TimeUnit.SECONDS);

//        ctx.fireChannelInactive();
    }

    @Override
    public void channelUnregistered(final ChannelHandlerContext ctx) throws Exception {
        LogUtil._if(NettyHeartbeatHandler.class.getName(), ctx.channel(), "连接断开", "channelUnregistered");

        ctx.fireChannelUnregistered();
    }

    /**
     * 管道从不活跃状态转到活跃状态触发
     * 覆盖 channelActive 方法 在channel被启用的时候触发 (在建立连接的时候)
     **/
    @Override
    public final void channelActive(ChannelHandlerContext ctx) throws Exception {
        //添加管理通道
        NettyConnectionProperty.createSession(ctx.channel());

        //继续向下执行
        ctx.fireChannelActive();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LogUtil._if(NettyHeartbeatHandler.class.getName(), ctx.channel(), "异常关闭连接", String.format("异常:%s",cause.getCause()));
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        //如果为空则什么也不处理
        if(msg==null)return;

        //过滤心跳包
        if(RECEIVE_HEARTBEAT.equals(msg)){
            LogUtil.i(NettyHeartbeatHandler.class.getName(), ctx.channel(), "服务器发送心跳", ctx.channel().remoteAddress().toString());
            return;
        }

        //往下传递
        ctx.fireChannelRead(msg);
    }

}
