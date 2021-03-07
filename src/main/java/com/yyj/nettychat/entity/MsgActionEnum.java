package com.yyj.nettychat.entity;

import java.util.Arrays;

/**
 * 发送消息类型的枚举
 */
public enum MsgActionEnum {

    CONNECT(0, "第一次(或重连)初始化连接"),
    CHATFRIEND(1, "好友聊天消息"),
    CHATGROUP(2, "群聊消息"),
    ADVISE(1000, "通知消息");

    public final Integer type;
    public final String content;

    MsgActionEnum(Integer type, String content) {
        this.type = type;
        this.content = content;
    }

    public Integer getType() {
        return type;
    }

    public static MsgActionEnum fromType(Integer type) {
        if (type < 0) {
            return null;
        }
        return Arrays.stream(MsgActionEnum.values())
                .filter(a -> type == a.getType())
                .findFirst()
                .orElse(null);
    }
}
