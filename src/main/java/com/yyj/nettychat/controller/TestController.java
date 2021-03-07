package com.yyj.nettychat.controller;

import com.yyj.nettychat.mapper.GroupMapper;
import com.yyj.nettychat.model.Group;
import com.yyj.nettychat.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.shiro.SecurityUtils;
import javax.servlet.http.HttpSession;

@Controller
public class TestController {

    @Autowired
    private GroupMapper groupMapper;

    @RequestMapping("/mmp")
    public String hello(Model model, HttpSession session) {
        System.out.println("user-==============================");
        User user =(User) SecurityUtils.getSubject().getPrincipal();
        System.out.println(user);
        model.addAttribute("userid", user.getUserid());
        model.addAttribute("username", user.getUsername());
        model.addAttribute("nickname", user.getNickname());
        model.addAttribute("useremail", user.getEmail());
        return "test";
    }

    @ResponseBody
    @RequestMapping("/tttt")
    public Group test(Model model, HttpSession session) {
        Group group = groupMapper.seleteGroup("1288095591954481152");
        // group.setMembers(null);
        return group;
    }

}
