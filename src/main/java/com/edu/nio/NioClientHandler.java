package com.edu.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by zhangxuan on 2019/3/21.
 */
public class NioClientHandler implements Runnable {

    private String host;

    private int port;

    private Selector selector;

    private SocketChannel socketChannel;

    private volatile boolean started;

    public NioClientHandler(String host, int port) {
        this.host = host;
        this.port = port;

        try {
            selector = Selector.open();

            socketChannel = SocketChannel.open();

            socketChannel.configureBlocking(false);

            started = true;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop(){
        started = false;
    }

    public void doconn() throws IOException {
        if (socketChannel.connect(new InetSocketAddress(host,port)));
        else socketChannel.register(selector, SelectionKey.OP_CONNECT);
        System.out.println("已经连接");
    }

    public void sendMsg(String msg) throws IOException {
        socketChannel.register(selector,SelectionKey.OP_READ);
        dowriter(socketChannel,msg);
    }


    private void handler(SelectionKey key) throws IOException {
        if (key.isValid()){
            SocketChannel sc = (SocketChannel) key.channel();
            if (key.isConnectable()){
                if (sc.finishConnect()){

                }else {
                    System.exit(1);
                }
            }


            if (key.isReadable()){
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                int read = sc.read(buffer);

                if (read > 0){
                    buffer.flip();

                    byte[] bytes = new byte[buffer.remaining()];

                    buffer.get(bytes);
                    String msg = new String(bytes,"UTF-8");
                    System.out.println(Thread.currentThread().getName()+"收到响应:"+msg);

                }else if (read < 0){
                    key.cancel();
                    sc.close();
                }


            }


        }

    }


    private void dowriter(SocketChannel socketChannel, String msg) throws IOException {

        byte[] bytes = msg.getBytes();

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put(bytes);

        buffer.flip();

        socketChannel.write(buffer);


    }

    public void run() {
        try {
            doconn();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        while (started){
            try {
                selector.select(1000);

                Set<SelectionKey> keys = selector.selectedKeys();

                Iterator<SelectionKey> it = keys.iterator();
                SelectionKey key = null;
                while (it.hasNext()){
                    key = it.next();
                    it.remove();
                    try {
                        handler(key);
                    } catch (Exception e) {
                        if (key != null){
                            key.cancel();
                            if (key.channel() != null){
                                key.channel().close();
                            }
                        }
                    }
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (selector != null){
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
