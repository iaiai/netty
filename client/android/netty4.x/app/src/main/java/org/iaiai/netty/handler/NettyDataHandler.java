package org.iaiai.netty.handler;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by iaiai(QQ:176291935) on 16/6/17.
 */
public final class NettyDataHandler extends BaseChannelHandler<String> {

    /**
     * 指令集
     * 说明:
     * 指令集按冒号(:)分割
     *
     * 举例说明:"xz:netty:command:location:get"
     * xz:代表项目公司简称
     * netty:代表项目
     * command:代表命令还会包含config等其它命令，但此类中只处理command相关命令
     * location:代表定义，还会有其它命令，比如sleep睡眠等其它命令
     * get:代表获取数据，这里代表的是获取定位数据
     */
    private enum NettyCommand{
        location_get("xz:netty:command:location:get")
        ;

        private String val;
        private NettyCommand(String val){
            this.val = val;
        }
        public String getVal(){
            return val;
        }
    }

    @Override
    protected final void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        //判断是否获取定义数据
        if(NettyCommand.location_get.getVal().equals(msg)){

        }else{

        }
    }

}
