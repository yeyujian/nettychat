package com.yyj.nettychat.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.yyj.nettychat.entity.ResponseResult;
import com.yyj.nettychat.model.User;
import com.yyj.nettychat.service.UserService;
import com.yyj.nettychat.util.MD5Utils;
import com.yyj.nettychat.util.RedisUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpSession;

@RestController
public class UserController {

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/user/login", method = RequestMethod.GET)
    public ResponseResult<User> login(HttpSession session, User user) throws Exception {
        ResponseResult<User> result = new ResponseResult<>();
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(),
                MD5Utils.getMD5Str(user.getPassword()));
        try {
            subject.login(token);
            user = (User) subject.getPrincipal();
            session.setAttribute("userid", ((User) subject.getPrincipal()).getUserid());
            redisUtils.set("session:userid:" + session.getId(), ((User) subject.getPrincipal()).getUserid());
            result.setStatus(10200);
            user.setPassword(null);
            result.setData(user);
            result.setMsg("登录成功");
        } catch (UnknownAccountException e) {
            result.setStatus(10402);
            result.setMsg("账号不存在");
        } catch (IncorrectCredentialsException e) {
            result.setStatus(10401);
            result.setMsg("密码错误");
        }
        return result;
    }

    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    public ResponseResult<User> register(User user) {
        ResponseResult<User> result = new ResponseResult<>();
        user.setRoleid("2");
        if (userService.register(user)) {
            result.setStatus(10200);
            result.setMsg("注册成功");
        } else {
            result.setStatus(10403);
            result.setMsg("注册失败");
        }
        user.setPassword(null);
        result.setData(user);
        return result;
    }

    @RequestMapping(value = "/user/update/nicename", method = RequestMethod.PUT)
    public ResponseResult<User> updateNickName(HttpSession session, User user) {
        ResponseResult<User> result = new ResponseResult<>();
        user.setUserid((String) session.getAttribute("userid"));
        if (userService.updateNickName(user)) {
            result.setStatus(10200);
            result.setData(user);
            result.setMsg("修改成功");
        } else {
            result.setStatus(10404);
            result.setData(user);
            result.setMsg("修改失败");
        }
        return result;
    }

    @RequestMapping(value = "/user/update/passowrd", method = RequestMethod.PUT)
    public ResponseResult<User> updatePassword(HttpSession session, User user) {
        ResponseResult<User> result = new ResponseResult<>();
        user.setUserid((String) session.getAttribute("userid"));
        if (userService.updatePassword(user)) {
            result.setStatus(10200);
            result.setData(user);
            result.setMsg("修改成功");
        } else {
            result.setStatus(10405);
            result.setData(user);
            result.setMsg("修改失败");
        }
        return result;
    }

    @RequestMapping(value = "/user/update/upload", method = RequestMethod.POST)
    public ResponseResult<User> upload(@RequestParam("file") MultipartFile file, HttpSession session) {
        ResponseResult<User> result = new ResponseResult<>();
        User user = new User();
        user.setUserid((String) session.getAttribute("userid"));
        if ((user = userService.upload(file, user)) != null) {
            result.setStatus(10200);
            result.setData(user);
            result.setMsg("修改头像成功");
        } else {
            result.setStatus(10406);
            result.setData(user);
            result.setMsg("修改头像失败");
        }
        return result;
    }

    @GetMapping("/user/find/{name}")
    @JsonView(ResponseResult.DefaultResult.class)
    public ResponseResult<User> findUser(HttpSession session, @PathVariable String name) {
        ResponseResult<User> result = new ResponseResult<>();
        User user = userService.getUserByName(name);
        if (user != null) {
            result.setData(user);
            result.setMsg("查找成功");
            result.setStatus(10200);
        } else {
            result.setMsg("查找不到此用户");
            result.setStatus(10400);
        }
        return result;
    }

    @GetMapping("/user/findbyid/{id:\\d+}")
    @JsonView(ResponseResult.DefaultResult.class)
    public ResponseResult<User> findUserById(HttpSession session, @PathVariable String id) {
        ResponseResult<User> result = new ResponseResult<>();
        User user = userService.findById(id);
        if (user != null) {
            result.setData(user);
            result.setMsg("查找成功");
            result.setStatus(10200);
        } else {
            result.setMsg("查找不到此用户");
            result.setStatus(10400);
        }
        return result;
    }
}
