package com.xiaozhi.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.internal.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

/**
 * Created by iaiai(QQ:176291935) on 16/6/16.
 */
public final class NettyLoginHandler extends ChannelHandlerAdapter {

    private final static Logger log = LogManager.getLogger(NettyLoginHandler.class.getName());

    private final static String LOGIN_EXISTS = "_NETTY_LOGIN_EXISTS";

    @Override
    public final void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(!isLogin(ctx.channel())){
            try {
                NettyLoginJson login = toObject((String)msg);

                if(exists(login)){
                    setExists(ctx.channel());
                    ctx.fireChannelRead(msg);
                    return;
                }
            }catch (Exception e){
                LogUtil.error(log,ctx.channel(),"未登录,传输的登录数据异常",(String)msg);
            }

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
        return NettyConnectionProperty.getConnectionAttribute(channel,LOGIN_EXISTS)==null?false:true;
    }

    /**
     * 记录登录状态
     * @param channel
     */
    private final void setExists(Channel channel){
        NettyConnectionProperty.setConnectionAttribute(channel,LOGIN_EXISTS,true);
    }

    /**
     * 把数据转成对象
     * @param msg
     * @return
     */
    private final NettyLoginJson toObject(String msg) throws Exception {
        return (NettyLoginJson) JsonUtil.toObject(new JSONObject(msg),NettyLoginJson.class);
    }

    /**
     * 验证数据
     * @param login
     * @return
     */
    private final boolean exists(NettyLoginJson login){
        //验证每一个属性都不允许为空
        if(login==null
                || StringUtil.isNullOrEmpty(login.username)
                || StringUtil.isNullOrEmpty(login.password)
                || StringUtil.isNullOrEmpty(login.code)
                ||login.createTime==null){
            return false;
        }

        //验证用户名密码
        return (login.username.equals("xiaozhikeji")&&login.password.equals("xx1111"))?true:false;
    }

}
