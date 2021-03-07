package com.yyj.nettychat.entity;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserChannelMap {

    private static Map<String, Channel> userChannelMap;

    static {
        userChannelMap = new ConcurrentHashMap<>();
    }


    /**
     * 建立用户和通道直接的关联
     *
     * @param userid
     * @param channel
     */
    public static void put(String userid, Channel channel) {
        userChannelMap.put(userid, channel);
    }

    /**
     * 解除用户和通道直接的关系
     *
     * @param userid
     */
    public static void remove(String userid) {
        userChannelMap.remove(userid);
    }

    /**
     * 根据用户id,获取通道
     */
    public static Channel get(String userid) {
        return userChannelMap.get(userid);
    }

    public static void removeByChannelId(String channelId) {
        for (String userId : userChannelMap.keySet()) {
            if (userChannelMap.get(userId).id().asLongText().equals(channelId)) {
                System.out.println("客户端连接断开,取消用户" + userId + "与通道" + channelId + "的关联");
                userChannelMap.remove(userId);
                break;
            }

        }
    }
}
