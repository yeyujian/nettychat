package com.yyj.nettychat.config.nettycontroller;

import com.yyj.nettychat.entity.Message;
import com.yyj.nettychat.entity.MsgActionEnum;
import com.yyj.nettychat.entity.UserChannelMap;
import com.yyj.nettychat.model.Group;
import com.yyj.nettychat.model.GroupMember;
import com.yyj.nettychat.service.GroupService;
import com.yyj.nettychat.service.UserService;
import com.yyj.nettychat.util.RedisUtils;
import com.yyj.nettychat.util.SpringUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;

import java.text.SimpleDateFormat;

/**
 * 处理聊天
 */
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    /**
     * 调测日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatHandler.class);

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:MM");

    private static UserService userService;

    private static RedisUtils redisUtils;

    private static GroupService groupService;

    public ChatHandler() {

    }

    static {
        userService = SpringUtil.getBean(UserService.class);
        redisUtils = SpringUtil.getBean(RedisUtils.class);
        groupService = SpringUtil.getBean(GroupService.class);
    }

    // 定义channel集合,管理channel,传入全局事件执行器
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public static ChannelGroup getChannelGroup() {
        return channelGroup;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame textWebSocketFrame) throws Exception {
        String content = textWebSocketFrame.text();
        Channel channel = null; // 发送目标的客户端
        Message message = JSON.parseObject(content, Message.class);
        String userid = null, friendid = null, groupid = null;
        userid = message.getMessage().getUserid();
        groupid = message.getMessage().getGroupid();
        friendid = message.getMessage().getFriendid();
        AttributeKey<String> key = AttributeKey.valueOf("perssion:userid");
        if (!ctx.channel().attr(key).get().equals(userid)) {
            throw new Exception("身份不匹配，非法连接");
        } // 检测是否本人userid
        switch (MsgActionEnum.fromType(message.getType())) {
            case CONNECT:// 处理刚连接信息
                System.out.println(userid + "与" + ctx.channel().id() + "建立了关联");
                // 获取未读信息
                List<String> list = redisUtils.getListAndRemove("UnReadMsg:" + userid);
                if (list != null) {
                    for (Iterator<String> it = list.iterator(); it.hasNext();) {
                        String str = it.next();
                        ctx.channel().writeAndFlush(new TextWebSocketFrame(str));
                    }
                }
                list = redisUtils.getListAndRemove("friendAdvise:" + userid);
                if (list != null) {
                    for (Iterator<String> it = list.iterator(); it.hasNext();) {
                        String str = it.next();
                        ctx.channel().writeAndFlush(new TextWebSocketFrame(str));
                    }
                }
                list = redisUtils.getListAndRemove("groupAdvise:" + userid);
                if (list != null) {
                    for (Iterator<String> it = list.iterator(); it.hasNext();) {
                        String str = it.next();
                        ctx.channel().writeAndFlush(new TextWebSocketFrame(str));
                    }
                }
                UserChannelMap.put(userid, ctx.channel()); // 将channel加入到 map 正式建立连接
                break;
            case CHATFRIEND:// 发送好友信息
                channel = UserChannelMap.get(userid); // 先检测连接已加入map
                if (channel == null) {
                    System.out.println("未进行连接确认");
                    throw new Exception("未进行连接确认");
                }

                if (friendid == null || groupid != null) {
                    System.out.println("非法消息发送");
                    throw new Exception("非法消息发送");

                }
                System.out.printf("%s 向 %s 发送消息:%s \n", userid, friendid, message.getMessage().getMessage());
                channel = UserChannelMap.get(friendid);
                if (channel == null) { // 好友不在线
                    System.out.println(friendid.concat(":不在线"));
                    redisUtils.listAdd("UnReadMsg:" + friendid, content); // 缓存未读消息
                } else {
                    channel.writeAndFlush(new TextWebSocketFrame(content));// 发送消息
                }
                channel = UserChannelMap.get(userid); // 将消息发给自己
                if (channel == null) { // 好友不在线
                    System.out.println(friendid.concat(":不在线"));
                    redisUtils.listAdd("UnReadMsg:" + userid, content); // 缓存未读消息
                } else {
                    channel.writeAndFlush(new TextWebSocketFrame(content));// 发送消息
                }
                break;
            case CHATGROUP:// 发送群聊消息
                channel = UserChannelMap.get(userid); // 先检测连接已加入map
                if (channel == null) {
                    System.out.println("未进行连接确认");
                    throw new Exception("未进行连接确认");
                }
                if (friendid != null || groupid == null) {
                    System.out.println("非法消息发送");
                    throw new Exception("非法消息发送");
                }
                Group group = new Group();
                group.setId(groupid);
                group = groupService.getGroup(group, userid);
                for (GroupMember groupMenber : group.getMembers()) {
                    friendid = groupMenber.getMember().getUserid();
                    channel = UserChannelMap.get(friendid); // 获取群员通信通道
                    if (channel == null) { // 群员不在线
                        System.out.println(friendid.concat(":不在线"));
                        redisUtils.listAdd("UnReadMsg:" + friendid, content); // 缓存未读消息
                    } else {
                        channel.writeAndFlush(new TextWebSocketFrame(content));// 发送消息
                    }
                }
                break;
            default:
                break;
        }

    }

    // 新客户端连接时自行调用
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {

        channelGroup.add(ctx.channel());
        // channelGroup.writeAndFlush(new TextWebSocketFrame("有新用户成员"));
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object event) throws Exception {

        // 握手成功以后，查询用户未读消息，发送未读消息
        if (event instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            System.out.println("触发握手成功事件");
        }

    }

    // 出现异常是关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
        System.out.println("出现异常.....关闭通道!");
        System.out.println(cause.getMessage());
        UserChannelMap.removeByChannelId(ctx.channel().id().asLongText());
        channelGroup.remove(ctx.channel());
        LOGGER.info(cause.getMessage());
    }

    // 当客户端关闭连接时关闭通道
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("关闭通道");
        UserChannelMap.removeByChannelId(ctx.channel().id().asLongText());
        channelGroup.remove(ctx.channel());
        ctx.channel().close();
    }

}
