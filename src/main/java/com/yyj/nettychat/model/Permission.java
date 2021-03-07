package com.yyj.nettychat.model;

public class Permission implements java.io.Serializable{

    private String pid;
    private String pname;

    public String getPidd() {
        return pid;
    }

    public void setPid(String id) {
        this.pid = id;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String name) {
        this.pname = name;
    }

    @Override
    public String toString() {
        return "Permission{" +
                "pid='" + pid + '\'' +
                ", pname='" + pname + '\'' +
                '}';
    }
}
