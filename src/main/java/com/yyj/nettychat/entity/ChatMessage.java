package com.yyj.nettychat.entity;

import java.util.Date;

public class ChatMessage {

    private String id;

    private String userid;

    private String friendid;

    private String groupid;

    private Integer hasRead;

    private String message;

    public ChatMessage() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getFriendid() {
        return friendid;
    }

    public void setFriendid(String friendid) {
        if (friendid.equals("null"))
            this.friendid = null;
        else
            this.friendid = friendid;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        if (groupid.equals("null"))
            this.groupid = null;
        else
            this.groupid = groupid;
    }

    public Integer getHasRead() {
        return hasRead;
    }

    public void setHasRead(Integer hasRead) {
        this.hasRead = hasRead;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ChatMessage{" + "userid='" + userid + '\'' + ", friendid='" + friendid + '\'' + ", groupid='" + groupid
                + '\'' + ", message='" + message + '\'' + '}';
    }
}
