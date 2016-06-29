package com.xiaozhi.netty;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by iaiai(QQ:176291935) on 16/6/16.
 */
public final class NettyConfigProperty {

    //配置文件
    private final static String NETTY_CONFIG_FILE  = "netty.properties";

    //netty端口号
    private final static String NETTY_PORT         = "NETTY_PORT";

    //连接的超时时间
    private final static String NETTY_TIMEOUT      = "NETTY_TIMEOUT";

    //套接字接收的缓存大小
    private final static String NETTY_SO_RCVBUF    = "NETTY_SO_RCVBUF";

    //套接字发送的缓存大小
    private final static String NETTY_SO_SNDBUF    = "NETTY_SO_SNDBUF";

    //读取数据的超时间
    private final static String NETTY_READ_TIMEOUT = "NETTY_READ_TIMEOUT";

    //写入的空闲时间
    private final static String NETTY_IDLE_WRITER  = "NETTY_IDLE_WRITER";

    private Properties prop;

    public NettyConfigProperty() throws IOException {
        readProperties();
    }

    /**
     * 读取配置信息
     * @return
     * @throws IOException
     */
    private void readProperties() throws IOException {
        prop = new Properties();//属性集合对象
        FileInputStream fis = new FileInputStream(NETTY_CONFIG_FILE);//属性文件流
        prop.load(fis);//将属性文件流装载到Properties对象中
        fis.close();
    }

    /**
     * 获取端口号
     * @return
     */
    public final int getPort(){
        return Integer.parseInt(prop.getProperty(NETTY_PORT));
    }

    /**
     * 获取超时时间
     * @return
     */
    public final int getTimeout(){
        return Integer.parseInt(prop.getProperty(NETTY_TIMEOUT));
    }

    /**
     * 套接字接收的缓存大小
     * @return
     */
    public final int getSoRcvbuf(){
        return Integer.parseInt(prop.getProperty(NETTY_SO_RCVBUF));
    }

    /**
     * 套接字发送的缓存大小
     * @return
     */
    public final int getSoSndbuf(){
        return Integer.parseInt(prop.getProperty(NETTY_SO_SNDBUF));
    }

    /**
     * 读取数据的超时间
     * @return
     */
    public final int getReadTimeout(){
        return Integer.parseInt(prop.getProperty(NETTY_READ_TIMEOUT));
    }

    /**
     * 写入的空闲时间
     * @return
     */
    public final int getIdleWriter(){
        return Integer.parseInt(prop.getProperty(NETTY_IDLE_WRITER));
    }

}
