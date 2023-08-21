package com.atguigu.boot3.message;

import com.atguigu.boot3.message.bean.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.StopWatch;

import java.util.concurrent.CompletableFuture;

@SpringBootTest
class Boot312MessageApplicationTests {


    @Autowired
    KafkaTemplate kafkaTemplate;

    @Test
    void contextLoads() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        CompletableFuture[] futures = new CompletableFuture[1000];
        for (int i = 0; i < 1000; i++) {
            CompletableFuture send = kafkaTemplate.send("newshh", "hh", "哈哈哈哈:" + i);
            futures[i] = send;
        }
        CompletableFuture.allOf(futures).join();
        stopWatch.stop();
        System.out.println("运行完毕耗时：" + stopWatch.getTotalTimeMillis());
    }

    @Test
    void sendObject() {
        Person person = new Person("zs", "zs@qq.com");
        kafkaTemplate.send("newshh", "hh", person).join();
    }

}
