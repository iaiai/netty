package org.iaiai.netty;

import android.app.Application;

import io.netty.channel.Channel;

/**
 * Created by iaiai(QQ:176291935) on 16/6/17.
 */
public class NettyApplication extends Application {

    //跟服务器连接的通道
    private Channel channel;

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

}
