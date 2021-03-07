package com.yyj.nettychat.entity;

import java.util.Date;

public class Message {

    private Integer type;// 消息类型

    private ChatMessage message;// 聊天信息

    private Object ext;// 附加信息 文件类型

    private Date createtime;

    public Message() {
        createtime = new Date();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public ChatMessage getMessage() {
        return message;
    }

    public void setMessage(ChatMessage message) {
        this.message = message;
    }

    public Object getExt() {
        return ext;
    }

    public void setExt(Object ext) {
        this.ext = ext;
    }

    @Override
    public String toString() {
        return "Message{" + "type=" + type + ", message=" + message + ", ext=" + ext + '}';
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

}
