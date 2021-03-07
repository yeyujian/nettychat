package com.yyj.nettychat.config.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TopicRabbitConfig {
    // 绑定键
    public final static String FRIENDREQUIRE = "friend.require"; // 好友请求发送
    public final static String FRIENDACCEPT = "friend.accept"; // 确定添加好友
    public final static String FRIENDDELETE = "friend.delete"; // 删除好友
    public final static String FRIENDIGNORE = "friend.ignore"; // 拒绝好友请求
    public final static String GROUPREQIRE = "group.require"; // 入群请求发送
    public final static String GROUPACCEPT = "group.accept"; // 同意入群
    public final static String GROUPDELETE = "group.delete"; // 踢出群组
    public final static String GROUPIGNORE = "group.ignore"; // 拒绝入群
    public final static String GROUPWITHDRAW = "group.withdraw"; // 退出群组

    @Bean
    public Queue friendRequireQueue() {
        return new Queue(TopicRabbitConfig.FRIENDREQUIRE);
    }

    @Bean
    public Queue friendAcceptQueue() {
        return new Queue(TopicRabbitConfig.FRIENDACCEPT);
    }

    @Bean
    public Queue friendDeleteQueue() {
        return new Queue(TopicRabbitConfig.FRIENDDELETE);
    }

    @Bean
    public Queue friendIgnoreQueue() {
        return new Queue(TopicRabbitConfig.FRIENDIGNORE);
    }

    @Bean
    public Queue GroupRequireQueue() {
        return new Queue(TopicRabbitConfig.GROUPREQIRE);
    }

    @Bean
    public Queue GroupIgnoreQueue() {
        return new Queue(TopicRabbitConfig.GROUPIGNORE);
    }

    @Bean
    public Queue GroupAcceptQueue() {
        return new Queue(TopicRabbitConfig.GROUPACCEPT);
    }

    @Bean
    public Queue GroupDeleteQueue() {
        return new Queue(TopicRabbitConfig.GROUPDELETE);
    }

    @Bean
    public Queue GroupWithdrawQueue() {
        return new Queue(TopicRabbitConfig.GROUPWITHDRAW);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange("topicExchange");
    }

    @Bean
    Binding bindingExchangeMessage_FriendRequire() {
        return BindingBuilder.bind(friendRequireQueue()).to(exchange()).with(FRIENDREQUIRE);
    }

    @Bean
    Binding bindingExchangeMessage_FriendAccept() {
        return BindingBuilder.bind(friendAcceptQueue()).to(exchange()).with(FRIENDACCEPT);
    }

    @Bean
    Binding bindingExchangeMessage_FriendDelete() {
        return BindingBuilder.bind(friendDeleteQueue()).to(exchange()).with(FRIENDDELETE);
    }
    @Bean
    Binding bindingExchangeMessage_FriendIgnore() {
        return BindingBuilder.bind(friendIgnoreQueue()).to(exchange()).with(FRIENDIGNORE);
    }
    @Bean
    Binding bindingExchangeMessage_GroupRequire() {
        return BindingBuilder.bind(GroupRequireQueue()).to(exchange()).with(GROUPREQIRE);
    }

    @Bean
    Binding bindingExchangeMessage_GroupAccept() {
        return BindingBuilder.bind(GroupAcceptQueue()).to(exchange()).with(GROUPACCEPT);
    }

    @Bean
    Binding bindingExchangeMessage_GroupIgnore() {
        return BindingBuilder.bind(GroupIgnoreQueue()).to(exchange()).with(GROUPIGNORE);
    }

    @Bean
    Binding bindingExchangeMessage_GroupDelete() {
        return BindingBuilder.bind(GroupDeleteQueue()).to(exchange()).with(GROUPDELETE);
    }

    @Bean
    Binding bindingExchangeMessage_GroupWithdraw() {
        return BindingBuilder.bind(GroupWithdrawQueue()).to(exchange()).with(GROUPWITHDRAW);
    }

}
