package com.yyj.nettychat.config.nettycontroller;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

public class WebSocketInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new HttpServerCodec())
                .addLast(new ChunkedWriteHandler())
                .addLast(new HttpObjectAggregator(1024 * 64))
                .addLast(new LoginCheckHandler())
                .addLast(new WebSocketServerProtocolHandler("/ws"))
                //添加Netty空闲超时检查的支持
                //30:读空闲超时,60:写空闲超时,70: 读写空闲超时
                .addLast(new IdleStateHandler(30, 60, 70))
                .addLast(new HearBeatHandler())
                .addLast(new ChatHandler());

    }
}