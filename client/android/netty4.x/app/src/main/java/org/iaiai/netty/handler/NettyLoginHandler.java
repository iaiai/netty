package org.iaiai.netty.handler;

import org.iaiai.netty.NettyConnectionProperty;
import org.iaiai.netty.util.LogUtil;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by iaiai(QQ:176291935) on 16/6/17.
 */
public class NettyLoginHandler extends BaseChannelHandler<String> {

    //在管道对应的属性上添加此属性,只要有此属性,说明此管道已经登录
    private final static String LOGIN_EXISTS = "_NETTY_LOGIN_EXISTS";

    //登录成功指令
    private final static String LOGIN_SUCCESS = "xz:netty:command:login:success";

    /**
     * 管道从不活跃状态转到活跃状态触发
     * 覆盖 channelActive 方法 在channel被启用的时候触发 (在建立连接的时候)
     **/
    @Override
    public final void channelActive(ChannelHandlerContext ctx) throws Exception {
        //继续向下执行
        ctx.fireChannelActive();
    }

    @Override
    protected final void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        if(msg==null)return;

        //判断是否是登录成功数据
        if(LOGIN_SUCCESS.equals(msg)){
            setExists(ctx.channel());
            return;
        }

        if(!isLogin(ctx.channel())){
            LogUtil.e(NettyLoginHandler.class.getName(), ctx.channel(), "未登录,数据异常", msg);
            return;
        }

        ctx.fireChannelRead(msg);
    }

    /**
     * 判断是否登录
     * @param channel
     * @return
     */
    private final boolean isLogin(Channel channel){
        return NettyConnectionProperty.getConnectionAttribute(channel, LOGIN_EXISTS)==null?false:true;
    }

    /**
     * 记录登录状态
     * @param channel
     */
    private final void setExists(Channel channel){
        NettyConnectionProperty.setConnectionAttribute(channel,LOGIN_EXISTS,true);
    }

}
