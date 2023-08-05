package com.atguigu.boot305ssm.bean;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String loginName;
    private String password;
}
