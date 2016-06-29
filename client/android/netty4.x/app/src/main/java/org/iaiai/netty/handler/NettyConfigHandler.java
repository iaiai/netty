package org.iaiai.netty.handler;

import io.netty.channel.ChannelHandlerContext;

/**
 * Created by iaiai(QQ:176291935) on 16/6/17.
 */
public class NettyConfigHandler extends BaseChannelHandler<String> {

    /**
     * 指令集
     * 说明:
     * 指令集按冒号(:)分割
     *
     * 举例说明:"xz:netty:config:system:restart"
     * xz:代表项目公司简称
     * netty:代表项目
     * config:代表命令还会包含command等其它命令，但此类中只处理config相关命令
     * system:代表定义
     * restart:代表操作，这里表示系统重启
     */
    private enum NettyCommand{
        system_restart("xz:netty:config:system:restart")    //系统重启
        ;

        private String val;
        NettyCommand(String val){
            this.val = val;
        }
        public String getVal(){
            return val;
        }
    }

    @Override
    protected final void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        //判断是否获取定义数据
        if(NettyCommand.system_restart.getVal().equals(msg)){
            return;
        }

        ctx.fireChannelRead(msg);
    }

}
