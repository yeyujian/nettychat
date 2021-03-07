package com.yyj.nettychat.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(value = {"handler"}) //json序列化时忽略bean中的一些属性序列化和反序列化时抛出的异常
public class GroupMember implements java.io.Serializable {


    private static final long serialVersionUID = 1546464L;

    private String id;

    private Date createtime;

    private User member;

    private String groupid;

    private Integer status;

    public GroupMember(){
        
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public User getMember() {
        return member;
    }

    public void setMember(User member) {
        this.member = member;
    }

    @Override
    public String toString() {
        return "GroupMenber{" + "id='" + id + '\'' + ", createtime=" + createtime + ", member=" + member + ", groupid='"
                + groupid + '\'' + '}';
    }
}
