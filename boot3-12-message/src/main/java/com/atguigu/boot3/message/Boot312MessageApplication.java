package com.atguigu.boot3.message;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * KafkaAutoConfiguration提供如下功能
 * 1,KafkaProperties: kafka的所有配置;以 spring.kafka开始- bootstrapServers: kafka集群的所有服务器地址
 * properties: 参数设置1
 * - consumer: 消费者
 * producer: 生产者l
 * 2,@EnableKafka: 开启Kafka的注解驱动功能
 * 3,KafkaTemplate: 收发消息
 * 4,KafkaAdmin 维护主题等
 * 5,@EnableKafka +@KafkaListener 接受消息
 * 1) 消费者来接受消息，需要有group-id2) 、
 * 2) 收消息使用 @KafkaListener + ConsumerRecord3)、
 * 3) spring.kafka 开始的所有配置核心概念
 * 6,核心概念
 * 分区:分散存储，1T的数据分散到N个节点副本:
 * 备份机制，每个小分区的数据都有备份
 * 主题:topics;消息是发送给某个主题
 * kafka.apache.org   官网
 */
@SpringBootApplication
@EnableKafka
public class Boot312MessageApplication {

    public static void main(String[] args) {
        SpringApplication.run(Boot312MessageApplication.class, args);
    }

}
