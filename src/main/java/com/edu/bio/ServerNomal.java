package com.edu.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Bio服务端源码
 * Created by zhangxuan on 2019/3/21.
 */
public class ServerNomal {
    //默认的端口号
    private static final int DEFAULT_PORT = 12345;
    //单例的serversocker
    private static ServerSocket serverSocket;

    private static ExecutorService es = Executors.newFixedThreadPool(4);

    public synchronized static void start() throws IOException {
        start(DEFAULT_PORT);
    }

    public synchronized static void start(int port) throws IOException {
        if (serverSocket != null) return;

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("服务器已经启动,端口号:"+port);
            while (true){
                //accept为阻塞方法,如果没有监听到客户端,阻塞在accept上
                Socket accept = serverSocket.accept();
                //如果监听到客户端接入,新启动线程加载逻辑
                es.execute(new ServerHandler(accept));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //加入进入finally时socket没有关闭 关闭socket
            if (serverSocket != null){
                serverSocket.close();
                serverSocket = null;
            }
        }

    }


}
