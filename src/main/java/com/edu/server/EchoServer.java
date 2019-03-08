package com.edu.server;

import com.edu.server.channel.ServerChannelHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class EchoServer {

    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public void start() throws InterruptedException {
        final ServerChannelHandler serverChannelHandler = new ServerChannelHandler();
        //1.创建Event-LoopGroup
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            //2.创建ServerBootstrap
            ServerBootstrap b = new ServerBootstrap();
            b.group(group).channel(NioServerSocketChannel.class)//创建使用的Nio传输的Channel
                    .localAddress(new InetSocketAddress(port))//使用指定的端口设置套接字地址 本地地址
                    .childHandler(new ChannelInitializer<SocketChannel>() {//添加一个handler到子channel的channelPipeline
                        //当一个新的连接被接收是,一个新的子channel被创建
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //addLast方法可以把你自己声明的一个handler实例加入到pipeline中
                            socketChannel.pipeline().addLast(serverChannelHandler);
                        }

                    });

            ChannelFuture f = b.bind().sync();//3.异步阻塞绑定服务器
            f.channel().closeFuture().sync();//4.异步阻塞获取channel的closeFuture
        }  finally {
            group.shutdownGracefully().sync();//5.关闭group 释放所有资源
        }

    }
}
