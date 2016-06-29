package com.xiaozhi.netty.handler;

import com.xiaozhi.netty.util.LogUtil;
import com.xiaozhi.netty.NettyConnectionProperty;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by iaiai(QQ:176291935) on 16/6/16.
 * 处理心跳
 */
public final class NettyHeartbeatHandler extends BaseChannelHandler<String> {

    private final static Logger log = LogManager.getLogger(NettyHeartbeatHandler.class.getName());

    //服务器发送0代表心跳
    private final static String SEND_HEARTBEAT = "0";

    //客户端发送过来的1代表心跳
    private final static String RECEIVE_HEARTBEAT = "1";

    @Override
    public final void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //心跳包只此类中执行,不往下执行
        if (IdleStateEvent.class.isAssignableFrom(evt.getClass())) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.WRITER_IDLE) {
                writeAndFlush(ctx,SEND_HEARTBEAT);
            }
        }
    }

    /**
     * 管道从活跃状态转到不活跃状态触发
     * @param ctx
     */
    @Override
    public final void channelInactive(final ChannelHandlerContext ctx) {
        NettyConnectionProperty.removeConnection(ctx.channel());
        LogUtil.info(log,ctx.channel(),"断开连接",null);

        //继续向下执行
        ctx.fireChannelInactive();
    }

    @Override
    public final void channelUnregistered(final ChannelHandlerContext ctx) throws Exception {
        //log.info(String.format("关闭连接[%s]",ctx.channel().remoteAddress()));

        //继续向下执行
        ctx.fireChannelUnregistered();
    }

    @Override
    public final void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //不管到哪层,一但出错直接关闭,不再往下执行
        LogUtil.info(log,ctx.channel(),"连接异常",cause.toString());
        ctx.close();
    }

    /**
     * 管道从不活跃状态转到活跃状态触发
     * 覆盖 channelActive 方法 在channel被启用的时候触发 (在建立连接的时候)
     **/
    @Override
    public final void channelActive(ChannelHandlerContext ctx) throws Exception {
        //添加管理通道
        NettyConnectionProperty.addConnection(ctx.channel());

        //继续向下执行
        ctx.fireChannelActive();
    }

    @Override
    protected final void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        //如果为空则什么也不处理
        if(msg==null)return;

        //过滤心跳包
        if(RECEIVE_HEARTBEAT.equals(msg)){
            LogUtil.info(log,ctx.channel(),"客户端发送心跳",ctx.channel().remoteAddress().toString());
            return;
        }

        //继续向下执行
        ctx.fireChannelRead(msg);
    }

}
