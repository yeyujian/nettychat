package com.yyj.nettychat.entity;

import java.util.Arrays;

public enum FriendPushEnum {
    REQUIRE(100, "发送好友请求"),
    ACCEPT(101, "接受好友请求"),
    IGNORE(102,"拒绝好友请求"),
    DELETE(103,"删除好友");

    private int code;
    private String msg;

    FriendPushEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static FriendPushEnum fromCode(Integer code) {
        if (code < 0) {
            return null;
        }
        return Arrays.stream(FriendPushEnum.values())
                .filter(a -> code == a.getCode())
                .findFirst()
                .orElse(null);
    }
}
