package com.edu.nio;

import java.io.IOException;

/**
 * Created by zhangxuan on 2019/3/21.
 */
public class TTst {

    public static void main(String[] args) throws IOException {
        for (int i = 0; i < 10; i++) {
            NioClient.sendMsg("发送msg:"+i);
        }
    }
}
