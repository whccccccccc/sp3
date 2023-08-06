package com.atguigu.boot3.core.listener;

import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.time.Duration;

// BootstrapRegistryInitializer  可以用来做付费产品的检测
public class MyAppListener implements SpringApplicationRunListener {
    @Override
    public void starting(ConfigurableBootstrapContext bootstrapContext) {
        System.out.println("1starting--------正在启动");
    }

    @Override
    public void environmentPrepared(ConfigurableBootstrapContext bootstrapContext, ConfigurableEnvironment environment) {
        System.out.println("2environmentPrepared--------环境准备完成");

    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        System.out.println("3contextPrepared--------ioc容器准备完成");
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
        System.out.println("4contextLoaded--------ioc容器加载完成");

    }

    @Override
    public void started(ConfigurableApplicationContext context, Duration timeTaken) {
        System.out.println("5started--------启动完成");

    }

    @Override
    public void ready(ConfigurableApplicationContext context, Duration timeTaken) {
        System.out.println("6ready--------准备就绪");

    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {
        System.out.println("failed--------发生错误");

    }
}
