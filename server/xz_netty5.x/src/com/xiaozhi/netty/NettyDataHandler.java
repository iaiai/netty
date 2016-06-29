package com.xiaozhi.netty;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

/**
 * Created by iaiai(QQ:176291935) on 16/6/16.
 * 接收到的数据先定暂时全部为字符串
 */
public final class NettyDataHandler extends ChannelHandlerAdapter {

    private final static Logger log = LogManager.getLogger(NettyDataHandler.class.getName());

    @Override
    public final void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            NettyData data = (NettyData) JsonUtil.toObject(new JSONObject(msg),NettyData.class);
            LogUtil.info(log,ctx.channel(),"信息",data.code+":"+msg);
        }catch (Exception e){
            LogUtil.error(log,ctx.channel(),"传输的数据异常",(String)msg);
        }
    }

}
