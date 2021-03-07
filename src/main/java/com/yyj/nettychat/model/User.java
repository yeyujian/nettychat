package com.yyj.nettychat.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.yyj.nettychat.entity.ResponseResult;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.Date;


public class User implements java.io.Serializable{

    public interface UserInfo{};

    @JsonView({UserInfo.class, ResponseResult.DefaultResult.class})
    private String userid;

    @JsonView({UserInfo.class, ResponseResult.DefaultResult.class})
    private String username;

    @Size(max = 10)
    private String password;

    @JsonView({UserInfo.class, ResponseResult.DefaultResult.class})
    private String nickname;

    @Email
    @JsonView({UserInfo.class, ResponseResult.DefaultResult.class})
    private String email;

    @JsonView({UserInfo.class, ResponseResult.DefaultResult.class})
    private String photo;

    private Date createtime;

    private String roleid;

    @Override
    public String toString() {
        return "User{" +
                "userid='" + userid + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", photo='" + photo + '\'' +
                ", createtime=" + createtime +
                ", roleid='" + roleid + '\'' +
                '}';
    }

    public String getRoleid() {

        return roleid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}
