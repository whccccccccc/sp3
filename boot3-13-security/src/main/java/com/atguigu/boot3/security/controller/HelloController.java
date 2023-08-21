package com.atguigu.boot3.security.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "hello security";
    }


    @GetMapping("/world")
    @PreAuthorize("hasAuthority('world_exec')")
    public String world() {
        return "world security";
    }
}
