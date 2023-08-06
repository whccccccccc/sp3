package com.atguigu.boot3.features.bean;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("dev")
@Component
public class Pig {
    private Long id;
    private String name;
}
