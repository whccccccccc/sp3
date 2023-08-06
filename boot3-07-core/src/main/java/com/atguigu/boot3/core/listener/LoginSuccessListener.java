package com.atguigu.boot3.core.listener;

import com.atguigu.boot3.core.event.LoginSuccessEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class LoginSuccessListener implements ApplicationListener<LoginSuccessEvent> {
    @Override
    public void onApplicationEvent(LoginSuccessEvent event) {
        String s = "收到用户的登录事件" + event.getSource().toString();
        System.out.println(s);
    }
}
