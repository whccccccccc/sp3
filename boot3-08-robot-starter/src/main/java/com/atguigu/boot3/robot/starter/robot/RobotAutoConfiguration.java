package com.atguigu.boot3.robot.starter.robot;

import com.atguigu.boot3.robot.starter.robot.controller.RobotController;
import com.atguigu.boot3.robot.starter.robot.properties.RobotProperties;
import com.atguigu.boot3.robot.starter.robot.service.RobotService;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({RobotController.class, RobotService.class, RobotProperties.class})
@Configuration
public class RobotAutoConfiguration {
}
