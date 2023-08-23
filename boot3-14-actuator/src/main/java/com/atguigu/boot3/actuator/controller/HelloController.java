package com.atguigu.boot3.actuator.controller;

import com.atguigu.boot3.actuator.component.MyHahaComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    private MyHahaComponent myHahaComponent;

    @GetMapping("/hello")
    public String hello() {
        myHahaComponent.hello();
        return "hello";
    }
}
