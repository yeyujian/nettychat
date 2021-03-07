package com.yyj.nettychat.entity;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class ChannelMap {

    private Channel channel;
    private String userid;

    public ChannelMap(Channel channel, String userid) {
        this.channel = channel;
        this.userid = userid;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

}
