package com.atguigu.boot304web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

@Controller
public class WelcomeController {
    @GetMapping("/welcome")
    public String welcome(@RequestParam(required = false) String name, ModelMap model) {
        model.put("name", StringUtils.isEmpty(name) ? "<span style='color:red'>hello</span>" : name);
        model.put("imgUrl", "/1.jpg");
        model.put("style", "width:400px");
        model.put("show", false);
        return "welcome";
    }
}
