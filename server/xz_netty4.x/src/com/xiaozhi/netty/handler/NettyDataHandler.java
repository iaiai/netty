package com.xiaozhi.netty.handler;

import com.xiaozhi.netty.util.JsonUtil;
import com.xiaozhi.netty.util.LogUtil;
import com.xiaozhi.netty.vo.NettyData;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

/**
 * Created by iaiai(QQ:176291935) on 16/6/16.
 * 接收到的数据先定暂时全部为字符串
 */
public final class NettyDataHandler extends BaseChannelHandler<String> {

    private final static Logger log = LogManager.getLogger(NettyDataHandler.class.getName());

    @Override
    protected final void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        try {
            NettyData data = (NettyData) JsonUtil.toObject(new JSONObject(msg),NettyData.class);
            LogUtil.info(log,ctx.channel(),"信息",data.code+":"+msg);
        }catch (Exception e){
            LogUtil.error(log,ctx.channel(),"传输的数据异常",msg);
        }
    }

}
