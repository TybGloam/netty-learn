package com.edu.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Nio
 * Created by zhangxuan on 2019/3/21.
 */
public class NioServerHandler implements Runnable {

    private Selector selector;

    private ServerSocketChannel channel;

    private volatile boolean started;

    public NioServerHandler(int port) {
        try {
            //创建selector
            selector = Selector.open();
            //打开监听通道
            channel = ServerSocketChannel.open();
            //开启非阻塞
            channel.configureBlocking(false);
            //绑定socket端口
            channel.socket().bind(new InetSocketAddress(port),1024);
            //监听客户端连接请求
            channel.register(selector, SelectionKey.OP_ACCEPT);
            started = true;
            System.out.println("服务器已经启动,端口:"+port);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void stop(){
        started = false;
    }

    //线程方法
    public void run() {
        while (started){
            try {
                //无论是否有读写事件发生,selector每秒被唤醒 如果不加参数则是阻塞 直到监听到一个注册事件
                selector.select(1000);
                Set<SelectionKey> keys = selector.selectedKeys();

                Iterator<SelectionKey> iterator = keys.iterator();
                SelectionKey key = null;
                while (iterator.hasNext()){
                    key = iterator.next();

                    iterator.remove();

                    try {
                        handleKey(key);
                    } catch (IOException e) {
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


    /**
     *
     * 处理selectionkey的方法
     * @param key
     */
    private void handleKey(SelectionKey key) throws IOException {
        //当这个key有效时:
        if (key.isValid()){
            //处理新接入的请求消息
            if(key.isAcceptable()){
                //可以通过ssc的accept创建socketchannel实例
                ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                //完成该动作表示完成三次握手
                SocketChannel channel = ssc.accept();
                //非阻塞
                channel.configureBlocking(false);
                //注册为读
                channel.register(selector,SelectionKey.OP_READ);
            }
            //假如是可读的
            if (key.isReadable()){
                SocketChannel sc = (SocketChannel) key.channel();
                //创建bytebuffer,开辟1M的缓存区
                ByteBuffer buffer = ByteBuffer.allocate(1024);

                int read = sc.read(buffer);

                if (read > 0){
                    //刷新postion与limit指针 读之前把postion指到0 limit指到字符串的最后
                    buffer.flip();

                    byte[] bytes = new byte[buffer.remaining()];

                    buffer.get(bytes);

                    String msg = new String(bytes, "UTF-8");
                    System.out.println("服务器收到消息:"+msg);

                    resp(sc,msg);


                }else if (read < 0){
                    key.cancel();
                    sc.close();
                }


            }

        }

    }

    private void resp(SocketChannel sc,String msg) throws IOException {
        byte[] bytes = msg.getBytes();
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);

        buffer.put(bytes);
        //写之后flip
        buffer.flip();

        sc.write(buffer);
    }
}
