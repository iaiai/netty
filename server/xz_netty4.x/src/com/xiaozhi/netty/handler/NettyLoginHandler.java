package com.xiaozhi.netty.handler;

import com.xiaozhi.netty.util.JsonUtil;
import com.xiaozhi.netty.util.LogUtil;
import com.xiaozhi.netty.NettyConnectionProperty;
import com.xiaozhi.netty.vo.NettyDataLogin;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

/**
 * Created by iaiai(QQ:176291935) on 16/6/16.
 */
public final class NettyLoginHandler extends BaseChannelHandler<String> {

    private final static Logger log = LogManager.getLogger(NettyLoginHandler.class.getName());

    //在管道对应的属性上添加此属性,只要有此属性,说明此管道已经登录
    private final static String LOGIN_EXISTS = "_NETTY_LOGIN_EXISTS";

    //登录成功指令
    private final static String LOGIN_SUCCESS = "xz:netty:command:login:success";

    @Override
    protected final void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        if(!isLogin(ctx.channel())){
            try {
                NettyDataLogin login = toObject(msg);

                if(exists(login)){
                    setExists(ctx.channel());

                    //给客户端发送登录成功指令
                    LogUtil.info(log,ctx.channel(),"登录成功",ctx.channel().remoteAddress().toString());
                    writeAndFlush(ctx,LOGIN_SUCCESS);
                    return;
                }
            }catch (Exception e){
                LogUtil.error(log,ctx.channel(),"未登录,传输的登录数据异常",msg);
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
    private final NettyDataLogin toObject(String msg) throws Exception {
        return (NettyDataLogin) JsonUtil.toObject(new JSONObject(msg),NettyDataLogin.class);
    }

    /**
     * 验证数据
     * @param login
     * @return
     */
    private final boolean exists(NettyDataLogin login){
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
