package com.edu.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 普通bio创建的客户端
 * Created by zhangxuan on 2019/3/21.
 */
public class Client {

    private static final int DEFAULT_PORT = 12345;

    private static final String DEFAULT_ADDR = "127.0.0.1";


    public static void send(String msg) throws IOException {
        send(DEFAULT_PORT,DEFAULT_ADDR,msg);
    }

    public static void send(int port,String addr,String msg) throws IOException {
        Socket socket = null;

        BufferedReader in = null;

        PrintWriter out = null;

        try {
            socket = new Socket(addr,port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //不设置自动刷新的话需要在print之后手动刷新
            out = new PrintWriter(socket.getOutputStream(),true);
            System.out.println("客户端发送消息:"+msg);
            out.println(msg);
            System.out.println("服务端响应:"+in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (in!= null){
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
                socket.close();
                socket = null;
            }
        }


    }



}
