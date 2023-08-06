package com.atguigu.boot3.features.bean;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("prod")
@Component
public class Dog {
    private Long id;
    private String name;
}
