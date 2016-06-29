package org.iaiai.netty;

/**
 * Created by iaiai(QQ:176291935) on 16/6/17.
 */
public final class NettyConfigProperty {

    //netty的服务器ip
    private final static String NETTY_SERVER_IP = "192.168.1.234";

    //netty的服务器端口
    private final static int NETTY_SERVER_PORT = 7777;

    //netty的服务重启的睡眠时间(毫秒)
    private final static int NETTY_SERVER_RESTART_SLEEP = 5000;

    //netty连接的超时时间(毫秒)
    private final static int NETTY_SERVER_CONNECTION_TIMEOUT = 5000;

    //读取数据的超时间(秒)
    private final static int NETTY_READ_TIMEOUT = 10;

    //写入的空闲时间(秒)
    private final static int NETTY_IDLE_WRITER = 5;

    /**
     * 获取netty的服务器ip
     * @return
     */
    public final static String getNettyServerIp(){
        return NETTY_SERVER_IP;
    }

    /**
     * 获取netty的服务器端口
     * @return
     */
    public final static int getNettyServerPort(){
        return NETTY_SERVER_PORT;
    }

    /**
     * netty的服务重启的睡眠时间
     * @return
     */
    public final static int getNettyServerRestartSleep(){
        return NETTY_SERVER_RESTART_SLEEP;
    }

    /**
     * netty连接的超时时间
     * @return
     */
    public final static int getNettyServerConnectionTimeout(){
        return NETTY_SERVER_CONNECTION_TIMEOUT;
    }

    /**
     * 读取数据的超时间
     * @return
     */
    public final static int getNettyReadTimeout(){
        return NETTY_READ_TIMEOUT;
    }

    /**
     * 写入的空闲时间
     * @return
     */
    public final static int getNettyIdleWriter(){
        return NETTY_IDLE_WRITER;
    }

}
