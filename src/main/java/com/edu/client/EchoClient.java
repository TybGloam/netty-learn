package com.edu.client;

import io.netty.channel.nio.NioEventLoopGroup;

/**
 * 客户端的主类
 */
public class EchoClient {
    private final String host;
    private final int port;

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start(){
        NioEventLoopGroup group = new NioEventLoopGroup();
    }

}
