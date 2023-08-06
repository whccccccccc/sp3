package com.atguigu.boot3.core.controller;

import com.atguigu.boot3.core.event.LoginSuccessEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @GetMapping("/login")
    public String login(@RequestParam String username) {


        //自动签到
        //优惠券
        //记录用户登录
        LoginSuccessEvent loginSuccessEvent = new LoginSuccessEvent(username);
        applicationEventPublisher.publishEvent(loginSuccessEvent);
        return username + "登陆成功";
    }

}
