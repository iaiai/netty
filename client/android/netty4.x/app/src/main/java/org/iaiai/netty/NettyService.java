package org.iaiai.netty;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import org.iaiai.netty.util.JSONUtil;
import org.iaiai.netty.util.LogUtil;
import org.iaiai.netty.vo.NettyDataLogin;

import java.util.Timer;
import java.util.TimerTask;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by iaiai(QQ:176291935) on 16/6/17.
 */
public final class NettyService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startNetty();

        return START_STICKY_COMPATIBILITY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 启动netty
     */
    private final void startNetty(){
        startNetty(NettyConfigProperty.getNettyServerIp(), NettyConfigProperty.getNettyServerPort(), new NioEventLoopGroup());
    }

    /**
     * 启动netty
     * @param g
     */
    public static final void startNetty(NettyApplication application,EventLoopGroup g){
        startNetty(application,NettyConfigProperty.getNettyServerIp(), NettyConfigProperty.getNettyServerPort(), g);
    }

    /**
     * 启动netty
     * @param application
     */
    public static final void startNetty(NettyApplication application){
        startNetty(application,NettyConfigProperty.getNettyServerIp(), NettyConfigProperty.getNettyServerPort(), new NioEventLoopGroup());
    }

    /**
     * 启动netty
     * @param ip
     * @param port
     * @param g
     */
    private final void startNetty(String ip,int port,EventLoopGroup g){
        startNetty((NettyApplication)getApplication(),ip,port,g);
    }

    /**
     * 启动netty
     * @param ip
     * @param port
     * @param g
     */
    private synchronized static final void startNetty(final NettyApplication application,final String ip,final int port, final EventLoopGroup g){
        Channel channel = application.getChannel();

        if(channel==null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    threadMina(application, ip, port, g);
                }
            }).start();
        }else{
            if(!channel.isRegistered()){
                try {
                    channel.closeFuture().sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try{
                    //优雅退出,释放nio线程组
                    g.shutdownGracefully();
                }catch (Exception ex){
                    LogUtil.i(NettyService.class.getName(), null, "关闭nio线程", String.format("异常:%s", ex.getCause()));
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        threadMina(application, ip, port, new NioEventLoopGroup());
                    }
                }).start();
            }
        }
    }

    //启动mina
    private static void threadMina(final NettyApplication application,String ip,int port, final EventLoopGroup g){
        try{
            Bootstrap b = new Bootstrap();
            b.group(g)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,NettyConfigProperty.getNettyServerConnectionTimeout()) //设置连接的超时时间
                    .option(ChannelOption.TCP_NODELAY, true)  //不延迟，消息立即发送,激活或禁止TCP_NODELAY套接字选项,它决定是否使用Nagle算法,如果是时延敏感型的应用,建议关闭Nagle算法.
                    .option(ChannelOption.SO_KEEPALIVE, true) //长连接
                    .remoteAddress(ip, port)  //设置ip端口
                    .handler(new NettyChannelInitializer());

            ChannelFuture future = b.connect().sync();  //等待创建完成

            Channel channel = future.channel();

            //存储发送通道，后面所有发送的消息全部通过此通道发送
            application.setChannel(channel);

            //存储到通道的属性中
            NettyConnectionProperty.setConnectionAttribute(channel,NettyApplication.class.getName(),application);

            //发送登录信息
            senderLogin(channel);

            //等待关闭阻塞
//            channel.closeFuture().sync();
//            LogUtil.i("netty服务已经销毁...");
        }catch (Exception e){
            LogUtil.i(NettyService.class.getName(),null,"启动netty异常",String.format("异常:%s",e.getCause()));

            try{
                //优雅退出,释放nio线程组
                g.shutdownGracefully();
            }catch (Exception ex){
                LogUtil.i(NettyService.class.getName(),null,"关闭nio线程",String.format("异常:%s",ex.getCause()));
            }

            //睡眠一下重启
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    startNetty(application);
                }
            }, NettyConfigProperty.getNettyServerRestartSleep());
        }
    }

    private static void senderLogin(Channel channel){
        //发送登录数据
        NettyDataLogin login = new NettyDataLogin();
        login.username = "xiaozhikeji";
        login.password = "xx1111";
        login.code = "xxf1s2d3a";
        login.createTime = System.currentTimeMillis();

        //发送数据
        channel.writeAndFlush(JSONUtil.toJson(login).toString());
    }

}
