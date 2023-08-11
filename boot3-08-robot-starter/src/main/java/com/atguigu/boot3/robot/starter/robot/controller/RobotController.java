package com.atguigu.boot3.robot.starter.robot.controller;

import com.atguigu.boot3.robot.starter.robot.service.RobotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RobotController {
    @Autowired
    private RobotService robotService;

    @GetMapping("robot/hello")
    public String sayHello() {
        String s = robotService.sayHello();
        return s;
    }

}
