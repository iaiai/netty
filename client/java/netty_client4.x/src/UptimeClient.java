import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 30   * Connects to a server periodically to measure and print the uptime of the
 31   * server.  This example demonstrates how to implement reliable reconnection
 32   * mechanism in Netty.
 33   */
public final class UptimeClient {

    static final String HOST = System.getProperty("host", "192.168.1.234");
    static final int PORT = Integer.parseInt(System.getProperty("port", "7777"));
    // Sleep 5 seconds before a reconnection attempt.
    static final int RECONNECT_DELAY = Integer.parseInt(System.getProperty("reconnectDelay", "5"));
    // Reconnect when the server sends nothing for 10 seconds.
    static final int READ_TIMEOUT = Integer.parseInt(System.getProperty("readTimeout", "10"));

    private static final UptimeClientHandler handler = new UptimeClientHandler();

    public static void main(String[] args) throws Exception {
        configureBootstrap(new Bootstrap()).connect();

          Timer timer = new Timer();
          timer.schedule(new TimerTask() {
              @Override
              public void run() {
                  handler.println("发送...");
                  handler.write("测试");
              }
          }, 19000,3000);
    }

    private static Bootstrap configureBootstrap(Bootstrap b) {
        return configureBootstrap(b, new NioEventLoopGroup());
    }

    static Bootstrap configureBootstrap(Bootstrap b, EventLoopGroup g) {
        b.group(g)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .remoteAddress(HOST, PORT)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new IdleStateHandler(READ_TIMEOUT, 5, 3), handler);
                    }
                });

        return b;
    }

    static void connect(Bootstrap b) {
        b.connect().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.cause() != null) {
                    handler.startTime = -1;
                    handler.println("Failed to connect: " + future.cause());
                }
            }
        });
    }
}
