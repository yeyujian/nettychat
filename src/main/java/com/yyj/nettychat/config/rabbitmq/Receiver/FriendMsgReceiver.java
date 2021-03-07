package com.yyj.nettychat.config.rabbitmq.Receiver;

import com.alibaba.fastjson.JSON;
import com.yyj.nettychat.config.rabbitmq.TopicRabbitConfig;
import com.yyj.nettychat.entity.*;
import com.yyj.nettychat.model.Friendship;
import com.yyj.nettychat.util.RedisUtils;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * friendship对象作为好友操作的主体消息处理 toid!=null && state==0 好友请求 toid==null && state==0
 * 拒绝好友 toid!=null && state==1 同意请求 toid==null && state==1 删除好友
 */
@Component
public class FriendMsgReceiver {

    @Autowired
    private RedisUtils redisUtils;

    @RabbitListener(queues = TopicRabbitConfig.FRIENDREQUIRE)
    public void friendRequireProcess(Map<String, Object> msg) {
        Friendship friendship = (Friendship) msg.get("msg");
        friendship.setState(0);
        Channel channel = UserChannelMap.get(friendship.getToid());
        Message message = new Message();
        message.setExt(friendship);
        message.setType(MsgActionEnum.ADVISE.getType() + FriendPushEnum.REQUIRE.getCode());
        if (channel != null) {
            channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message))); // 直接发送请求
        } else
            redisUtils.listAdd("friendAdvise:" + friendship.getToid(), JSON.toJSONString(message)); // 好友请求追加到redis
    }

    @RabbitListener(queues = TopicRabbitConfig.FRIENDACCEPT)
    public void friendAcceptProcess(Map<String, Object> msg) {
        Friendship friendship = (Friendship) msg.get("msg");
        friendship.setState(1);
        Channel channel = UserChannelMap.get(friendship.getToid());
        Message message = new Message();
        message.setExt(friendship);
        message.setType(MsgActionEnum.ADVISE.getType() + FriendPushEnum.ACCEPT.getCode());
        if (channel != null) { // 如果用户在线
            channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message))); // 直接发送通知
        } else
            redisUtils.listAdd("friendAdvise:" + friendship.getToid(), JSON.toJSONString(message)); // 同意好友请求通知添加到redis
    }

    @RabbitListener(queues = TopicRabbitConfig.FRIENDDELETE)
    public void friendDeleteProcess(Map<String, Object> msg) {
        Friendship friendship = (Friendship) msg.get("msg");
        friendship.setState(1);
        Channel channel = UserChannelMap.get(friendship.getToid());
        Message message = new Message();
        friendship.setToid(null);
        message.setExt(friendship);
        message.setType(MsgActionEnum.ADVISE.getType() + FriendPushEnum.DELETE.getCode());
        if (channel != null) { // 如果用户在线
            channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message))); // 直接发送通知
        } else {
            redisUtils.listAdd("friendAdvise:" + friendship.getToid(), JSON.toJSONString(message)); // 删除好友通知添加到redis
        }
    }

    @RabbitListener(queues = TopicRabbitConfig.FRIENDIGNORE)
    public void friendIgnoreProcess(Map<String, Object> msg) {
        Friendship friendship = (Friendship) msg.get("msg");
        friendship.setState(0);
        Channel channel = UserChannelMap.get(friendship.getToid());
        Message message = new Message();
        friendship.setToid(null);
        message.setExt(friendship);
        message.setType(MsgActionEnum.ADVISE.getType() + FriendPushEnum.IGNORE.getCode());
        if (channel != null) { // 如果用户在线
            channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message))); // 直接发送通知
        } else {
            redisUtils.listAdd("friendAdvise:" + friendship.getToid(), JSON.toJSONString(message)); // 删除好友通知添加到redis
        }
    }
}
