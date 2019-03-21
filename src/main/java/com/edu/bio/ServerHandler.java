package com.edu.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 客户端线程处理类
 * Created by zhangxuan on 2019/3/21.
 */
public class ServerHandler implements Runnable {

    private Socket socket;

    public ServerHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //获取socekt输出流
            out = new PrintWriter(socket.getOutputStream(),true);

            String expression;

            while (true){
                //假如读到空值 跳出
                try {
                    expression = in.readLine();

                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                if (expression == null) break;

                System.out.println("服务端收到消息:"+expression);

                out.println(expression);

            }


        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                in = null;
            }

            if (out != null){
                out.close();
                out = null;
            }
            if (socket != null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                socket = null;
            }
        }
    }
}
