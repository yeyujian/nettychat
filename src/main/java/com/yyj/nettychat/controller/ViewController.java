package com.yyj.nettychat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ViewController {
    @RequestMapping("/")
    public String index() {
        return "chattest";
    }

    @RequestMapping("/user/add")
    public String add() {
        return "user/add";
    }

    @RequestMapping("/user/update")
    public String update() {
        return "user/update";
    }

    @RequestMapping(value = "/user/loginpage", method = RequestMethod.GET)
    public String loginPage() {
        return "user/login";
    }

    @RequestMapping(value = "/user/registerpage", method = RequestMethod.GET)
    public String registerPage() {
        return "user/regist";
    }

    @RequestMapping(value = "/user/upload", method = RequestMethod.GET)
    public String uploadFace() {
        return "user/upload";
    }

}
