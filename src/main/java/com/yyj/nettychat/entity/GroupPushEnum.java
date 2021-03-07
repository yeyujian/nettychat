package com.yyj.nettychat.entity;

import java.util.Arrays;
public enum GroupPushEnum {
    REQUIRE(200, "发送入群请求"),
    ACCEPT(201, "接受入群请求"),
    IGNORE(202,"拒绝入群请求"),
    DELETE(203,"踢出群组"),
    WITHDRAW(204,"退出群组");

    private int code;
    private String msg;

    GroupPushEnum(int code, String msg) {
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

    public static GroupPushEnum fromCode(Integer code) {
        if (code < 0) {
            return null;
        }
        return Arrays.stream(GroupPushEnum.values())
                .filter(a -> code == a.getCode())
                .findFirst()
                .orElse(null);
    }
}