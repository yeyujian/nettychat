package com.yyj.nettychat.config.nettycontroller;

import com.yyj.nettychat.entity.UserChannelMap;
import com.yyj.nettychat.util.RedisUtils;
import com.yyj.nettychat.util.SpringUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.AttributeKey;

public class LoginCheckHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            String cookie = (request.headers().get("Cookie"));
//            System.out.println(cookie);
            String sessionid = cookie.substring(cookie.indexOf("=") + 1);
//            System.out.println(sessionid);
            AttributeKey<String> key = AttributeKey.valueOf("perssion:userid");
            RedisUtils redisUtils = SpringUtil.getBean(RedisUtils.class);
            String userid = redisUtils.get("session:userid:" + sessionid);
            if (redisUtils.get("shiro:session:" + sessionid) != null && userid != null) {
                //处理多设备登录
                Channel channel = UserChannelMap.get(userid);
                if (channel != null) {
                    System.out.println("多设备登录，强制关闭连接");
                    channel.close();
                    UserChannelMap.remove(userid);
                }
                ctx.channel().attr(key).setIfAbsent(userid);
                ctx.fireChannelRead(request.retain());
            } else {
                System.out.println("未登录，非法链接");
                ctx.close();//非法登录关闭
            }
        }
        ctx.fireChannelRead(msg);
    }


}
