package com.example.volleytest.Netty;

/**
 * Created by djc on 2017/4/25.
 */

import com.example.volleytest.MainApp;
import com.example.volleytest.Utils.L;
import com.example.volleytest.Utils.NetWorkUtils;
import com.orhanobut.logger.Logger;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class NettyServer {
    private static final String TAG = "NettyServer";
    //    private static NettyServer nettyServer = null;
//    public static NettyServer getInstance() {
//        if (nettyServer == null) {
//            nettyServer = new NettyServer();
//        }
//        return nettyServer;
//    }
    SimpleChannelHandler callback;
    Channel bind;

    public NettyServer() {
        this.callback = new ServerHandler();
    }

    public NettyServer(SimpleChannelHandler callback) {
        this.callback = callback;
    }

    public void init() {
        ServerBootstrap bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
        // Set up the default event pipeline.
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(new StringDecoder(), new StringEncoder(), callback);
            }
        });
        String ip = NetWorkUtils.getWiFiIP(MainApp.getInstance());
        L.i(TAG, "wifi地址：" + ip);
        // Bind and start to accept incoming connections.
        bind = bootstrap.bind(new InetSocketAddress(ip, 8000));
//        L.i(TAG,"Server已经启动，监听端口: " + bind.getLocalAddress() + "， 等待客户端注册。。。");
        Logger.i("Server已经启动，监听端口: " + bind.getLocalAddress() + "， 等待客户端注册。。。");
    }

    public boolean write(String message) {
        if (bind.isWritable()) {
            bind.write(message);
            return true;
        }
        return false;
    }

    private class ServerHandler extends SimpleChannelHandler {
        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
            super.messageReceived(ctx, e);
            if (e.getMessage() instanceof String) {
                String message = (String) e.getMessage();
                L.i(TAG, "Client发来:" + message);
                e.getChannel().write("Server已收到刚发送的:" + message);
                L.i(TAG, "\n等待客户端输入。。。");
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
            super.exceptionCaught(ctx, e);
        }

        @Override
        public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
            L.i(TAG, "有一个客户端注册上来了。。。");
            L.i(TAG, "Client:" + e.getChannel().getRemoteAddress());
            L.i(TAG, "Server:" + e.getChannel().getLocalAddress());
            L.i(TAG, "等待客户端输入。。。");
            e.getChannel().write("nihao");
            super.channelConnected(ctx, e);
        }

        @Override
        public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
            super.channelDisconnected(ctx, e);
            L.i(TAG, "客户端关闭。。。");
        }
    }
}
