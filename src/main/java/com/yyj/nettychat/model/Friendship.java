package com.yyj.nettychat.model;

import java.util.Date;
import java.util.List;

public class Friendship implements java.io.Serializable{

    private String id;

    private String fromid;// 添加好友请求发送方

    private String toid; //添加好友请求接受方

    private int state;// 好友状态 0=待添加  1=接受

    private Date createtime;

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFromid() {
        return fromid;
    }

    public void setFromid(String fromid) {
        this.fromid = fromid;
    }

    public String getToid() {
        return toid;
    }

    public void setToid(String toid) {
        this.toid = toid;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Friendship{" +
                "id='" + id + '\'' +
                ", fromid='" + fromid + '\'' +
                ", toid='" + toid + '\'' +
                ", state=" + state +
                ", createtime=" + createtime +
                '}';
    }
}
