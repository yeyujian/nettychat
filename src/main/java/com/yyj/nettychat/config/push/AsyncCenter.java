package com.yyj.nettychat.config.push;

import com.yyj.nettychat.config.rabbitmq.TopicRabbitConfig;
import com.yyj.nettychat.entity.FriendPushEnum;
import com.yyj.nettychat.entity.GroupPushEnum;
import com.yyj.nettychat.model.Group;
import com.yyj.nettychat.model.GroupMember;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 异步发送推送消息
 */
@Component
public class AsyncCenter {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Async
    public void sendPush(Object msgCode, Object object) {

        Map<String, Object> msgMap = new HashMap<>();
        msgMap.put("msg", object);
        if (msgCode instanceof FriendPushEnum) {
            switch (FriendPushEnum.fromCode(((FriendPushEnum) msgCode).getCode())) {
                case REQUIRE:
                    rabbitTemplate.convertAndSend("topicExchange", TopicRabbitConfig.FRIENDREQUIRE, msgMap);
                    break;
                case ACCEPT:
                    rabbitTemplate.convertAndSend("topicExchange", TopicRabbitConfig.FRIENDACCEPT, msgMap);
                    break;
                case IGNORE:
                    rabbitTemplate.convertAndSend("topicExchange", TopicRabbitConfig.FRIENDIGNORE, msgMap);
                    break;
                case DELETE:
                    rabbitTemplate.convertAndSend("topicExchange", TopicRabbitConfig.FRIENDDELETE, msgMap);
                    break;
            }
        } else if (msgCode instanceof GroupPushEnum) {
            switch (GroupPushEnum.fromCode(((GroupPushEnum) msgCode).getCode())) {
                case REQUIRE:
                    rabbitTemplate.convertAndSend("topicExchange", TopicRabbitConfig.GROUPREQIRE, msgMap);
                    break;
                case ACCEPT:
                    rabbitTemplate.convertAndSend("topicExchange", TopicRabbitConfig.GROUPACCEPT, msgMap);
                    break;
                case IGNORE:
                    rabbitTemplate.convertAndSend("topicExchange", TopicRabbitConfig.GROUPIGNORE, msgMap);
                    break;
                case DELETE:
                    Group group = new Group();
                    for (GroupMember menber : ((Group) object).getMembers()) {
                        group.setMasterid(menber.getMember().getUserid());
                        group.getMembers().set(0, menber);
                        menber.setStatus(-1);
                        msgMap.put("msg", group);
                        rabbitTemplate.convertAndSend("topicExchange", TopicRabbitConfig.GROUPDELETE, msgMap);
                    }
                    break;
                case WITHDRAW:
                    rabbitTemplate.convertAndSend("topicExchange", TopicRabbitConfig.GROUPWITHDRAW, msgMap);
                    break;
            }
        }
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
