package com.yyj.nettychat.entity;

import com.fasterxml.jackson.annotation.JsonView;

/**
 * 10200：表示成功 10400：操作失败 10401：表示密码错误 10402：表示账号不存在 10403：注册失败 10404：修改昵称失败
 * 10405：修改密码失败 10406：修改头像失败 10407：好友请求发送失败 10500：服务器发生错误
 * 
 * @param <T>
 */
// @Data
// @AllArgsConstructor
// @NoArgsConstructor
public class ResponseResult<T> {

    public interface DefaultResult {
    };

    // 响应业务状态
    @JsonView(DefaultResult.class)
    private Integer status;

    // 响应消息
    @JsonView(DefaultResult.class)
    private String msg;

    // 响应中的数据
    @JsonView(DefaultResult.class)
    private T data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
        if (msg == null) {
            if (status == 10200)
                this.setMsg("success");
            else
                this.setMsg("fail");
        }
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
