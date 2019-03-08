package com.edu.server.channel;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * 服务端的ChannelHandler
 * 实现负责接收并相应事件通知
 * 一般集成ChannelInboundHandlerAdapter足够 因为他提供了默认实现
 */
@ChannelHandler.Sharable//该注解表示一个Channel-Handler可以被多个Channel安全地共享
public class ServerChannelHandler extends ChannelInboundHandlerAdapter {

    /**
     * 对于每个传入的消息都要调用
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf bb = (ByteBuf) msg;
        //把消息打印到控制台
        System.out.println(System.currentTimeMillis()+"Server received: " + bb.toString(CharsetUtil.UTF_8));
        //将当前消息写回给client
        ctx.write(bb);
    }

    /**
     * 通知ChannelInboundHandler
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println(System.currentTimeMillis()+"消息未决");
        //将未决消息冲刷到远程节点,并且关闭该Channel
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 对于异常处理
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
