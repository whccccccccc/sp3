package com.atguigu.boot304web.controller;

import com.atguigu.boot304web.domain.Person;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HelloController {
    /**
     * 默认使用新版 进行路径匹配不能匹配 **在中间的情况下 剩下的和老版没有区别
     *
     * @param request
     * @param p1
     * @return
     */
    @GetMapping("/a*/b?/{p1:[a-f]+}/**")
    public String hello(HttpServletRequest request, @PathVariable String p1) {
        log.info("路径变量p1:{}", p1);
        return request.getRequestURI();
    }


    /**
     * 默认支持写为json  因为导入了jackson处理
     * jackson也支持把数据写入xml
     * @return
     */
    @GetMapping("/person")
    public Person person() {
        Person person = new Person();
        person.setAge(18);
        person.setName("whc");
        person.setPhone("155555555555");
        return person;
    }

    public static void main(String[] args) throws JsonProcessingException {
        Person person = new Person();
        person.setAge(18);
        person.setName("whc");
        person.setPhone("155555555555");
        YAMLFactory yamlFactory = new YAMLFactory();
        yamlFactory.disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER);
        ObjectMapper objectMapper = new ObjectMapper(yamlFactory);
        String s = objectMapper.writeValueAsString(person);
        System.out.println(s);
    }
}
