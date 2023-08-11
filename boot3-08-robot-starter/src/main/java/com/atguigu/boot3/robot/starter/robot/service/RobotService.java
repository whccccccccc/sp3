package com.atguigu.boot3.robot.starter.robot.service;

import com.atguigu.boot3.robot.starter.robot.properties.RobotProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RobotService {

    @Autowired
    private RobotProperties robotProperties;

    public String sayHello() {
        return "hello: " + robotProperties.getName();
    }

}
