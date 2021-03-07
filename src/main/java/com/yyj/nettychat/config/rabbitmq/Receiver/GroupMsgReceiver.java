package com.yyj.nettychat.config.rabbitmq.Receiver;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.yyj.nettychat.config.rabbitmq.TopicRabbitConfig;
import com.yyj.nettychat.entity.GroupPushEnum;
import com.yyj.nettychat.entity.Message;
import com.yyj.nettychat.entity.MsgActionEnum;
import com.yyj.nettychat.entity.UserChannelMap;
import com.yyj.nettychat.model.Group;
import com.yyj.nettychat.util.RedisUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

@Component
public class GroupMsgReceiver {

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 此处 group-》 masterid 表示的不是群主id 而是 发送目标的id
     * 
     * @param group
     * @throws CloneNotSupportedException
     */
    private void sendAdvise(Group group, GroupPushEnum code) throws CloneNotSupportedException {
        Channel channel = UserChannelMap.get(group.getMasterid());
        Message message = new Message();
        message.setExt(group.getMembers().get(0));
        message.setType(MsgActionEnum.ADVISE.getType() + code.getCode());
        if (channel != null) {
            channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message))); // 直接发送请求
        } else {
            redisUtils.listAdd("groupAdvise:" + group.getMasterid(), JSON.toJSONString(message)); // 好友请求追加到redis
        }
    }

    /**
     * masterid -> 群主id userid 群员id status=0
     * 
     * @param msg
     */
    @RabbitListener(queues = TopicRabbitConfig.GROUPREQIRE)
    public void GroupRequireProcess(Map<String, Object> msg) {
        try {
            sendAdvise((Group) msg.get("msg"), GroupPushEnum.REQUIRE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * masterid -> 群员id userid 群员id status=1
     * 
     * @param msg
     */
    @RabbitListener(queues = TopicRabbitConfig.GROUPACCEPT)
    public void GroupAcceptProcess(Map<String, Object> msg) {
        try {
            sendAdvise((Group) msg.get("msg"), GroupPushEnum.ACCEPT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * masterid -> 群员id userid 群员id status=0
     * 
     * @param msg
     */
    @RabbitListener(queues = TopicRabbitConfig.GROUPIGNORE)
    public void GroupIgnoreProcess(Map<String, Object> msg) {
        try {
            sendAdvise((Group) msg.get("msg"), GroupPushEnum.IGNORE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * masterid -> 群员id userid 群员id status=-1
     * 
     * @param msg
     */
    @RabbitListener(queues = TopicRabbitConfig.GROUPDELETE)
    public void GroupDeleteProcess(Map<String, Object> msg) {
        try {
            sendAdvise((Group) msg.get("msg"), GroupPushEnum.DELETE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * masterid -> 群主id userid 群员id status=-1
     * 
     * @param msg
     */
    @RabbitListener(queues = TopicRabbitConfig.GROUPWITHDRAW)
    public void GroupWithdrawProcess(Map<String, Object> msg) {
        try {
            sendAdvise((Group) msg.get("msg"), GroupPushEnum.WITHDRAW);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}