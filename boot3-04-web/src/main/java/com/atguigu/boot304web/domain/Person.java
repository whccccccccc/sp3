package com.atguigu.boot304web.domain;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

@JacksonXmlRootElement
@Data
public class Person {

    private String name;
    private Integer age;
    private String phone;
}
