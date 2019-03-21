package com.edu.nio;

/**
 * NioServer
 * Created by zhangxuan on 2019/3/21.
 */
public class NioServer {
    private static int DEFAULT_PORT = 12345;

    private static NioServerHandler handler;

    public synchronized static void start(int port){
        if (handler != null) handler.stop();

        handler = new NioServerHandler(port);
        new Thread(handler).start();
    }

    public static void start(){
        start(DEFAULT_PORT);
    }

    public static void main(String[] args) {
        start();
    }

}
