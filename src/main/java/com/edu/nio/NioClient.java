package com.edu.nio;

import java.io.IOException;

/**
 * Created by zhangxuan on 2019/3/21.
 */
public class NioClient {

    private static final String DEFAULT_ADDR = "127.0.0.1";

    private static final int DEFAULT_PORT = 12345;

    private static NioClientHandler handler;

    public static void start(){
        start(DEFAULT_ADDR,DEFAULT_PORT);
    }

    public synchronized static void start(String addr, int port){
        if (handler != null) handler.stop();
        handler = new NioClientHandler(addr,port);
        new Thread(handler).start();
    }

    public static void sendMsg(String msg) throws IOException {
        handler.sendMsg(msg);

    }

    public static void main(String[] args) throws IOException, InterruptedException {
        start();
        Thread.sleep(1000);
        for (int i = 0; i < 10; i++) {

            sendMsg(Thread.currentThread().getName()+" "+i);
        }
    }

}
