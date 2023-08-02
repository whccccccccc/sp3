package com.atguigu.boot303logg.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
public class HelloController {
//    Logger logger = LoggerFactory.getLogger(getClass());

    @GetMapping("/h")
    public String hello() {
//        logger.info("这是info日志");
        log.info("这是info日志");
        return "hello";
    }
}
