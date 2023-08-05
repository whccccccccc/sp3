package com.atguigu.boot304web.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ResponseBody

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e) {
        return "后端接口错误:" + e.getMessage();
    }
}
