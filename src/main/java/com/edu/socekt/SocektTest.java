package com.edu.socekt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocektTest {

    public void socektTest(int port) throws IOException {
        //1.创建Socket并监听端口的连接请求
        ServerSocket serverSocket = new ServerSocket(port);
        //2.accept被调用
        Socket clientSocket = serverSocket.accept();
        //3.创建Socket的输入输出流
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        //4.循环
        String request,resp;
        while ((request = in.readLine()) != null){
            if ("Done".equals(request)){
                break;
            }
        }
        resp = process(request);
        System.out.println(resp);

    }

    public String process(String request){
        return request;
    }

    public static void main(String[] args) {
        SocektTest socektTest = new SocektTest();
    }
}
