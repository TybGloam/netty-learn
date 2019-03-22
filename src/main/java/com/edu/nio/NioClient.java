package com.edu.nio;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zhangxuan on 2019/3/21.
 */
public class NioClient {

    private static final String DEFAULT_ADDR = "127.0.0.1";

    private static final int DEFAULT_PORT = 12345;

    private NioClientHandler handler;

    public void start(){
        start(DEFAULT_ADDR,DEFAULT_PORT);
    }

    public synchronized void start(String addr, int port){
        if (handler != null) handler.stop();
        handler = new NioClientHandler(addr,port);
        new Thread(handler).start();
    }

    public void sendMsg(String msg) throws IOException {
        handler.sendMsg(msg);

    }

    public static void main(String[] args) throws IOException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        for (int i = 0; i < 5; i++) {
            executorService.execute(new Runnable() {
                public void run() {
                    NioClient nioClient = new NioClient();
                    nioClient.start();
                    try {
                        Thread.sleep(1000);
                        nioClient.sendMsg(Thread.currentThread().getName()+" "+ ((int)(Math.random()*10))+"说:Hi!\n");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

}
