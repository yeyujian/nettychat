package com.yyj.nettychat.controller;

import com.yyj.nettychat.entity.ResponseResult;
import com.yyj.nettychat.model.Friendship;
import com.yyj.nettychat.model.User;
import com.yyj.nettychat.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@RestController
public class FriendController {

    @Autowired
    private FriendService friendService;

    /**
     * 好友请求
     * 
     * @param id
     * @param httpSession
     * @return
     */
    @GetMapping("/friend/send/{id:\\d+}")
    public ResponseResult<Friendship> sendRequire(@PathVariable String id, HttpSession httpSession) {
        String userid = (String) httpSession.getAttribute("userid");
        ResponseResult<Friendship> responseResult = new ResponseResult<>();
        Friendship friendship = new Friendship();
        friendship.setState(0);
        friendship.setCreatetime(new Date());
        friendship.setFromid(userid);
        friendship.setToid(id);
        if (friendService.addFriend(friendship)) {
            responseResult.setData(friendship);
            responseResult.setMsg("请求已经发送！");
            responseResult.setStatus(10200);
        } else {
            responseResult.setData(friendship);
            responseResult.setMsg("请求发送失败！");
            responseResult.setStatus(10407);
        }
        return responseResult;
    }

    /**
     * 获取好友列表
     * 
     * @param httpSession
     * @return
     */
    @GetMapping("/friend/findall")
    public ResponseResult<List<User>> findAllFriends(HttpSession httpSession) {
        String userid = (String) httpSession.getAttribute("userid");
        ResponseResult<List<User>> responseResult = new ResponseResult<>();
        List<User> list = friendService.findFriendsByUserid(userid);
        responseResult.setData(list);
        responseResult.setMsg("获取好友列表成功");
        responseResult.setStatus(10200);
        return responseResult;
    }

    /**
     * 删除好友
     * 
     * @param id
     * @param httpSession
     * @return
     */
    @RequestMapping(value = "/friend/delete/{id:\\d+}", method = RequestMethod.DELETE)
    public ResponseResult<Friendship> deleteFriendship(@PathVariable String id, HttpSession httpSession) {
        String userid = (String) httpSession.getAttribute("userid");
        ResponseResult<Friendship> responseResult = new ResponseResult<>();
        Friendship friendship = new Friendship();
        friendship.setFromid(id);
        friendship.setToid(userid);
        if (friendService.ignoreFriendship(friendship)) {
            responseResult.setData(friendship);
            responseResult.setMsg("删除好友成功");
            responseResult.setStatus(10200);
        } else {
            responseResult.setData(friendship);
            responseResult.setMsg("删除好友失败");
            responseResult.setStatus(10400);
        }
        return responseResult;
    }

    /**
     * 拒绝添加好友
     * 
     * @param id
     * @param httpSession
     * @return
     */
    @RequestMapping(value = "/friend/ignore/{id:\\d+}", method = RequestMethod.DELETE)
    public ResponseResult<Friendship> ignoreFriendRequire(@PathVariable String id, HttpSession httpSession) {
        String userid = (String) httpSession.getAttribute("userid");
        ResponseResult<Friendship> responseResult = new ResponseResult<>();
        Friendship friendship = new Friendship();
        friendship.setFromid(id);
        friendship.setToid(userid);
        if (friendService.ignoreFriendship(friendship)) {
            responseResult.setData(friendship);
            responseResult.setMsg("已经拒绝");
            responseResult.setStatus(10200);
        } else {
            responseResult.setData(friendship);
            responseResult.setMsg("拒绝失败");
            responseResult.setStatus(10400);
        }
        return responseResult;
    }

    /**
     * 接受好友请求
     * 
     * @param id
     * @param httpSession
     * @return
     */
    @GetMapping("friend/accept/{id:\\d+}")
    public ResponseResult<Friendship> acceptFriendRequire(@PathVariable String id, HttpSession httpSession) {
        String userid = (String) httpSession.getAttribute("userid");
        ResponseResult<Friendship> responseResult = new ResponseResult<>();
        Friendship friendship = new Friendship();
        friendship.setState(1);
        friendship.setFromid(id);
        friendship.setToid(userid);
        if (friendService.acceptFriendship(friendship)) {
            responseResult.setData(friendship);
            responseResult.setMsg("同意添加好友");
            responseResult.setStatus(10200);
        } else {
            responseResult.setData(friendship);
            responseResult.setMsg("同意操作失败");
            responseResult.setStatus(10400);
        }
        return responseResult;
    }
}
