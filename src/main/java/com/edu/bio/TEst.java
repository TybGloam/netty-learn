package com.edu.bio;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zhangxuan on 2019/3/21.
 */
public class TEst {

    public static void main(String[] args) throws InterruptedException, IOException {
        new Thread(new Runnable() {
            public void run() {
                try {
                    ServerNomal.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        Thread.sleep(1000);

        ExecutorService es = Executors.newFixedThreadPool(10);

        Runnable r = new Runnable() {
            public void run() {
                try {
                    Client.send(Thread.currentThread().getName()+"线程发送");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        for (int i = 0; i < 10; i++) {
            es.execute(r);

        }
        es.shutdown();

        Client.send("q");

    }
}
