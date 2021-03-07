package com.yyj.nettychat.model;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(value = { "handler" }) // json序列化时忽略bean中的一些属性序列化和反序列化时抛出的异常
public class Group implements java.io.Serializable {

    private static final long serialVersionUID = 14567676L;

    private String id;

    private String name;

    private String masterid;

    private Date createtime;

    private List<GroupMember> members;

    public Group() {

    }

    public void addMember(GroupMember groupMember) {
        if (this.members == null)
            this.members = new LinkedList<>();
        this.members.add(groupMember);
    }

    public String getMasterid() {
        return masterid;
    }

    public void setMasterid(String masterid) {
        this.masterid = masterid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public List<GroupMember> getMembers() {
        return members;
    }

    public void setMembers(List<GroupMember> members) {
        this.members = members;
    }

    @Override
    public String toString() {
        return "Group{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", masterid='" + masterid + '\''
                + ", createtime=" + createtime + ", members=" + members + '}';
    }

}
