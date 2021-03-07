package com.yyj.nettychat.config.nettycontroller;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;


/**
 * Netty的服务端,与SpringBoot整合
 * 修改为单例模式启动---静态内部类
 */
@Component
public class WebSocketServer {

    private EventLoopGroup bossGroup;//主线程组

    private EventLoopGroup workerGroup;//工作线程组

    private ServerBootstrap server;//服务器

    private ChannelFuture future; //回调


    /**
     * 私有构造器
     */
    private WebSocketServer() {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        server = new ServerBootstrap();
        server.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new WebSocketInitializer());
    }

    /**
     * 静态内部类
     */
    private static class SingleWebSocketServer {
        static final WebSocketServer instance = new WebSocketServer();
    }

    /**
     * 公开获取的静态方法
     */
    public static WebSocketServer getInstance() {
        return SingleWebSocketServer.instance;
    }

    /**
     * 服务器启动方法
     */
    public void start()  {
        try {
            future = server.bind(8888);
            System.out.println("chat服务器启动成功!!!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void destroy() {
        System.out.println("关闭netty");
        future.channel().closeFuture().syncUninterruptibly();
        ChatHandler.getChannelGroup().close();
        //优雅关闭
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }
}
