package com.atguigu.boot3.redis.controller;

import com.atguigu.boot3.redis.bean.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisTestController {

//常见数据类型 k: v value可以有很多类型
//string:普通字符串 : redisTemplate.opsForValue()redisTemplate.opsForList()redisTemplate.opsForSet(
//list:列表:
//set:集合:
//zset:有序集合:redisTemplate.opsForZSetO
//hash:map结构:redisTemplate.opsForHash()

//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

//    @GetMapping("/test")
//    public String test() {
//        Long hello = stringRedisTemplate.opsForValue().increment("hello");
//        return "hello world:" + hello;
//    }

    @GetMapping("set")
    public String set() {
        Person person = new Person();
        person.setName("whc");
        person.setAge("18");
        redisTemplate.opsForValue().set("person", person);
        return "success";
    }
    @GetMapping("get")
    public Person get() {
        return (Person) redisTemplate.opsForValue().get("person");
    }
}
